package com.capgemini.warehouse.service.impl;

import com.capgemini.warehouse.data.ArticleRepository;
import com.capgemini.warehouse.data.ProductArticleRepository;
import com.capgemini.warehouse.data.ProductRepository;
import com.capgemini.warehouse.dto.ProductReq;
import com.capgemini.warehouse.dto.ProductRes;
import com.capgemini.warehouse.dto.SellProductReq;
import com.capgemini.warehouse.exception.InsufficientStockException;
import com.capgemini.warehouse.model.Article;
import com.capgemini.warehouse.model.Product;
import com.capgemini.warehouse.model.ProductArticle;
import com.capgemini.warehouse.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WarehouseServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductArticleRepository productArticleRepository;

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private WarehouseServiceImpl warehouseService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getProducts() {
        // Mock data
        Map<TestUtils.ObjectType, Object> productInventoryMockMap = TestUtils.getMockMap();

        // Mocking the repository method
        when(articleRepository.findAll()).thenReturn((List<Article>) productInventoryMockMap.get(TestUtils.ObjectType.ARTICLE));
        when(productRepository.findAll()).thenReturn((List<Product>) productInventoryMockMap.get(TestUtils.ObjectType.PRODUCT));
        when(productArticleRepository.findByProduct(((List<Product>) productInventoryMockMap.get(TestUtils.ObjectType.PRODUCT)).get(0))).thenReturn((List<ProductArticle>)productInventoryMockMap.get(TestUtils.ObjectType.PRODUCT_1_ARTICLE));
        when(productArticleRepository.findByProduct(((List<Product>) productInventoryMockMap.get(TestUtils.ObjectType.PRODUCT)).get(1))).thenReturn((List<ProductArticle>)productInventoryMockMap.get(TestUtils.ObjectType.PRODUCT_2_ARTICLE));

        // Call the service method
        List<ProductRes> result = warehouseService.getProducts();

        // Verify the result
        assertEquals(2, result.size());
        assertEquals("dining chair", result.get(0).getName());
        assertEquals("dining table", result.get(1).getName());
        assertEquals(2, result.get(0).getQuantity());
        assertEquals(1, result.get(1).getQuantity());
    }

    @Test
    void sellProduct() {
        // Mock data
        Map<TestUtils.ObjectType,Object> map = TestUtils.getMockMap();
        Product product = ((List<Product>)map.get(TestUtils.ObjectType.PRODUCT)).get(0);
        List<SellProductReq> requestList = List.of(new SellProductReq(1L, 1));
        List<Article> articles = (List<Article>)map.get(TestUtils.ObjectType.ARTICLE);

        // Mocking repository methods
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(articleRepository.findAll()).thenReturn(articles);
        when(productArticleRepository.findByProduct(product)).thenReturn((List<ProductArticle>)map.get(TestUtils.ObjectType.PRODUCT_1_ARTICLE));

        // Call the service method
        warehouseService.sellProduct(requestList);

        // Verify interactions
        verify(productRepository).findById(1L);
        verify(articleRepository).findAll();
        verify(articleRepository).save(articles.get(0));
        verify(productArticleRepository).findByProduct(product);

        assertEquals(8, articles.get(0).getStock());
    }

    @Test
    void sellProductWithLessInventory() {
        // Mock data
        Map<TestUtils.ObjectType,Object> map = TestUtils.getMockMap();
        Product product = ((List<Product>)map.get(TestUtils.ObjectType.PRODUCT)).get(0);
        List<Article> articles = (List<Article>)map.get(TestUtils.ObjectType.ARTICLE);
        List<SellProductReq> requestList = List.of(new SellProductReq(1L, 200));

        // Mocking repository methods
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(articleRepository.findAll()).thenReturn(articles);
        when(productArticleRepository.findByProduct(product)).thenReturn((List<ProductArticle>)map.get(TestUtils.ObjectType.PRODUCT_1_ARTICLE));

        // Call the service method
        assertThrows(InsufficientStockException.class,() -> warehouseService.sellProduct(requestList));

        // Verify interactions
        verify(productRepository).findById(1L);
        verify(articleRepository).findAll();
        verify(productArticleRepository).findByProduct(product);
    }

    @Test
    void saveArticles() {
        // Mock data
        Map<TestUtils.ObjectType,Object> map = TestUtils.getMockMap();
        List<Article> articles = (List<Article>)map.get(TestUtils.ObjectType.ARTICLE);

        // Mocking repository methods
        when(articleRepository.findAll()).thenReturn(articles);

        // Call the service method
        warehouseService.saveArticles(articles);

        // Verify repository method invocation
        verify(articleRepository).saveAll(articles);
    }

    @Test
    void saveProducts() {
        // Mock data
        Map<TestUtils.ObjectType,Object> map = TestUtils.getMockMap();
        List<ProductReq> productReqs = new ArrayList<>();
        List<Article> articles = (List<Article>)map.get(TestUtils.ObjectType.ARTICLE);
        List<Product> products = ((List<Product>)map.get(TestUtils.ObjectType.PRODUCT));

        // Mock repository methods as needed
        when(articleRepository.findAll()).thenReturn(articles);
        when(productRepository.saveAll(any())).thenReturn(products);

        // Call the service method
        warehouseService.saveProducts(productReqs);

        // Verify repository method invocation
        verify(productRepository).saveAll(any());
        verify(productArticleRepository).saveAll(any());
    }


}