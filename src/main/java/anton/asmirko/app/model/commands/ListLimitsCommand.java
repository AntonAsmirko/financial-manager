package anton.asmirko.app.model.commands;

import java.util.Map;

public record ListLimitsCommand(Map<ListTransactionsCommand.Key, String> keys)
    implements CLICommand {

  public static final String STR_REP = "limit-list";

  @Override
  public String getStrRep() {
    return STR_REP;
  }
}
