package com.pricingservice.strategy;

import com.pricingservice.dto.ItemDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("TIME_BASED")
public class TimeBasedStrategy implements PricingStrategy {


	@Override
	public BigDecimal applyStrategy(List<ItemDTO> items, Map<String, Object> parameters) {
		BigDecimal total = (BigDecimal) parameters.get("baseTotal");

		// Extract rule parameters
//		Map<String, Object> ruleParams = (Map<String, Object>) parameters.get("ruleParameters");
//		System.out.println(ruleParams);
		log.info("✅ [TimeBasedStrategy] Applied {}% discount based on parameters from time based ");

		// Default discount
		BigDecimal discountPercentage = BigDecimal.ZERO;

		if (parameters.get("discountPercentage") != null) {
			discountPercentage = new BigDecimal(parameters.get("discountPercentage").toString());
		}

		// Apply discount
		if (discountPercentage.compareTo(BigDecimal.ZERO) > 0) {
			BigDecimal discountAmount = total.multiply(discountPercentage)
					.divide(BigDecimal.valueOf(100));
			BigDecimal finalTotal = total.subtract(discountAmount);

			log.info("✅ [TimeBasedStrategy] Applied {}% discount based on parameters", discountPercentage);
			return finalTotal;
		}

		// If no discount defined
		log.info("ℹ [TimeBasedStrategy] No discount applied. Discount percentage = {}", discountPercentage);
		return total;
	}


	@Override
	public String getStrategyKey() {
		return "TIME_BASED";
	}
}
