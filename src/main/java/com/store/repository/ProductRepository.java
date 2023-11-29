package com.store.repository;

import com.store.entity.Product;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.olingo.commons.api.data.Property;

import java.util.List;

@ApplicationScoped
public class ProductRepository implements PanacheRepositoryBase<Product, Long> {
    public List<Product> listAllByCategoryId(Long categoryId) {
        return list("category.id", categoryId);
    }
}