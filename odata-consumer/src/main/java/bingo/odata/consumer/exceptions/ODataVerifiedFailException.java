package bingo.odata.consumer.exceptions;


public class ODataVerifiedFailException extends ODataConsumerException {

	private static final long serialVersionUID = -1533447768593885495L;
	
	public ODataVerifiedFailException(String name, String value) {
		super("No such object within metadata document: [{0} = {1}]", name, value);
	}
}