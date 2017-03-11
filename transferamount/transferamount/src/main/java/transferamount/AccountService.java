package transferamount;

import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;

import java.math.BigDecimal;
import java.util.List;

public class AccountService {
    /**
     * A connection to an OrientDB database.
     */
    private OObjectDatabaseTx connection;

    /**
     * Constructor.
     *
     * @param connection a connection to an OrientDB database
     */
    public AccountService(OObjectDatabaseTx connection) {
        this.connection = connection;
    }

    /**
     * Creates a new account.
     *
     * @param id    id of an account
     * @param money initial amount of money on the account
     * @return
     */
    public Account createAccount(int id, BigDecimal money) {
        System.out.println("Creating account...");
        // create an object
        Account account = connection.newInstance(Account.class);
        account.setId(id);
        account.setMoney(money);
        connection.save(account);
        return account;
    }

    /**
     * Prints data about all accounts.
     */
    public void queryAllAccounts() {
        // make some queries
        System.out.println("Getting all accounts...");
        for (Account account : connection.browseClass(Account.class)) {
            System.out.println("There is an account:" + account.toString());
        }
    }

    /**
     * Gets an account by its ID.
     *
     * @param id account ID
     * @return
     */
    public Account getAccountByID(Integer id) {
        final String sql = "select * from Account where id like '" + id + "'";
        final List<Account> accounts = connection.query(new OSQLSynchQuery<Account>(sql));
        System.out.println("Number of accounts found: " + accounts.size());
        if (accounts.size() > 1) {
            throw new RuntimeException("More than one account found with ID=" + id);
        } else if (accounts.size() == 0) {
            return null;
        } else {
            return accounts.get(0);
        }
    }

    /**
     * Deletes all accounts.
     */
    public void deleteAllAccounts() {
        final String sql = "DELETE FROM Account";
        OCommandSQL query = new OCommandSQL(sql);
        connection.command(query).execute();
    }

    /**
     * Transfers money from one account to another.
     *
     * @param from   account from which the money will be withdrawn
     * @param to     account to which the money will be transferred
     * @param amount amount of money being transferred
     */
    public void transferAmount(Account from, Account to, BigDecimal amount) {
        connection.begin();
        // if the amount of money is bigger than the money on the first account
        if (amount.compareTo(from.getMoney()) > 0) {
            throw new RuntimeException("Not enough money left.");
        }
        from.setMoney(from.getMoney().subtract(amount));
        to.setMoney(to.getMoney().add(amount));
        connection.save(from);
        connection.save(to);
        connection.commit();
    }
}
