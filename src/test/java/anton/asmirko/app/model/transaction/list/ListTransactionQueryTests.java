package anton.asmirko.app.model.transaction.list;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import anton.asmirko.app.model.commands.ListTransactionsCommand;
import anton.asmirko.app.model.transaction.IncomingTransaction;
import anton.asmirko.app.model.transaction.OutcomingTransaction;
import anton.asmirko.app.model.transaction.Transaction;
import anton.asmirko.app.model.transaction.TransactionFactory;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ListTransactionQueryTests {

  private Transaction t1;
  private Transaction t2;
  private Transaction t3;
  private Transaction t4;
  private List<Transaction> transactions;
  private LocalDateTime baseTime;

  @BeforeEach
  void setup() {
    baseTime = LocalDateTime.of(2025, 1, 1, 10, 0);

    t1 = spy(new IncomingTransaction(BigDecimal.valueOf(100), "еда"));
    t2 = spy(new OutcomingTransaction(BigDecimal.valueOf(-50), "еда"));
    t3 = spy(new IncomingTransaction(BigDecimal.valueOf(200), "работа"));
    t4 = spy(new OutcomingTransaction(BigDecimal.valueOf(-150), "подарки"));

    when(t1.getTime()).thenReturn(baseTime.minusDays(2));
    when(t2.getTime()).thenReturn(baseTime.minusHours(1));
    when(t3.getTime()).thenReturn(baseTime.plusHours(1));
    when(t4.getTime()).thenReturn(baseTime.plusDays(1));

    when(t1.getCategory()).thenReturn("еда");
    when(t2.getCategory()).thenReturn("еда");
    when(t3.getCategory()).thenReturn("работа");
    when(t4.getCategory()).thenReturn("подарки");

    transactions = List.of(t1, t2, t3, t4);
  }

  @Test
  void listTransactionQueryShouldReturnAll() {
    var q = new ListTransactionQuery(transactions);
    assertEquals(4, q.execute().size());
  }

  @Test
  void categoryDecoratorShouldFilterByCategory() {
    var q =
        new ListTransactionQueryCategoryDecorator(
            new ListTransactionQuery(transactions), List.of("еда", "работа"));
    var result = q.execute();
    assertTrue(result.contains(t1));
    assertTrue(result.contains(t2));
    assertTrue(result.contains(t3));
    assertFalse(result.contains(t4));
  }

  @Test
  void fromTimeDecoratorShouldFilterCorrectly() {
    var q =
        new ListTransactionQueryFromTimeDecorator(new ListTransactionQuery(transactions), baseTime);
    var result = q.execute();
    assertTrue(result.contains(t3));
    assertTrue(result.contains(t4));
    assertFalse(result.contains(t1));
    assertFalse(result.contains(t2));
  }

  @Test
  void toTimeDecoratorShouldFilterCorrectly() {
    var q =
        new ListTransactionQueryToTimeDecorator(new ListTransactionQuery(transactions), baseTime);
    var result = q.execute();
    assertTrue(result.contains(t1));
    assertTrue(result.contains(t2));
    assertFalse(result.contains(t3));
    assertFalse(result.contains(t4));
  }

  @Test
  void incomingDecoratorShouldFilterIncoming() {
    var q = new ListTransactionQueryIncomingDecorator(new ListTransactionQuery(transactions));
    var result = q.execute();
    assertTrue(result.contains(t1));
    assertTrue(result.contains(t3));
    assertFalse(result.contains(t2));
    assertFalse(result.contains(t4));
  }

  @Test
  void outcomingDecoratorShouldFilterOutcoming() {
    var q = new ListTransactionQueryOutcomingDecorator(new ListTransactionQuery(transactions));
    var result = q.execute();
    assertTrue(result.contains(t2));
    assertTrue(result.contains(t4));
    assertFalse(result.contains(t1));
    assertFalse(result.contains(t3));
  }

  @Test
  void builderShouldApplyAllDecorators() {
    var builder =
        ListTransactionQueryBuilder.from(transactions)
            .incoming()
            .fromTime(baseTime.minusDays(1))
            .toTime(baseTime.plusHours(2))
            .categories(List.of("еда", "работа"));
    var q = builder.build();
    var result = q.execute();
    assertTrue(result.contains(t3));
    assertFalse(result.contains(t1));
    assertFalse(result.contains(t4));
  }

  @Test
  void factoryShouldBuildQueryFromKeys() {
    var command = mock(ListTransactionsCommand.class);
    when(command.keys())
        .thenReturn(
            Map.of(
                ListTransactionsCommand.Key.INCOMING,
                "true",
                ListTransactionsCommand.Key.CATEGORY,
                "еда,работа",
                ListTransactionsCommand.Key.FROM_TIME,
                baseTime.minusDays(3).format(TransactionFactory.TIME_FORMAT),
                ListTransactionsCommand.Key.TO_TIME,
                baseTime.plusDays(1).format(TransactionFactory.TIME_FORMAT)));
    var factory = new ListTransactionQueryFactory();
    var q = factory.fromCliCommand(command, transactions);
    var result = q.execute();
    assertTrue(result.contains(t1));
    assertTrue(result.contains(t3));
    assertFalse(result.contains(t4));
  }
}
