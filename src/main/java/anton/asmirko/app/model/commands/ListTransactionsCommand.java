package anton.asmirko.app.model.commands;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record ListTransactions(Map<Key, String> keys) implements CLICommand {

    public static final String STR_REP = "transaction-list";

    @Override
    public String getStrRep() {
        return STR_REP;
    }

    public enum Key implements KeyValueValidator {
        CATEGORY("-c", "^([a-z]|[A-Z]).*$"),
        FROM_TIME("-from", "^\\d{4}-\\d{2}-\\d{2}T(?:[01]\\d|2[0-3]):[0-5]\\d$"),
        TO_TIME("-from", "^\\d{4}-\\d{2}-\\d{2}T(?:[01]\\d|2[0-3]):[0-5]\\d$"),
        INCOMING("-incoming", null),
        OUTCOMING("-outcoming", null);

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
        public int getArgCount() {
            return pattern != null ? 1 : 0;
        }

        @Override
        public String toString() {
            return strRep;
        }
    }
}
