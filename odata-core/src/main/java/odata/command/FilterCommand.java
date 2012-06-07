package odata.command;

public interface FilterCommand<TContext extends CommandContext> extends Command<TContext> {

    FilterResult postProcess(TContext context, Exception e);

}
