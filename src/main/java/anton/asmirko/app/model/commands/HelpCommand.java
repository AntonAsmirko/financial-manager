package anton.asmirko.app.model.commands;

public class HelpCommand implements CLICommand {
  public static final String STR_REP = "help";

  @Override
  public String getStrRep() {
    return STR_REP;
  }
}
