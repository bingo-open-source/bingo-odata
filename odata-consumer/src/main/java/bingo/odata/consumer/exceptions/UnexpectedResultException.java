package bingo.odata.consumer.exceptions;

public class UnexpectedResultException extends ODataConsumerException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -72728810755205783L;

	public UnexpectedResultException(String expected, String actual) {
		super("Expected result is \"{0}\", but actual is \"{1}\".", expected, actual);
	}
}
