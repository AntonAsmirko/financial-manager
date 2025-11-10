package anton.asmirko.app.state;

import anton.asmirko.app.model.commands.CLICommand;

public interface AppFsm {
    <T extends CLICommand> void handleCommand(T cliCommand);
}
