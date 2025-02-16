package com.capgemini.warehouse.controller;


import com.capgemini.warehouse.dto.ProductReq;
import com.capgemini.warehouse.dto.ProductRes;
import com.capgemini.warehouse.dto.SellProductReq;
import com.capgemini.warehouse.model.Article;
import com.capgemini.warehouse.service.WarehouseService;
import com.capgemini.warehouse.util.WarehouseUtility;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/warehouse")
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/upload/products")
    public String upload_products(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        WarehouseUtility.validateFile(multipartFile);
        List<ProductReq> products = objectMapper.readValue(multipartFile.getInputStream(),
                new TypeReference<>() {});
        warehouseService.saveProducts(products);
        return "Products JSON file processed successfully.";
    }

    @PostMapping("/upload/articles")
    public String upload_articles(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        WarehouseUtility.validateFile(multipartFile);
        List<Article> articles = objectMapper.readValue(multipartFile.getInputStream(),
                new TypeReference<>() {});
        warehouseService.saveArticles(articles);
        return "Articles JSON file processed successfully.";
    }

    @PostMapping("/products")
    public String products(@RequestBody List<ProductReq> productReqs){
        warehouseService.saveProducts(productReqs);
        return "Products processed successfully.";
    }

    @PostMapping("/articles")
    public String articles(@RequestBody List<Article> articles) {
        warehouseService.saveArticles(articles);
        return "Articles processed successfully.";
    }

    @GetMapping("/getProducts")
    public List<ProductRes> getProducts() {
        return warehouseService.getProducts();
    }

    @PostMapping("/sell")
    public String sellProduct(@RequestBody List<SellProductReq> request) {
        warehouseService.sellProduct(request);
        return "Product sold successfully.";
    }
}
