package bingo.odata.producer.command;

import bingo.odata.OEntityKey;
import bingo.odata.producer.CountResponse;
import bingo.odata.producer.QueryInfo;

public interface GetNavPropertyCountCommandContext extends ProducerCommandContext<CountResponse> {

    String getEntitySetName();

    OEntityKey getEntityKey();

    String getNavProp();

    QueryInfo getQueryInfo();

}
