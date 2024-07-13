package com.capgemini.warehouse.controller;

import com.capgemini.warehouse.dto.ProductReq;
import com.capgemini.warehouse.dto.ProductRes;
import com.capgemini.warehouse.dto.SellProductReq;
import com.capgemini.warehouse.model.Article;
import com.capgemini.warehouse.service.WarehouseService;
import com.capgemini.warehouse.util.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WarehouseControllerTest {

    @Mock
    private WarehouseService warehouseService;

    @InjectMocks
    private WarehouseController warehouseController;


    @Test
    public void test_upload_products() throws Exception {
        // Mocking multipart file
        List<ProductReq> mockProducts = (List<ProductReq>) TestUtils.getMockMap().get(TestUtils.ObjectType.PRODUCT_DTO);

        // Mocking service method
        doNothing().when(warehouseService).saveProducts(mockProducts);

        // Call the controller method
        String response = warehouseController.upload_products(TestUtils.createMultipartFile(mockProducts,"products.json"));

        // Verify the interactions
        verify(warehouseService, times(1)).saveProducts(mockProducts);

        assertEquals("Products JSON file processed successfully.", response);

    }

    @Test
    public void test_save_products(){
        // Mocking multipart file
        List<ProductReq> mockProducts = (List<ProductReq>) TestUtils.getMockMap().get(TestUtils.ObjectType.PRODUCT_DTO);

        // Mocking service method
        doNothing().when(warehouseService).saveProducts(mockProducts);

        // Call the controller method
        String response = warehouseController.products(mockProducts);

        // Verify the interactions
        verify(warehouseService, times(1)).saveProducts(mockProducts);

        assertEquals("Products processed successfully.", response);
    }


    @Test
    public void test_upload_articles() throws Exception {
        // Mocking multipart file
        List<Article> mockArticle = (List<Article>) TestUtils.getMockMap().get(TestUtils.ObjectType.ARTICLE);

        // Mocking service method
        doNothing().when(warehouseService).saveArticles(mockArticle);

        // Call the controller method
        String response = warehouseController.upload_articles(TestUtils.createMultipartFile(mockArticle,"inventory.json"));

        // Verify the interactions
        verify(warehouseService, times(1)).saveArticles(mockArticle);

        assertEquals("Articles JSON file processed successfully.", response);


    }

    @Test
    public void test_save_articles() {
        // Mocking multipart file
        List<Article> mockArticle = (List<Article>) TestUtils.getMockMap().get(TestUtils.ObjectType.ARTICLE);

        // Mocking service method
        doNothing().when(warehouseService).saveArticles(mockArticle);

        // Call the controller method
        String response = warehouseController.articles(mockArticle);

        // Verify the interactions
        verify(warehouseService, times(1)).saveArticles(mockArticle);

        assertEquals("Articles processed successfully.", response);


    }

    @Test
    void testGetProducts() {
        // Mock data
        ProductRes product1 = new ProductRes(1,"dining chair",4);
        ProductRes product2 = new ProductRes(2,"dining table",5);
        List<ProductRes> productList = List.of(product1, product2);

        // Mocking the service method
        when(warehouseService.getProducts()).thenReturn(productList);

        // Call the controller method
        List<ProductRes> result = warehouseController.getProducts();

        // Verify the result
        assertEquals(2, result.size());
        assertEquals("dining chair", result.get(0).getName());
        assertEquals("dining table", result.get(1).getName());
    }

    @Test
    void testSellProduct() {
        // Mock data
        SellProductReq request1 = new SellProductReq(1L, 1L);
        List<SellProductReq> requestList = List.of(request1);

        // Call the controller method
        String result = warehouseController.sellProduct(requestList);

        // Verify the result
        assertEquals("Product sold successfully.", result);

        // Verify that warehouseService.sellProduct() was called with the correct argument
        verify(warehouseService).sellProduct(requestList);
    }
}