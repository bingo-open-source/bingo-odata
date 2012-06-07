package odata.producer.command;

import odata.OEntityKey;
import odata.producer.CountResponse;
import odata.producer.QueryInfo;

public interface GetNavPropertyCountCommandContext extends ProducerCommandContext<CountResponse> {

    String getEntitySetName();

    OEntityKey getEntityKey();

    String getNavProp();

    QueryInfo getQueryInfo();

}
