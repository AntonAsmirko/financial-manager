package anton.asmirko.app.state.algorithms;

import anton.asmirko.app.model.commands.TransactionCommand;
import anton.asmirko.app.model.fsm.FsmState;
import anton.asmirko.app.model.fsm.WorkingState;
import anton.asmirko.app.model.limit.Limit;
import anton.asmirko.app.model.transaction.OutcomingTransaction;
import anton.asmirko.app.model.transaction.Transaction;
import anton.asmirko.app.service.LimitService;
import anton.asmirko.app.service.TransactionService;
import anton.asmirko.app.service.UserService;
import java.math.BigDecimal;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component(TransactionCommand.STR_REP)
@RequiredArgsConstructor
public class TransactionProcessingAlg implements CLICommandProcessingAlg<TransactionCommand> {

  private final TransactionService transactionService;
  private final LimitService limitService;
  private final UserService userService;

  @Override
  public FsmState process(TransactionCommand cliCommand) {
    final Map<Limit, BigDecimal> failedLimitsBefore = limitService.getFailedLimits();
    final Transaction transaction = transactionService.addTransaction(cliCommand);
    if (transaction != null) {
      System.out.println("Транзакция успешно добавлена");
      final Map<Limit, BigDecimal> failedLimitsAfter = limitService.getFailedLimits();
      failedLimitsAfter.entrySet().stream()
          .filter(e -> !failedLimitsBefore.containsKey(e.getKey()))
          .forEach(
              e ->
                  System.err.printf(
                      "Превышен лимит на категорию %s (%s) на %s%n",
                      e.getKey().name(), e.getKey().limit(), e.getValue().abs()));
      final BigDecimal balance = userService.getCurrentUser().getWallet().getBalance();
      if (transaction instanceof OutcomingTransaction && balance.compareTo(BigDecimal.ZERO) < 0) {
        System.err.printf("Расходы превысили ваши доходы, ваш текущий баланс %s%n", balance);
      }
    } else {
      System.out.println("Транзакция отменена");
    }
    return WorkingState.getInstance();
  }
}
