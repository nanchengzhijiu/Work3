package Util;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MybatisUtils {
    public static SqlSessionFactory factory;
    static {
        try {
            factory = new SqlSessionFactoryBuilder().build(new FileInputStream("mybatis-config.xml"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static SqlSession getSqlSession(boolean autoCommit) {
        return factory.openSession(autoCommit);
    }
}
