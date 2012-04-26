package bingo.odata.producer.command;

import bingo.odata.producer.CountResponse;
import bingo.odata.producer.QueryInfo;

public interface GetEntitiesCountCommandContext extends ProducerCommandContext<CountResponse> {

    String getEntitySetName();

    QueryInfo getQueryInfo();

}
