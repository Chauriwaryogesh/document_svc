package com.SecureAccessPortal.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component
public class DateUtil {

	public LocalDate stringToLocalDateConvert(String date) {
		// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate localDate = LocalDate.parse(date);
		return localDate;
	}

	public LocalDateTime calculateNextDueDate(LocalDateTime startDate, String frequency, int installmentNumber) {
    switch (frequency.toUpperCase()) {
        case "DAILY": return startDate.plusDays(installmentNumber);
        case "MONTHLY": return startDate.plusMonths(installmentNumber);
        case "QUATERLY": return startDate.plusMonths(3L * installmentNumber);
        case "YEARLY": return startDate.plusYears(installmentNumber);
        default: throw new IllegalArgumentException("Invalid frequency: " + frequency);
    }
}


}
