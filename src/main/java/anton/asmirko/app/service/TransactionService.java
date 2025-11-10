package anton.asmirko.app.service;

import anton.asmirko.app.model.commands.ListTransactionsCommand;
import anton.asmirko.app.model.commands.TransactionCommand;
import anton.asmirko.app.model.limit.Limit;
import anton.asmirko.app.model.transaction.Transaction;
import anton.asmirko.app.model.transaction.TransactionFactory;
import anton.asmirko.app.model.transaction.list.ListTransactionQuery;
import anton.asmirko.app.model.transaction.list.ListTransactionQueryFactory;
import anton.asmirko.app.model.user.User;
import anton.asmirko.app.model.wallet.Wallet;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

  private final UserService userService;
  private final ListTransactionQueryFactory listTransactionQueryFactory;
  private final TransactionFactory transactionFactory;
  private final LimitService limitService;
  private final Scanner scanner = new Scanner(System.in);

  public List<Transaction> getList(ListTransactionsCommand cliCommand) {
    final User curUser = userService.getCurrentUser();
    final Wallet usersWallet = curUser.getWallet();
    final List<Transaction> transactions = usersWallet.getTransactions();
    final ListTransactionQuery query =
        listTransactionQueryFactory.fromCliCommand(cliCommand, transactions);
    return query.execute();
  }

  public Transaction addTransaction(TransactionCommand cliCommand) {
    final User user = userService.getCurrentUser();
    final Wallet wallet = user.getWallet();
    final Transaction transaction = transactionFactory.fromCliCommand(cliCommand);
    final List<Transaction> transactions = wallet.getTransactions();
    final List<Transaction> newTransactions = new ArrayList<>(transactions);
    newTransactions.add(transaction);
    final Map<Limit, BigDecimal> failedBefore = limitService.getFailedLimits();
    final Map<Limit, BigDecimal> failedAfter = limitService.getFailedLimits(newTransactions);
    final Map<Limit, BigDecimal> overflownLimits =
        failedAfter.entrySet().stream()
            .filter(e -> !failedBefore.containsKey(e.getKey()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    if (!overflownLimits.isEmpty()) {
      String command;
      do {
        System.err.printf(
            "После выполнения транзакции следующие лимиты будут переполнены %s%nДа - выполнить транзакцию%nНет - отменить%n",
            overflownLimits.keySet().stream().map(Limit::name).collect(Collectors.joining(", ")));
        command = scanner.nextLine();
      } while (!"да".equalsIgnoreCase(command) && !"нет".equalsIgnoreCase(command));
      if ("да".equalsIgnoreCase(command)) {
        wallet.addTransaction(transaction);
        return transaction;
      } else if ("нет".equalsIgnoreCase(command)) {
        return null;
      } else {
        throw new IllegalArgumentException(String.format("Неверный формат ввода %s%n", command));
      }
    } else {
      wallet.addTransaction(transaction);
      return transaction;
    }
  }
}
