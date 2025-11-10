package anton.asmirko.app.model.limit.list;

import anton.asmirko.app.model.limit.Limit;
import anton.asmirko.app.model.transaction.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public final class ListLimitQueryBuilder {

    private ListLimitQuery listLimitQuery;

    private ListLimitQueryBuilder() {
    }

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
