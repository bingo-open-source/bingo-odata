package bingo.odata.producer.command;

import java.util.Map;

import bingo.odata.OFunctionParameter;
import bingo.odata.edm.EdmFunctionImport;
import bingo.odata.producer.BaseResponse;
import bingo.odata.producer.QueryInfo;

public interface CallFunctionCommandContext extends ProducerCommandContext<BaseResponse> {

    EdmFunctionImport getName();

    Map<String, OFunctionParameter> getParams();

    QueryInfo getQueryInfo();

}
