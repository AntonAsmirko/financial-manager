package anton.asmirko.app.model.limit.list;

import anton.asmirko.app.model.limit.Limit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListLimitQueryCategoryDecorator extends ListLimitQuery {

  private final ListLimitQuery decoratedQuery;
  private final Set<String> categories;

  public ListLimitQueryCategoryDecorator(
      ListLimitQuery listTransactionQuery, List<String> categories) {
    decoratedQuery = listTransactionQuery;
    this.categories = new HashSet<>(categories);
  }

  @Override
  public List<Limit> execute() {
    return decoratedQuery.execute().stream()
        .filter(it -> categories.contains(it.category()))
        .toList();
  }
}
