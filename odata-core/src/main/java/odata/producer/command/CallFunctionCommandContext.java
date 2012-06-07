package odata.producer.command;

import java.util.Map;

import odata.OFunctionParameter;
import odata.edm.EdmFunctionImport;
import odata.producer.BaseResponse;
import odata.producer.QueryInfo;


public interface CallFunctionCommandContext extends ProducerCommandContext<BaseResponse> {

    EdmFunctionImport getName();

    Map<String, OFunctionParameter> getParams();

    QueryInfo getQueryInfo();

}
