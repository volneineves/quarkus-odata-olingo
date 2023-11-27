package com.store.service.mapper;

import com.store.entity.Product;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;

import static com.store.service.util.Util.createId;

public class ProductMapper {

    public static Entity convertToEntity(Product product) {
        Entity entity = new Entity()
                .addProperty(new Property(null, "id", ValueType.PRIMITIVE, product.getId()))
                .addProperty(new Property(null, "name", ValueType.PRIMITIVE, product.getName()))
                .addProperty(new Property(null, "description", ValueType.PRIMITIVE, product.getDescription()))
                .addProperty(new Property(null, "price", ValueType.PRIMITIVE, product.getPrice()))
                .addProperty(new Property(null, "stockQuantity", ValueType.PRIMITIVE, product.getStockQuantity()))
                .addProperty(new Property(null, "category", ValueType.PRIMITIVE, product.getCategory().getName()))
                .addProperty(new Property(null, "createdAt", ValueType.PRIMITIVE, product.getCreatedAt()))
                .addProperty(new Property(null, "updatedAt", ValueType.PRIMITIVE, product.getUpdatedAt()));

        entity.setId(createId("Products", product.getId()));
        return entity;
    }
}
