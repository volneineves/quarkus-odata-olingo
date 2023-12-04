package com.store.service.storage;

import com.store.entity.Category;
import com.store.entity.Product;
import com.store.repository.CategoryRepository;
import com.store.repository.ProductRepository;
import com.store.service.mapper.CategoryMapper;
import com.store.service.mapper.ProductMapper;
import com.store.service.util.Util;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriParameter;

import java.util.List;
import java.util.function.Function;

import static com.store.provider.EdmProvider.*;

@ApplicationScoped
public class EntityStorage {

    @Inject
    ProductRepository productRepository;

    @Inject
    CategoryRepository categoryRepository;

    public EntityCollection retrieveEntities(EdmEntitySet edmEntitySet) throws ODataApplicationException {
        return switch (edmEntitySet.getName()) {
            case ES_PRODUCTS_NAME -> getEntityCollection(productRepository.listAll(), ProductMapper::convertToEntity);
            case ES_CATEGORIES_NAME ->
                    getEntityCollection(categoryRepository.listAll(), CategoryMapper::convertToEntity);
            default -> throw new ODataApplicationException("Invalid Entity Set Name", 404, null);
        };
    }

    public Entity retrieveEntity(EdmEntitySet edmEntitySet, List<UriParameter> keyParams) throws ODataApplicationException {
        return switch (edmEntitySet.getEntityType().getName()) {
            case ET_PRODUCT_NAME -> getProductEntity(keyParams);
            case ET_CATEGORY_NAME -> getCategoryEntity(keyParams);
            default -> throw new ODataApplicationException("Invalid Entity Type Name", 404, null);
        };
    }

    public Entity retrieveEntityByRelation(Entity sourceEntity, EdmEntityType relatedEntityType) {
        Property id = sourceEntity.getProperty("id");

        if (relatedEntityType.getName().equals(ET_CATEGORY_NAME)) {
            return findCategoryByProduct((Long) id.getValue());
        }

        throw new IllegalArgumentException("Invalid Related Entity Type Name");
    }

    public Entity retrieveEntityByRelation(Entity sourceEntity, EdmEntityType relatedEntityType, List<UriParameter> navKeyPredicates) throws ODataApplicationException {
        if (navKeyPredicates.isEmpty()) {
            return retrieveEntityByRelation(sourceEntity, relatedEntityType);
        } else {
            Property id = sourceEntity.getProperty("id");

            if (relatedEntityType.getName().equals(ET_PRODUCT_NAME)) {
                Long categoryId = (Long) id.getValue();
                Long productId = Util.extractId(navKeyPredicates, relatedEntityType.getName());
                return findProductByCategory(categoryId, productId);
            }
        }

        throw new IllegalArgumentException("Invalid Related Entity Type Name");
    }

    public EntityCollection retrieveEntitiesByRelation(Entity sourceEntity, EdmEntityType relatedEntityType) {
        Property id = sourceEntity.getProperty("id");

        if (relatedEntityType.getName().equals(ET_PRODUCT_NAME)) {
            return findProductsByCategory((Long) id.getValue());
        }
        throw new IllegalArgumentException("Invalid Related Entity Type Name");
    }


    private Entity getProductEntity(List<UriParameter> keyParams) throws ODataApplicationException {
        Long productId = Util.extractId(keyParams, ET_PRODUCT_NAME);
        return ProductMapper.convertToEntity(productRepository.findById(productId));
    }

    private Entity getCategoryEntity(List<UriParameter> keyParams) throws ODataApplicationException {
        Long categoryId = Util.extractId(keyParams, ET_CATEGORY_NAME);
        return CategoryMapper.convertToEntity(categoryRepository.findById(categoryId));
    }

    private EntityCollection findProductsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId);
        return getEntityCollection(category.getProducts(), ProductMapper::convertToEntity);
    }


    private Entity findProductByCategory(Long categoryId, Long productId) {
        Category category = categoryRepository.findById(categoryId);
        Product product = category.getProducts().stream().filter(prd -> prd.getId().equals(productId)).findFirst().orElseThrow();
        return ProductMapper.convertToEntity(product);
    }

    private Entity findCategoryByProduct(Long productId) {
        Product product = productRepository.findById(productId);
        return CategoryMapper.convertToEntity(product.getCategory());
    }


    private <T> EntityCollection getEntityCollection(List<T> entities, Function<T, Entity> mapper) {
        EntityCollection collection = new EntityCollection();
        collection.getEntities().addAll(entities.stream().map(mapper).toList());
        return collection;
    }
}
