package anton.asmirko.app.state.app;

import anton.asmirko.app.model.fsm.AuthState;
import anton.asmirko.app.model.fsm.FsmState;
import anton.asmirko.app.model.user.User;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class AppState {

  private final Set<User> users = new HashSet<>();

  @Setter @Getter private User currentUser;

  @Getter private FsmState fsmState = AuthState.getInstance();

  public void load(List<User> users) {
    this.users.addAll(users);
  }

  public Set<User> getUsers() {
    return new HashSet<>(users);
  }

  public void addUser(final User user) {
    users.add(user);
  }

  void setFsmState(FsmState fsmState) {
    this.fsmState = fsmState;
  }
}
