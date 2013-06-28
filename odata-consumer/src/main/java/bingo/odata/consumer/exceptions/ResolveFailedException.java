package bingo.odata.consumer.exceptions;

public class ResolveFailedException extends ODataResponseException{
	
	public ResolveFailedException(Throwable e) {
		super("Response receieved resolved failed!", e);
	}
}
