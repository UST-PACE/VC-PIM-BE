package com.ust.retail.store.pim.model.upcmaster;

import com.ust.retail.store.pim.dto.upcmaster.UpcVendorStoreCostDTO;
import com.ust.retail.store.pim.model.catalog.CatalogModel;
import com.ust.retail.store.pim.model.general.Audits;
import com.ust.retail.store.pim.model.security.UserModel;
import com.ust.retail.store.pim.model.vendormaster.VendorMasterModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "upc_vendor_details",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"upc_master_id", "vendor_master_id"}),
				@UniqueConstraint(columnNames = {"upc_master_id", "default_vendor"})
		})
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class UpcVendorDetailsModel extends Audits implements Serializable {

	private static final long serialVersionUID = 6216200128180640088L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "upc_vendor_detail_id")
	private Long upcVendorDetailId;

	@Column(name = "default_vendor")
	private Boolean defaultVendor;

	@Column(name = "units_per_case", nullable = false)
	private Integer unitsPerCase;

	@Column(name = "case_upc")
	private String caseUpc;

	@Column(name = "case_weight")
	private Double caseWeight;

	@Column(name = "case_length")
	private Double caseLength;

	@Column(name = "case_height")
	private Double caseHeight;

	@Column(name = "case_width")
	private Double caseWidth;

	@Column(name = "cases_per_pallet", nullable = false)
	private Integer casesPerPallet;

	@Column(name = "pallet_weight")
	private Double palletWeight;

	@Column(name = "pallet_length")
	private Double palletLength;

	@Column(name = "pallet_height")
	private Double palletHeight;

	@Column(name = "pallet_width")
	private Double palletWidth;

	@Column(name = "moq")
	private Double moq;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "upc_master_id", referencedColumnName = "upc_master_id")
	private UpcMasterModel upcMaster;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_master_id", referencedColumnName = "vendor_master_id")
	private VendorMasterModel vendorMaster;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "package_type_id", referencedColumnName = "catalog_id")
	private CatalogModel packageType;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "country_of_origin_id", referencedColumnName = "catalog_id")
	private CatalogModel countryOfOrigin;

	@OneToMany(mappedBy = "upcVendorDetail", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<UpcVendorStoreCostModel> storeCosts = new HashSet<>();

	public UpcVendorDetailsModel(Long upcVendorDetailId) {
		super();
		this.upcVendorDetailId = upcVendorDetailId;
	}

	public UpcVendorDetailsModel(UpcMasterModel upcMaster) {
		this.upcMaster = upcMaster;
	}

	public UpcVendorDetailsModel(Long upcVendorDetailId, Integer unitsPerCase,
								 String caseUpc, Double caseWeight, Double caseLength, Double caseHeight, Double caseWidth,
								 Integer casesPerPallet, Double palletWeight, Double palletLength, Double palletHeight, Double palletWidth,
								 Double moq, Long upcMasterId, Long vendorMasterId, Long packageTypeId,
								 Long countryOfOriginId, Boolean defaultVendor, Set<UpcVendorStoreCostDTO> storeCosts, Long userCreatedId) {
		super();
		this.upcVendorDetailId = upcVendorDetailId;
		this.unitsPerCase = Optional.ofNullable(unitsPerCase).orElse(0);
		this.caseUpc = caseUpc;
		this.caseWeight = caseWeight;
		this.caseLength = caseLength;
		this.caseHeight = caseHeight;
		this.caseWidth = caseWidth;
		this.casesPerPallet = Optional.ofNullable(casesPerPallet).orElse(0);
		this.palletWeight = palletWeight;
		this.palletLength = palletLength;
		this.palletHeight = palletHeight;
		this.palletWidth = palletWidth;
		this.moq = Optional.ofNullable(moq).orElse(0d);
		this.upcMaster = new UpcMasterModel(upcMasterId);
		this.vendorMaster = new VendorMasterModel(vendorMasterId);
		this.packageType = new CatalogModel(packageTypeId);
		this.countryOfOrigin = new CatalogModel(countryOfOriginId);
		this.defaultVendor = defaultVendor ? true : null; // Unique constraints allow multiple null values

		Optional.ofNullable(storeCosts).orElse(Set.of()).stream()
				.map(UpcVendorStoreCostDTO::createModel)
				.forEach(this::addStoreCost);

		super.userCreate = new UserModel(userCreatedId);
	}

	public void addStoreCost(UpcVendorStoreCostModel storeCost) {
		getStoreCosts().add(storeCost.setUpcVendorDetail(this));
	}

	public Double getProductCost(Long storeNumId) {
		return getStoreCosts().stream()
				.filter(sc -> Objects.equals(sc.getStoreNumber().getStoreNumId(), storeNumId))
				.map(UpcVendorStoreCostModel::getCost)
				.findFirst()
				.orElse(0d);
	}

	public Double getAverageProductCost() {
		return getStoreCosts().stream()
				.mapToDouble(UpcVendorStoreCostModel::getCost)
				.average()
				.orElse(0d);
	}
}
