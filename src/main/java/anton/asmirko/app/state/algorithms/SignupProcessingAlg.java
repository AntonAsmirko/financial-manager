package anton.asmirko.app.state.algorithms;

import anton.asmirko.app.model.commands.SignupCommand;
import anton.asmirko.app.model.fsm.AuthState;
import anton.asmirko.app.model.fsm.FsmState;
import anton.asmirko.app.model.user.User;
import anton.asmirko.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component(SignupCommand.STR_REP)
@RequiredArgsConstructor
public class SignupProcessingAlg implements CLICommandProcessingAlg<SignupCommand> {

  private final UserService userService;

  @Override
  public FsmState process(SignupCommand cliCommand) {
    final var keys = cliCommand.keys();
    final String login = cliCommand.login();
    final String password = cliCommand.password();
    final String name = keys.getOrDefault(SignupCommand.Key.NAME, login);
    final User user = userService.createNewUser(login, password, name);
    System.out.printf("Пользователь %s успешно создан%n", user);
    return AuthState.getInstance();
  }
}
