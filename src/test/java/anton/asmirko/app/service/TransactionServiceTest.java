package anton.asmirko.app.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import anton.asmirko.app.model.commands.ListTransactionsCommand;
import anton.asmirko.app.model.commands.TransactionCommand;
import anton.asmirko.app.model.limit.Limit;
import anton.asmirko.app.model.transaction.IncomingTransaction;
import anton.asmirko.app.model.transaction.OutcomingTransaction;
import anton.asmirko.app.model.transaction.Transaction;
import anton.asmirko.app.model.transaction.TransactionFactory;
import anton.asmirko.app.model.transaction.list.ListTransactionQuery;
import anton.asmirko.app.model.transaction.list.ListTransactionQueryFactory;
import anton.asmirko.app.model.user.User;
import anton.asmirko.app.model.wallet.Wallet;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransactionServiceTest {

  private UserService userService;
  private ListTransactionQueryFactory queryFactory;
  private TransactionFactory transactionFactory;
  private LimitService limitService;
  private TransactionService transactionService;
  private User user;
  private Wallet wallet;

  @BeforeEach
  void setup() {
    userService = mock(UserService.class);
    queryFactory = mock(ListTransactionQueryFactory.class);
    transactionFactory = mock(TransactionFactory.class);
    limitService = mock(LimitService.class);

    transactionService =
        new TransactionService(userService, queryFactory, transactionFactory, limitService);

    user = mock(User.class);
    wallet = new Wallet();
    when(userService.getCurrentUser()).thenReturn(user);
    when(user.getWallet()).thenReturn(wallet);
  }

  private OutcomingTransaction makeOut(BigDecimal amount, String category) {
    OutcomingTransaction t = new OutcomingTransaction(amount, "источник");
    t.setCategory(category);
    return t;
  }

  private IncomingTransaction makeIn(BigDecimal amount, String category) {
    IncomingTransaction t = new IncomingTransaction(amount, "источник");
    t.setCategory(category);
    return t;
  }

  @Test
  void getListShouldUseQueryFactory() {
    ListTransactionsCommand cmd = mock(ListTransactionsCommand.class);
    ListTransactionQuery query = mock(ListTransactionQuery.class);
    List<OutcomingTransaction> expected = List.of(makeOut(BigDecimal.valueOf(-10), "еда"));

    when(queryFactory.fromCliCommand(cmd, wallet.getTransactions())).thenReturn(query);
    when(query.execute()).thenReturn(List.copyOf(expected));

    List<?> result = transactionService.getList(cmd);

    assertEquals(expected.size(), result.size());
    verify(queryFactory).fromCliCommand(cmd, wallet.getTransactions());
    verify(query).execute();
  }

  @Test
  void addTransactionShouldAddWhenNoOverflownLimits() {
    TransactionCommand cmd = mock(TransactionCommand.class);
    OutcomingTransaction transaction = makeOut(BigDecimal.valueOf(-50), "еда");

    when(transactionFactory.fromCliCommand(cmd)).thenReturn(transaction);
    when(limitService.getFailedLimits()).thenReturn(Map.of());
    when(limitService.getFailedLimits(anyList())).thenReturn(Map.of());

    Transaction result = transactionService.addTransaction(cmd);

    assertEquals(transaction, result);
    assertTrue(wallet.getTransactions().contains(transaction));
  }

  @Test
  void addTransactionShouldCancelWhenUserInputsNo() {
    System.setIn(new ByteArrayInputStream("нет\n".getBytes()));

    TransactionCommand cmd = mock(TransactionCommand.class);
    OutcomingTransaction transaction = makeOut(BigDecimal.valueOf(-50), "еда");
    Limit limit = mock(Limit.class);
    when(limit.name()).thenReturn("еда");

    when(transactionFactory.fromCliCommand(cmd)).thenReturn(transaction);
    when(limitService.getFailedLimits()).thenReturn(Map.of());
    when(limitService.getFailedLimits(anyList()))
        .thenReturn(Map.of(limit, BigDecimal.valueOf(-10)));

    transactionService =
        new TransactionService(userService, queryFactory, transactionFactory, limitService);

    Transaction result = transactionService.addTransaction(cmd);

    assertNull(result);
    assertTrue(wallet.getTransactions().isEmpty());
  }

  @Test
  void addTransactionShouldAddWhenUserInputsYes() {
    System.setIn(new ByteArrayInputStream("да\n".getBytes()));

    TransactionCommand cmd = mock(TransactionCommand.class);
    OutcomingTransaction transaction = makeOut(BigDecimal.valueOf(-50), "еда");
    Limit limit = mock(Limit.class);
    when(limit.name()).thenReturn("еда");

    when(transactionFactory.fromCliCommand(cmd)).thenReturn(transaction);
    when(limitService.getFailedLimits()).thenReturn(Map.of());
    when(limitService.getFailedLimits(anyList()))
        .thenReturn(Map.of(limit, BigDecimal.valueOf(-10)));

    transactionService =
        new TransactionService(userService, queryFactory, transactionFactory, limitService);

    Transaction result = transactionService.addTransaction(cmd);

    assertEquals(transaction, result);
    assertTrue(wallet.getTransactions().contains(transaction));
  }
}
