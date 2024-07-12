package com.capgemini.warehouse.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private String name;
    @JsonProperty("articles")
    private List<ArticleDTO> articles;
    private double price;



}
