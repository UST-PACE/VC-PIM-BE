package com.ust.retail.store.pim.service.upcmaster;

import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.common.bases.BaseService;
import com.ust.retail.store.pim.dto.upcmaster.PrincipalUpcDTO;
import com.ust.retail.store.pim.dto.upcmaster.SimpleUpcDTO;
import com.ust.retail.store.pim.dto.upcmaster.UpcVendorDetailDTO;
import com.ust.retail.store.pim.dto.upcmaster.UpcVendorDetailFiltersDTO;
import com.ust.retail.store.pim.dto.vendormaster.VendorMasterDTO;
import com.ust.retail.store.pim.event.upcmaster.UpcVendorDetailChangedEvent;
import com.ust.retail.store.pim.event.upcmaster.VendorStoreRemovedEvent;
import com.ust.retail.store.pim.exceptions.InvalidUPCException;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.model.upcmaster.UpcVendorDetailsModel;
import com.ust.retail.store.pim.model.upcmaster.UpcVendorStoreCostModel;
import com.ust.retail.store.pim.model.vendormaster.VendorMasterStoreModel;
import com.ust.retail.store.pim.model.vendormaster.VendorMasterStorePK;
import com.ust.retail.store.pim.repository.upcmaster.UpcMasterRepository;
import com.ust.retail.store.pim.repository.upcmaster.UpcVendorDetailsRepository;
import com.ust.retail.store.pim.repository.upcmaster.UpcVendorStoreCostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UpcVendorDetailsService extends BaseService {

	private final UpcVendorDetailsRepository upcVendorDetailsRepository;
	private final UpcVendorStoreCostRepository upcVendorStoreCostRepository;
	private final UpcMasterRepository upcMasterRepository;
	private final AuthenticationFacade authenticationFacade;
	private final ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	public UpcVendorDetailsService(UpcVendorDetailsRepository upcVendorDetailsRepository,
								   UpcVendorStoreCostRepository upcVendorStoreCostRepository,
								   UpcMasterRepository upcMasterRepository,
								   AuthenticationFacade authenticationFacade,
								   ApplicationEventPublisher applicationEventPublisher) {
		super();
		this.upcVendorDetailsRepository = upcVendorDetailsRepository;
		this.upcVendorStoreCostRepository = upcVendorStoreCostRepository;
		this.upcMasterRepository = upcMasterRepository;
		this.authenticationFacade = authenticationFacade;
		this.applicationEventPublisher = applicationEventPublisher;
	}

	@Transactional
	public UpcVendorDetailDTO saveOrUpdate(UpcVendorDetailDTO upcVendorDetailDTO) {

		UpcVendorDetailsModel upcVendorDetailsModel = this.upcVendorDetailsRepository
				.saveAndFlush(upcVendorDetailDTO.createModel(this.authenticationFacade.getCurrentUserId()));

		if (upcVendorDetailDTO.getUpcVendorDetailId() != null && upcVendorDetailDTO.getDefaultVendor()) {
			applicationEventPublisher.publishEvent(new UpcVendorDetailChangedEvent(this, upcVendorDetailDTO.getUpcVendorDetailId()));
		}

		return upcVendorDetailDTO.parseToDTO(upcVendorDetailsModel);
	}

	public UpcVendorDetailDTO findById(Long upcVendorDetailId) {
		UpcVendorDetailsModel model = this.upcVendorDetailsRepository.findById(upcVendorDetailId)
				.orElseThrow(() -> new ResourceNotFoundException("Vendor Upc Detail", "id", upcVendorDetailId));

		model.getVendorMaster().getVendorStoreNumbers().stream()
				.map(VendorMasterStoreModel::getPk)
				.map(VendorMasterStorePK::getStoreNum)
				.map(st -> new UpcVendorStoreCostModel().setUpcVendorDetail(model).setStoreNumber(st))
				.forEach(model::addStoreCost);

		return new UpcVendorDetailDTO().parseToDTO(model);
	}

	public Page<UpcVendorDetailFiltersDTO> getUpcVendorDetailsByFilters(UpcVendorDetailFiltersDTO upcMasterFiltersDTO) {

		Page<UpcVendorDetailsModel> searchResults = upcVendorDetailsRepository.findByFilters(
				upcMasterFiltersDTO.getVendorName(), upcMasterFiltersDTO.getCode(),
				upcMasterFiltersDTO.getProductName(), upcMasterFiltersDTO.createPageable());

		return searchResults
				.map(currentDetail -> new UpcVendorDetailFiltersDTO().parseToDTO(currentDetail));
	}

	public PrincipalUpcDTO principalUpcLookup(String code) {

		return upcMasterRepository.findByPrincipalUpc(code)
				.map(m -> new PrincipalUpcDTO(m.getUpcMasterId(), m.getPrincipalUpc()))
				.orElseThrow(() -> new InvalidUPCException(code));
	}

	public VendorMasterDTO findDefaultVendorFor(Long upcMasterId) {
		return upcVendorDetailsRepository.findByDefaultVendorTrueAndUpcMasterUpcMasterId(upcMasterId)
				.map(UpcVendorDetailsModel::getVendorMaster)
				.map(v -> new VendorMasterDTO().parseToDTO(v))
				.orElseThrow(() -> new ResourceNotFoundException("UPC Master Id", "id", upcMasterId));
	}

	public Optional<UpcVendorDetailsModel> findDefaultVendorDetailsFor(Long upcMasterId) {
		return upcVendorDetailsRepository.findByDefaultVendorTrueAndUpcMasterUpcMasterId(upcMasterId);
	}

	public boolean productHasDefaultVendor(Long upcMasterId) {
		return upcVendorDetailsRepository.findByDefaultVendorTrueAndUpcMasterUpcMasterId(upcMasterId).isPresent();
	}

	public List<UpcVendorDetailsModel> findVendorDetailsFor(Long upcMasterId) {
		return upcVendorDetailsRepository.findByUpcMasterUpcMasterId(upcMasterId);
	}

	public List<SimpleUpcDTO> findProductsForVendor(Long vendorMasterId) {
		return upcVendorDetailsRepository.findByVendorMasterVendorMasterId(vendorMasterId).stream()
				.map(detail -> new SimpleUpcDTO().parseToDTO(detail.getUpcMaster()))
				.collect(Collectors.toUnmodifiableList());
	}

	@Transactional
	public void deleteCostByVendorAndStore(Long vendorMasterId, Long storeNumId) {
		upcVendorStoreCostRepository.deleteAll(upcVendorStoreCostRepository.findCostsByVendorAndStore(vendorMasterId, storeNumId));
		applicationEventPublisher.publishEvent(new VendorStoreRemovedEvent(this, vendorMasterId, storeNumId));
	}
}
