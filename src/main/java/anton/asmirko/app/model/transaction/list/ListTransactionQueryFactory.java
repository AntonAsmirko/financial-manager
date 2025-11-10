package anton.asmirko.app.model.transaction.list;

import anton.asmirko.app.model.commands.ListTransactionsCommand;
import anton.asmirko.app.model.transaction.Transaction;
import anton.asmirko.app.model.transaction.TransactionFactory;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ListTransactionQueryFactory {

  public ListTransactionQuery fromCliCommand(
      ListTransactionsCommand command, List<Transaction> userTransactions) {
    ListTransactionQueryBuilder builder = ListTransactionQueryBuilder.from(userTransactions);
    final var keys = command.keys();
    final boolean incomingFlag = keys.containsKey(ListTransactionsCommand.Key.INCOMING);
    final boolean outcomingFlag = keys.containsKey(ListTransactionsCommand.Key.OUTCOMING);
    final String fromTimeRaw = keys.get(ListTransactionsCommand.Key.FROM_TIME);
    final String toTimeRaw = keys.get(ListTransactionsCommand.Key.TO_TIME);
    final String categoriesRaw = keys.get(ListTransactionsCommand.Key.CATEGORY);
    if (incomingFlag) {
      builder.incoming();
    }
    if (outcomingFlag) {
      builder.outcoming();
    }
    if (fromTimeRaw != null) {
      LocalDateTime from = LocalDateTime.parse(fromTimeRaw, TransactionFactory.TIME_FORMAT);
      builder.fromTime(from);
    }
    if (toTimeRaw != null) {
      LocalDateTime to = LocalDateTime.parse(toTimeRaw, TransactionFactory.TIME_FORMAT);
      builder.toTime(to);
    }
    if (categoriesRaw != null) {
      builder.categories(Arrays.stream(categoriesRaw.split(",")).toList());
    }
    return builder.build();
  }
}
