package anton.asmirko.app.model.wallet;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public sealed abstract class Transaction permits IncomingTransaction, OutcomingTransaction {

    @Getter
    protected BigDecimal amount;

    @Getter
    @Setter
    protected boolean isMuted;

    @Getter
    protected String source;

    @Setter
    @Getter
    protected LocalDateTime time;

    public Transaction(final BigDecimal amount, final String source) {
        this.amount = amount;
        this.source = source;
    }
}
