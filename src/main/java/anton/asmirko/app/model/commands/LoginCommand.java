package anton.asmirko.app.model.commands;

public record LoginCommand(String login, String password) implements CLICommand {

  public static final String STR_REP = "login";

  @Override
  public String getStrRep() {
    return STR_REP;
  }
}
