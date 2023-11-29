package com.store.service.processor;

import com.store.service.storage.EntityStorage;
import com.store.service.util.Util;
import jakarta.inject.Inject;
import org.apache.olingo.commons.api.data.ContextURL;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmNavigationProperty;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.http.HttpHeader;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.*;
import org.apache.olingo.server.api.serializer.EntitySerializerOptions;
import org.apache.olingo.server.api.serializer.ODataSerializer;
import org.apache.olingo.server.api.serializer.SerializerResult;
import org.apache.olingo.server.api.uri.*;
import org.apache.olingo.server.api.uri.queryoption.ExpandOption;
import org.apache.olingo.server.api.uri.queryoption.SelectOption;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EntityProcessor implements org.apache.olingo.server.api.processor.EntityProcessor {


    private OData odata;
    private ServiceMetadata serviceMetadata;
    private final EntityStorage storage;

    @Inject
    public EntityProcessor(EntityStorage productStorage) {
        this.storage = productStorage;
    }

    @Override
    public void init(OData odata, ServiceMetadata serviceMetadata) {
        this.odata = odata;
        this.serviceMetadata = serviceMetadata;
    }

    @Override
    public void readEntity(ODataRequest request, ODataResponse response, UriInfo uriInfo, ContentType responseFormat)
            throws ODataApplicationException, ODataLibraryException {

        EdmEntityType responseEdmEntityType = null;
        Entity responseEntity = null;
        EdmEntitySet responseEdmEntitySet = null;

        List<UriResource> resourceParts = uriInfo.getUriResourceParts();
        int segmentCount = resourceParts.size();

        validateUri(resourceParts);
        validateSegmentCount(segmentCount);

        UriResource uriResource = resourceParts.get(0);
        UriResourceEntitySet uriResourceEntitySet = (UriResourceEntitySet) uriResource;
        EdmEntitySet startEdmEntitySet = uriResourceEntitySet.getEntitySet();

        // navigation
        if (segmentCount == 1) {
            responseEdmEntityType = startEdmEntitySet.getEntityType();
            responseEdmEntitySet = startEdmEntitySet;
            List<UriParameter> keyPredicates = uriResourceEntitySet.getKeyPredicates();
            responseEntity = storage.retrieveEntity(startEdmEntitySet, keyPredicates);
        } else if (segmentCount == 2) {
            validateNavigationSegment(resourceParts.get(1));

            UriResource navSegment = resourceParts.get(1);
            UriResourceNavigation uriResourceNavigation = (UriResourceNavigation) navSegment;
            EdmNavigationProperty edmNavigationProperty = uriResourceNavigation.getProperty();
            responseEdmEntityType = edmNavigationProperty.getType();
            responseEdmEntitySet = Util.getNavigationTargetEntitySet(startEdmEntitySet, edmNavigationProperty);

            List<UriParameter> keyPredicates = uriResourceEntitySet.getKeyPredicates();
            Entity sourceEntity = storage.retrieveEntity(startEdmEntitySet, keyPredicates);
            List<UriParameter> navKeyPredicates = uriResourceNavigation.getKeyPredicates();

            if (navKeyPredicates.isEmpty()) {
                responseEntity = storage.retrieveEntityByRelation(sourceEntity, responseEdmEntityType);
            } else {
                responseEntity = storage.retrieveEntityByRelation(sourceEntity, responseEdmEntityType, navKeyPredicates);
            }
        }

        validateResponseEntity(responseEntity);

        ExpandOption expandOption = uriInfo.getExpandOption();

        SelectOption selectOption = uriInfo.getSelectOption();
        ContextURL contextUrl = ContextURL.with().entitySet(responseEdmEntitySet).build();
        EntitySerializerOptions opts = EntitySerializerOptions.with().contextURL(contextUrl).select(selectOption).expand(expandOption).build();

        ODataSerializer serializer = odata.createSerializer(responseFormat);
        SerializerResult serializerResult = serializer.entity(serviceMetadata, responseEdmEntityType, responseEntity, opts);

        InputStream entityStream = serializerResult.getContent();

        response.setContent(entityStream);
        response.setStatusCode(HttpStatusCode.OK.getStatusCode());
        response.setHeader(HttpHeader.CONTENT_TYPE, responseFormat.toContentTypeString());
    }

    private static void validateResponseEntity(Entity responseEntity) throws ODataApplicationException {
        if (responseEntity == null) {
            throw new ODataApplicationException("Nothing found.", HttpStatusCode.NOT_FOUND.getStatusCode(), Locale.ROOT);
        }
    }

    private static void validateSegmentCount(int segmentCount) throws ODataApplicationException {
        // Segment cannot be bigger than 2
        if (segmentCount > 2) {
            throw new ODataApplicationException("Not supported", HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ROOT);
        }
    }

    private void validateUri(List<UriResource> resourceParts) throws ODataApplicationException {
        if (resourceParts.isEmpty() || !(resourceParts.get(0) instanceof UriResourceEntitySet)) {
            throw new ODataApplicationException("Only EntitySet is supported", HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ROOT);
        }
    }

    private void validateNavigationSegment(UriResource resource) throws ODataApplicationException {
        if (!(resource instanceof UriResourceNavigation)) {
            throw new ODataApplicationException("Not supported", HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ROOT);
        }
    }

    @Override
    public void createEntity(ODataRequest request, ODataResponse response, UriInfo uriInfo, ContentType requestFormat, ContentType responseFormat) throws ODataApplicationException, ODataLibraryException {

    }

    @Override
    public void updateEntity(ODataRequest request, ODataResponse response, UriInfo uriInfo, ContentType requestFormat, ContentType responseFormat) throws ODataApplicationException, ODataLibraryException {

    }

    @Override
    public void deleteEntity(ODataRequest request, ODataResponse response, UriInfo uriInfo) throws ODataApplicationException, ODataLibraryException {

    }
}
