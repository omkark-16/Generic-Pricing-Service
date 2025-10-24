package com.pricingservice.strategy;

import com.pricingservice.dto.ItemDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class WholesaleStrategy implements PricingStrategy {

	@Override
	public BigDecimal applyStrategy(List<ItemDTO> items, Map<String, Object> parameters) {
		BigDecimal totalQuantity = BigDecimal.ZERO;
		for (ItemDTO item : items) {
			totalQuantity = totalQuantity.add(BigDecimal.valueOf(item.getQuentity()));
		}

		BigDecimal total = (BigDecimal) parameters.get("baseTotal");
		if (totalQuantity.compareTo(BigDecimal.valueOf(100)) >= 0) {
			return total.subtract(total.multiply(BigDecimal.valueOf(15)).divide(BigDecimal.valueOf(100)));
		}
		return total;
	}

	@Override
	public String getStrategyKey() {
		return "WHOLESALE";
	}
}
