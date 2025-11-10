package anton.asmirko.app.model.transaction;

import anton.asmirko.app.model.wallet.Transaction;

import java.util.List;

public class ListTransactionQuery {

    protected List<Transaction> transactions = List.of();

    public ListTransactionQuery() {
    }

    public ListTransactionQuery(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Transaction> getList() {
        return transactions;
    }
}
