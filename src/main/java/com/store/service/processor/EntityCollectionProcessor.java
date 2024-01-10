package com.store.service.processor;

import com.store.service.filter.FilterExpressionVisitor;
import com.store.service.storage.EntityStorage;
import com.store.service.util.Util;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.apache.olingo.commons.api.data.ContextURL;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmNavigationProperty;
import org.apache.olingo.commons.api.edm.EdmProperty;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.http.HttpHeader;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.*;
import org.apache.olingo.server.api.serializer.EntityCollectionSerializerOptions;
import org.apache.olingo.server.api.serializer.ODataSerializer;
import org.apache.olingo.server.api.serializer.SerializerException;
import org.apache.olingo.server.api.serializer.SerializerResult;
import org.apache.olingo.server.api.uri.*;
import org.apache.olingo.server.api.uri.queryoption.*;
import org.apache.olingo.server.api.uri.queryoption.expression.Expression;
import org.apache.olingo.server.api.uri.queryoption.expression.ExpressionVisitException;
import org.apache.olingo.server.api.uri.queryoption.expression.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RequestScoped
public class EntityCollectionProcessor implements org.apache.olingo.server.api.processor.EntityCollectionProcessor {

    private OData oData;
    private ServiceMetadata serviceMetadata;
    private final EntityStorage storage;

    @Inject
    public EntityCollectionProcessor(EntityStorage storage) {
        this.storage = storage;
    }

    @Override
    public void init(OData oData, ServiceMetadata serviceMetadata) {
        this.oData = oData;
        this.serviceMetadata = serviceMetadata;
    }

    public void readEntityCollection(ODataRequest request, ODataResponse response, UriInfo uriInfo, ContentType responseFormat)
            throws ODataApplicationException, SerializerException {

        EdmEntitySet edmEntitySet = Util.getEdmEntitySet(uriInfo);
        EntityCollection entityCollection;

        List<UriResource> resourceParts = uriInfo.getUriResourceParts();
        int segmentCount = resourceParts.size();

        UriResource uriResource = resourceParts.get(0);
        if (! (uriResource instanceof UriResourceEntitySet uriResourceEntitySet)) {
            throw new ODataApplicationException("Only EntitySet is supported", HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(),Locale.ROOT);
        }

        if (segmentCount == 1) {
            entityCollection = storage.retrieveEntities(edmEntitySet);
        } else if (segmentCount == 2) {
            entityCollection = storage.retrieveEntities(edmEntitySet);
        } else {
            throw new ODataApplicationException("Not supported", HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(),Locale.ENGLISH);
        }

        applyFilter(uriInfo.getFilterOption(), entityCollection);
        applySystemQueryOptions(uriInfo, entityCollection);

        EntityCollectionSerializerOptions opts = buildSerializerOptions(request, uriInfo, edmEntitySet);
        SerializerResult serializerResult = serializeEntityCollection(responseFormat, edmEntitySet, entityCollection, opts);

        configureResponse(response, serializerResult, responseFormat);
    }

    private void applyFilter(FilterOption filterOption, EntityCollection entityCollection)
            throws ODataApplicationException {
        if (filterOption != null) {
            Expression filterExpression = filterOption.getExpression();
            List<Entity> entityList = new ArrayList<>(entityCollection.getEntities());
            List<Entity> filteredEntities = new ArrayList<>();

            for (Entity entity : entityList) {
                if (evaluateFilterExpression(entity, filterExpression)) {
                    filteredEntities.add(entity);
                }
            }

            entityCollection.getEntities().clear();
            entityCollection.getEntities().addAll(filteredEntities);
        }
    }

