package anton.asmirko.app.model.limit.list;

import anton.asmirko.app.model.limit.Limit;
import java.time.LocalDateTime;
import java.util.List;

public final class ListLimitQueryBuilder {

  private ListLimitQuery listLimitQuery;

  private ListLimitQueryBuilder() {}

  public static ListLimitQueryBuilder from(List<Limit> limit) {
    final ListLimitQueryBuilder builder = new ListLimitQueryBuilder();
    final ListLimitQuery query = new ListLimitQuery();
    query.setLimit(limit);
    builder.listLimitQuery = query;
    return builder;
  }

  public ListLimitQueryBuilder categories(List<String> categories) {
    final ListLimitQuery curQuery = listLimitQuery;
    this.listLimitQuery = new ListLimitQueryCategoryDecorator(curQuery, categories);
    return this;
  }

  public ListLimitQueryBuilder fromTime(LocalDateTime fromTime) {
    final ListLimitQuery curQuery = listLimitQuery;
    this.listLimitQuery = new ListLimitQueryFromTimeDecorator(curQuery, fromTime);
    return this;
  }

  public ListLimitQueryBuilder toTime(LocalDateTime toTime) {
    final ListLimitQuery curQuery = listLimitQuery;
    this.listLimitQuery = new ListLimitQueryToTimeDecorator(curQuery, toTime);
    return this;
  }

  public ListLimitQuery build() {
    return listLimitQuery;
  }
}
