package com.ust.retail.store.pim.service.vendormaster;

import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.common.bases.BaseService;
import com.ust.retail.store.pim.common.catalogs.VendorContactTypeCatalog;
import com.ust.retail.store.pim.dto.vendorcredit.VendorCreditDTO;
import com.ust.retail.store.pim.dto.vendormaster.VendorMasterDTO;
import com.ust.retail.store.pim.dto.vendormaster.VendorMasterDropdownDTO;
import com.ust.retail.store.pim.dto.vendormaster.VendorMasterFilterDTO;
import com.ust.retail.store.pim.dto.vendormaster.VendorMasterStoreDTO;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.model.vendorcredits.VendorCreditModel;
import com.ust.retail.store.pim.model.vendormaster.VendorContactModel;
import com.ust.retail.store.pim.model.vendormaster.VendorMasterModel;
import com.ust.retail.store.pim.model.vendormaster.VendorMasterStoreModel;
import com.ust.retail.store.pim.repository.vendorcredits.VendorCreditRepository;
import com.ust.retail.store.pim.repository.vendormaster.VendorMasterRepository;
import com.ust.retail.store.pim.repository.vendormaster.VendorMasterStoreRepository;
import com.ust.retail.store.pim.service.upcmaster.UpcVendorDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VendorMasterService extends BaseService {

	private final VendorMasterRepository vendorMasterRepository;
	private final VendorMasterStoreRepository vendorMasterStoreRepository;
	private final UpcVendorDetailsService upcVendorDetailsService;
	private final VendorCodeGenerator vendorCodeGenerator;
	private final AuthenticationFacade authenticationFacade;
	private final VendorCreditRepository vendorCreditRepository;

	@Autowired
	public VendorMasterService(
			VendorMasterRepository vendorMasterRepository,
			VendorMasterStoreRepository vendorMasterStoreRepository,
			UpcVendorDetailsService upcVendorDetailsService,
			VendorCodeGenerator vendorCodeGenerator,
			AuthenticationFacade authenticationFacade,
			VendorCreditRepository vendorCreditRepository) {
		super();
		this.vendorMasterRepository = vendorMasterRepository;
		this.vendorMasterStoreRepository = vendorMasterStoreRepository;
		this.upcVendorDetailsService = upcVendorDetailsService;
		this.vendorCodeGenerator = vendorCodeGenerator;
		this.authenticationFacade = authenticationFacade;
		this.vendorCreditRepository = vendorCreditRepository;
	}

	@Transactional
	public VendorMasterDTO saveOrUpdate(VendorMasterDTO vendorMasterDTO) {

		List<VendorMasterStoreModel> stores = vendorMasterStoreRepository
				.findByPkVendorMasterVendorMasterId(vendorMasterDTO.getVendorMasterId());

		VendorCreditDTO vendorCredit = Optional.ofNullable(vendorMasterDTO.getVendorMasterId())
				.flatMap(vendorMasterRepository::findById)
				.map(VendorMasterModel::getVendorCredit)
				.map(m -> new VendorCreditDTO().parseToDTO(m))
				.orElse(null);

		VendorMasterModel vendorMasterModel = this.vendorMasterRepository
				.save(vendorMasterDTO.createModel(this.authenticationFacade.getCurrentUserId(), stores, vendorCredit, vendorCodeGenerator));

		initVendorCredit(vendorMasterModel);

		return vendorMasterDTO.parseToDTO(vendorMasterModel);
	}

	private void initVendorCredit(VendorMasterModel vendorMasterModel) {

		if (vendorMasterModel.getVendorCredit() == null) {
			VendorCreditModel credits = new VendorCreditModel(vendorMasterModel.getVendorMasterId(), 0.0D);
			vendorCreditRepository.save(credits);
		}
	}

	public VendorMasterDTO findById(Long vendorMasterId) {
		VendorMasterModel vendorMasterModel = this.vendorMasterRepository.findById(vendorMasterId)
				.orElseThrow(() -> new ResourceNotFoundException("Vendor Master", "id", vendorMasterId));

		return new VendorMasterDTO().parseToDTO(vendorMasterModel);
	}

	public VendorMasterStoreDTO addStoreNumber(VendorMasterStoreDTO vendorMasterStoreDTO) {

		VendorMasterStoreModel model = vendorMasterStoreRepository.save(vendorMasterStoreDTO.createModel());

		return vendorMasterStoreDTO.parseToDTO(model);
	}

	@Transactional
	public void removeStoreNumber(Long storeNumId, Long vendorMasterId) {
		vendorMasterStoreRepository.delete(new VendorMasterStoreModel(vendorMasterId, storeNumId));
		upcVendorDetailsService.deleteCostByVendorAndStore(vendorMasterId, storeNumId);
	}

	public List<VendorMasterDTO> getAutocompleteVendorName(String vendorName) {
		return vendorMasterRepository.getAutocompleteListVendorName(vendorName);
	}

	public List<VendorMasterDTO> getAutocompleteVendorCode(String vendorCode) {
		return vendorMasterRepository.getAutocompleteListCodeName(vendorCode);
	}

	public Page<VendorMasterFilterDTO> getVendorMasterByFilters(VendorMasterFilterDTO vendorMasterFilterDTO) {

		Page<VendorMasterModel> vendorMasters = vendorMasterRepository.findByFilters(vendorMasterFilterDTO.getVendorName(),
				vendorMasterFilterDTO.getVendorCode(), vendorMasterFilterDTO.getStoreNumberId(), vendorMasterFilterDTO.getDistributorId(),
				vendorMasterFilterDTO.createPageable());

		return vendorMasters.map(currentVendorMaster -> new VendorMasterFilterDTO(currentVendorMaster.getVendorMasterId(),
				currentVendorMaster.getVendorName(), currentVendorMaster.getVendorCode(), currentVendorMaster.getVendorStoreNumbers()));
	}

	public List<VendorMasterDropdownDTO> load() {
		return vendorMasterRepository.findAll()
				.stream().map(m -> new VendorMasterDropdownDTO().parseToDTO(m))
				.collect(Collectors.toList());
	}

	public Optional<VendorContactModel> getVendorSalesRepresentative(Long vendorMasterId) {
		return vendorMasterRepository.findById(vendorMasterId)
				.orElseThrow(() -> new ResourceNotFoundException("Vendor Master", "id", vendorMasterId)).
				getVendorContacts().stream()
				.filter(contact -> Objects.equals(contact.getVendorType().getCatalogId(), VendorContactTypeCatalog.CONTACT_TYPE_SALES_REPRESENTATIVE))
				.findFirst();
	}

	public Optional<VendorContactModel> getVendorEscalationContact(Long vendorMasterId) {
		return vendorMasterRepository.findById(vendorMasterId)
				.orElseThrow(() -> new ResourceNotFoundException("Vendor Master", "id", vendorMasterId)).
				getVendorContacts().stream()
				.filter(contact -> Objects.equals(contact.getVendorType().getCatalogId(), VendorContactTypeCatalog.CONTACT_TYPE_ESCALATION))
				.findFirst();
	}
}
