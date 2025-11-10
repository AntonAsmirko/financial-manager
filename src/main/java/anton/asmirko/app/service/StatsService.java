package anton.asmirko.app.service;

import static java.math.RoundingMode.HALF_UP;

import anton.asmirko.app.model.transaction.IncomingTransaction;
import anton.asmirko.app.model.transaction.OutcomingTransaction;
import anton.asmirko.app.model.transaction.Transaction;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class StatsService {

  public Optional<Transaction> getMaxIncoming(List<Transaction> transactions) {
    return transactions.stream()
        .filter(it -> it instanceof IncomingTransaction)
        .max(Comparator.comparing(Transaction::getAmount));
  }

  public Optional<Transaction> getMaxOutcoming(List<Transaction> transactions) {
    return transactions.stream()
        .filter(it -> it instanceof OutcomingTransaction)
        .max(Comparator.comparing(Transaction::getAmount).reversed());
  }

  public BigDecimal getSumIncoming(List<Transaction> transactions) {
    return transactions.stream()
        .filter(it -> it instanceof IncomingTransaction)
        .map(Transaction::getAmount)
        .reduce(BigDecimal::add)
        .orElse(BigDecimal.ZERO);
  }

  public BigDecimal getSumOutcoming(List<Transaction> transactions) {
    return transactions.stream()
        .filter(it -> it instanceof OutcomingTransaction)
        .map(Transaction::getAmount)
        .reduce(BigDecimal::add)
        .orElse(BigDecimal.ZERO);
  }

  public int getIncomingCount(List<Transaction> transactions) {
    return transactions.stream().filter(it -> it instanceof IncomingTransaction).toList().size();
  }

  public int getOutcomingCount(List<Transaction> transactions) {
    return transactions.stream().filter(it -> it instanceof OutcomingTransaction).toList().size();
  }

  public BigDecimal getTotalSum(List<Transaction> transactions) {
    return transactions.stream()
        .map(Transaction::getAmount)
        .reduce(BigDecimal::add)
        .orElse(BigDecimal.ZERO);
  }

  public int getTotalCount(List<Transaction> transactions) {
    return getIncomingCount(transactions) + getIncomingCount(transactions);
  }

  public BigDecimal getAvgIncoming(List<Transaction> transactions) {
    var incomingCount = getIncomingCount(transactions);
    if (incomingCount == 0) {
      return BigDecimal.ZERO;
    }
    var incomingSum = getSumIncoming(transactions);
    return incomingSum.divide(new BigDecimal(incomingCount), HALF_UP);
  }

  public BigDecimal getAvgOutcoming(List<Transaction> transactions) {
    var outcomingCount = getOutcomingCount(transactions);
    if (outcomingCount == 0) {
      return BigDecimal.ZERO;
    }
    var outcomingSum = getSumOutcoming(transactions);
    return outcomingSum.divide(new BigDecimal(outcomingCount), HALF_UP);
  }
}
