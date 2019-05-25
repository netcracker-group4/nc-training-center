package ua.com.nc.dao.implementation;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;
import ua.com.nc.dao.PersistException;
import ua.com.nc.dao.interfaces.SuitabilityDao;
import ua.com.nc.domain.Suitability;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Repository
@PropertySource("classpath:sql_queries.properties")
public class SuitabilityDaoImpl extends AbstractDaoImpl<Suitability> implements SuitabilityDao {

    @Value("${suitability.select-all}")
    private String suitabilitySelectAll;

    @Autowired
    public SuitabilityDaoImpl(DataSource dataSource) throws PersistException {
        super(dataSource);
    }

    @Override
    protected String getSelectQuery() {
        return suitabilitySelectAll;
    }

    @Override
    protected List<Suitability> parseResultSet(ResultSet rs) throws SQLException {
        List<Suitability> list = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("ID");
            String title = rs.getString("title");
            int priority = rs.getInt("priority");
            String color = rs.getString("color");
            list.add(new Suitability(id, title, color, priority));
        }
        log.info("Retrieved Suitabilities from database " + list);
        return list;
    }
}
