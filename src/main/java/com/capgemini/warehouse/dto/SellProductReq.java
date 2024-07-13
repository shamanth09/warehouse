package com.capgemini.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data@AllArgsConstructor
public class SellProductReq {
    private long productId;
    private long quantity;
}
