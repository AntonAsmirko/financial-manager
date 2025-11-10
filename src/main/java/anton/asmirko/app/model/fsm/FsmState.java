package anton.asmirko.app.model.fsm;

import anton.asmirko.app.model.commands.CLICommand;

public sealed interface FsmState permits AuthState, WorkingState {

  boolean acceptsCommand(CLICommand cliCommand);
}
