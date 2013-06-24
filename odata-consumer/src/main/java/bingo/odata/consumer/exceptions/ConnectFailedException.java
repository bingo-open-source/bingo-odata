package bingo.odata.consumer.exceptions;

public class ConnectFailedException extends ODataConsumerException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1452585954039032869L;
	
	public ConnectFailedException(String targetUrl) {
		super("Couldn't connet to the target url: " + targetUrl);
	}

}
