package ua.com.nc.dao.implementation;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.ProblemStatusDao;
import ua.com.nc.domain.ProblemStatus;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j
@Component
@PropertySource("classpath:sql_queries.properties")
public class ProblemStatusDaoImpl extends AbstractDaoImpl<ProblemStatus> implements ProblemStatusDao {

    @Value("${problem-status.select-all}")
    private String selectAllProblemStatuses;

    @Autowired
    public ProblemStatusDaoImpl (DataSource dataSource) throws PersistException {
        super (dataSource);
    }

    @Override
    protected List <ProblemStatus> parseResultSet (ResultSet rs) throws SQLException {
        List <ProblemStatus> list = new ArrayList <> ();
        while (rs.next ()) {
            Integer id = rs.getInt("ID");
            String title = rs.getString("TITLE");
            String description = rs.getString ("DESCRIPTION");
            ProblemStatus ps = new ProblemStatus (id, title, description);
            list.add(ps);
        }
        return list;
    }

    @Override
    public List <ProblemStatus> getAll() {
        String sql = selectAllProblemStatuses;
        log.info(sql + " select all problem statuses");
        return getListFromSql(sql);
    }

}
