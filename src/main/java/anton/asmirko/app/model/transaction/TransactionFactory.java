package anton.asmirko.app.model.wallet;

import anton.asmirko.app.model.commands.TransactionCommand;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TransactionFactory {

    final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

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
            if (category != null) {
                ((OutcomingTransaction) transaction).setCategory(category);
            }
        } else {
            throw new IllegalArgumentException("Сумма транзакции не может быть рана нулю");
        }
        if (time != null) {
            LocalDateTime dt = LocalDateTime.parse(time, fmt);
            transaction.setTime(dt);
        } else {
            transaction.setTime(LocalDateTime.now());
        }
        return transaction;
    }
}
