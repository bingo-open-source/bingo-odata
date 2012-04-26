package bingo.odata;

import bingo.odata.edm.EdmType;

/**
 * OData instance object of the given {@link EdmType}.
 *
 * @see OEntity
 * @see OSimpleObject
 * @see OComplexObject
 * @see OCollection
 */
public interface OObject {

    /** Gets the edm type of this object */
    EdmType getType();

}
