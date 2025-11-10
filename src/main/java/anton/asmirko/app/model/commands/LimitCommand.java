package anton.asmirko.app.model.commands;

import java.math.BigDecimal;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record LimitCommand(BigDecimal limit, Map<Key, String> keys) implements CLICommand {

  public static final String STR_REP = "limit";

  @Override
  public String getStrRep() {
    return STR_REP;
  }

  public enum Key implements KeyValueValidator {
    NAME("-n", "^([a-z]|[A-Z]|[А-Я]|[а-я]).*$"),
    CATEGORY("-c", "^([a-z]|[A-Z]|[А-Я]|[а-я]).*$"),
    FROM("-from", "^\\d{4}-\\d{2}-\\d{2}T(?:[01]\\d|2[0-3]):[0-5]\\d$"),
    TO("-to", "^\\d{4}-\\d{2}-\\d{2}T(?:[01]\\d|2[0-3]):[0-5]\\d$");

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
