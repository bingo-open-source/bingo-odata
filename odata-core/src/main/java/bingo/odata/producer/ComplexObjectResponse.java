package bingo.odata.producer;

import bingo.odata.OComplexObject;

/**
 * An <code>ComplexObjectResponse</code> is a response to a client request expecting a single instance of a complex type.
 *
 * <p>The {@link Responses} static factory class can be used to create <code>ComplexObjectResponse</code> instances.</p>
 */
public interface ComplexObjectResponse extends BaseResponse {

    OComplexObject getObject();

}
