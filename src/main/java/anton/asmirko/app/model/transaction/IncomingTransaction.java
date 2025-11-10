package anton.asmirko.app.model.transaction;

import java.math.BigDecimal;

public final class IncomingTransaction extends Transaction {

  public IncomingTransaction(final BigDecimal amount, final String source) {
    super(amount, source);
    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Доходы не могут быть отрицательными");
    }
  }
}
