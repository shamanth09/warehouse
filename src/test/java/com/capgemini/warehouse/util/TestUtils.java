package com.capgemini.warehouse.util;

import com.capgemini.warehouse.dto.ArticleReq;
import com.capgemini.warehouse.dto.ProductReq;
import com.capgemini.warehouse.model.Article;
import com.capgemini.warehouse.model.Product;
import com.capgemini.warehouse.model.ProductArticle;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public enum ObjectType {
        PRODUCT,
        ARTICLE,
        PRODUCT_1_ARTICLE,
        PRODUCT_2_ARTICLE,
        ARTICLE_DTO,
        PRODUCT_DTO
    }

    public static Map<ObjectType, Object> getMockMap() {
        Map<ObjectType, Object> map = new HashMap<>();
        Product product1 = new Product(1L, "dining chair", 20.0);
        Product product2 = new Product(2L, "dining table", 40.0);

        List<Product> productList = List.of(product1, product2);
        map.put(ObjectType.PRODUCT, productList);

        Article article1 = new Article(1L, "leg", 12);
        Article article2 = new Article(2L, "screw", 17);
        Article article3 = new Article(3L, "seat", 2);
        Article article4 = new Article(4L, "table_top", 1);
        map.put(ObjectType.ARTICLE,List.of(article1,article2,article3,article4));

        ProductArticle productArticle1 = new ProductArticle(1L, product1, article1, 4);
        ProductArticle productArticle2 = new ProductArticle(1L, product1, article2, 8);
        ProductArticle productArticle3 = new ProductArticle(1L, product1, article3, 1);
        map.put(ObjectType.PRODUCT_1_ARTICLE ,List.of(productArticle1,productArticle2,productArticle3));

        ProductArticle productArticle4 = new ProductArticle(1L, product2, article1, 4);
        ProductArticle productArticle5 = new ProductArticle(1L, product2, article2, 8);
        ProductArticle productArticle6 = new ProductArticle(1L, product2, article4, 1);
        map.put(ObjectType.PRODUCT_2_ARTICLE ,List.of(productArticle4,productArticle5,productArticle6));

        ArticleReq articleReq = new ArticleReq("12","1");
        ProductReq productReq = new ProductReq("product 1", List.of(articleReq), 20);
        map.put(ObjectType.PRODUCT_DTO, List.of(productReq));

        return map;
    }

    public static <T> MultipartFile createMultipartFile(T t, String fileName) throws IOException {
        String json = objectMapper.writeValueAsString(t);
        InputStream inputStream = IOUtils.toInputStream(json, StandardCharsets.UTF_8);
        return new MockMultipartFile("file", fileName, "application/json", inputStream);
    }
}
