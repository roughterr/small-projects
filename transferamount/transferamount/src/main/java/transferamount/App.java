package transferamount;

import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;

import java.math.BigDecimal;
import java.util.ConcurrentModificationException;
import java.util.List;

public class App {
    private static OObjectDatabaseTx getDatabase() {
        // create a database in memory
//        OObjectDatabaseTx db = new OObjectDatabaseTx("memory:data").create();
        // create a database on disk
        OObjectDatabaseTx db = new OObjectDatabaseTx("remote:localhost/marsdb").open("root", "1111");
        // register all classes from a package
        // db.getEntityManager().registerEntityClasses(Person.class.getPackage().getName());
        // register class by class
        db.getEntityManager().registerEntityClass(Account.class);
        return db;
    }

    public static void main(String[] args) throws Exception {
        final OObjectDatabaseTx connection = getDatabase();
        try {
            AccountService accountService = new AccountService(connection);
//            accountService.createAccount(1, new BigDecimal(17));
//            accountService.createAccount(2, new BigDecimal(12));
            Account account1 = accountService.getAccountByID(1);
            Account account2 = accountService.getAccountByID(2);
            try {
                accountService.transferAmount(account1, account2, new BigDecimal(1));
            } catch (ConcurrentModificationException e) {
                // try once again
                accountService.transferAmount(account1, account2, new BigDecimal(1));
            }
            accountService.queryAllAccounts();
        } catch (Exception e) {
            throw e;
        } finally {
            connection.close();
        }
    }
}
