package anton.asmirko.app.model.fsm;

import anton.asmirko.app.model.commands.*;
import java.util.Set;

public final class AuthState implements FsmState {

  private final Set<Class<? extends CLICommand>> permittedCommands =
      Set.of(
          HelpCommand.class,
          LoginCommand.class,
          QuitCommand.class,
          SignupCommand.class,
          SaveCommand.class);

  private static AuthState instance;

  private AuthState() {}

  public static AuthState getInstance() {
    if (instance == null) {
      instance = new AuthState();
    }
    return instance;
  }

  @Override
  public boolean acceptsCommand(CLICommand cliCommand) {
    return permittedCommands.contains(cliCommand.getClass());
  }

  @Override
  public String toString() {
    return "Аутентификация";
  }
}
