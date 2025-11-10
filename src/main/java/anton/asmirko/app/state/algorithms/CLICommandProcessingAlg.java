package anton.asmirko.app.state.algorithms;

import anton.asmirko.app.model.commands.CLICommand;
import anton.asmirko.app.model.fsm.FsmState;

public interface CLICommandProcessingAlg<T extends CLICommand> {
  FsmState process(T cliCommand);
}
