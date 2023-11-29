package com.store.service.processor;

import com.store.service.storage.EntityStorage;
import jakarta.inject.Inject;
import org.apache.olingo.commons.api.data.ContextURL;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmPrimitiveType;
import org.apache.olingo.commons.api.edm.EdmProperty;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.http.HttpHeader;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.*;
import org.apache.olingo.server.api.serializer.ODataSerializer;
import org.apache.olingo.server.api.serializer.PrimitiveSerializerOptions;
import org.apache.olingo.server.api.serializer.SerializerResult;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;
import org.apache.olingo.server.api.uri.UriResourceProperty;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;

public class PrimitiveProcessor implements org.apache.olingo.server.api.processor.PrimitiveProcessor {

    private OData odata;
    private ServiceMetadata serviceMetadata;
    private final EntityStorage storage;

    @Inject
    public PrimitiveProcessor(EntityStorage storage) {
        this.storage = storage;
    }

    // Initializes the processor with the OData context and service metadata.
    @Override
    public void init(OData odata, ServiceMetadata serviceMetadata) {
        this.odata = odata;
        this.serviceMetadata = serviceMetadata;
    }

    // Processes the reading of a primitive property of an entity.
    @Override
    public void readPrimitive(ODataRequest request, ODataResponse response, UriInfo uriInfo, ContentType responseFormat)
            throws ODataApplicationException, ODataLibraryException {
        // Gets the entity set and property from the URI.

        EdmEntitySet edmEntitySet = getEdmEntitySet(uriInfo);
        EdmProperty edmProperty = getEdmProperty(uriInfo);

        // Gets the entity and property to read.
        Entity entity = getEntity(edmEntitySet, uriInfo);
        Property property = getProperty(entity, edmProperty);

        // Serializes the property and sets the response.
        serializeAndSetResponse(response, edmEntitySet, edmProperty, property, responseFormat);
    }

    // Gets the set of entities from the URI.
    private EdmEntitySet getEdmEntitySet(UriInfo uriInfo) {
        UriResourceEntitySet uriEntityset = (UriResourceEntitySet) uriInfo.getUriResourceParts().get(0);
        return uriEntityset.getEntitySet();
    }

    // Gets the EDM property to read from the URI.
    private EdmProperty getEdmProperty(UriInfo uriInfo) {
        UriResourceProperty uriProperty = (UriResourceProperty) uriInfo.getUriResourceParts().get(uriInfo.getUriResourceParts().size() - 1);
        return uriProperty.getProperty();
    }

    // Gets the storage entity.
    private Entity getEntity(EdmEntitySet edmEntitySet, UriInfo uriInfo) throws ODataApplicationException {
        List<UriParameter> keyPredicates = ((UriResourceEntitySet) uriInfo.getUriResourceParts().get(0)).getKeyPredicates();
        Entity entity = storage.retrieveEntity(edmEntitySet, keyPredicates);
        if (entity == null) {
            throw new ODataApplicationException("Entity not found", HttpStatusCode.NOT_FOUND.getStatusCode(), Locale.ENGLISH);
        }
        return entity;
    }

    // Gets the entity-specific property.
    private Property getProperty(Entity entity, EdmProperty edmProperty) throws ODataApplicationException {
        Property property = entity.getProperty(edmProperty.getName());
        if (property == null) {
            throw new ODataApplicationException("Property not found", HttpStatusCode.NOT_FOUND.getStatusCode(), Locale.ENGLISH);
        }
        return property;
    }

    // Serialize the primitive property and configure the HTTP response.
    private void serializeAndSetResponse(ODataResponse response, EdmEntitySet edmEntitySet, EdmProperty edmProperty,
                                         Property property, ContentType responseFormat) throws ODataLibraryException {
        if (property.getValue() != null) {
            ODataSerializer serializer = odata.createSerializer(responseFormat);
            ContextURL contextUrl = ContextURL.with().entitySet(edmEntitySet).navOrPropertyPath(edmProperty.getName()).build();
            PrimitiveSerializerOptions options = PrimitiveSerializerOptions.with().contextURL(contextUrl).build();
            SerializerResult serializerResult = serializer.primitive(serviceMetadata, (EdmPrimitiveType) edmProperty.getType(), property, options);
            InputStream propertyStream = serializerResult.getContent();
            response.setContent(propertyStream);
            response.setStatusCode(HttpStatusCode.OK.getStatusCode());
            response.setHeader(HttpHeader.CONTENT_TYPE, responseFormat.toContentTypeString());
        } else {
            response.setStatusCode(HttpStatusCode.NO_CONTENT.getStatusCode());
        }
    }

    @Override
    public void updatePrimitive(ODataRequest request, ODataResponse response, UriInfo uriInfo, ContentType requestFormat, ContentType responseFormat) throws ODataApplicationException, ODataLibraryException {

    }

    @Override
    public void deletePrimitive(ODataRequest request, ODataResponse response, UriInfo uriInfo) throws ODataApplicationException, ODataLibraryException {

    }
}
