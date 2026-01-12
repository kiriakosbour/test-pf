package gr.deddie.pfr.dao;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by M.Paschou on 16/12/2017.
 */

public class ApeConnectionFactory {
	private static SqlSessionFactory factory;
    private static Logger logger = LogManager.getLogger(ApeConnectionFactory.class);
    private ApeConnectionFactory() {
    }

    static{
        Reader reader = null;
        try {

            reader = Resources.getResourceAsReader("mybatis-config.xml");

        } catch (IOException e) {

            throw new RuntimeException(e.getMessage());

        }

        factory = new SqlSessionFactoryBuilder().build(reader, "oracle_ape_jndi");

    }

    public static SqlSessionFactory getSqlSessionFactory() {
        return factory;
    }
}
