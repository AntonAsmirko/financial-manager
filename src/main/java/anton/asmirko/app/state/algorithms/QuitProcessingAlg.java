package anton.asmirko.app.state.algorithms;

import anton.asmirko.app.model.commands.QuitCommand;
import anton.asmirko.app.model.fsm.FsmState;
import anton.asmirko.app.state.app.AppState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component(QuitCommand.STR_REP)
@RequiredArgsConstructor
public class QuitProcessingAlg implements CLICommandProcessingAlg<QuitCommand> {

  private final AppState appState;

  @Override
  public FsmState process(QuitCommand cliCommand) {
    return appState.getFsmState();
  }
}
