package bingo.odata.command;

public interface Command<TContext extends CommandContext> {

    CommandResult execute(TContext context) throws Exception;

}
