package anton.asmirko.app.model.limit;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public record Limit(
    String name, String category, BigDecimal limit, LocalDateTime from, LocalDateTime to) {

  @Override
  public boolean equals(Object that) {
    if (that == null) {
      return false;
    }
    if (that instanceof Limit) {
      if (((Limit) that).name == null) {
        return false;
      } else {
        return ((Limit) that).name.equals(this.name);
      }
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name);
  }
}
