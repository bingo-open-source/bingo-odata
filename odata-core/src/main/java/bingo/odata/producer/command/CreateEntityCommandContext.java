package bingo.odata.producer.command;

import bingo.odata.OEntity;
import bingo.odata.producer.EntityResponse;

public interface CreateEntityCommandContext extends ProducerCommandContext<EntityResponse> {

    String getEntitySetName();

    OEntity getEntity();

}
