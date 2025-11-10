package anton.asmirko.app.model.limit.list;

import anton.asmirko.app.model.limit.Limit;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ListLimitQueryFromTimeDecorator extends ListLimitQuery {

  private final ListLimitQuery decoratedQuery;
  private final LocalDateTime fromTime;

  @Override
  public List<Limit> execute() {
    return decoratedQuery.execute().stream().filter(it -> it.to().isAfter(fromTime)).toList();
  }
}
