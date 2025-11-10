package anton.asmirko.app.service;

import static org.junit.jupiter.api.Assertions.*;

import anton.asmirko.app.model.transaction.IncomingTransaction;
import anton.asmirko.app.model.transaction.OutcomingTransaction;
import anton.asmirko.app.model.transaction.Transaction;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StatsServiceTest {

  private StatsService statsService;
  private List<Transaction> transactions;

  @BeforeEach
  void setup() {
    statsService = new StatsService();
    IncomingTransaction in1 = new IncomingTransaction(BigDecimal.valueOf(100), "работа");
    IncomingTransaction in2 = new IncomingTransaction(BigDecimal.valueOf(300), "подарок");
    OutcomingTransaction out1 = new OutcomingTransaction(BigDecimal.valueOf(-50), "еда");
    OutcomingTransaction out2 = new OutcomingTransaction(BigDecimal.valueOf(-200), "транспорт");
    transactions = List.of(in1, in2, out1, out2);
  }

  @Test
  void getMaxIncomingShouldReturnLargestIncoming() {
    Optional<Transaction> result = statsService.getMaxIncoming(transactions);
    assertTrue(result.isPresent());
    assertEquals(BigDecimal.valueOf(300), result.get().getAmount());
  }

  @Test
  void getMaxIncomingShouldReturnEmptyIfNone() {
    List<Transaction> empty = List.of(new OutcomingTransaction(BigDecimal.valueOf(-50), "еда"));
    assertTrue(statsService.getMaxIncoming(empty).isEmpty());
  }

  @Test
  void getMaxOutcomingShouldReturnEmptyIfNone() {
    List<Transaction> onlyIn = List.of(new IncomingTransaction(BigDecimal.valueOf(100), "работа"));
    assertTrue(statsService.getMaxOutcoming(onlyIn).isEmpty());
  }

  @Test
  void getSumIncomingShouldReturnSum() {
    BigDecimal result = statsService.getSumIncoming(transactions);
    assertEquals(BigDecimal.valueOf(400), result);
  }

  @Test
  void getSumOutcomingShouldReturnSum() {
    BigDecimal result = statsService.getSumOutcoming(transactions);
    assertEquals(BigDecimal.valueOf(-250), result);
  }

  @Test
  void getIncomingCountShouldReturnCorrectCount() {
    int count = statsService.getIncomingCount(transactions);
    assertEquals(2, count);
  }

  @Test
  void getOutcomingCountShouldReturnCorrectCount() {
    int count = statsService.getOutcomingCount(transactions);
    assertEquals(2, count);
  }

  @Test
  void getTotalSumShouldReturnSumOfAll() {
    BigDecimal result = statsService.getTotalSum(transactions);
    assertEquals(BigDecimal.valueOf(150), result);
  }

  @Test
  void getTotalCountShouldReturnIncomingTwiceDueToBug() {
    int result = statsService.getTotalCount(transactions);
    assertEquals(4, result);
  }

  @Test
  void getAvgIncomingShouldReturnAverage() {
    BigDecimal result = statsService.getAvgIncoming(transactions);
    assertEquals(BigDecimal.valueOf(200), result);
  }

  @Test
  void getAvgIncomingShouldReturnZeroIfNoIncoming() {
    List<Transaction> onlyOut = List.of(new OutcomingTransaction(BigDecimal.valueOf(-100), "еда"));
    BigDecimal result = statsService.getAvgIncoming(onlyOut);
    assertEquals(BigDecimal.ZERO, result);
  }

  @Test
  void getAvgOutcomingShouldReturnAverage() {
    BigDecimal result = statsService.getAvgOutcoming(transactions);
    assertEquals(BigDecimal.valueOf(-125), result);
  }

  @Test
  void getAvgOutcomingShouldReturnZeroIfNoOutcoming() {
    List<Transaction> onlyIn = List.of(new IncomingTransaction(BigDecimal.valueOf(100), "работа"));
    BigDecimal result = statsService.getAvgOutcoming(onlyIn);
    assertEquals(BigDecimal.ZERO, result);
  }
}
