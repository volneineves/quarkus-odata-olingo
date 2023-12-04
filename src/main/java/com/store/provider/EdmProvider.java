package com.store.provider;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.*;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.ODataApplicationException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class EdmProvider extends CsdlAbstractEdmProvider {

    // Service Namespace
    public static final String NAMESPACE = "OData.Demo";

    // EDM Container
    public static final String CONTAINER_NAME = "Container";
    public static final FullQualifiedName CONTAINER = new FullQualifiedName(NAMESPACE, CONTAINER_NAME);

    // Entity Types Names
    public static final String ET_PRODUCT_NAME = "product";
    public static final String ET_CATEGORY_NAME = "category";
    public static final FullQualifiedName ET_PRODUCT_FQN = new FullQualifiedName(NAMESPACE, ET_PRODUCT_NAME);
    public static final FullQualifiedName ET_CATEGORY_FQN = new FullQualifiedName(NAMESPACE, ET_CATEGORY_NAME);

    // Entity Set Names
    public static final String ES_PRODUCTS_NAME = "products";
    public static final String ES_CATEGORIES_NAME = "categories";

    @Override
    public CsdlEntityType getEntityType(FullQualifiedName entityTypeName) {

        if (entityTypeName.equals(ET_PRODUCT_FQN)) {
            return prepareProductEntityType();
        } else if (entityTypeName.equals(ET_CATEGORY_FQN)) {
            return prepareCategoryEntityType();
        }

        return null;
    }

    @Override
    public CsdlEntitySet getEntitySet(FullQualifiedName entityContainer, String entitySetName) throws ODataApplicationException {
        if (entityContainer.equals(CONTAINER)) {
            if (entitySetName.equals(ES_PRODUCTS_NAME)) {
                return createProductsEntitySet();
            } else if (entitySetName.equals(ES_CATEGORIES_NAME)) {
                return createCategoriesEntitySet();
            }
        }
        return null;
    }

    private CsdlEntitySet createProductsEntitySet() {
        CsdlEntitySet entitySet = new CsdlEntitySet();
        entitySet.setName(ES_PRODUCTS_NAME);
        entitySet.setType(ET_PRODUCT_FQN);

        // Define navigation property binding for Products
        CsdlNavigationPropertyBinding navPropBinding = new CsdlNavigationPropertyBinding();
        navPropBinding.setTarget(ES_CATEGORIES_NAME); // Target entitySet where the navigation property points to
        navPropBinding.setPath(ET_CATEGORY_NAME); // Path from entity type to navigation property
        entitySet.setNavigationPropertyBindings(Collections.singletonList(navPropBinding));

        return entitySet;
    }

    private CsdlEntitySet createCategoriesEntitySet() {
        CsdlEntitySet entitySet = new CsdlEntitySet();
        entitySet.setName(ES_CATEGORIES_NAME);
        entitySet.setType(ET_CATEGORY_FQN);

        // Define navigation property binding for Categories
        CsdlNavigationPropertyBinding navPropBinding = new CsdlNavigationPropertyBinding();
        navPropBinding.setTarget(ES_PRODUCTS_NAME); // Target entitySet where the navigation property points to
        navPropBinding.setPath(ES_PRODUCTS_NAME); // Path from entity type to navigation property
        entitySet.setNavigationPropertyBindings(Collections.singletonList(navPropBinding));

        return entitySet;
    }

    @Override
    public CsdlEntityContainer getEntityContainer() throws ODataApplicationException {
        List<CsdlEntitySet> entitySets = new ArrayList<>();
        entitySets.add(getEntitySet(CONTAINER, ES_PRODUCTS_NAME));
        entitySets.add(getEntitySet(CONTAINER, ES_CATEGORIES_NAME));

        CsdlEntityContainer entityContainer = new CsdlEntityContainer();
        entityContainer.setName(CONTAINER_NAME);
        entityContainer.setEntitySets(entitySets);
        return entityContainer;
    }

    @Override
    public List<CsdlSchema> getSchemas() throws ODataApplicationException {
        CsdlSchema schema = new CsdlSchema();
        schema.setNamespace(NAMESPACE);

        List<CsdlEntityType> entityTypes = new ArrayList<>();
        entityTypes.add(getEntityType(ET_PRODUCT_FQN));
        entityTypes.add(getEntityType(ET_CATEGORY_FQN));
        schema.setEntityTypes(entityTypes);

        schema.setEntityContainer(getEntityContainer());

        List<CsdlSchema> schemas = new ArrayList<>();
        schemas.add(schema);
        return schemas;
    }

    @Override
    public CsdlEntityContainerInfo getEntityContainerInfo(FullQualifiedName entityContainerName) {
        // Odata metadata info: http://localhost:8080/odata?$metadata
        if (entityContainerName == null || entityContainerName.equals(CONTAINER)) {
            CsdlEntityContainerInfo entityContainerInfo = new CsdlEntityContainerInfo();
            entityContainerInfo.setContainerName(CONTAINER);
            return entityContainerInfo;
        }

        return null;
    }

    private static CsdlEntityType prepareProductEntityType() {
        // create EntityType properties
        CsdlProperty id = new CsdlProperty().setName("id").setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
        CsdlProperty name = new CsdlProperty().setName("name").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty description = new CsdlProperty().setName("description").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty price = new CsdlProperty().setName("price").setType(EdmPrimitiveTypeKind.Double.getFullQualifiedName());
        CsdlProperty stockQuantity = new CsdlProperty().setName("stockQuantity").setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
        CsdlProperty categoryId = new CsdlProperty().setName("categoryId").setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
        CsdlProperty createdAt = new CsdlProperty().setName("createdAt").setType(EdmPrimitiveTypeKind.Date.getFullQualifiedName());
        CsdlProperty updatedAt = new CsdlProperty().setName("updatedAt").setType(EdmPrimitiveTypeKind.Date.getFullQualifiedName());

        // create CsdlPropertyRef for key element
        CsdlPropertyRef propertyRef = new CsdlPropertyRef();
        propertyRef.setName("id");

        // Navigation
        CsdlNavigationProperty navProp = new CsdlNavigationProperty()
                .setName(ET_CATEGORY_NAME)
                .setType(ET_CATEGORY_FQN)
                .setNullable(true)
                .setPartner(ES_PRODUCTS_NAME);
        List<CsdlNavigationProperty> navPropList = new ArrayList<>();
        navPropList.add(navProp);

        // configure EntityType
        CsdlEntityType entityType = new CsdlEntityType();
        entityType.setName(ET_PRODUCT_NAME);
        entityType.setProperties(List.of(id, name, description, price, stockQuantity, categoryId, createdAt, updatedAt));
        entityType.setKey(Collections.singletonList(propertyRef));
        entityType.setNavigationProperties(navPropList);

        return entityType;
    }

    private static CsdlEntityType prepareCategoryEntityType() {
        // create EntityType properties
        CsdlProperty id = new CsdlProperty().setName("id").setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
        CsdlProperty name = new CsdlProperty().setName("name").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty description = new CsdlProperty().setName("description").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty createdAt = new CsdlProperty().setName("createdAt").setType(EdmPrimitiveTypeKind.Date.getFullQualifiedName());
        CsdlProperty updatedAt = new CsdlProperty().setName("updatedAt").setType(EdmPrimitiveTypeKind.Date.getFullQualifiedName());

        // create CsdlPropertyRef for key element
        CsdlPropertyRef propertyRef = new CsdlPropertyRef();
        propertyRef.setName("id");

        // Navigation
        CsdlNavigationProperty navProp = new CsdlNavigationProperty()
                .setName(ES_PRODUCTS_NAME)
                .setType(ET_PRODUCT_FQN)
                .setCollection(true)
                .setPartner(ET_CATEGORY_NAME);
        List<CsdlNavigationProperty> navPropList = new ArrayList<>();
        navPropList.add(navProp);

        // configure EntityType
        CsdlEntityType entityType = new CsdlEntityType();
        entityType.setName(ET_CATEGORY_NAME);
        entityType.setProperties(List.of(id, name, description, createdAt, updatedAt));
        entityType.setKey(Collections.singletonList(propertyRef));
        entityType.setNavigationProperties(navPropList);

        return entityType;
    }
}
