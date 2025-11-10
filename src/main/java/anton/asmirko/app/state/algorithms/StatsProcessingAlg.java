package anton.asmirko.app.state.algorithms;

import anton.asmirko.app.model.commands.ListTransactionsCommand;
import anton.asmirko.app.model.commands.StatsCommand;
import anton.asmirko.app.model.fsm.FsmState;
import anton.asmirko.app.model.fsm.WorkingState;
import anton.asmirko.app.model.limit.Limit;
import anton.asmirko.app.model.transaction.Transaction;
import anton.asmirko.app.service.LimitService;
import anton.asmirko.app.service.StatsService;
import anton.asmirko.app.service.TransactionService;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component(StatsCommand.STR_REP)
@RequiredArgsConstructor
public class StatsProcessingAlg implements CLICommandProcessingAlg<StatsCommand> {

  private final TransactionService transactionService;
  private final StatsService statsService;
  private final LimitService limitService;

  @Override
  public FsmState process(StatsCommand cliCommand) {
    final List<Transaction> transactions =
        transactionService.getList(new ListTransactionsCommand(cliCommand.keys()));
    final Optional<Transaction> maxIncoming = statsService.getMaxIncoming(transactions);
    final Optional<Transaction> maxOutcoming = statsService.getMaxOutcoming(transactions);
    final BigDecimal sumIncoming = statsService.getSumIncoming(transactions);
    final BigDecimal sumOutcoming = statsService.getSumOutcoming(transactions);
    final int incomingCount = statsService.getIncomingCount(transactions);
    final int outcomingCount = statsService.getOutcomingCount(transactions);
    final BigDecimal totalSum = statsService.getTotalSum(transactions);
    final int totalCount = statsService.getTotalCount(transactions);
    final BigDecimal avgIncoming = statsService.getAvgIncoming(transactions);
    final BigDecimal avgOutcoming = statsService.getAvgOutcoming(transactions);

    final Map<Limit, BigDecimal> failedLimits = limitService.getFailedLimits(transactions);

    AsciiTable at = new AsciiTable();
    at.addRule();
    at.addRow(
        "Максимальное поступление",
        maxIncoming.map(Transaction::getAmount).orElse(BigDecimal.ZERO));
    at.addRow(
        "Максимальное списание", maxOutcoming.map(Transaction::getAmount).orElse(BigDecimal.ZERO));
    at.addRow("Сумма поступлений", sumIncoming);
    at.addRow("Сумма списаний", sumOutcoming);
    at.addRow("Число операций по начислению", incomingCount);
    at.addRow("Число операций по списанию", outcomingCount);
    at.addRow("Общее число операций", totalCount);
    at.addRow("Сумма всех операций", totalSum);
    at.addRow("Величина среднего поступления", avgIncoming);
    at.addRow("Величина среднего списания", avgOutcoming);
    at.addRow("Переполнений лимитов", failedLimits.size());
    at.addRule();
    at.getRenderer().setCWC(new CWC_LongestLine());
    System.out.println(at.render());
    AsciiTable at1 = new AsciiTable();
    at1.addRule();
    at1.addRow("Выход за пределы лимита", "Категория", "Переполнение");
    failedLimits.forEach(
        (k, v) -> {
          at1.addRow(k.name(), k.category(), v);
        });
    at1.addRule();
    at1.getRenderer().setCWC(new CWC_LongestLine());
    System.out.println(at1.render());
    return WorkingState.getInstance();
  }
}
