package com.capgemini.warehouse.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductReq {

    private String name;
    @JsonProperty("articles")
    private List<ArticleReq> articles;
    private double price;



}
