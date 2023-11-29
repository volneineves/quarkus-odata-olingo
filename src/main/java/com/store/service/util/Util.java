package com.store.service.util;


import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.edm.*;
import org.apache.olingo.commons.api.ex.ODataException;
import org.apache.olingo.commons.api.ex.ODataRuntimeException;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;

public class Util {

    public static EdmEntitySet getEdmEntitySet(UriInfoResource uriInfo) throws ODataApplicationException {

        List<UriResource> resourcePaths = uriInfo.getUriResourceParts();

        // To get the entity set we have to interpret all URI segments
        if (!(resourcePaths.get(0) instanceof UriResourceEntitySet uriResource)) {
            throw new ODataApplicationException("Invalid resource type for first segment.",
                    HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ENGLISH);
        }
        return uriResource.getEntitySet();
    }

    public static Entity findEntity(EdmEntityType edmEntityType,
                                    EntityCollection rt_entitySet, List<UriParameter> keyParams)
            throws ODataApplicationException {

        List<Entity> entityList = rt_entitySet.getEntities();

        // loop over all entities in order to find that one that matches all keys in request
        // an example could be e.g. contacts(ContactID=1, CompanyID=1)
        for (Entity rt_entity : entityList) {
            boolean foundEntity = entityMatchesAllKeys(edmEntityType, rt_entity, keyParams);
            if (foundEntity) {
                return rt_entity;
            }
        }

        return null;
    }


    public static boolean entityMatchesAllKeys(EdmEntityType edmEntityType, Entity rt_entity, List<UriParameter> keyParams)
            throws ODataApplicationException {

        // loop over all keys
        for (final UriParameter key : keyParams) {
            // key
            String keyName = key.getName();
            String keyText = key.getText();

            // Edm: we need this info for the comparison below
            EdmProperty edmKeyProperty = (EdmProperty) edmEntityType.getProperty(keyName);
            Boolean isNullable = edmKeyProperty.isNullable();
            Integer maxLength = edmKeyProperty.getMaxLength();
            Integer precision = edmKeyProperty.getPrecision();
            Boolean isUnicode = edmKeyProperty.isUnicode();
            Integer scale = edmKeyProperty.getScale();

            // get the EdmType in order to compare
            EdmType edmType = edmKeyProperty.getType();

            // Key properties must be instance of primitive type
            EdmPrimitiveType edmPrimitiveType = (EdmPrimitiveType) edmType;

            // Runtime data: the value of the current entity
            Object valueObject = rt_entity.getProperty(keyName).getValue(); // null-check is done in FWK

            // now need to compare the valueObject with the keyText String
            String valueAsString;
            try {
                valueAsString = edmPrimitiveType.valueToString(valueObject, isNullable, maxLength,
                        precision, scale, isUnicode);
            } catch (EdmPrimitiveTypeException e) {
                throw new ODataApplicationException("Failed to retrieve String value",
                        HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH, e);
            }

            if (valueAsString == null) {
                return false;
            }

            boolean matches = valueAsString.equals(keyText);
            if (!matches) {
                // if any of the key properties is not found in the entity, we don't need to search further
                return false;
            }
        }

        return true;
    }

    public static Entity getEntity(EdmEntityType edmEntityType, List<UriParameter> keyParams, EntityCollection entitySet) throws ODataApplicationException {

        // generic approach  to find the requested entity
        Entity requestedEntity = Util.findEntity(edmEntityType, entitySet, keyParams);

        if (requestedEntity == null) {
            throw new ODataApplicationException("Entity for requested key doesn't exist", HttpStatusCode.NOT_FOUND.getStatusCode(), Locale.ENGLISH);
        }

        return requestedEntity;
    }

    public static URI createId(String entitySetName, Long id) {
        try {
            return new URI(entitySetName + "(" + id + ")");
        } catch (URISyntaxException e) {
            throw new ODataRuntimeException("Unable to create id for entity: " + entitySetName, e);
        }
    }

    public static EdmEntitySet getNavigationTargetEntitySet(EdmEntitySet startEdmEntitySet, EdmNavigationProperty edmNavigationProperty)
            throws ODataApplicationException {


        String navPropName = edmNavigationProperty.getName();
        EdmBindingTarget edmBindingTarget = startEdmEntitySet.getRelatedBindingTarget(navPropName);
        if (edmBindingTarget == null) {
            throw new ODataApplicationException("Not teste.",
                    HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ROOT);
        }

        if (edmBindingTarget instanceof EdmEntitySet) {
            startEdmEntitySet = (EdmEntitySet) edmBindingTarget;
        } else {
            throw new ODataApplicationException("Not carlos.",
                    HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ROOT);
        }

        return startEdmEntitySet;
    }

    public static Long extractId(List<UriParameter> keyParams, String entityType) throws ODataApplicationException {
        if (keyParams == null || keyParams.isEmpty()) {
            throw new ODataApplicationException(
                    "No key parameters provided for " + entityType,
                    400, // HTTP Status Code for Bad Request
                    null);
        }

        try {
            // Assuming the ID is the first parameter and its value can be parsed to a Long
            String idString = keyParams.get(0).getText();
            return Long.parseLong(idString);
        } catch (NumberFormatException e) {
            throw new ODataApplicationException(
                    "Invalid ID format for " + entityType + ": " + keyParams.get(0).getText(),
                    400, // HTTP Status Code for Bad Request
                    null);
        }
    }
}
