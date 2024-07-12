package com.capgemini.warehouse.util;

import com.capgemini.warehouse.model.Article;
import com.capgemini.warehouse.model.Product;
import com.capgemini.warehouse.model.ProductArticle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestUtils {

    private final static String PRODUCT = "PRODUCT";
    private final static String ARTICLE = "ARTICLE";
    private final static String PRODUCT_1_ARTICLE = "PRODUCT_1_ARTICLE";
    private final static String PRODUCT_2_ARTICLE = "PRODUCT_2_ARTICLE";

    public static Map<String, Object> getMockMap() {
        Map<String, Object> map = new HashMap<>();
        Product product1 = new Product(1L, "dining chair", 20.0);
        Product product2 = new Product(2L, "dining table", 40.0);

        List<Product> productList = List.of(product1, product2);
        map.put(PRODUCT, productList);

        Article article1 = new Article(1L, "leg", 12);
        Article article2 = new Article(2L, "screw", 17);
        Article article3 = new Article(3L, "seat", 2);
        Article article4 = new Article(4L, "table_top", 1);
        map.put(ARTICLE,List.of(article1,article2,article3,article4));

        ProductArticle productArticle1 = new ProductArticle(1L, product1, article1, 4);
        ProductArticle productArticle2 = new ProductArticle(1L, product1, article2, 8);
        ProductArticle productArticle3 = new ProductArticle(1L, product1, article3, 1);
        map.put(PRODUCT_1_ARTICLE ,List.of(productArticle1,productArticle2,productArticle3));

        ProductArticle productArticle4 = new ProductArticle(1L, product2, article1, 4);
        ProductArticle productArticle5 = new ProductArticle(1L, product2, article2, 8);
        ProductArticle productArticle6 = new ProductArticle(1L, product2, article4, 1);
        map.put(PRODUCT_2_ARTICLE ,List.of(productArticle4,productArticle5,productArticle6));
        return map;
    }
}
