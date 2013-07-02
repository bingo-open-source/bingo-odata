package bingo.odata.consumer.exceptions;

public class ResolveFailedException extends ODataResponseException{
	
	public ResolveFailedException(Throwable e) {
		super("Response receieved resolved failed!", e);
	}
	
	public ResolveFailedException(String resolveTarget) {
		super("Could not resolve target: {0}", resolveTarget);
	}
}
