package odata.producer.command;

import odata.OEntityKey;
import odata.producer.BaseResponse;
import odata.producer.QueryInfo;

public interface GetNavPropertyCommandContext extends ProducerCommandContext<BaseResponse> {

    String getEntitySetName();

    OEntityKey getEntityKey();

    String getNavProp();

    QueryInfo getQueryInfo();

}
