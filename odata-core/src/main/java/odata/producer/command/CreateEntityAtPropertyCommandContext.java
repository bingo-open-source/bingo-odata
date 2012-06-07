package odata.producer.command;

import odata.OEntity;
import odata.OEntityKey;
import odata.producer.EntityResponse;

public interface CreateEntityAtPropertyCommandContext extends ProducerCommandContext<EntityResponse> {

    String getEntitySetName();

    OEntityKey getEntityKey();

    String getNavProp();

    OEntity getEntity();

}
