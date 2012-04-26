package bingo.odata.producer.command;

import bingo.odata.producer.EntitiesResponse;
import bingo.odata.producer.QueryInfo;

public interface GetEntitiesCommandContext extends ProducerCommandContext<EntitiesResponse> {

    String getEntitySetName();

    QueryInfo getQueryInfo();

}
