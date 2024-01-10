package com.store.provider;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.*;
import org.apache.olingo.server.api.ODataApplicationException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EdmProvider extends CsdlAbstractEdmProvider {

    // Service Namespace
    public static final String NAMESPACE = "OData.Demo";

    // EDM Container
    public static final String CONTAINER_NAME = "Container";
    public static final FullQualifiedName CONTAINER = new FullQualifiedName(NAMESPACE, CONTAINER_NAME);

    // Entity Types Names
    public static final String ET_PRODUCT_NAME = "product";
    public static final String ET_CLIENT_NAME = "client";
    public static final FullQualifiedName ET_PRODUCT_FQN = new FullQualifiedName(NAMESPACE, ET_PRODUCT_NAME);
    public static final FullQualifiedName ET_CLIENT_FQN = new FullQualifiedName(NAMESPACE, ET_CLIENT_NAME);

    // Entity Set Names
    public static final String ES_PRODUCTS_NAME = "products";
    public static final String ES_CLIENT_NAME = "clients";

    @Override
    public CsdlEntityType getEntityType(FullQualifiedName entityTypeName) {

        if (entityTypeName.equals(ET_PRODUCT_FQN)) {
            return prepareProductEntityType();
        } else if (entityTypeName.equals(ET_CLIENT_FQN)) {
            return prepareClientEntityType();
        }

        return null;
    }

    @Override
    public CsdlEntitySet getEntitySet(FullQualifiedName entityContainer, String entitySetName) throws ODataApplicationException {
        if (entityContainer.equals(CONTAINER)) {
            if (entitySetName.equals(ES_PRODUCTS_NAME)) {
                return createProductsEntitySet();
            } else if (entitySetName.equals(ES_CLIENT_NAME)) {
                return createClientsEntitySet();
            }
        }
        return null;
    }

    private CsdlEntitySet createProductsEntitySet() {
        CsdlEntitySet entitySet = new CsdlEntitySet();
        entitySet.setName(ES_PRODUCTS_NAME);
        entitySet.setType(ET_PRODUCT_FQN);
        return entitySet;
    }

    private CsdlEntitySet createClientsEntitySet() {
        CsdlEntitySet entitySet = new CsdlEntitySet();
        entitySet.setName(ES_CLIENT_NAME);
        entitySet.setType(ET_CLIENT_FQN);
        return entitySet;
    }

    @Override
    public CsdlEntityContainer getEntityContainer() throws ODataApplicationException {
        List<CsdlEntitySet> entitySets = new ArrayList<>();
        entitySets.add(getEntitySet(CONTAINER, ES_PRODUCTS_NAME));
        entitySets.add(getEntitySet(CONTAINER, ES_CLIENT_NAME));

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
        entityTypes.add(getEntityType(ET_CLIENT_FQN));
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
        // Create EntityType properties
        CsdlProperty productSK = new CsdlProperty().setName("productSK").setType(EdmPrimitiveTypeKind.Int64.getFullQualifiedName());
        CsdlProperty productCode = new CsdlProperty().setName("productCode").setType(EdmPrimitiveTypeKind.Double.getFullQualifiedName());
        CsdlProperty currentProductDescription = new CsdlProperty().setName("currentProductDescription").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty productName = new CsdlProperty().setName("productName").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty currentProductName = new CsdlProperty().setName("currentProductName").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty currentSubAssortment = new CsdlProperty().setName("currentSubAssortment").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty currentSupplierCode = new CsdlProperty().setName("currentSupplierCode").setType(EdmPrimitiveTypeKind.Double.getFullQualifiedName());
        CsdlProperty currentSupplierSwitchDescription = new CsdlProperty().setName("currentSupplierSwitchDescription").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty currentSSFSupplierSwitch = new CsdlProperty().setName("currentSSFSupplierSwitch").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty currentBrandDescription = new CsdlProperty().setName("currentBrandDescription").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty currentEANBarcodeCode = new CsdlProperty().setName("currentEANBarcodeCode").setType(EdmPrimitiveTypeKind.Double.getFullQualifiedName());
        CsdlProperty eanBarcodeCode = new CsdlProperty().setName("eanBarcodeCode").setType(EdmPrimitiveTypeKind.Double.getFullQualifiedName());
        CsdlProperty currentMonitoredIndicator = new CsdlProperty().setName("currentMonitoredIndicator").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty currentImportedIndicator = new CsdlProperty().setName("currentImportedIndicator").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty currentOriginCode = new CsdlProperty().setName("currentOriginCode").setType(EdmPrimitiveTypeKind.Int64.getFullQualifiedName());
        CsdlProperty currentCommercialCategoryDescription = new CsdlProperty().setName("currentCommercialCategoryDescription").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty currentCommercialCategory = new CsdlProperty().setName("currentCommercialCategory").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty currentSeasonalityDescription = new CsdlProperty().setName("currentSeasonalityDescription").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty currentStorageConditionCode = new CsdlProperty().setName("currentStorageConditionCode").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty currentBrandTreeDescription = new CsdlProperty().setName("currentBrandTreeDescription").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty currentWholesaleBuyer = new CsdlProperty().setName("currentWholesaleBuyer").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty currentPriceGroupCode = new CsdlProperty().setName("currentPriceGroupCode").setType(EdmPrimitiveTypeKind.Int64.getFullQualifiedName());
        CsdlProperty currentPriceCurveCode = new CsdlProperty().setName("currentPriceCurveCode").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty supplierSwitchSK = new CsdlProperty().setName("supplierSwitchSK").setType(EdmPrimitiveTypeKind.Int64.getFullQualifiedName());
        CsdlProperty currentWholesaleCoordinator = new CsdlProperty().setName("currentWholesaleCoordinator").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty currentRetailCoordinator = new CsdlProperty().setName("currentRetailCoordinator").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

        // Create CsdlPropertyRef for key element
        CsdlPropertyRef propertyRef = new CsdlPropertyRef();
        propertyRef.setName("productSK");

        // Configure EntityType
        CsdlEntityType entityType = new CsdlEntityType();
        entityType.setName(ET_PRODUCT_NAME);
        entityType.setProperties(Arrays.asList(
                productSK, productCode, currentProductDescription, productName, currentProductName,
                currentSubAssortment, currentSupplierCode, currentSupplierSwitchDescription, currentSSFSupplierSwitch,
                currentBrandDescription, currentEANBarcodeCode, eanBarcodeCode, currentMonitoredIndicator,
                currentImportedIndicator, currentOriginCode, currentCommercialCategoryDescription,
                currentCommercialCategory, currentSeasonalityDescription, currentStorageConditionCode,
                currentBrandTreeDescription, currentWholesaleBuyer, currentPriceGroupCode,
                currentPriceCurveCode, supplierSwitchSK, currentWholesaleCoordinator, currentRetailCoordinator
        ));
        entityType.setKey(Collections.singletonList(propertyRef));

        return entityType;
    }


    private static CsdlEntityType prepareClientEntityType() {
        // Create EntityType properties
        CsdlProperty clientSK = new CsdlProperty().setName("clientSK").setType(EdmPrimitiveTypeKind.Int64.getFullQualifiedName());
        CsdlProperty clientCode = new CsdlProperty().setName("clientCode").setType(EdmPrimitiveTypeKind.Double.getFullQualifiedName());
        CsdlProperty currentClient = new CsdlProperty().setName("currentClient").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty currentNetwork = new CsdlProperty().setName("currentNetwork").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty currentAssociation = new CsdlProperty().setName("currentAssociation").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty clientState = new CsdlProperty().setName("clientState").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty currentSector = new CsdlProperty().setName("currentSector").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty currentSupervisor = new CsdlProperty().setName("currentSupervisor").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty currentManagerDescription = new CsdlProperty().setName("currentManagerDescription").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty currentManager = new CsdlProperty().setName("currentManager").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty currentDivisionalDescription = new CsdlProperty().setName("currentDivisionalDescription").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty currentSuperintendenceDescription = new CsdlProperty().setName("currentSuperintendenceDescription").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty currentNeighborhood = new CsdlProperty().setName("currentNeighborhood").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty lastPurchaseDate = new CsdlProperty().setName("lastPurchaseDate").setType(EdmPrimitiveTypeKind.Date.getFullQualifiedName());
        CsdlProperty sapGroupCode = new CsdlProperty().setName("sapGroupCode").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty currentSapGroupCode = new CsdlProperty().setName("currentSapGroupCode").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty currentMicroRegion = new CsdlProperty().setName("currentMicroRegion").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty currentCNPJCPF = new CsdlProperty().setName("currentCNPJCPF").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty currentMesoRegion = new CsdlProperty().setName("currentMesoRegion").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty currentClientSegmentationDescription = new CsdlProperty().setName("currentClientSegmentationDescription").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty currentClientGroupTermDescription = new CsdlProperty().setName("currentClientGroupTermDescription").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty currentControlledIndicator = new CsdlProperty().setName("currentControlledIndicator").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty activeRegistrationIndicator = new CsdlProperty().setName("activeRegistrationIndicator").setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());

        // Create CsdlPropertyRef for key element
        CsdlPropertyRef propertyRef = new CsdlPropertyRef();
        propertyRef.setName("clientSK");

        // Configure EntityType
        CsdlEntityType entityType = new CsdlEntityType();
        entityType.setName(ET_CLIENT_NAME);
        entityType.setProperties(Arrays.asList(
                clientSK, clientCode, currentClient, currentNetwork, currentAssociation,
                clientState, currentSector, currentSupervisor, currentManagerDescription,
                currentManager, currentDivisionalDescription, currentSuperintendenceDescription,
                currentNeighborhood, lastPurchaseDate, sapGroupCode, currentSapGroupCode,
                currentMicroRegion, currentCNPJCPF, currentMesoRegion,
                currentClientSegmentationDescription, currentClientGroupTermDescription,
                currentControlledIndicator, activeRegistrationIndicator
        ));
        entityType.setKey(Collections.singletonList(propertyRef));

        return entityType;
    }

}
