package anton.asmirko.app.model.commands;

public class LogoutCommand implements CLICommand {

  public static final String STR_REP = "logout";

  @Override
  public String getStrRep() {
    return STR_REP;
  }
}
