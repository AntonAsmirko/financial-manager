package anton.asmirko.app.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import anton.asmirko.app.model.commands.LoginCommand;
import anton.asmirko.app.model.user.User;
import anton.asmirko.app.model.wallet.Wallet;
import anton.asmirko.app.state.app.AppState;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserServiceTest {

  private AppState appState;
  private UserService userService;

  @BeforeEach
  void setup() {
    appState = mock(AppState.class);
    userService = new UserService(appState);
  }

  @Test
  void loginShouldReturnUserWhenCredentialsCorrect() {
    User user = new User("anton", "123", "Anton", new Wallet());
    when(appState.getUsers()).thenReturn(Set.of(user));
    LoginCommand cmd = new LoginCommand("anton", "123");

    User result = userService.login(cmd);

    assertEquals(user, result);
  }

  @Test
  void loginShouldThrowIfUserNotFound() {
    when(appState.getUsers()).thenReturn(Set.of());
    LoginCommand cmd = new LoginCommand("anton", "123");

    assertThrows(IllegalArgumentException.class, () -> userService.login(cmd));
  }

  @Test
  void loginShouldThrowIfPasswordIncorrect() {
    User user = new User("anton", "wrong", "Anton", new Wallet());
    when(appState.getUsers()).thenReturn(Set.of(user));
    LoginCommand cmd = new LoginCommand("anton", "123");

    assertThrows(IllegalArgumentException.class, () -> userService.login(cmd));
  }

  @Test
  void setCurrentUserShouldDelegateToAppState() {
    User user = new User("anton", "123", "Anton", new Wallet());
    userService.setCurrentUser(user);
    verify(appState).setCurrentUser(user);
  }

  @Test
  void createNewUserShouldAddUser() {
    Set<User> users = new HashSet<>();
    when(appState.getUsers()).thenReturn(users);

    User result = userService.createNewUser("anton", "123", "Anton");

    assertEquals("anton", result.getLogin());
    verify(appState).addUser(result);
  }

  @Test
  void createNewUserShouldThrowIfAlreadyExists() {
    User existing = new User("anton", "123", "Anton", new Wallet());
    when(appState.getUsers()).thenReturn(Set.of(existing));

    assertThrows(
        IllegalStateException.class, () -> userService.createNewUser("anton", "123", "Anton"));
  }

  @Test
  void getCurrentUserShouldReturnFromAppState() {
    User user = new User("anton", "123", "Anton", new Wallet());
    when(appState.getCurrentUser()).thenReturn(user);

    User result = userService.getCurrentUser();

    assertEquals(user, result);
    verify(appState).getCurrentUser();
  }
}
