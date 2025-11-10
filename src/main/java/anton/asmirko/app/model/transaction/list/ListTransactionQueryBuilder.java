package anton.asmirko.app.model.transaction.list;

import anton.asmirko.app.model.transaction.Transaction;
import java.time.LocalDateTime;
import java.util.List;

public final class ListTransactionQueryBuilder {

  private ListTransactionQuery listTransactionQuery;

  private ListTransactionQueryBuilder() {}

  public static ListTransactionQueryBuilder from(List<Transaction> transactions) {
    final ListTransactionQueryBuilder builder = new ListTransactionQueryBuilder();
    final ListTransactionQuery query = new ListTransactionQuery();
    query.setTransactions(transactions);
    builder.listTransactionQuery = query;
    return builder;
  }

  public ListTransactionQueryBuilder categories(List<String> categories) {
    final ListTransactionQuery curQuery = listTransactionQuery;
    this.listTransactionQuery = new ListTransactionQueryCategoryDecorator(curQuery, categories);
    return this;
  }

  public ListTransactionQueryBuilder fromTime(LocalDateTime fromTime) {
    final ListTransactionQuery curQuery = listTransactionQuery;
    this.listTransactionQuery = new ListTransactionQueryFromTimeDecorator(curQuery, fromTime);
    return this;
  }

  public ListTransactionQueryBuilder toTime(LocalDateTime toTime) {
    final ListTransactionQuery curQuery = listTransactionQuery;
    this.listTransactionQuery = new ListTransactionQueryToTimeDecorator(curQuery, toTime);
    return this;
  }

  public ListTransactionQueryBuilder outcoming() {
    final ListTransactionQuery curQuery = listTransactionQuery;
    this.listTransactionQuery = new ListTransactionQueryOutcomingDecorator(curQuery);
    return this;
  }

  public ListTransactionQueryBuilder incoming() {
    final ListTransactionQuery curQuery = listTransactionQuery;
    this.listTransactionQuery = new ListTransactionQueryIncomingDecorator(curQuery);
    return this;
  }

  public ListTransactionQuery build() {
    return listTransactionQuery;
  }
}
