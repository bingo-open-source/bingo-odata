package bingo.odata.producer.command;

import bingo.odata.OEntityKey;
import bingo.odata.producer.BaseResponse;
import bingo.odata.producer.QueryInfo;

public interface GetNavPropertyCommandContext extends ProducerCommandContext<BaseResponse> {

    String getEntitySetName();

    OEntityKey getEntityKey();

    String getNavProp();

    QueryInfo getQueryInfo();

}
