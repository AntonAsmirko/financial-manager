package anton.asmirko.app.model.commands;

public final class QuitCommand implements CLICommand {

  public static final String STR_REP = "\\q";

  @Override
  public String getStrRep() {
    return STR_REP;
  }
}
