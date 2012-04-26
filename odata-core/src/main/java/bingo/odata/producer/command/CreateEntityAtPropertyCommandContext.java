package bingo.odata.producer.command;

import bingo.odata.OEntity;
import bingo.odata.OEntityKey;
import bingo.odata.producer.EntityResponse;

public interface CreateEntityAtPropertyCommandContext extends ProducerCommandContext<EntityResponse> {

    String getEntitySetName();

    OEntityKey getEntityKey();

    String getNavProp();

    OEntity getEntity();

}
