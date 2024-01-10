package com.store.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "produto")
public class Product {

    @Id
    @Column(name = "skProduto") // TODO validar primary key
    private Long productSK;

    @Column(name = "Cód Produto")
    private Double productCode;

    @Column(name = "Desc Produto (Atual)")
    private String currentProductDescription;

    @Column(name = "Produto")
    private String productName;

    @Column(name = "Produto (Atual)")
    private String currentProductName;

    @Column(name = "SubSortimento (Atual)")
    private String currentSubAssortment;

    @Column(name = "Cód Fornecedor (Atual)")
    private Double currentSupplierCode;

    @Column(name = "Desc Chaveamento Fornecedor (Atual)")
    private String currentSupplierSwitchDescription;

    @Column(name = "SSF Chaveamento Fornecedor (Atual)")
    private String currentSSFSupplierSwitch;

    @Column(name = "Desc Marca (Atual)")
    private String currentBrandDescription;

    @Column(name = "Cód Barra EAN (Atual)")
    private Double currentEANBarcodeCode;

    @Column(name = "Cód Barra EAN")
    private Double eanBarcodeCode;

    @Column(name = "Indicador Monitorado (Atual)")
    private String currentMonitoredIndicator;

    @Column(name = "Indicador Importado (Atual)")
    private String currentImportedIndicator;

    @Column(name = "Cód Origem (Atual)")
    private Long currentOriginCode;

    @Column(name = "Desc Categoria Comercial (Atual)")
    private String currentCommercialCategoryDescription;

    @Column(name = "Categoria Comercial (Atual)")
    private String currentCommercialCategory;

    @Column(name = "Desc Sazonalidade (Atual)")
    private String currentSeasonalityDescription;

    @Column(name = "Cód Condição Estocagem (Atual)")
    private String currentStorageConditionCode;

    @Column(name = "Desc Marca Arvore Mercadológica (Atual)")
    private String currentBrandTreeDescription;

    @Column(name = "Comprador Atacado (Atual)")
    private String currentWholesaleBuyer;

    @Column(name = "Cód Grupo Preço (Atual)")
    private Long currentPriceGroupCode;

    @Column(name = "Cód Curva Preço (Atual)")
    private String currentPriceCurveCode;

    @Column(name = "skChaveamentoFornecedor")
    private Long supplierSwitchSK;

    @Column(name = "Coordenador Atacado (Atual)")
    private String currentWholesaleCoordinator;

    @Column(name = "Coordenador Varejo (Atual)")
    private String currentRetailCoordinator;

    public Long getProductSK() {
        return productSK;
    }

    public Double getProductCode() {
        return productCode;
    }

    public String getCurrentProductDescription() {
        return currentProductDescription;
    }

    public String getProductName() {
        return productName;
    }

    public String getCurrentProductName() {
        return currentProductName;
    }

    public String getCurrentSubAssortment() {
        return currentSubAssortment;
    }

    public Double getCurrentSupplierCode() {
        return currentSupplierCode;
    }

    public String getCurrentSupplierSwitchDescription() {
        return currentSupplierSwitchDescription;
    }

    public String getCurrentSSFSupplierSwitch() {
        return currentSSFSupplierSwitch;
    }

    public String getCurrentBrandDescription() {
        return currentBrandDescription;
    }

    public Double getCurrentEANBarcodeCode() {
        return currentEANBarcodeCode;
    }

    public Double getEanBarcodeCode() {
        return eanBarcodeCode;
    }

    public String getCurrentMonitoredIndicator() {
        return currentMonitoredIndicator;
    }

    public String getCurrentImportedIndicator() {
        return currentImportedIndicator;
    }

    public Long getCurrentOriginCode() {
        return currentOriginCode;
    }

    public String getCurrentCommercialCategoryDescription() {
        return currentCommercialCategoryDescription;
    }

    public String getCurrentCommercialCategory() {
        return currentCommercialCategory;
    }

    public String getCurrentSeasonalityDescription() {
        return currentSeasonalityDescription;
    }

    public String getCurrentStorageConditionCode() {
        return currentStorageConditionCode;
    }

    public String getCurrentBrandTreeDescription() {
        return currentBrandTreeDescription;
    }

    public String getCurrentWholesaleBuyer() {
        return currentWholesaleBuyer;
    }

    public Long getCurrentPriceGroupCode() {
        return currentPriceGroupCode;
    }

    public String getCurrentPriceCurveCode() {
        return currentPriceCurveCode;
    }

    public Long getSupplierSwitchSK() {
        return supplierSwitchSK;
    }

    public String getCurrentWholesaleCoordinator() {
        return currentWholesaleCoordinator;
    }

    public String getCurrentRetailCoordinator() {
        return currentRetailCoordinator;
    }
}
