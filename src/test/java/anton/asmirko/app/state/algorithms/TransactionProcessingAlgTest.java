package anton.asmirko.app.state.algorithms;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import anton.asmirko.app.model.commands.TransactionCommand;
import anton.asmirko.app.model.fsm.FsmState;
import anton.asmirko.app.model.fsm.WorkingState;
import anton.asmirko.app.model.limit.Limit;
import anton.asmirko.app.model.transaction.IncomingTransaction;
import anton.asmirko.app.model.transaction.OutcomingTransaction;
import anton.asmirko.app.model.user.User;
import anton.asmirko.app.model.wallet.Wallet;
import anton.asmirko.app.service.LimitService;
import anton.asmirko.app.service.TransactionService;
import anton.asmirko.app.service.UserService;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransactionProcessingAlgTest {

  private TransactionService transactionService;
  private LimitService limitService;
  private UserService userService;
  private TransactionProcessingAlg algorithm;
  private TransactionCommand command;
  private User user;
  private Wallet wallet;
  private ByteArrayOutputStream outContent;
  private ByteArrayOutputStream errContent;

  @BeforeEach
  void setup() {
    transactionService = mock(TransactionService.class);
    limitService = mock(LimitService.class);
    userService = mock(UserService.class);
    algorithm = new TransactionProcessingAlg(transactionService, limitService, userService);
    command = mock(TransactionCommand.class);

    wallet = new Wallet();
    user = mock(User.class);
    when(user.getWallet()).thenReturn(wallet);
    when(userService.getCurrentUser()).thenReturn(user);

    outContent = new ByteArrayOutputStream();
    errContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
  }

  @Test
  void processShouldAddTransactionSuccessfully() {
    IncomingTransaction transaction = new IncomingTransaction(BigDecimal.valueOf(100), "работа");
    wallet.addTransaction(transaction);

    when(transactionService.addTransaction(command)).thenReturn(transaction);
    when(limitService.getFailedLimits()).thenReturn(Map.of());

    FsmState result = algorithm.process(command);

    assertEquals(WorkingState.getInstance(), result);
    assertTrue(outContent.toString().contains("Транзакция успешно добавлена"));
  }

  @Test
  void processShouldPrintLimitExceeded() {
    IncomingTransaction transaction = new IncomingTransaction(BigDecimal.valueOf(100), "работа");
    wallet.addTransaction(transaction);

    Limit limit = mock(Limit.class);
    when(limit.name()).thenReturn("еда");
    when(limit.limit()).thenReturn(BigDecimal.valueOf(100));

    when(transactionService.addTransaction(command)).thenReturn(transaction);
    when(limitService.getFailedLimits())
        .thenReturn(Map.of()) // before
        .thenReturn(Map.of(limit, BigDecimal.valueOf(-50))); // after

    algorithm.process(command);

    assertTrue(errContent.toString().contains("Превышен лимит на категорию еда"));
  }

  @Test
  void processShouldWarnIfBalanceNegativeAfterOutcomingTransaction() {
    OutcomingTransaction transaction = new OutcomingTransaction(BigDecimal.valueOf(-200), "еда");
    wallet.addTransaction(transaction);
    wallet.addTransaction(transaction); // уменьшает баланс

    when(transactionService.addTransaction(command)).thenReturn(transaction);
    when(limitService.getFailedLimits()).thenReturn(Map.of());
    when(userService.getCurrentUser()).thenReturn(user);

    algorithm.process(command);

    assertTrue(errContent.toString().contains("Расходы превысили ваши доходы"));
  }

  @Test
  void processShouldPrintTransactionCancelled() {
    when(transactionService.addTransaction(command)).thenReturn(null);
    FsmState result = algorithm.process(command);

    assertEquals(WorkingState.getInstance(), result);
    assertTrue(outContent.toString().contains("Транзакция отменена"));
  }
}
