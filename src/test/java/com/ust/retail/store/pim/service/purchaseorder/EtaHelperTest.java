package com.ust.retail.store.pim.service.purchaseorder;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;

import com.ust.retail.store.pim.util.DateUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasToString;

class EtaHelperTest {

	@Test
	void getEtaIsCorrectWhenDateIsAfterCutOffDay() {
		Date poDate = DateUtils.parseStringToDate("08/09/2021");
		Date eta = EtaHelper.getEta(poDate, Calendar.MONDAY, Calendar.TUESDAY, 1);
		assertThat(eta, hasToString("Wed Sep 15 00:00:00 CDT 2021"));
	}

	@Test
	void getEtaIsCorrectWhenDateIsCutOffDay() {
		Date poDate = DateUtils.parseStringToDate("06/09/2021");
		Date eta = EtaHelper.getEta(poDate, Calendar.MONDAY, Calendar.TUESDAY, 1);
		assertThat(eta, hasToString("Wed Sep 08 00:00:00 CDT 2021"));
	}

	@Test
	void getEtaIsCorrectWhenDateIsSaturdayAndCutOffIsSunday() {
		Date poDate = DateUtils.parseStringToDate("04/09/2021");
		Date eta = EtaHelper.getEta(poDate, Calendar.SUNDAY, Calendar.MONDAY, 1);
		assertThat(eta, hasToString("Tue Sep 07 00:00:00 CDT 2021"));
	}

}
