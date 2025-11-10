package anton.asmirko.app.model.fsm;

import anton.asmirko.app.model.commands.*;
import java.util.Set;

public final class WorkingState implements FsmState {

  private static WorkingState instance;

  private final Set<Class<? extends CLICommand>> permittedCommands =
      Set.of(
          HelpCommand.class,
          QuitCommand.class,
          LogoutCommand.class,
          TransactionCommand.class,
          ListTransactionsCommand.class,
          LimitCommand.class,
          ListLimitsCommand.class,
          StatsCommand.class,
          SaveCommand.class);

  private WorkingState() {}

  public static WorkingState getInstance() {
    if (instance == null) {
      instance = new WorkingState();
    }
    return instance;
  }

  @Override
  public boolean acceptsCommand(CLICommand cliCommand) {
    return permittedCommands.contains(cliCommand.getClass());
  }

  @Override
  public String toString() {
    return "Работа с аккаунтом";
  }
}
