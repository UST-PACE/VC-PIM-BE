package com.ust.retail.store.pim.service.external;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.ust.retail.store.pim.common.catalogs.MenuDayCalendarCatalog;
import com.ust.retail.store.pim.common.catalogs.MenuMonthCalendarCatalog;
import com.ust.retail.store.pim.common.catalogs.MenuWeekCalendarCatalog;
import com.ust.retail.store.pim.common.catalogs.UpcMasterStatusCatalog;
import com.ust.retail.store.pim.common.catalogs.UpcProductTypeCatalog;
import com.ust.retail.store.pim.common.catalogs.UpcSellingChannelCatalog;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;
import com.ust.retail.store.pim.dto.external.ExternalMenuConfiguratorDTO;
import com.ust.retail.store.pim.dto.external.ExternalMenuConfiguratorProductDTO;
import com.ust.retail.store.pim.repository.menuconfigurator.MenuConfiguratorRepository;
import com.ust.retail.store.pim.repository.upcmaster.UpcMasterRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExternalMenuService {

	private final MenuConfiguratorRepository menuConfiguratorRepository;

	private final MenuMonthCalendarCatalog menuMonthCalendarCatalog;

	private final MenuWeekCalendarCatalog menuWeekCalendarCatalog;

	private final MenuDayCalendarCatalog menuDayCalendarCatalog;

	private final UpcMasterRepository upcMasterRepository;

	public List<ExternalMenuConfiguratorDTO> filterByCalendarMenu(ExternalMenuConfiguratorDTO req) {

		List<ExternalMenuConfiguratorDTO> scheduledMenuProducts = getMenuConfiguredProducts(req);
		return scheduledMenuProducts.stream()
				.peek(w -> w.getMenuWeeks().forEach(
						d -> d.getMenuDays().forEach(m -> m.getMenuProducts().addAll((getAlwaysAvailableProducts())))))
				.collect(Collectors.toList());
	}
	
	public ExternalMenuConfiguratorDTO getScheduledMenuProducts(ExternalMenuConfiguratorDTO req) {

		List<ExternalMenuConfiguratorDTO> scheduledMenuProducts = getMenuConfiguredProducts(req);
		scheduledMenuProducts.stream().forEach(week -> {
			week.getMenuWeeks().removeIf(w -> w.getWeekId() != getWeekId(req));
			week.getMenuWeeks().stream().forEach(day -> {
				day.getMenuDays().removeIf(w -> w.getDayId() != getDayId(req));
			});
		});
		return scheduledMenuProducts.stream()
				.peek(w -> w.getMenuWeeks().forEach(
						d -> d.getMenuDays().forEach(m -> m.getMenuProducts().addAll((getAlwaysAvailableProducts())))))
				.findFirst().orElse(null);
	}
	
	private List<ExternalMenuConfiguratorDTO> getMenuConfiguredProducts(ExternalMenuConfiguratorDTO req){
		return menuConfiguratorRepository
				.getScheduledMenuProducts(req.getYear(), getMonthId(req), getWeekId(req), getDayId(req),
						UpcMasterStatusCatalog.UPC_MASTER_STATUS_DISCONTINUE_TRADING,
						UpcSellingChannelCatalog.VC_UPC_SELLING_CHANNEL)
				.stream().map(m -> new ExternalMenuConfiguratorDTO(m)).collect(Collectors.toList());
	}
	
	private Long getMonthId(ExternalMenuConfiguratorDTO request) {
		return menuMonthCalendarCatalog.getCatalogOptions().stream()
				.filter(f -> f.getCatalogOptions().equalsIgnoreCase(request.getMonthName())).findFirst().get()
				.getCatalogId();
	}
	
	private Long getWeekId(ExternalMenuConfiguratorDTO request) {
		return menuWeekCalendarCatalog.getCatalogOptions().stream()
				.filter(f -> f.getCatalogOptions().equalsIgnoreCase(request.getWeekName())).map(CatalogDTO::getCatalogId).findFirst()
				.orElse(StringUtils.isBlank(request.getWeekName()) ? null : 0L);
	}
	
	private Long getDayId(ExternalMenuConfiguratorDTO request) {
		return menuDayCalendarCatalog.getCatalogOptions().stream()
				.filter(f -> f.getCatalogOptions().equalsIgnoreCase(request.getDayName())).map(CatalogDTO::getCatalogId)
				.findFirst().orElse(StringUtils.isBlank(request.getDayName()) ? null : 0L);
	}
	
	private List<ExternalMenuConfiguratorProductDTO> getAlwaysAvailableProducts() {
		return upcMasterRepository
				.findByUpcProductTypeCatalogIdNot(UpcProductTypeCatalog.ROTATING_TYPE,
						UpcMasterStatusCatalog.UPC_MASTER_STATUS_DISCONTINUE_TRADING,
						UpcSellingChannelCatalog.VC_UPC_SELLING_CHANNEL,
						UpcProductTypeCatalog.MODIFYE_TYPE)
				.stream().map(m -> new ExternalMenuConfiguratorProductDTO(m)).collect(Collectors.toList());
	}
	
}
