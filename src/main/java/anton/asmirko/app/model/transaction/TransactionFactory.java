package anton.asmirko.app.model.transaction;

import anton.asmirko.app.model.commands.TransactionCommand;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

@Component
public class TransactionFactory {

  public static final DateTimeFormatter TIME_FORMAT =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

  public Transaction fromCliCommand(final TransactionCommand transactionCommand) {
    final BigDecimal amount = transactionCommand.amount();
    final var keys = transactionCommand.keys();
    final String category = keys.get(TransactionCommand.Key.CATEGORY);
    final String time = keys.get(TransactionCommand.Key.TIME);
    Transaction transaction;
    if (transactionCommand.amount().compareTo(BigDecimal.ZERO) > 0) {
      transaction = new IncomingTransaction(amount, transactionCommand.source());
    } else if (transactionCommand.amount().compareTo(BigDecimal.ZERO) < 0) {
      transaction = new OutcomingTransaction(amount, transactionCommand.source());
    } else {
      throw new IllegalArgumentException("Сумма транзакции не может быть рана нулю");
    }
    if (category != null) {
      transaction.setCategory(category);
    } else {
      transaction.setCategory("-");
    }
    if (time != null) {
      LocalDateTime dt = LocalDateTime.parse(time, TIME_FORMAT);
      transaction.setTime(dt);
    } else {
      transaction.setTime(LocalDateTime.now());
    }
    return transaction;
  }
}
