/**
 * file created at 2013-6-24
 */
package bingo.odata.producer.requests;

import bingo.odata.ODataContext;
import bingo.odata.ODataRequest;
import bingo.odata.ODataResponse;

public abstract class ODataRequestPluginAdapter implements ODataRequestPlugin {

	public boolean preHandle(ODataContext context, ODataRequest request, ODataResponse response) throws Throwable {
		return false;
	}

	public void postHandle(ODataContext context, ODataRequest request, ODataResponse response) throws Throwable {

	}

	public void doFinally(ODataContext context, ODataRequest request, ODataResponse response, boolean handled) throws Throwable {

	}
}