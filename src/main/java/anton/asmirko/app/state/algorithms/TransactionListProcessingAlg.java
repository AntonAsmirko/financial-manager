package anton.asmirko.app.state.algorithms;

import anton.asmirko.app.model.commands.ListTransactionsCommand;
import anton.asmirko.app.model.fsm.FsmState;
import anton.asmirko.app.model.fsm.WorkingState;
import anton.asmirko.app.model.transaction.TransactionFactory;
import anton.asmirko.app.service.TransactionService;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component(ListTransactionsCommand.STR_REP)
@RequiredArgsConstructor
public class TransactionListProcessingAlg
    implements CLICommandProcessingAlg<ListTransactionsCommand> {

  private final TransactionService transactionService;

  @Override
  public FsmState process(ListTransactionsCommand cliCommand) {
    AsciiTable at = new AsciiTable();
    at.addRule();
    at.addRow("Категория", "Величина", "Откуда/Куда", "Время");
    at.addRule();
    transactionService
        .getList(cliCommand)
        .forEach(
            it ->
                at.addRow(
                    it.getCategory(),
                    it.getAmount(),
                    it.getSource(),
                    it.getTime().format(TransactionFactory.TIME_FORMAT)));
    at.addRule();
    at.getRenderer().setCWC(new CWC_LongestLine());
    System.out.println(at.render());
    return WorkingState.getInstance();
  }
}
