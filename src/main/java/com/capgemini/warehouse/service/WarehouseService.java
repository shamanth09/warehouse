package com.capgemini.warehouse.service;

import com.capgemini.warehouse.dto.ProductReq;
import com.capgemini.warehouse.dto.ProductRes;
import com.capgemini.warehouse.dto.SellProductReq;
import com.capgemini.warehouse.model.Article;

import java.util.List;

public interface WarehouseService {

    void sellProduct(List<SellProductReq> sellProductReqs);

    void saveArticles(List<Article> articles);

    void saveProducts(List<ProductReq> productReqs);

    List<ProductRes> getProducts();

}
