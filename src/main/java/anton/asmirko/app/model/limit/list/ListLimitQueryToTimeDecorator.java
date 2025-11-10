package anton.asmirko.app.model.limit.list;

import anton.asmirko.app.model.limit.Limit;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ListLimitQueryToTimeDecorator extends ListLimitQuery {

  private final ListLimitQuery decoratedQuery;
  private final LocalDateTime toTime;

  @Override
  public List<Limit> execute() {
    return decoratedQuery.execute().stream().filter(it -> it.from().isBefore(toTime)).toList();
  }
}
