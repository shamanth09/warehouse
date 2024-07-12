package com.capgemini.warehouse.data;

import com.capgemini.warehouse.model.Product;
import com.capgemini.warehouse.model.ProductArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductArticleRepository extends JpaRepository<ProductArticle, Long> {

    List<ProductArticle> findByProduct(Product product);

}
