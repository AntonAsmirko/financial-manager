package anton.asmirko.app.model.user;

import anton.asmirko.app.model.wallet.Wallet;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class User {

  @NonNull private final String login;

  @NonNull private String password;

  @NonNull private String name;

  @NonNull @Getter private final Wallet wallet;

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof User) {
      return ((User) obj).login.equals(this.login);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(login);
  }

  @Override
  public String toString() {
    return login;
  }

  public @NonNull String getPassword() {
    return password;
  }

  public @NonNull String getLogin() {
    return login;
  }
}
