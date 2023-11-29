
package com.store.service.mapper;

import com.store.entity.Category;
import com.store.entity.Product;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;

import static com.store.service.util.Util.createId;

public class CategoryMapper {

    public static Entity convertToEntity(Category category) {

        Entity entity = new Entity()
                .addProperty(new Property(null, "id", ValueType.PRIMITIVE, category.getId()))
                .addProperty(new Property(null, "name", ValueType.PRIMITIVE, category.getName()))
                .addProperty(new Property(null, "description", ValueType.PRIMITIVE, category.getDescription()))
                .addProperty(new Property(null, "createdAt", ValueType.PRIMITIVE, category.getCreatedAt()))
                .addProperty(new Property(null, "updatedAt", ValueType.PRIMITIVE, category.getUpdatedAt()));

        entity.setId(createId("Categories", category.getId()));
        return entity;
    }
}
