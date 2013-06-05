/**
 * file created at 2013-6-5
 */
package bingo.odata.consumer;

import bingo.odata.ODataContextBase;
import bingo.odata.ODataReaderContext;
import bingo.odata.ODataWriterContext;

public class ODataConsumerContext extends ODataContextBase implements ODataReaderContext,ODataWriterContext {
	
	public boolean isConsumer() {
	    return true;
    }

	public boolean isProducer() {
	    return false;
    }

	public boolean isMinimal() {
	    return false;
    }
}
