package odata.producer.command;

import odata.command.CommandContext;

public interface ProducerCommandContext<TResult> extends CommandContext {

    TResult getResult();

    void setResult(TResult result);

}
