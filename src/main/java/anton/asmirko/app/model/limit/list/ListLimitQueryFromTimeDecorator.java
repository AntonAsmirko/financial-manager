package anton.asmirko.app.model.limit.list;

import anton.asmirko.app.model.transaction.Transaction;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class ListLimitQueryFromTimeDecorator extends ListLimitQuery {

    private final ListTransactionQuery decoratedQuery;
    private final LocalDateTime fromTime;

    @Override
    public List<Transaction> execute() {
        return decoratedQuery.execute()
                .stream()
                .filter(it -> it.getTime().isAfter(fromTime))
                .toList();
    }
}
