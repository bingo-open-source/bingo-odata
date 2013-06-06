/**
 * file created at 2013-6-6
 */
package bingo.odata.producer;

import bingo.meta.edm.EdmFunctionImport;
import bingo.odata.model.ODataParameters;
import bingo.odata.model.ODataValue;

public interface ODataFunction {

	EdmFunctionImport getMetadata(); 
	
	ODataValue invoke(ODataProducerContext context,ODataParameters parameters);

}