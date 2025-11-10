package anton.asmirko.app.model.limit.list;

import anton.asmirko.app.model.commands.ListLimitsCommand;
import anton.asmirko.app.model.commands.ListTransactionsCommand;
import anton.asmirko.app.model.limit.Limit;
import anton.asmirko.app.model.transaction.TransactionFactory;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ListLimitQueryFactory {

  public ListLimitQuery fromCliCommand(ListLimitsCommand command, List<Limit> userLimits) {
    ListLimitQueryBuilder builder = ListLimitQueryBuilder.from(userLimits);
    final var keys = command.keys();
    final String fromTimeRaw = keys.get(ListTransactionsCommand.Key.FROM_TIME);
    final String toTimeRaw = keys.get(ListTransactionsCommand.Key.TO_TIME);
    final String categoriesRaw = keys.get(ListTransactionsCommand.Key.CATEGORY);
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
