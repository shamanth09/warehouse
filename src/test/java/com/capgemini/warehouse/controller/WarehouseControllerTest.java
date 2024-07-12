package com.capgemini.warehouse.controller;

import com.capgemini.warehouse.dto.ArticleDTO;
import com.capgemini.warehouse.dto.ProductDTO;
import com.capgemini.warehouse.dto.ProductResponse;
import com.capgemini.warehouse.dto.SellProductRequest;
import com.capgemini.warehouse.model.Article;
import com.capgemini.warehouse.service.WarehouseService;
import com.capgemini.warehouse.util.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WarehouseControllerTest {

    @Mock
    private WarehouseService warehouseService;

    @InjectMocks
    private WarehouseController warehouseController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    private final static String ARTICLE = "ARTICLE";

    @Test
    public void test_upload_products() throws Exception {
        // Mocking multipart file
        ArticleDTO articleDTO = new ArticleDTO("12","1");
        ProductDTO productDTO = new ProductDTO("product 1", List.of(articleDTO), 20);
        List<ProductDTO> mockProducts = List.of(productDTO);
        String mockProductsJson = objectMapper.writeValueAsString(mockProducts);
        InputStream inputStream = IOUtils.toInputStream(mockProductsJson, "UTF-8");
        MultipartFile mockMultipartFile = new MockMultipartFile("file", "products.json", "application/json", inputStream);

        // Mocking service method
        doNothing().when(warehouseService).saveProducts(mockProducts);

        // Call the controller method
        String response = warehouseController.upload_products(mockMultipartFile);

        // Verify the interactions
        verify(warehouseService, times(1)).saveProducts(mockProducts);

        assertEquals("Products JSON file processed successfully.", response);

    }

    @Test
    public void test_save_products(){
        // Mocking multipart file
        ArticleDTO articleDTO = new ArticleDTO("12","1");
        ProductDTO productDTO = new ProductDTO("product 1", List.of(articleDTO), 20);
        List<ProductDTO> mockProducts = List.of(productDTO);

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
        List<Article> mockArticle = (List<Article>) TestUtils.getMockMap().get(ARTICLE);
        String mockArticleJson = objectMapper.writeValueAsString(mockArticle);
        InputStream inputStream = IOUtils.toInputStream(mockArticleJson, "UTF-8");
        MultipartFile mockMultipartFile = new MockMultipartFile("file", "inventory.json", "application/json", inputStream);

        // Mocking service method
        doNothing().when(warehouseService).saveArticles(mockArticle);

        // Call the controller method
        String response = warehouseController.upload_articles(mockMultipartFile);

        // Verify the interactions
        verify(warehouseService, times(1)).saveArticles(mockArticle);

        assertEquals("Articles JSON file processed successfully.", response);


    }

    @Test
    public void test_save_articles() {
        // Mocking multipart file
        List<Article> mockArticle = (List<Article>) TestUtils.getMockMap().get(ARTICLE);

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
        ProductResponse product1 = new ProductResponse(1,"dining chair",4);
        ProductResponse product2 = new ProductResponse(2,"dining table",5);
        List<ProductResponse> productList = List.of(product1, product2);

        // Mocking the service method
        when(warehouseService.getProducts()).thenReturn(productList);

        // Call the controller method
        List<ProductResponse> result = warehouseController.getProducts();

        // Verify the result
        assertEquals(2, result.size());
        assertEquals("dining chair", result.get(0).getName());
        assertEquals("dining table", result.get(1).getName());
    }

    @Test
    void testSellProduct() {
        // Mock data
        SellProductRequest request1 = new SellProductRequest(1L, 1L);
        List<SellProductRequest> requestList = List.of(request1);

        // Call the controller method
        String result = warehouseController.sellProduct(requestList);

        // Verify the result
        assertEquals("Product sold successfully.", result);

        // Verify that warehouseService.sellProduct() was called with the correct argument
        verify(warehouseService).sellProduct(requestList);
    }
}