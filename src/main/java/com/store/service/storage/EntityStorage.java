package com.store.service.storage;

import com.store.repository.ClientRepository;
import com.store.repository.ProductRepository;
import com.store.service.mapper.ClientMapper;
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
    ClientRepository clientRepository;

    public EntityCollection retrieveEntities(EdmEntitySet edmEntitySet) throws ODataApplicationException {
        return switch (edmEntitySet.getName()) {
            case ES_PRODUCTS_NAME -> getEntityCollection(productRepository.listAll(), ProductMapper::convertToEntity);
            case ES_CLIENT_NAME ->
                    getEntityCollection(clientRepository.listAll(), ClientMapper::convertToEntity);
            default -> throw new ODataApplicationException("Invalid Entity Set Name", 404, null);
        };
    }

    public Entity retrieveEntity(EdmEntitySet edmEntitySet, List<UriParameter> keyParams) throws ODataApplicationException {
        return switch (edmEntitySet.getEntityType().getName()) {
            case ET_PRODUCT_NAME -> getProductEntity(keyParams);
            case ET_CLIENT_NAME -> getClientEntity(keyParams);
            default -> throw new ODataApplicationException("Invalid Entity Type Name", 404, null);
        };
    }

    private Entity getProductEntity(List<UriParameter> keyParams) throws ODataApplicationException {
        Long productId = Util.extractId(keyParams, ET_PRODUCT_NAME);
        return ProductMapper.convertToEntity(productRepository.findById(productId));
    }

    private Entity getClientEntity(List<UriParameter> keyParams) throws ODataApplicationException {
        Long clientId = Util.extractId(keyParams, ET_CLIENT_NAME);
        return ClientMapper.convertToEntity(clientRepository.findById(clientId));
    }

    private <T> EntityCollection getEntityCollection(List<T> entities, Function<T, Entity> mapper) {
        EntityCollection collection = new EntityCollection();
        collection.getEntities().addAll(entities.stream().map(mapper).toList());
        return collection;
    }
}
