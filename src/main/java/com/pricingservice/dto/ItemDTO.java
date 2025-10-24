package com.pricingservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {

	
	
    public ItemDTO(@NotBlank(message = "Product ID is mandatory") String productId,
			@NotBlank(message = "Product name is mandatory") String productName,
			@NotNull(message = "Quantity is mandatory") @Min(value = 1, message = "Quantity must be at least 1") Integer quentity) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.quentity = quentity;
	}
    
    public ItemDTO() {};

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getQuentity() {
		return quentity;
	}

	public void setQuentity(Integer quentity) {
		this.quentity = quentity;
	}

	@NotBlank(message = "Product ID is mandatory")
    private String productId;

    @NotBlank(message = "Product name is mandatory")
    private String productName;

    @NotNull(message = "Quantity is mandatory")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quentity;
}
