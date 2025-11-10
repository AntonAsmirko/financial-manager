package anton.asmirko.app.model.transaction;

import java.math.BigDecimal;

public final class OutcomingTransaction extends Transaction {

  public OutcomingTransaction(final BigDecimal amount, final String source) {
    super(amount, source);
    if (amount.compareTo(BigDecimal.ZERO) >= 0) {
      throw new IllegalArgumentException("Траты должны быть отрицательными");
    }
  }
}
