package anton.asmirko.app.cli;

import anton.asmirko.app.model.commands.*;
import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class CLIReader {

  public CLICommand readArgs(final String[] args) {
    if (args.length == 0) {
      return new HelpCommand();
    }
    final String command = args[0];
    CLICommand resCommand;
    resCommand =
        switch (command) {
          case SaveCommand.STR_REP -> readSaveCommand(args);
          case StatsCommand.STR_REP -> readStatsCommand(args);
          case ListLimitsCommand.STR_REP -> readListLimitsCommand(args);
          case LimitCommand.STR_REP -> readCreateLimitCommand(args);
          case ListTransactionsCommand.STR_REP -> readListTransactionsCommand(args);
          case TransactionCommand.STR_REP -> readTransactionCommand(args);
          case SignupCommand.STR_REP -> readSignupCommand(args);
          case LoginCommand.STR_REP -> readLoginCommand(args);
          case QuitCommand.STR_REP -> new QuitCommand();
          case HelpCommand.STR_REP -> new HelpCommand();
          case LogoutCommand.STR_REP -> new LogoutCommand();
          default -> throw new IllegalArgumentException(
              String.format("Неизвестная команда %s, попробуйте ввести команду help", command));
        };

    return resCommand;
  }

  private SaveCommand readSaveCommand(final String[] args) {
    if (args.length < 2) {
      throw new IllegalArgumentException("Необходимо указать путь к файлу");
    }
    final String path = args[1];
    return new SaveCommand(path);
  }

  private StatsCommand readStatsCommand(final String[] args) {
    final var keyRes = readCommandKeys(args, ListTransactionsCommand.Key.class);
    return new StatsCommand(keyRes.keys);
  }

  private ListLimitsCommand readListLimitsCommand(final String[] args) {
    final var keyRes = readCommandKeys(args, ListTransactionsCommand.Key.class);
    return new ListLimitsCommand(keyRes.keys);
  }

  private LimitCommand readCreateLimitCommand(final String[] args) {
    final var keyRes = readCommandKeys(args, LimitCommand.Key.class);
    final int pos = keyRes.pos;
    try {
      final String limitRaw = args[pos];
      final BigDecimal limit = new BigDecimal(limitRaw);
      return new LimitCommand(limit, keyRes.keys);
    } catch (Exception e) {
      throw new IllegalArgumentException("Некорректно введен лимит");
    }
  }

  private ListTransactionsCommand readListTransactionsCommand(final String[] args) {
    final var keyRes = readCommandKeys(args, ListTransactionsCommand.Key.class);
    return new ListTransactionsCommand(keyRes.keys);
  }

  private TransactionCommand readTransactionCommand(final String[] args) {
    final var keyRes = readCommandKeys(args, TransactionCommand.Key.class);
    int pos = keyRes.pos;
    final Map<TransactionCommand.Key, String> keys = keyRes.keys;
    final String amountRaw = args[pos++];
    final String source = args[pos];
    try {
      return new TransactionCommand(new BigDecimal(amountRaw), source, keys);
    } catch (Exception e) {
      throw new IllegalArgumentException("Некорректно введена сумма");
    }
  }

  private SignupCommand readSignupCommand(final String[] args) {
    try {
      final var keyRes = readCommandKeys(args, SignupCommand.Key.class);
      int pos = keyRes.pos;
      final Map<SignupCommand.Key, String> keys = keyRes.keys;
      final String login = args[pos++];
      final String password = args[pos];
      return new SignupCommand(login, password, keys);
    } catch (Exception e) {
      throw new IllegalArgumentException(
          String.format(
              "Проблема при вводе команды signup%nСинтаксис команды signup [-n <имя>]? <логин> <пароль>"));
    }
  }

  private LoginCommand readLoginCommand(final String[] args) {
    if (args.length != 3) {
      throw new IllegalArgumentException(
          String.format(
              "Для команды login необходимо указать логин и пароль%nПример: login <логин> <пароль>"));
    }
    final String login = args[1];
    final String password = args[2];
    return new LoginCommand(login, password);
  }

  private <E extends Enum<E> & KeyValueValidator> ReadCommandKeysResult<E> readCommandKeys(
      final String[] args, final Class<E> keyType) {
    final String missingKeyValueMsg = "Отсутствует значение для ключа %s для команды %s";
    final Map<String, List<E>> keysSet =
        EnumSet.allOf(keyType).stream()
            .map(it -> new Case<>(it.toString(), it))
            .collect(
                Collectors.groupingBy(
                    it -> it.key, Collectors.mapping(it -> it.value, Collectors.toList())));
    final EnumMap<E, String> resultKeys = new EnumMap<>(keyType);
    int i = 1;
    while (i < args.length) {
      final String curKey = args[i];
      if (!keysSet.containsKey(curKey)) {
        break;
      }
      final E key = keysSet.get(curKey).get(0);
      final int argCount = key.getArgCount();
      if (argCount == 0) {
        resultKeys.put(key, null);
        i++;
      } else {
        if (i < args.length - 1) {
          final String curValue = args[i + 1];
          final boolean isValidValue = key.isValid(curValue);
          if (isValidValue) {
            resultKeys.put(key, curValue);
          } else {
            throw new IllegalArgumentException(
                String.format("Невалидное значения для параметра %s: %s", curKey, curValue));
          }
          i += 2;
        } else {
          throw new IllegalArgumentException(String.format(missingKeyValueMsg, curKey, args[0]));
        }
      }
    }
    return new ReadCommandKeysResult<>(resultKeys, i);
  }

  private record Case<T>(String key, T value) {}

  private record ReadCommandKeysResult<E>(Map<E, String> keys, int pos) {}
}
