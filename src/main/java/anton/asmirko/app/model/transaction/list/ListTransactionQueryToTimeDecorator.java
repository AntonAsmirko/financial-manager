package anton.asmirko.app.model.transaction.list;

import anton.asmirko.app.model.transaction.Transaction;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ListTransactionQueryToTimeDecorator extends ListTransactionQuery {

  private final ListTransactionQuery decoratedQuery;
  private final LocalDateTime toTime;

  @Override
  public List<Transaction> execute() {
    return decoratedQuery.execute().stream().filter(it -> it.getTime().isBefore(toTime)).toList();
  }
}
