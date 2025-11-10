package anton.asmirko.app.model.transaction;

import anton.asmirko.app.jackson.TransactionDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@JsonDeserialize(using = TransactionDeserializer.class)
public abstract sealed class Transaction permits IncomingTransaction, OutcomingTransaction {

  @Getter protected BigDecimal amount;

  @Getter @Setter protected boolean isMuted;

  @Getter protected String source;

  @Setter @Getter protected LocalDateTime time;

  @Getter @Setter protected String category;

  public Transaction(final BigDecimal amount, final String source) {
    this.amount = amount;
    this.source = source;
  }
}
