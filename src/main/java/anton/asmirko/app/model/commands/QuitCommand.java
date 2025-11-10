package anton.asmirko.app.model.commands;

public class DummyCommand implements CLICommand {

    public static final String STR_REP = "dummy";

    @Override
    public String getStrRep() {
        return STR_REP;
    }
}
