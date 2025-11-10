package anton.asmirko.app.model.commands;

import java.util.Map;

public record StatsCommand(Map<ListTransactionsCommand.Key, String> keys) implements CLICommand {

  public static final String STR_REP = "stats";

  @Override
  public String getStrRep() {
    return STR_REP;
  }
}
