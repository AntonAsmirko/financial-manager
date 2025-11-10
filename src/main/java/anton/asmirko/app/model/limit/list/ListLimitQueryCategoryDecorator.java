package anton.asmirko.app.model.limit.list;

import anton.asmirko.app.model.transaction.Transaction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ListLimitQueryCategoryDecorator extends ListLimitQuery {

    private final ListTransactionQuery decoratedQuery;
    private final Set<String> categories;

    public ListLimitQueryCategoryDecorator(ListTransactionQuery listTransactionQuery, List<String> categories) {
        decoratedQuery = listTransactionQuery;
        this.categories = new HashSet<>(categories);
    }

    @Override
    public List<Transaction> execute() {
        return decoratedQuery.execute()
                .stream()
                .filter(it -> categories.contains(it.getCategory()))
                .toList();
    }
}
