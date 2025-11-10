package anton.asmirko.app.model.wallet;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

public final class OutcomingTransaction extends Transaction {

    @Getter
    @Setter
    public String category;

    public OutcomingTransaction(final BigDecimal amount, final String source) {
        super(amount, source);
        if (amount.compareTo(BigDecimal.ZERO) >= 0) {
            throw new IllegalArgumentException("Траты должны быть отрицательными");
        }
    }
}
