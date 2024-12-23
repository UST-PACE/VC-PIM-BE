package com.ust.retail.store.pim.service.upcmaster;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.gson.Gson;
import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.common.bases.BaseService;
import com.ust.retail.store.pim.dto.upcmaster.*;
import com.ust.retail.store.pim.exceptions.GeniUpcValidationException;
import com.ust.retail.store.pim.model.upcmaster.UpcAliasVerifyModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import com.ust.retail.store.pim.repository.upcmaster.UpcAliasVerifyRepository;
import com.ust.retail.store.pim.repository.upcmaster.UpcMasterRepository;
import com.ust.retail.store.pim.util.DownloadImagesDisplayUtils;
import com.ust.retail.store.pim.util.WebClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.net.ssl.SSLException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Array;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class UpcAliasVerifyService extends BaseService {

    private final UpcAliasVerifyRepository upcAliasVerifyRepository;
    private final UpcMasterRepository upcMasterRepository;
    private final AuthenticationFacade authenticationFacade;

    @Value("${pim.geni.photo.url}")
    private String PIM_GENI_PHOTO_URL_SERVICE;

    @Value("${pim.geni.mach.url}")
    private String PIM_GENI_MACH_URL_SERVICE;


    @Value("${pim.geni.jsonfile.path}")
    private String GENI_JSON_FILE_PATH;

    @Autowired
    public UpcAliasVerifyService(UpcAliasVerifyRepository upcAliasVerifyRepository,
                                 UpcMasterRepository upcMasterRepository,
                                 AuthenticationFacade authenticationFacade) {
        this.upcAliasVerifyRepository = upcAliasVerifyRepository;
        this.upcMasterRepository = upcMasterRepository;
        this.authenticationFacade = authenticationFacade;
    }

    public Page<ListUpcAliasDTO> listProductsByFilters(UpcMasterAliasFilterDTO filterDTO) {
        return upcAliasVerifyRepository.getProductsByFilters(
                filterDTO.getPrincipalUpc(),
                filterDTO.getProductName(),
                filterDTO.getProductCategoryId(),
                filterDTO.getUpcProductTypeId(),
                filterDTO.createPageable());
    }

    public Map runGenerateAliasGeni(UpcMasterAliasDTO dto) throws SSLException {
        DownloadImagesDisplayUtils downloadImages = new DownloadImagesDisplayUtils();
        if (dto.getProductImage1() != null) {
            downloadImages.downloadImage("photo_1", dto.getProductImage1());
            return validateAliasWithGeni("photo_1");

        } else
            return null;
    }

    public ResponseEntity<UpcAliasVerifyDTO> runDishesGeniValidationTest(UpcMasterAliasDTO upcAliasDTO) {

        String testAlias = upcAliasDTO.getAliasDisplay();
        Long upcMasterId = upcAliasDTO.getUpcMasterId();
        boolean[] statusResult = new boolean[4];
        Long upcAliasId = upcAliasDTO.getAliasId();
        JsonNode geniResponse = null;


        List<DishJsonFileDTO> dishJsonFileDTOList = upcMasterRepository.getVerifiedMenu();

        //
        for (DishJsonFileDTO currentJsonItem : dishJsonFileDTOList) {
            if (Long.parseLong(currentJsonItem.getSku()) == upcMasterId && testAlias != null && testAlias != "") {
                currentJsonItem.changeProductName(testAlias);
                break;
            }
        }
        generateDishesJsonFile(dishJsonFileDTOList);

        List<String> displayPhotos = new ArrayList<>();
        displayPhotos.add(upcAliasDTO.getProductImage1());
        displayPhotos.add(upcAliasDTO.getProductImage2());
        displayPhotos.add(upcAliasDTO.getProductImage3());
        displayPhotos.add(upcAliasDTO.getProductImage4());

        try {
            StringBuilder productGeni = new StringBuilder();
            List<String > listProducts= new ArrayList<>();
            for (String photo : displayPhotos) {
                int index = displayPhotos.indexOf(photo);
                boolean isPhotoCreated = new DownloadImagesDisplayUtils().downloadImage("photo_" + index, photo);
                if(isPhotoCreated){
                    List<Long> skuResponse = new ArrayList<>();
                    geniResponse = validateMachWithGeni(index);
                    //geniResponse = identifyDishesCpgs();
                    JsonNode result =geniResponse.get("result");

                    for(JsonNode s:result){
                        String sku = s.findValue("sku").toString();
                        sku =sku.substring(1,sku.length()-1);
                        String product = s.findValues("product_name").toString();
                        product =product.substring(2,product.length()-2);
                        skuResponse.add(Long.parseLong(sku));
                        String listProduct = String.format("%s: %s",sku,product);
                        if(!listProducts.contains(listProduct)){
                            productGeni.append(String.format("%s: %s \n",sku,product));
                            listProducts.add(listProduct);
                        }

                    }
                    statusResult[index] = skuResponse.stream().anyMatch(s -> s.equals(upcMasterId));

                }

            }
            String testResults = "P1:" + getPassFailedResult(statusResult[0])
            		+ " P2:" + getPassFailedResult(statusResult[1])
            		+ " P3:" + getPassFailedResult(statusResult[2])
            		+ " P4:" + getPassFailedResult(statusResult[3]);

            UpcAliasVerifyDTO geniResults = new UpcAliasVerifyDTO(upcAliasDTO.getAliasId(),
                    upcAliasDTO.getUpcMasterId(),
                    statusResult[0],
                    statusResult[1],
                    statusResult[2],
                    statusResult[3],
                    testResults,
                    productGeni.toString(),
                    new Date(System.currentTimeMillis()));

            if (upcAliasId != null) {
                Optional<UpcAliasVerifyModel> optCurrentModel = upcAliasVerifyRepository.findById(upcAliasDTO.getAliasId());

                if (optCurrentModel.isPresent()) {
                    UpcAliasVerifyModel currentModel = optCurrentModel.get();
                    currentModel.updateUpcAlias(geniResults);
                    upcAliasVerifyRepository.save(currentModel);
                }

            } else {
                upcAliasVerifyRepository.save(geniResults.parseToModel());
            }
//            Map<UpcAliasVerifyDTO, UpcAliasVerifyDTO> hashMap = new HashMap<>();
//            hashMap.put(geniResults,geniResults);

            return ResponseEntity.ok()
                    .body(geniResults);

        } catch (SSLException | RuntimeException | JsonProcessingException e) {
            log.error("Internal error server exception", e);
            throw new GeniUpcValidationException(e);
        }
    }

    public IUpcMasterAliasDTO findById(Long upcMasterId) {
        return upcAliasVerifyRepository.findByUpcMasterId(upcMasterId);
    }

    private Map validateAliasWithGeni(String name) throws SSLException, RuntimeException {
        WebClient webClient = new WebClientUtils().createWebClient();
        File fileImageGeni = new File("./files/" + name + ".jpg");
        Type mapType = Map.class;
        Gson gson = new Gson();
        FileSystemResource image = new FileSystemResource(fileImageGeni);
        JsonNode responseGeni = null;
        String responseToString = "";

        if (image.exists()) {
            //Thread.sleep(2000);
            responseGeni = webClient.post().uri(PIM_GENI_PHOTO_URL_SERVICE)
                    .body(BodyInserters.fromMultipartData("image", image))
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
            fileImageGeni.delete();
            responseToString = responseGeni.get("result").toString();
        }

        return gson.fromJson(responseToString, mapType);
    }

    private JsonNode validateMachWithGeni(int photoName) throws SSLException, RuntimeException, JsonProcessingException {//private
        WebClient webClient = new WebClientUtils().createWebClient();
        File fileImageGeni = new File("./files/" + "photo_" + photoName + ".jpg");
        FileSystemResource image = new FileSystemResource(fileImageGeni);
        FileSystemResource json = new FileSystemResource(new File("./files/dishes.json"));
        JsonNode responseGeni =null;

        if (image.exists()) {
            responseGeni = webClient.post().uri(PIM_GENI_MACH_URL_SERVICE)
                    .body(BodyInserters.fromMultipartData("image", image).with("dishes_file", json))
                    .retrieve()
//                    .onStatus(HttpStatus::is5xxServerError, clientResponse ->
//                         Mono.error(new RuntimeException("Error 504: Gateway ")
//                    ))
                    .bodyToMono(JsonNode.class)
                    .onErrorResume(e -> {
                        // Fallback logic in case of an error
                        System.out.println("Error occurred: " + e.getMessage());
                        return Mono.error(new RuntimeException("Client Error: " +e.getMessage()));
                    })
                    .block();

            fileImageGeni.delete();
        }
        return responseGeni;
    }

    public UpcMasterAliasDTO updateUpcMaster(UpcMasterAliasDTO upcAliasVerifyDTO) {
        UpcMasterModel model = new UpcMasterModel();
        Long upcMasterId = upcAliasVerifyDTO.getUpcMasterId();

        if (upcMasterId != null) {
            Optional<UpcMasterModel> optCurrentModel = upcMasterRepository.findById(upcMasterId);
            if (optCurrentModel.isPresent()) {
                UpcMasterModel currentModel = optCurrentModel.get();
                currentModel.setGeniSuggest(upcAliasVerifyDTO.getAlias(), upcAliasVerifyDTO.getPackageColor());
                model = upcMasterRepository.save(currentModel);
            }
        }
        return upcAliasVerifyDTO;
    }

    private void generateDishesJsonFile(List<DishJsonFileDTO> menu) {

        String arrayToJson = null;
        log.info("Dish Json File Location:" + GENI_JSON_FILE_PATH);
        FileWriter writer = null;

        try {
            Gson gson = new Gson();
            String jsonArray = gson.toJson(menu);

            arrayToJson = "{ \"menu\":" + jsonArray + "}";
            writer = new FileWriter(GENI_JSON_FILE_PATH);
            writer.write(arrayToJson);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private JsonNode mockIdentifyItemsInPhoto() throws JsonProcessingException {
        String json = "{\n" +
                "  \"result\": {\n" +
                "    \"mainDish\": {\n" +
                "      \"name\": \"Scrambled Eggs\",\n" +
                "      \"description\": \"Scrambled eggs with tater tots and bacon\",\n" +
                "      \"protein\": \"Eggs\"\n" +
                "    },\n" +
                "    \"sides\": [\n" +
                "      {\n" +
                "        \"name\": \"Bacon\",\n" +
                "        \"count\": 5\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"Tater Tots\",\n" +
                "        \"count\": 20\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"Scrambled Eggs\",\n" +
                "        \"count\": 1\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"Breakfast Sausage Patties\",\n" +
                "        \"count\": 3\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"Breakfast Sausage Links\",\n" +
                "        \"count\": 2\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"Scrambled Eggs\",\n" +
                "        \"count\": 1\n" +
                "      }\n" +
                "    ],\n" +
                "    \"CPGs\": [\n" +
                "      {\n" +
                "        \"name\": \"Frutsi Ponche de Fruta\",\n" +
                "        \"package_color\": \"Red\",\n" +
                "        \"barcode\": \"-1\"\n" +
                "      }\n" +
                "    ]\n" +
                "\n" +
                "  }\n" +
                "}";
        return new ObjectMapper().readTree(json);
    }

    private JsonNode identifyDishesCpgs() throws  SSLException,JsonProcessingException {
        String json = "{\n" +
                "        \"result\": [\n" +
                "        {\n" +
                "            \"sku\": \"130\",\n" +
                "                \"quantity\": 1,\n" +
                "                \"product_name\": \"Sausage Sandwich\",\n" +
                "                \"category_name\": \"Zuhause\",\n" +
                "                \"category_id\": 23\n" +
                "        },\n" +
                "        {\n" +
                "            \"sku\": \"8\",\n" +
                "                \"quantity\": 1,\n" +
                "                \"product_name\": \"Sauerkraut\",\n" +
                "                \"category_name\": \"Zuhause\",\n" +
                "                \"category_id\": 23\n" +
                "        }\n" +
                "  ],\n" +
                "        \"total_items_in_tray:\": 3,\n" +
                "            \"unidentified_cpgs\": 1,\n" +
                "            \"unidentified_sides\": 0\n" +
                "    }";
        return new ObjectMapper().readTree(json);
    }

    private String getPassFailedResult(Boolean geniResult) {
    	return geniResult ? "Pass" : "Failed";
    }
}
