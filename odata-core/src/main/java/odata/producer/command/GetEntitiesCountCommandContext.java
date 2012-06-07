package odata.producer.command;

import odata.producer.CountResponse;
import odata.producer.QueryInfo;

public interface GetEntitiesCountCommandContext extends ProducerCommandContext<CountResponse> {

    String getEntitySetName();

    QueryInfo getQueryInfo();

}
