package anton.asmirko.app.state.algorithms;

import anton.asmirko.app.model.commands.LogoutCommand;
import anton.asmirko.app.model.fsm.AuthState;
import anton.asmirko.app.model.fsm.FsmState;
import anton.asmirko.app.model.user.User;
import anton.asmirko.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component(LogoutCommand.STR_REP)
@RequiredArgsConstructor
public class LogoutProcessingAlg implements CLICommandProcessingAlg<LogoutCommand> {

  private final UserService userService;

  @Override
  public FsmState process(LogoutCommand cliCommand) {
    final User user = userService.getCurrentUser();
    userService.setCurrentUser(null);
    System.out.printf("Вы вышли из аккаунта %s%n", user);
    return AuthState.getInstance();
  }
}
