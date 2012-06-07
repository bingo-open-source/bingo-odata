package odata.producer.command;

import odata.OEntity;
import odata.producer.EntityResponse;

public interface CreateEntityCommandContext extends ProducerCommandContext<EntityResponse> {

    String getEntitySetName();

    OEntity getEntity();

}
