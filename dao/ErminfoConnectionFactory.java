package gr.deddie.pfr.dao;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by M.Masikos on 24/4/2017.
 */
public class ErminfoConnectionFactory {
    private static SqlSessionFactory factory;
    private static Logger logger = LogManager.getLogger(ErminfoConnectionFactory.class);
    private ErminfoConnectionFactory() {
    }

    static{
        Reader reader = null;
        try {

            reader = Resources.getResourceAsReader("mybatis-config.xml");

        } catch (IOException e) {

            throw new RuntimeException(e.getMessage());

        }

        factory = new SqlSessionFactoryBuilder().build(reader, "oracle_erminfo_jndi");

    }

    public static SqlSessionFactory getSqlSessionFactory() {
        return factory;
    }
}
