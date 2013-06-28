package bingo.odata.consumer.exceptions;

public class ODataResponseException extends ODataConsumerException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2879852659127267592L;

	public ODataResponseException() {
		super();
	}
	
	public ODataResponseException(String message) {
		super(message);
	}
	
	public ODataResponseException(Throwable arg0) {
		super(arg0);
	}
	
	public ODataResponseException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ODataResponseException(String message, Object... args) {
	    super(message, args);
    }
}
