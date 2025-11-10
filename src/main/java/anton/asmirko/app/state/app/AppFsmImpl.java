package anton.asmirko.app.state.app;

import anton.asmirko.app.model.commands.CLICommand;
import anton.asmirko.app.model.fsm.FsmState;
import anton.asmirko.app.state.algorithms.CLICommandProcessingAlg;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppFsmImpl implements AppFsm {

  private final AppState appState;
  private final Map<String, CLICommandProcessingAlg<?>> algorithms;

  @Override
  @SuppressWarnings("unchecked")
  public <T extends CLICommand> void handleCommand(T cliCommand) {
    final FsmState state = appState.getFsmState();
    if (!state.acceptsCommand(cliCommand)) {
      throw new IllegalStateException(
          String.format("Команда %s не допустима в состоянии %s", cliCommand.getStrRep(), state));
    }
    var alg = (CLICommandProcessingAlg<CLICommand>) algorithms.get(cliCommand.getStrRep());
    appState.setFsmState(
        Optional.ofNullable(alg)
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        String.format("Команда %s не поддердивается", cliCommand.getStrRep())))
            .process(cliCommand));
  }
}
