package anton.asmirko.app.cli;

import static org.junit.jupiter.api.Assertions.*;

import anton.asmirko.app.model.commands.*;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CLIReaderTest {

  private CLIReader reader;

  @BeforeEach
  void setup() {
    reader = new CLIReader();
  }

  @Test
  void shouldReturnHelpCommandWhenNoArgs() {
    CLICommand cmd = reader.readArgs(new String[] {});
    assertTrue(cmd instanceof HelpCommand);
  }

  @Test
  void shouldParseSignupCommandWithoutName() {
    CLICommand cmd = reader.readArgs(new String[] {"signup", "anton", "123"});
    assertInstanceOf(SignupCommand.class, cmd);
    SignupCommand signup = (SignupCommand) cmd;
    assertEquals("anton", signup.login());
    assertEquals("123", signup.password());
    assertTrue(signup.keys().isEmpty());
  }

  @Test
  void shouldParseSignupCommandWithName() {
    CLICommand cmd = reader.readArgs(new String[] {"signup", "-n", "Anton", "anton", "123"});
    SignupCommand signup = (SignupCommand) cmd;
    assertEquals("Anton", signup.keys().get(SignupCommand.Key.NAME));
  }

  @Test
  void shouldThrowOnInvalidSignupSyntax() {
    Exception e =
        assertThrows(
            IllegalArgumentException.class, () -> reader.readArgs(new String[] {"signup", "-n"}));
    assertTrue(e.getMessage().contains("Синтаксис"));
  }

  @Test
  void shouldParseLoginCommand() {
    CLICommand cmd = reader.readArgs(new String[] {"login", "anton", "123"});
    assertInstanceOf(LoginCommand.class, cmd);
  }

  @Test
  void shouldThrowOnInvalidLoginCommand() {
    assertThrows(
        IllegalArgumentException.class, () -> reader.readArgs(new String[] {"login", "anton"}));
  }

  @Test
  void shouldParseTransactionCommand() {
    CLICommand cmd =
        reader.readArgs(
            new String[] {"transaction", "-c", "еда", "-t", "2025-11-09T22:04", "1000", "магазин"});
    assertInstanceOf(TransactionCommand.class, cmd);
    TransactionCommand tr = (TransactionCommand) cmd;
    assertEquals(new BigDecimal("1000"), tr.amount());
    assertEquals("магазин", tr.source());
  }

  @Test
  void shouldThrowOnInvalidTransactionAmount() {
    assertThrows(
        IllegalArgumentException.class,
        () -> reader.readArgs(new String[] {"transaction", "-c", "еда", "abc", "источник"}));
  }

  @Test
  void shouldParseLimitCommand() {
    CLICommand cmd =
        reader.readArgs(new String[] {"limit", "-n", "мой-первый-лимит", "-c", "еда", "40000"});
    assertInstanceOf(LimitCommand.class, cmd);
    LimitCommand limit = (LimitCommand) cmd;
    assertEquals(new BigDecimal("40000"), limit.limit());
    assertEquals("еда", limit.keys().get(LimitCommand.Key.CATEGORY));
  }

  @Test
  void shouldThrowOnInvalidLimitValue() {
    assertThrows(
        IllegalArgumentException.class,
        () -> reader.readArgs(new String[] {"limit", "-c", "еда", "abc"}));
  }

  @Test
  void shouldParseListLimitsCommand() {
    CLICommand cmd = reader.readArgs(new String[] {"limit-list"});
    assertInstanceOf(ListLimitsCommand.class, cmd);
  }

  @Test
  void shouldParseStatsCommand() {
    CLICommand cmd = reader.readArgs(new String[] {"stats"});
    assertInstanceOf(StatsCommand.class, cmd);
  }

  @Test
  void shouldParseHelpAndQuitAndLogout() {
    assertInstanceOf(HelpCommand.class, reader.readArgs(new String[] {"help"}));
    assertInstanceOf(QuitCommand.class, reader.readArgs(new String[] {"\\q"}));
    assertInstanceOf(LogoutCommand.class, reader.readArgs(new String[] {"logout"}));
  }

  @Test
  void shouldThrowOnUnknownCommand() {
    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class, () -> reader.readArgs(new String[] {"abracadabra"}));
    assertTrue(ex.getMessage().contains("Неизвестная команда"));
  }
}
