package anton.asmirko.app.state.algorithms;

import anton.asmirko.app.model.commands.LimitCommand;
import anton.asmirko.app.model.fsm.FsmState;
import anton.asmirko.app.model.fsm.WorkingState;
import anton.asmirko.app.model.limit.Limit;
import anton.asmirko.app.service.LimitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component(LimitCommand.STR_REP)
@RequiredArgsConstructor
public class LimitProcessingAlg implements CLICommandProcessingAlg<LimitCommand> {

  private final LimitService limitService;

  @Override
  public FsmState process(LimitCommand cliCommand) {
    final Limit limit = limitService.addLimit(cliCommand);
    System.out.printf("Лимит %s успешно создан для категории %s%n", limit.name(), limit.category());
    return WorkingState.getInstance();
  }
}
