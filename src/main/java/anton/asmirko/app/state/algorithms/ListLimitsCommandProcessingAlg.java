package anton.asmirko.app.state.algorithms;

import anton.asmirko.app.model.commands.ListLimitsCommand;
import anton.asmirko.app.model.fsm.FsmState;
import anton.asmirko.app.model.fsm.WorkingState;
import anton.asmirko.app.model.limit.Limit;
import anton.asmirko.app.model.transaction.TransactionFactory;
import anton.asmirko.app.service.LimitService;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component(ListLimitsCommand.STR_REP)
@RequiredArgsConstructor
public class ListLimitsCommandProcessingAlg implements CLICommandProcessingAlg<ListLimitsCommand> {

  private final LimitService limitService;

  @Override
  public FsmState process(ListLimitsCommand cliCommand) {
    final List<Limit> limits = limitService.getLimits(cliCommand);
    AsciiTable at = new AsciiTable();
    at.addRule();
    at.addRow("Наименование", "Категория", "Величина", "Время с", "Время до");
    at.addRule();
    limits.forEach(
        it ->
            at.addRow(
                it.name(),
                it.category(),
                it.limit(),
                Optional.ofNullable(it.from())
                    .map(dateTime -> dateTime.format(TransactionFactory.TIME_FORMAT))
                    .orElseGet(() -> "-"),
                Optional.ofNullable(it.to())
                    .map(dateTime -> dateTime.format(TransactionFactory.TIME_FORMAT))
                    .orElseGet(() -> "-")));
    at.addRule();
    at.getRenderer().setCWC(new CWC_LongestLine());
    System.out.println(at.render());
    return WorkingState.getInstance();
  }
}
