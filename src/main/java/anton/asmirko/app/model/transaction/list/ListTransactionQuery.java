package anton.asmirko.app.model.transaction.list;

import anton.asmirko.app.model.transaction.Transaction;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class ListTransactionQuery {

  @Getter(AccessLevel.PROTECTED)
  @Setter(AccessLevel.PROTECTED)
  protected List<Transaction> transactions = List.of();

  public ListTransactionQuery() {}

  public ListTransactionQuery(List<Transaction> transactions) {
    this.transactions = transactions;
  }

  public List<Transaction> execute() {
    return transactions;
  }
}