    private boolean evaluateFilterExpression(Entity entity, Expression filterExpression)
            throws ODataApplicationException {
        FilterExpressionVisitor expressionVisitor = new FilterExpressionVisitor(entity);
        try {
            Object visitorResult = filterExpression.accept(expressionVisitor);
            if (!(visitorResult instanceof Boolean)) {
                throw new ODataApplicationException("A filter expression must evaluate to type Edm.Boolean",
                        HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
            }
            return (Boolean) visitorResult;
        } catch (ExpressionVisitException e) {
            throw new ODataApplicationException("Exception in filter evaluation",
                    HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH);
        }
    }


    private void applySystemQueryOptions(UriInfo uriInfo, EntityCollection entityCollection)
            throws ODataApplicationException {
        applySkip(uriInfo.getSkipOption(), entityCollection);
        applyTop(uriInfo.getTopOption(), entityCollection);
        applyOrderBy(uriInfo.getOrderByOption(), entityCollection);
        applyCount(uriInfo.getCountOption(), entityCollection);
    }

    private void applyCount(CountOption countOption, EntityCollection entityCollection) {
        if (countOption != null && countOption.getValue()) {
            entityCollection.setCount(entityCollection.getEntities().size());
        }
    }

    private void applySkip(SkipOption skipOption, EntityCollection entityCollection) throws ODataApplicationException {
        if (skipOption != null) {
            int skipNumber = skipOption.getValue();
            if (skipNumber >= 0) {
                List<Entity> entities = entityCollection.getEntities();
                if (skipNumber < entities.size()) {
                    List<Entity> skipEntities = new ArrayList<>(entities.subList(skipNumber, entities.size()));
                    entityCollection.getEntities().clear();
                    entityCollection.getEntities().addAll(skipEntities);
                } else {
                    entityCollection.getEntities().clear();
                }
            } else {
                throw new ODataApplicationException("Invalid value for $skip", HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ROOT);
            }
        }
    }

    private void applyTop(TopOption topOption, EntityCollection entityCollection) throws ODataApplicationException {
        if (topOption != null) {
            int topNumber = topOption.getValue();
            if (topNumber >= 0) {
                List<Entity> entities = entityCollection.getEntities();
                if (topNumber < entities.size()) {
                    List<Entity> topEntities = new ArrayList<>(entities.subList(0, topNumber));
                    entityCollection.getEntities().clear();
                    entityCollection.getEntities().addAll(topEntities);
                }
            } else {
                throw new ODataApplicationException("Invalid value for $top", HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ROOT);
            }
        }
    }

    private void applyOrderBy(OrderByOption orderByOption, EntityCollection entityCollection) {
        if (orderByOption != null) {
            OrderByItem orderByItem = orderByOption.getOrders().get(0);
            Expression expression = orderByItem.getExpression();
            if (expression instanceof Member) {
                UriInfoResource resourcePath = ((Member) expression).getResourcePath();
                UriResource uriResource = resourcePath.getUriResourceParts().get(0);
                if (uriResource instanceof UriResourcePrimitiveProperty) {
                    EdmProperty edmProperty = ((UriResourcePrimitiveProperty) uriResource).getProperty();
                    String sortPropertyName = edmProperty.getName();

                    entityCollection.getEntities().sort((entity1, entity2) -> compareEntities(entity1, entity2, sortPropertyName, orderByItem.isDescending()));
                }
            }
        }
    }

    private int compareEntities(Entity entity1, Entity entity2, String propertyName, boolean descending) {
        Object value1 = entity1.getProperty(propertyName).getValue();
        Object value2 = entity2.getProperty(propertyName).getValue();

        int result = 0;
        if (value1 instanceof Comparable && value2 instanceof Comparable) {
            result = ((Comparable) value1).compareTo(value2);
        }

        return descending ? -result : result;
    }

    private EntityCollectionSerializerOptions buildSerializerOptions(ODataRequest request, UriInfo uriInfo, EdmEntitySet edmEntitySet) throws SerializerException {
        String selectList = oData.createUriHelper().buildContextURLSelectList(edmEntitySet.getEntityType(), null, uriInfo.getSelectOption());
        ContextURL contextUrl = ContextURL.with().entitySet(edmEntitySet).selectList(selectList).build();
        final String id = request.getRawBaseUri() + "/" + edmEntitySet.getName();
        return EntityCollectionSerializerOptions.with()
                .contextURL(contextUrl)
                .id(id)
                .select(uriInfo.getSelectOption())
                .count(uriInfo.getCountOption())
                .build();
    }

    private SerializerResult serializeEntityCollection(ContentType responseFormat, EdmEntitySet edmEntitySet, EntityCollection entityCollection, EntityCollectionSerializerOptions opts)
            throws SerializerException {
        ODataSerializer serializer = oData.createSerializer(responseFormat);
        EdmEntityType edmEntityType = edmEntitySet.getEntityType();
        return serializer.entityCollection(serviceMetadata, edmEntityType, entityCollection, opts);
    }

    private void configureResponse(ODataResponse response, SerializerResult serializerResult, ContentType responseFormat) {
        response.setContent(serializerResult.getContent());
        response.setStatusCode(HttpStatusCode.OK.getStatusCode());
        response.setHeader(HttpHeader.CONTENT_TYPE, responseFormat.toContentTypeString());
    }

}
