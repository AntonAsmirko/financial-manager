package anton.asmirko.app.model.transaction;

import anton.asmirko.app.model.wallet.Transaction;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ListTransactionQueryCategoryDecorator extends ListTransactionQuery {

    private final ListTransactionQuery decoratedQuery;
    private final Set<String> categories;

    public ListTransactionQueryCategoryDecorator(ListTransactionQuery listTransactionQuery, List<String> categories) {
        decoratedQuery = listTransactionQuery;
        this.categories = new HashSet<>(categories);
    }

    @Override
    public List<Transaction> getList() {
        return super.getList().stream().filter(it -> categories.contains(it.));
    }
}
