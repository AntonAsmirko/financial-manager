package anton.asmirko.app.model.commands;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record SignupCommand(String login, String password, Map<Key, String> keys)
    implements CLICommand {

  public static final String STR_REP = "signup";

  @Override
  public String getStrRep() {
    return STR_REP;
  }

  public enum Key implements KeyValueValidator {
    NAME("-n", "^[A-Z][a-z]+(?:[-'\\s][A-Z][a-z]+)*$");

    private final String strRep;
    private final Pattern pattern;

    Key(String strRep, String regexPattern) {
      this.strRep = strRep;
      if (regexPattern != null) {
        this.pattern = Pattern.compile(regexPattern);
      } else {
        this.pattern = null;
      }
    }

    @Override
    public boolean isValid(final String value) {
      final Matcher matcher = pattern.matcher(value);
      return matcher.matches();
    }

    @Override
    public Integer getArgCount() {
      return pattern != null ? 1 : 0;
    }

    @Override
    public String toString() {
      return strRep;
    }
  }
}
