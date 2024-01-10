package com.store.service.mapper;

import com.store.entity.Product;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;

import static com.store.provider.EdmProvider.ES_PRODUCTS_NAME;
import static com.store.service.util.Util.createId;

public class ProductMapper {

    public static Entity convertToEntity(Product product) {
        Entity entity = new Entity()
                .addProperty(new Property(null, "productSK", ValueType.PRIMITIVE, product.getProductSK()))
                .addProperty(new Property(null, "productCode", ValueType.PRIMITIVE, product.getProductCode()))
                .addProperty(new Property(null, "currentProductDescription", ValueType.PRIMITIVE, product.getCurrentProductDescription()))
                .addProperty(new Property(null, "productName", ValueType.PRIMITIVE, product.getProductName()))
                .addProperty(new Property(null, "currentProductName", ValueType.PRIMITIVE, product.getCurrentProductName()))
                .addProperty(new Property(null, "currentSubAssortment", ValueType.PRIMITIVE, product.getCurrentSubAssortment()))
                .addProperty(new Property(null, "currentSupplierCode", ValueType.PRIMITIVE, product.getCurrentSupplierCode()))
                .addProperty(new Property(null, "currentSupplierSwitchDescription", ValueType.PRIMITIVE, product.getCurrentSupplierSwitchDescription()))
                .addProperty(new Property(null, "currentSSFSupplierSwitch", ValueType.PRIMITIVE, product.getCurrentSSFSupplierSwitch()))
                .addProperty(new Property(null, "currentBrandDescription", ValueType.PRIMITIVE, product.getCurrentBrandDescription()))
                .addProperty(new Property(null, "currentEANBarcodeCode", ValueType.PRIMITIVE, product.getCurrentEANBarcodeCode()))
                .addProperty(new Property(null, "eanBarcodeCode", ValueType.PRIMITIVE, product.getEanBarcodeCode()))
                .addProperty(new Property(null, "currentMonitoredIndicator", ValueType.PRIMITIVE, product.getCurrentMonitoredIndicator()))
                .addProperty(new Property(null, "currentImportedIndicator", ValueType.PRIMITIVE, product.getCurrentImportedIndicator()))
                .addProperty(new Property(null, "currentOriginCode", ValueType.PRIMITIVE, product.getCurrentOriginCode()))
                .addProperty(new Property(null, "currentCommercialCategoryDescription", ValueType.PRIMITIVE, product.getCurrentCommercialCategoryDescription()))
                .addProperty(new Property(null, "currentCommercialCategory", ValueType.PRIMITIVE, product.getCurrentCommercialCategory()))
                .addProperty(new Property(null, "currentSeasonalityDescription", ValueType.PRIMITIVE, product.getCurrentSeasonalityDescription()))
                .addProperty(new Property(null, "currentStorageConditionCode", ValueType.PRIMITIVE, product.getCurrentStorageConditionCode()))
                .addProperty(new Property(null, "currentBrandTreeDescription", ValueType.PRIMITIVE, product.getCurrentBrandTreeDescription()))
                .addProperty(new Property(null, "currentWholesaleBuyer", ValueType.PRIMITIVE, product.getCurrentWholesaleBuyer()))
                .addProperty(new Property(null, "currentPriceGroupCode", ValueType.PRIMITIVE, product.getCurrentPriceGroupCode()))
                .addProperty(new Property(null, "currentPriceCurveCode", ValueType.PRIMITIVE, product.getCurrentPriceCurveCode()))
                .addProperty(new Property(null, "supplierSwitchSK", ValueType.PRIMITIVE, product.getSupplierSwitchSK()))
                .addProperty(new Property(null, "currentWholesaleCoordinator", ValueType.PRIMITIVE, product.getCurrentWholesaleCoordinator()))
                .addProperty(new Property(null, "currentRetailCoordinator", ValueType.PRIMITIVE, product.getCurrentRetailCoordinator()));

        entity.setId(createId(ES_PRODUCTS_NAME, product.getProductSK()));
        return entity;
    }
}
