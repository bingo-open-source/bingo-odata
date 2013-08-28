package bingo.odata.consumer.exceptions;

public class ResolveFailedException extends ODataResponseException{
	
	private static final long serialVersionUID = -4997807991772221024L;

	public ResolveFailedException(Throwable e) {
		super("Response receieved resolved failed!", e);
	}
	
	public ResolveFailedException(String resolveTarget) {
		super("Could not resolve target: {0}", resolveTarget);
	}
}
