package bingo.odata.producer.command;

import bingo.odata.OEntityKey;
import bingo.odata.producer.EntityQueryInfo;
import bingo.odata.producer.EntityResponse;

public interface GetEntityCommandContext extends ProducerCommandContext<EntityResponse> {

    String getEntitySetName();

    OEntityKey getEntityKey();

    EntityQueryInfo getQueryInfo();

}
