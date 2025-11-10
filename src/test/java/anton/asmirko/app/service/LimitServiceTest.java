package anton.asmirko.app.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import anton.asmirko.app.model.commands.LimitCommand;
import anton.asmirko.app.model.commands.ListLimitsCommand;
import anton.asmirko.app.model.limit.Limit;
import anton.asmirko.app.model.limit.list.ListLimitQuery;
import anton.asmirko.app.model.limit.list.ListLimitQueryFactory;
import anton.asmirko.app.model.transaction.OutcomingTransaction;
import anton.asmirko.app.model.transaction.Transaction;
import anton.asmirko.app.model.user.User;
import anton.asmirko.app.model.wallet.Wallet;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LimitServiceTest {

  private UserService userService;
  private ListLimitQueryFactory listLimitQueryFactory;
  private LimitService limitService;
  private User user;
  private Wallet wallet;

  @BeforeEach
  void setup() {
    userService = mock(UserService.class);
    listLimitQueryFactory = mock(ListLimitQueryFactory.class);
    limitService = new LimitService(userService, listLimitQueryFactory);
    wallet = new Wallet();
    user = mock(User.class);
    when(userService.getCurrentUser()).thenReturn(user);
    when(user.getWallet()).thenReturn(wallet);
  }

  private Transaction makeTransaction(String category, BigDecimal amount) {
    var t = new OutcomingTransaction(amount, "источник");
    t.setCategory(category);
    t.setTime(LocalDateTime.now());
    return t;
  }

  @Test
  void getLimitDiffShouldSubtractTransactions() {
    Limit limit =
        new Limit(
            "лимит",
            "еда",
            BigDecimal.valueOf(100),
            LocalDateTime.now().minusDays(1),
            LocalDateTime.now().plusDays(1));
    Transaction t = makeTransaction("еда", BigDecimal.valueOf(-30));
    BigDecimal result = limitService.getLimitDiff(limit, List.of(t));
    assertEquals(BigDecimal.valueOf(70), result);
  }

  @Test
  void getFailedLimitsShouldDetectOverflow() {
    Limit limit =
        new Limit(
            "лимит",
            "еда",
            BigDecimal.valueOf(10),
            LocalDateTime.now().minusDays(1),
            LocalDateTime.now().plusDays(1));
    Transaction t = makeTransaction("еда", BigDecimal.valueOf(-50));
    wallet.addLimit(limit);
    Map<Limit, BigDecimal> result = limitService.getFailedLimits(List.of(t));
    assertEquals(1, result.size());
    assertTrue(result.values().iterator().next().compareTo(BigDecimal.ZERO) < 0);
  }

  @Test
  void getOkLimitsShouldReturnPositiveDiff() {
    Limit limit =
        new Limit(
            "лимит",
            "еда",
            BigDecimal.valueOf(100),
            LocalDateTime.now().minusDays(1),
            LocalDateTime.now().plusDays(1));
    Transaction t = makeTransaction("еда", BigDecimal.valueOf(-50));
    wallet.addLimit(limit);
    Map<Limit, BigDecimal> result = limitService.getOkLimits(List.of(t));
    assertEquals(1, result.size());
    BigDecimal diff = result.values().iterator().next();
    assertTrue(diff.compareTo(BigDecimal.ZERO) >= 0);
  }

  @Test
  void addLimitShouldAddNewLimit() {
    LimitCommand cmd = mock(LimitCommand.class);
    when(cmd.limit()).thenReturn(BigDecimal.valueOf(1000));
    when(cmd.keys())
        .thenReturn(
            Map.of(
                LimitCommand.Key.NAME, "тест",
                LimitCommand.Key.CATEGORY, "еда"));
    Limit limit = limitService.addLimit(cmd);
    assertEquals("тест", limit.name());
    assertEquals("еда", limit.category());
    assertFalse(wallet.getLimits().isEmpty());
  }

  @Test
  void addLimitShouldThrowIfNoName() {
    LimitCommand cmd = mock(LimitCommand.class);
    when(cmd.limit()).thenReturn(BigDecimal.TEN);
    when(cmd.keys()).thenReturn(Map.of(LimitCommand.Key.CATEGORY, "еда"));
    assertThrows(IllegalArgumentException.class, () -> limitService.addLimit(cmd));
  }

  @Test
  void getLimitsShouldDelegateToFactory() {
    ListLimitsCommand cmd = mock(ListLimitsCommand.class);
    List<Limit> limits = List.of(new Limit("a", "b", BigDecimal.ONE, null, null));
    ListLimitQuery query = mock(ListLimitQuery.class);
    when(listLimitQueryFactory.fromCliCommand(cmd, limits)).thenReturn(query);
    when(query.execute()).thenReturn(limits);
    wallet.addLimit(limits.get(0));
    List<Limit> result = limitService.getLimits(cmd);
    assertEquals(1, result.size());
  }
}
