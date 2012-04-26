package bingo.odata.producer.command;

import bingo.odata.command.CommandContext;

public interface ProducerCommandContext<TResult> extends CommandContext {

    TResult getResult();

    void setResult(TResult result);

}
