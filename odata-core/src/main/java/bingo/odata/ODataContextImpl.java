/**
 * file created at 2013-6-6
 */
package bingo.odata;

public class ODataContextImpl extends ODataContextBase implements ODataReaderContext, ODataWriterContext {

	protected boolean minimal;
	protected boolean consumer;
	protected boolean producer;

	public boolean isMinimal() {
		return minimal;
	}

	public void setMinimal(boolean minimal) {
		this.minimal = minimal;
	}

	public boolean isConsumer() {
		return consumer;
	}

	public void setConsumer(boolean consumer) {
		this.consumer = consumer;
	}

	public boolean isProducer() {
		return producer;
	}

	public void setProducer(boolean producer) {
		this.producer = producer;
	}
}