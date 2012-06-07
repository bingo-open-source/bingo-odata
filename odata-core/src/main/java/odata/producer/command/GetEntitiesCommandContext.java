package odata.producer.command;

import odata.producer.EntitiesResponse;
import odata.producer.QueryInfo;

public interface GetEntitiesCommandContext extends ProducerCommandContext<EntitiesResponse> {

    String getEntitySetName();

    QueryInfo getQueryInfo();

}
