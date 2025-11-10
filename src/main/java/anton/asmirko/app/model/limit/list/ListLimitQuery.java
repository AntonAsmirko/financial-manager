package anton.asmirko.app.model.limit.list;

import anton.asmirko.app.model.limit.Limit;
import anton.asmirko.app.model.transaction.Transaction;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class ListLimitQuery {

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    protected List<Limit> limit = List.of();

    public ListLimitQuery() {
    }

    public ListLimitQuery(List<Limit> limit) {
        this.limit = limit;
    }

    public List<Limit> execute() {
        return limit;
    }
}
