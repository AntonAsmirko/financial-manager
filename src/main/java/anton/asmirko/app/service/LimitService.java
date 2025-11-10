package anton.asmirko.app.service;

import anton.asmirko.app.model.commands.LimitCommand;
import anton.asmirko.app.model.commands.ListLimitsCommand;
import anton.asmirko.app.model.limit.Limit;
import anton.asmirko.app.model.limit.list.ListLimitQueryFactory;
import anton.asmirko.app.model.transaction.Transaction;
import anton.asmirko.app.model.transaction.TransactionFactory;
import anton.asmirko.app.model.user.User;
import anton.asmirko.app.model.wallet.Wallet;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LimitService {

  private final UserService userService;
  private final ListLimitQueryFactory listLimitQueryFactory;

  public BigDecimal getLimitDiff(Limit limit, List<Transaction> transactions) {
    final BigDecimal transactionSum =
        transactions.stream()
            .filter(it -> isInBounds(limit, it))
            .map(Transaction::getAmount)
            .reduce(BigDecimal::add)
            .orElse(BigDecimal.ZERO);
    return limit.limit().add(transactionSum);
  }

  public Map<Limit, BigDecimal> getFailedLimits() {
    final User curUser = userService.getCurrentUser();
    final Wallet userWallet = curUser.getWallet();
    return getFailedLimits(userWallet.getTransactions());
  }

  public Map<Limit, BigDecimal> getFailedLimitsWith(Transaction transaction) {
    final User curUser = userService.getCurrentUser();
    final Wallet userWallet = curUser.getWallet();
    return getFailedLimits(userWallet.getTransactions());
  }

  public Map<Limit, BigDecimal> getFailedLimits(List<Transaction> transactions) {
    final User curUser = userService.getCurrentUser();
    final Wallet userWallet = curUser.getWallet();
    return userWallet.getLimits().stream()
        .map(it -> new MPair(it, getLimitDiff(it, transactions)))
        .filter(it -> it.diff.compareTo(BigDecimal.ZERO) < 0)
        .collect(Collectors.toMap(MPair::limit, MPair::diff, (a, b) -> a, HashMap::new));
  }

  public Map<Limit, BigDecimal> getOkLimits(List<Transaction> transactions) {
    final User curUser = userService.getCurrentUser();
    final Wallet userWallet = curUser.getWallet();
    return userWallet.getLimits().stream()
        .map(it -> new MPair(it, getLimitDiff(it, transactions)))
        .filter(it -> it.diff.compareTo(BigDecimal.ZERO) >= 0)
        .collect(Collectors.toMap(MPair::limit, MPair::diff, (a, b) -> a, HashMap::new));
  }

  private boolean isInBounds(Limit limit, Transaction transaction) {
    final LocalDateTime transactionTime = transaction.getTime();
    if (limit.from() != null && transactionTime.isBefore(limit.from())) {
      return false;
    }
    if (limit.to() != null && transactionTime.isAfter(limit.to())) {
      return false;
    }
    return limit.category().equals(transaction.getCategory());
  }

  public List<Limit> getLimits(ListLimitsCommand listLimitsCommand) {
    final User curUser = userService.getCurrentUser();
    final Wallet userWallet = curUser.getWallet();
    final List<Limit> limits = userWallet.getLimits();
    return getLimits(listLimitsCommand, limits);
  }

  public List<Limit> getLimits(ListLimitsCommand listLimitsCommand, List<Limit> limits) {
    return listLimitQueryFactory.fromCliCommand(listLimitsCommand, limits).execute();
  }

  public Limit addLimit(LimitCommand limitCommand) {
    final User curUser = userService.getCurrentUser();
    final Wallet userWallet = curUser.getWallet();
    final BigDecimal limitAmount = limitCommand.limit();
    final var keys = limitCommand.keys();
    final String name = keys.get(LimitCommand.Key.NAME);
    if (name == null) {
      throw new IllegalArgumentException("Параметр name (-n) является обязательным");
    }
    final String category = keys.get(LimitCommand.Key.CATEGORY);
    if (category == null) {
      throw new IllegalArgumentException("Параметр category (-c) является обязательным");
    }
    final String fromRaw = keys.get(LimitCommand.Key.FROM);
    final String toRaw = keys.get(LimitCommand.Key.TO);
    final LocalDateTime from;
    final LocalDateTime to;
    if (fromRaw != null) {
      from = LocalDateTime.parse(fromRaw, TransactionFactory.TIME_FORMAT);
    } else {
      from = LocalDateTime.now();
    }
    if (toRaw != null) {
      to = LocalDateTime.parse(toRaw, TransactionFactory.TIME_FORMAT);
    } else {
      to = null;
    }
    final Limit limit = new Limit(name, category, limitAmount, from, to);
    userWallet.addLimit(limit);
    return limit;
  }

  private record MPair(Limit limit, BigDecimal diff) {}
}
