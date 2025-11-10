package anton.asmirko.app.model.wallet;

import anton.asmirko.app.model.limit.Limit;
import anton.asmirko.app.model.transaction.Transaction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class Wallet {

  private final List<Transaction> transactions = new ArrayList<>();
  private final HashSet<Limit> limits = new HashSet<>();

  @JsonIgnore
  public BigDecimal getBalance() {
    return transactions.stream()
        .map(Transaction::getAmount)
        .reduce(BigDecimal::add)
        .orElse(BigDecimal.ZERO);
  }

  public void addTransaction(final Transaction transaction) {
    transactions.add(transaction);
  }

  public void addLimit(final Limit limit) {
    limits.remove(limit);
    limits.add(limit);
  }

  public List<Transaction> getTransactions() {
    return Collections.unmodifiableList(transactions);
  }

  public List<Limit> getLimits() {
    return limits.stream().toList();
  }
}
