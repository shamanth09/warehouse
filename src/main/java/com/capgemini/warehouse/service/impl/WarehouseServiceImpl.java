package com.capgemini.warehouse.service.impl;

import com.capgemini.warehouse.data.ArticleRepository;
import com.capgemini.warehouse.data.ProductArticleRepository;
import com.capgemini.warehouse.data.ProductRepository;
import com.capgemini.warehouse.dto.ArticleReq;
import com.capgemini.warehouse.dto.ProductReq;
import com.capgemini.warehouse.dto.ProductRes;
import com.capgemini.warehouse.dto.SellProductReq;
import com.capgemini.warehouse.exception.ArticleNotPresentException;
import com.capgemini.warehouse.exception.InsufficientStockException;
import com.capgemini.warehouse.exception.NoSuchProductException;
import com.capgemini.warehouse.model.Article;
import com.capgemini.warehouse.model.Product;
import com.capgemini.warehouse.model.ProductArticle;
import com.capgemini.warehouse.service.WarehouseService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductArticleRepository productArticleRepository;


    public List<ProductRes> getProducts() {
        return producible_Product(productRepository.findAll(), articleRepository.findAll());
    }


    @Transactional
    public void sellProduct(List<SellProductReq> sellProductReqs) {
        for(SellProductReq sellProductReq : sellProductReqs) {
            Product product = productRepository.findById(sellProductReq.getProductId()).orElseThrow(() -> new NoSuchProductException("requested product is not found"));
            validateStockAvailabilityAndUpdate(productArticleRepository.findByProduct(product), sellProductReq.getQuantity());
        }
    }

    @Transactional
    public void saveArticles(List<Article> articles) {
        Map<Long,Article> articleMap = this.getArticleMap(articleRepository.findAll());
        for (Article article : articles) {
            if (articleMap.containsKey(article.getArt_id())) {
                article.setStock(articleMap.get(article.getArt_id()).getStock() + article.getStock());
            }
        }
        articleRepository.saveAll(articles);
    }

    @Transactional
    public void saveProducts(List<ProductReq> productReqs) {
        saveProductsToRepository(validateProduct(productReqs));
    }

    private void saveProductsToRepository(List<ProductReq> productReqs) {
        List<Product> products = new ArrayList<>();
        List<ProductArticle> productArticles = new ArrayList<>();
        Map<Long, Article> articleMap = getArticleMap(articleRepository.findAll());
        for (ProductReq productReq : productReqs) {
            Product product = new Product();
            product.setName(productReq.getName());
            product.setPrice(productReq.getPrice());
            products.add(product);
            for (ArticleReq articleReq : productReq.getArticles()){
                ProductArticle productArticle = new ProductArticle();
                productArticle.setProduct(product);
                Article article = articleMap.get(Long.parseLong(articleReq.getArt_id()));
                if(article == null)
                    throw new ArticleNotPresentException("requested article is not found");
                productArticle.setArticle(article);
                productArticle.setQuantity(Integer.parseInt(articleReq.getAmount_of()));
                productArticles.add(productArticle);
            }
        }
        productRepository.saveAll(products);
        productArticleRepository.saveAll(productArticles);
    }

    private void validateStockAvailabilityAndUpdate(List<ProductArticle> productArticles, long quantity) {
        Map<Long, Long> articleStockUpdate = new HashMap<>();
        Map<Long, Article> articleMap = getArticleMap(articleRepository.findAll());
        for (ProductArticle productArticle : productArticles) {
            Article article = articleMap.get(productArticle.getArticle().getArt_id());
            long requiredQuantity = productArticle.getQuantity() * quantity;
            if (article == null || requiredQuantity > article.getStock()) {
                throw new InsufficientStockException("Sorry! please come back later");
            }
            articleStockUpdate.put(article.getArt_id(), articleStockUpdate.getOrDefault(article.getArt_id(), article.getStock()) - requiredQuantity);
        }

        for (Map.Entry<Long, Long> entry : articleStockUpdate.entrySet()) {
            Article article = articleMap.get(entry.getKey());
            if (article != null) {
                article.setStock(entry.getValue());
                articleRepository.save(article);
            }
        }
    }

    private List<ProductReq> validateProduct(List<ProductReq> products) {
        List<ProductReq> productReqs = new ArrayList<>();
        List<ProductReq> duplicateProductReqs = new ArrayList<>();

        for(ProductReq productReq : products){
            Optional<Product> product = productRepository.findByName(productReq.getName().toLowerCase());
            if(product.isEmpty()){
                productReqs.add(productReq);
            }else{
                duplicateProductReqs.add(productReq);
            }
        }
        processDuplicateProducts(duplicateProductReqs);
        return productReqs;
    }

    private List<ProductRes> producible_Product(List<Product> products, List<Article> articles) {
        List<ProductRes> productResponses = new ArrayList<>();
        Map<Long, Article> articleMap = getArticleMap(articles);
        for (Product product : products) {
            ProductRes productResponse = new ProductRes();
            List<ProductArticle> productArticles = productArticleRepository.findByProduct(product);
            long minProductCount = Integer.MAX_VALUE;
            for (ProductArticle productArticle : productArticles) {
                Article article = articleMap.get(productArticle.getArticle().getArt_id());
                if (article != null) {
                    long count = article.getStock() / productArticle.getQuantity();
                    minProductCount = Math.min(minProductCount, count);
                }
            }
            productResponse.setQuantity(minProductCount > 0 ? minProductCount : 0);
            productResponse.setName(product.getName());
            productResponse.setId(product.getId());
            productResponses.add(productResponse);
        }
        return productResponses;
    }

    private Map<Long, Article> getArticleMap(List<Article> articles) {
        return articles.parallelStream().collect(Collectors.toMap(Article::getArt_id, article -> article));
    }

    private void processDuplicateProducts(List<ProductReq> duplicateProductReqs) {
        //todo : process duplicate products and return to client as unprocessed as duplicate.
    }
}
