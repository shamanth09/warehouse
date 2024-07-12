package com.capgemini.warehouse.service.impl;

import com.capgemini.warehouse.data.ArticleRepository;
import com.capgemini.warehouse.data.ProductArticleRepository;
import com.capgemini.warehouse.data.ProductRepository;
import com.capgemini.warehouse.dto.ArticleDTO;
import com.capgemini.warehouse.dto.ProductDTO;
import com.capgemini.warehouse.dto.ProductResponse;
import com.capgemini.warehouse.dto.SellProductRequest;
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


    public List<ProductResponse> getProducts() {
        return producible_Product(productRepository.findAll(), articleRepository.findAll());
    }


    @Transactional
    public void sellProduct(List<SellProductRequest> sellProductRequests) {
        for(SellProductRequest sellProductRequest : sellProductRequests) {
            Product product = productRepository.findById(sellProductRequest.getProductId()).orElseThrow(() -> new NoSuchProductException("requested product is not found"));
            validateStockAvailabilityAndUpdate(productArticleRepository.findByProduct(product), sellProductRequest.getQuantity());
        }
    }

    @Transactional
    public void saveArticles(List<Article> articles) {
        articleRepository.saveAll(articles);
    }

    @Transactional
    public void saveProducts(List<ProductDTO> productDTOS) {
        saveProductsToRepository(validateProduct(productDTOS));
    }

    private void saveProductsToRepository(List<ProductDTO> productDTOS) {
        List<Product> products = new ArrayList<>();
        List<ProductArticle> productArticles = new ArrayList<>();
        Map<Long, Article> articleMap = getArticleMap(articleRepository.findAll());
        for (ProductDTO productDTO : productDTOS) {
            Product product = new Product();
            product.setName(productDTO.getName());
            product.setPrice(productDTO.getPrice());
            products.add(product);
            for (ArticleDTO articleDTO : productDTO.getArticles()){
                ProductArticle productArticle = new ProductArticle();
                productArticle.setProduct(product);
                Article article = articleMap.get(Long.parseLong(articleDTO.getArt_id()));
                if(article == null)
                    throw new ArticleNotPresentException("requested article is not found");
                productArticle.setArticle(article);
                productArticle.setQuantity(Integer.parseInt(articleDTO.getAmount_of()));
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
                throw new InsufficientStockException("Not enough stock available for article ");
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

    private List<ProductDTO> validateProduct(List<ProductDTO> products) {
        List<ProductDTO> productDTOS = new ArrayList<>();
        List<ProductDTO> duplicateProductDTOS = new ArrayList<>();

        for(ProductDTO productDTO : products){
            Optional<Product> product = productRepository.findByName(productDTO.getName().toLowerCase());
            if(product.isEmpty()){
                productDTOS.add(productDTO);
            }else{
                duplicateProductDTOS.add(productDTO);
            }
        }
        processDuplicateProducts(duplicateProductDTOS);
        return productDTOS;
    }

    private List<ProductResponse> producible_Product(List<Product> products, List<Article> articles) {
        List<ProductResponse> productResponses = new ArrayList<>();
        Map<Long, Article> articleMap = getArticleMap(articles);
        for (Product product : products) {
            ProductResponse productResponse = new ProductResponse();
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

    private void processDuplicateProducts(List<ProductDTO> duplicateProductDTOS) {
        //todo : process duplicate products and return to client as unprocessed as duplicate.
    }
}
