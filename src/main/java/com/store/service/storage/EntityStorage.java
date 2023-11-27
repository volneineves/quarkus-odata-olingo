package com.store.service.storage;

import com.store.service.mapper.ProductMapper;
import com.store.repository.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriParameter;

import java.util.List;

import static com.store.provider.EdmProvider.ES_PRODUCTS_NAME;
import static com.store.provider.EdmProvider.ET_PRODUCT_NAME;
import static com.store.service.util.Util.getEntity;

@ApplicationScoped
public class EntityStorage {

    @Inject
    ProductRepository repository;

    public EntityCollection readEntitySetData(EdmEntitySet edmEntitySet) {
        return switch (edmEntitySet.getName()) {
            case ES_PRODUCTS_NAME -> getProducts();
            default -> null;
        };

    }

    public Entity readEntityData(EdmEntitySet edmEntitySet, List<UriParameter> keyParams) throws ODataApplicationException {

        EdmEntityType edmEntityType = edmEntitySet.getEntityType();

        return switch (edmEntityType.getName()) {
            case ET_PRODUCT_NAME -> getEntity(edmEntityType, keyParams, getProducts());
            default -> null;
        };

    }

    private EntityCollection getProducts() {
        EntityCollection productsCollection = new EntityCollection();

        productsCollection.getEntities().addAll(
                repository.listAll().stream()
                        .map(ProductMapper::convertToEntity)
                        .toList()
        );

        return productsCollection;
    }
}
