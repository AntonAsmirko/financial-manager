package anton.asmirko.app.state.algorithms;

import anton.asmirko.app.model.commands.SaveCommand;
import anton.asmirko.app.model.fsm.FsmState;
import anton.asmirko.app.service.StateService;
import anton.asmirko.app.state.app.AppState;
import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component(SaveCommand.STR_REP)
@RequiredArgsConstructor
public class SaveCommandProcessingAlg implements CLICommandProcessingAlg<SaveCommand> {

  private final StateService service;
  private final AppState appState;

  @Override
  public FsmState process(SaveCommand cliCommand) {
    service.saveToFile(Path.of(cliCommand.path()));
    return appState.getFsmState();
  }
}
