package anton.asmirko.app.state.algorithms;

import anton.asmirko.app.model.commands.LoginCommand;
import anton.asmirko.app.model.fsm.FsmState;
import anton.asmirko.app.model.fsm.WorkingState;
import anton.asmirko.app.model.user.User;
import anton.asmirko.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component(LoginCommand.STR_REP)
@RequiredArgsConstructor
public class AuthProcessingAlg implements CLICommandProcessingAlg<LoginCommand> {

  private final UserService userService;

  @Override
  public FsmState process(LoginCommand cliCommand) {
    final User user = userService.login(cliCommand);
    userService.setCurrentUser(user);
    System.out.printf("Успешно выполнен вход пользователем %s%n", user);
    return WorkingState.getInstance();
  }
}
