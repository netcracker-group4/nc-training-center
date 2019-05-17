package ua.com.nc.dao.transactions;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import ua.com.nc.dao.PersistException;

import javax.sql.DataSource;
import java.sql.SQLException;

@Log4j
public class PostgresTransactionFactory implements TransactionFactory {

    @Autowired
    private DataSource dataSource;

    @Override
    public Transaction getTransaction() {
        try {
            return new PostgresTransaction(dataSource.getConnection());
        } catch (SQLException e) {
            log.trace(e);
            throw new PersistException("Error while creating transaction");
        }
    }
}
