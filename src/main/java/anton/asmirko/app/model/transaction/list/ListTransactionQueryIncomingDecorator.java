package anton.asmirko.app.model.transaction.list;

import anton.asmirko.app.model.transaction.IncomingTransaction;
import anton.asmirko.app.model.transaction.Transaction;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ListTransactionQueryIncomingDecorator extends ListTransactionQuery {

  private final ListTransactionQuery decoratedQuery;

  @Override
  public List<Transaction> execute() {
    return decoratedQuery.execute().stream()
        .filter(it -> it instanceof IncomingTransaction)
        .toList();
  }
}
