package anton.asmirko.app.model.commands;

import java.util.Map;
import java.util.regex.Pattern;

public record ListTransactionsCommand(Map<Key, String> keys) implements CLICommand {

  public static final String STR_REP = "transaction-list";

  @Override
  public String getStrRep() {
    return STR_REP;
  }

  public enum Key implements KeyValueValidator {
    CATEGORY("-c", 1, "^([a-zA-ZА-Яа-я][^,]*)(,[a-zA-ZА-Яа-я][^,]*)*$"),
    FROM_TIME("-from", 1, "^\\d{4}-\\d{2}-\\d{2}T(?:[01]\\d|2[0-3]):[0-5]\\d$"),
    TO_TIME("-to", 1, "^\\d{4}-\\d{2}-\\d{2}T(?:[01]\\d|2[0-3]):[0-5]\\d$"),
    INCOMING("-incoming", 0, null),
    OUTCOMING("-outcoming", 0, null);

    private final String strRep;
    private final Pattern pattern;
    private final Integer argCount;

    Key(String strRep, Integer argCount, String regexPattern) {
      this.strRep = strRep;
      this.argCount = argCount;
      if (regexPattern != null) {
        this.pattern = Pattern.compile(regexPattern);
      } else {
        this.pattern = null;
      }
    }

    @Override
    public boolean isValid(final String value) {
      if (pattern != null) {
        return pattern.matcher(value).matches();
      } else {
        return true;
      }
    }

    @Override
    public Integer getArgCount() {
      return argCount;
    }

    @Override
    public String toString() {
      return strRep;
    }
  }
}
