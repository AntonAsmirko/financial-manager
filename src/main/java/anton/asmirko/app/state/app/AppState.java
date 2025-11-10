package anton.asmirko.app.state;

import anton.asmirko.app.model.fsm.AuthState;
import anton.asmirko.app.model.fsm.FsmState;
import anton.asmirko.app.model.user.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

@Component
public class AppState {

    private final Set<User> users = new HashSet<>();
    @Setter
    private User currentUser;

    @Getter
    private FsmState fsmState = AuthState.getInstance();

    public void load(final Path filePath) {
        if (filePath == null) {
            return;
        }
    }

    public Set<User> getUsers() {
        return new HashSet<>(users);
    }
}
