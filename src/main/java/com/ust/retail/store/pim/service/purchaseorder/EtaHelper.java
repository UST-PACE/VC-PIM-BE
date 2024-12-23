package com.ust.retail.store.pim.service.purchaseorder;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

class EtaHelper {
	private EtaHelper() {
	}

	static Date getEta(Date poDate, int cutOffDay, int shipmentDay, int vendorEta) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(poDate);
		Calendar cutOffDate = new GregorianCalendar();
		cutOffDate.setTime(poDate);
		cutOffDate.set(Calendar.DAY_OF_WEEK, cutOffDay);
		if (calendar.after(cutOffDate)) {
			calendar.add(Calendar.DAY_OF_YEAR, 7);
			calendar.set(Calendar.DAY_OF_WEEK, cutOffDay);
		}
		int offset = shipmentDay - calendar.get(Calendar.DAY_OF_WEEK) + vendorEta;
		calendar.add(Calendar.DAY_OF_YEAR, offset);
		return calendar.getTime();
	}

}
