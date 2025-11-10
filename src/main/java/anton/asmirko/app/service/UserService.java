package anton.asmirko.app.service;

import anton.asmirko.app.model.commands.LoginCommand;
import anton.asmirko.app.model.user.User;
import anton.asmirko.app.model.wallet.Wallet;
import anton.asmirko.app.state.app.AppState;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final AppState appState;

  public User login(final LoginCommand loginCommand) {
    final User user =
        appState.getUsers().stream()
            .findFirst()
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        String.format("Пользователя %s не существует", loginCommand.login())));
    if (user.getLogin().equals(loginCommand.login())
        && user.getPassword().equals(loginCommand.password())) {
      return user;
    }
    throw new IllegalArgumentException(String.format("Неверный пароль для пользователя %s", user));
  }

  public void setCurrentUser(final User user) {
    appState.setCurrentUser(user);
  }

  public User createNewUser(final String login, final String password, final String name) {
    final User user = new User(login, password, name, new Wallet());
    final Set<User> users = appState.getUsers();
    if (users.contains(user)) {
      throw new IllegalStateException(
          String.format("Пользователь с логином %s уже существует", user));
    }
    appState.addUser(user);
    return user;
  }

  public User getCurrentUser() {
    return appState.getCurrentUser();
  }
}
