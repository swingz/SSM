import cn.swingz.dao.AccountDao;
import cn.swingz.domain.Account;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class TestMyBatis {

    @Test
    public void run1() throws IOException {
        //加载配置文件
        InputStream in = Resources.getResourceAsStream("sqlMapConfig.xml");
        //创建SqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
        //创建SqlSession对象
//        ture自动提交事务
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        //获取代理对象
        AccountDao mapper = sqlSession.getMapper(AccountDao.class);
        Account account = new Account();
        account.setName("hello");
        account.setMoney(1010d);
        mapper.saveAccount(account);
//        提交事务
//        sqlSession.commit();
        List<Account> all = mapper.findAll();
        sqlSession.close();
        in.close();
        for (Account account1 : all) {
            System.out.println(account1);
        }

    }
}
