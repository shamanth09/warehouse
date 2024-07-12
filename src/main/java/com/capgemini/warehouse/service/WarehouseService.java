package com.capgemini.warehouse.service;

import com.capgemini.warehouse.dto.ProductDTO;
import com.capgemini.warehouse.dto.ProductResponse;
import com.capgemini.warehouse.dto.SellProductRequest;
import com.capgemini.warehouse.model.Article;

import java.util.List;

public interface WarehouseService {

    void sellProduct(List<SellProductRequest> sellProductRequests);

    void saveArticles(List<Article> articles);

    void saveProducts(List<ProductDTO> productDTOS);

    List<ProductResponse> getProducts();

}
