package com.store.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "cliente")
public class Client {

    @Id
    @Column(name = "skCliente") // TODO validar primary key
    private Long clientSK;

    @Column(name = "Cód Cliente")
    private Double clientCode;

    @Column(name = "Cliente (Atual)")
    private String currentClient;

    @Column(name = "Rede (Atual)")
    private String currentNetwork;

    @Column(name = "Associação (Atual)")
    private String currentAssociation;

    @Column(name = "UF Cliente (Atual)")
    private String clientState;

    @Column(name = "Setor (Atual)")
    private String currentSector;

    @Column(name = "Supervisor (Atual)")
    private String currentSupervisor;

    @Column(name = "Desc Gerente (Atual)")
    private String currentManagerDescription;

    @Column(name = "Gerente (Atual)")
    private String currentManager;

    @Column(name = "Desc Divisional (Atual)")
    private String currentDivisionalDescription;

    @Column(name = "Desc Superintendência (Atual)")
    private String currentSuperintendenceDescription;

    @Column(name = "Bairro (Atual)")
    private String currentNeighborhood;

    @Column(name = "Data Últ Compra")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastPurchaseDate;

    @Column(name = "Cód Grupo SAP")
    private String sapGroupCode;

    @Column(name = "Cód Grupo SAP (Atual)")
    private String currentSapGroupCode;

    @Column(name = "Micro Região (Atual)")
    private String currentMicroRegion;

    @Column(name = "CNPJ/CPF (Atual)")
    private String currentCNPJCPF;

    @Column(name = "Meso Região (Atual)")
    private String currentMesoRegion;

    @Column(name = "Desc Segmentação Cliente (Atual)")
    private String currentClientSegmentationDescription;

    @Column(name = "Desc Grupo Prazo Cliente (Atual)")
    private String currentClientGroupTermDescription;

    @Column(name = "Indicador Controlado (Atual)")
    private String currentControlledIndicator;

    @Column(name = "inRegistroAtivo")
    private Integer activeRegistrationIndicator;

    public Long getClientSK() {
        return clientSK;
    }

    public Double getClientCode() {
        return clientCode;
    }

    public String getCurrentClient() {
        return currentClient;
    }

    public String getCurrentNetwork() {
        return currentNetwork;
    }

    public String getCurrentAssociation() {
        return currentAssociation;
    }

    public String getClientState() {
        return clientState;
    }

    public String getCurrentSector() {
        return currentSector;
    }

    public String getCurrentSupervisor() {
        return currentSupervisor;
    }

    public String getCurrentManagerDescription() {
        return currentManagerDescription;
    }

    public String getCurrentManager() {
        return currentManager;
    }

    public String getCurrentDivisionalDescription() {
        return currentDivisionalDescription;
    }

    public String getCurrentSuperintendenceDescription() {
        return currentSuperintendenceDescription;
    }

    public String getCurrentNeighborhood() {
        return currentNeighborhood;
    }

    public Date getLastPurchaseDate() {
        return lastPurchaseDate;
    }

    public String getSapGroupCode() {
        return sapGroupCode;
    }

    public String getCurrentSapGroupCode() {
        return currentSapGroupCode;
    }

    public String getCurrentMicroRegion() {
        return currentMicroRegion;
    }

    public String getCurrentCNPJCPF() {
        return currentCNPJCPF;
    }

    public String getCurrentMesoRegion() {
        return currentMesoRegion;
    }

    public String getCurrentClientSegmentationDescription() {
        return currentClientSegmentationDescription;
    }

    public String getCurrentClientGroupTermDescription() {
        return currentClientGroupTermDescription;
    }

    public String getCurrentControlledIndicator() {
        return currentControlledIndicator;
    }

    public Integer getActiveRegistrationIndicator() {
        return activeRegistrationIndicator;
    }
}
