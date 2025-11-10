package anton.asmirko.app.model.commands;

public record SaveCommand(String path) implements CLICommand {

  public static final String STR_REP = "save";

  @Override
  public String getStrRep() {
    return STR_REP;
  }
}
