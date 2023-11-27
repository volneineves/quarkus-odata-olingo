package com.store.service.processor;

import com.store.service.storage.EntityStorage;
import jakarta.inject.Inject;
import org.apache.olingo.commons.api.data.ContextURL;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.http.HttpHeader;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.*;
import org.apache.olingo.server.api.serializer.EntitySerializerOptions;
import org.apache.olingo.server.api.serializer.ODataSerializer;
import org.apache.olingo.server.api.serializer.SerializerResult;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;

import java.io.InputStream;
import java.util.List;

public class EntityProcessor implements org.apache.olingo.server.api.processor.EntityProcessor {


    private OData odata;
    private ServiceMetadata serviceMetadata;
    private final EntityStorage storage;

    @Inject
    public EntityProcessor(EntityStorage productStorage) {
        this.storage = productStorage;
    }

    // Initializes the processor with the OData context and service metadata.
    @Override
    public void init(OData odata, ServiceMetadata serviceMetadata) {
        this.odata = odata;
        this.serviceMetadata = serviceMetadata;
    }

    // Processes the read request for a specific entity.
    @Override
    public void readEntity(ODataRequest request, ODataResponse response, UriInfo uriInfo, ContentType responseFormat)
            throws ODataApplicationException, ODataLibraryException {
        EdmEntitySet edmEntitySet = getEdmEntitySet(uriInfo);
        Entity entity = readEntityData(uriInfo, edmEntitySet);
        serializeAndSetResponse(entity, edmEntitySet, response, responseFormat);
    }

    // Gets the EDM entity set from the URI information.
    private EdmEntitySet getEdmEntitySet(UriInfo uriInfo) {
        UriResourceEntitySet uriResourceEntitySet = (UriResourceEntitySet) uriInfo.getUriResourceParts().get(0);
        return uriResourceEntitySet.getEntitySet();
    }

    // Reads entity data from storage.
    private Entity readEntityData(UriInfo uriInfo, EdmEntitySet edmEntitySet)
            throws ODataApplicationException {
        List<UriResource> resourcePaths = uriInfo.getUriResourceParts();
        UriResourceEntitySet uriResourceEntitySet = (UriResourceEntitySet) resourcePaths.get(0);
        List<UriParameter> keyPredicates = uriResourceEntitySet.getKeyPredicates();
        return storage.readEntityData(edmEntitySet, keyPredicates);
    }


    // Serializes the entity and configures the response.
    private void serializeAndSetResponse(Entity entity, EdmEntitySet edmEntitySet,
                                         ODataResponse response, ContentType responseFormat)
            throws ODataLibraryException {
        EdmEntityType entityType = edmEntitySet.getEntityType();
        ContextURL contextUrl = ContextURL.with().entitySet(edmEntitySet).build();
        EntitySerializerOptions options = EntitySerializerOptions.with().contextURL(contextUrl).build();

        ODataSerializer serializer = odata.createSerializer(responseFormat);
        SerializerResult serializerResult = serializer.entity(serviceMetadata, entityType, entity, options);
        InputStream entityStream = serializerResult.getContent();

        response.setContent(entityStream);
        response.setStatusCode(HttpStatusCode.OK.getStatusCode());
        response.setHeader(HttpHeader.CONTENT_TYPE, responseFormat.toContentTypeString());
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
