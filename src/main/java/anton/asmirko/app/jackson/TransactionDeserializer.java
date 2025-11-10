package anton.asmirko.app.jackson;

import anton.asmirko.app.model.transaction.IncomingTransaction;
import anton.asmirko.app.model.transaction.OutcomingTransaction;
import anton.asmirko.app.model.transaction.Transaction;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionDeserializer extends StdDeserializer<Transaction> {

  public TransactionDeserializer() {
    super(Transaction.class);
  }

  @Override
  public Transaction deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    ObjectNode node = p.getCodec().readTree(p);
    BigDecimal amount = new BigDecimal(node.get("amount").asText());
    String source = node.get("source").asText();

    Transaction tx =
        amount.signum() >= 0
            ? new IncomingTransaction(amount, source)
            : new OutcomingTransaction(amount, source);

    if (node.has("time")) tx.setTime(LocalDateTime.parse(node.get("time").asText()));

    if (node.has("category")) tx.setCategory(node.get("category").asText());

    return tx;
  }
}
