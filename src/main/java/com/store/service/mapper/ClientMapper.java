package com.store.service.mapper;

import com.store.entity.Client;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;

import static com.store.provider.EdmProvider.ES_CLIENT_NAME;
import static com.store.service.util.Util.createId;

public class ClientMapper {

    public static Entity convertToEntity(Client client) {
        Entity entity = new Entity()
                .addProperty(new Property(null, "clientSK", ValueType.PRIMITIVE, client.getClientSK()))
                .addProperty(new Property(null, "clientCode", ValueType.PRIMITIVE, client.getClientCode()))
                .addProperty(new Property(null, "currentClient", ValueType.PRIMITIVE, client.getCurrentClient()))
                .addProperty(new Property(null, "currentNetwork", ValueType.PRIMITIVE, client.getCurrentNetwork()))
                .addProperty(new Property(null, "currentAssociation", ValueType.PRIMITIVE, client.getCurrentAssociation()))
                .addProperty(new Property(null, "clientState", ValueType.PRIMITIVE, client.getClientState()))
                .addProperty(new Property(null, "currentSector", ValueType.PRIMITIVE, client.getCurrentSector()))
                .addProperty(new Property(null, "currentSupervisor", ValueType.PRIMITIVE, client.getCurrentSupervisor()))
                .addProperty(new Property(null, "currentManagerDescription", ValueType.PRIMITIVE, client.getCurrentManagerDescription()))
                .addProperty(new Property(null, "currentManager", ValueType.PRIMITIVE, client.getCurrentManager()))
                .addProperty(new Property(null, "currentDivisionalDescription", ValueType.PRIMITIVE, client.getCurrentDivisionalDescription()))
                .addProperty(new Property(null, "currentSuperintendenceDescription", ValueType.PRIMITIVE, client.getCurrentSuperintendenceDescription()))
                .addProperty(new Property(null, "currentNeighborhood", ValueType.PRIMITIVE, client.getCurrentNeighborhood()))
                .addProperty(new Property(null, "lastPurchaseDate", ValueType.PRIMITIVE, client.getLastPurchaseDate()))
                .addProperty(new Property(null, "sapGroupCode", ValueType.PRIMITIVE, client.getSapGroupCode()))
                .addProperty(new Property(null, "currentSapGroupCode", ValueType.PRIMITIVE, client.getCurrentSapGroupCode()))
                .addProperty(new Property(null, "currentMicroRegion", ValueType.PRIMITIVE, client.getCurrentMicroRegion()))
                .addProperty(new Property(null, "currentCNPJCPF", ValueType.PRIMITIVE, client.getCurrentCNPJCPF()))
                .addProperty(new Property(null, "currentMesoRegion", ValueType.PRIMITIVE, client.getCurrentMesoRegion()))
                .addProperty(new Property(null, "currentClientSegmentationDescription", ValueType.PRIMITIVE, client.getCurrentClientSegmentationDescription()))
                .addProperty(new Property(null, "currentClientGroupTermDescription", ValueType.PRIMITIVE, client.getCurrentClientGroupTermDescription()))
                .addProperty(new Property(null, "currentControlledIndicator", ValueType.PRIMITIVE, client.getCurrentControlledIndicator()))
                .addProperty(new Property(null, "activeRegistrationIndicator", ValueType.PRIMITIVE, client.getActiveRegistrationIndicator()));

        entity.setId(createId(ES_CLIENT_NAME, client.getClientSK()));
        return entity;
    }
}
