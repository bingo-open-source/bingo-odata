package odata.producer.command;

import odata.OEntityKey;
import odata.producer.EntityQueryInfo;
import odata.producer.EntityResponse;

public interface GetEntityCommandContext extends ProducerCommandContext<EntityResponse> {

    String getEntitySetName();

    OEntityKey getEntityKey();

    EntityQueryInfo getQueryInfo();

}
