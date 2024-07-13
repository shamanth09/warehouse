package com.capgemini.warehouse.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data@AllArgsConstructor
@NoArgsConstructor
public class ArticleReq {
    @JsonProperty("art_id")
    private String art_id;
    @JsonProperty("amount_of")
    private String amount_of;
}
