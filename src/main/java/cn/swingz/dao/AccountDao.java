package cn.swingz.dao;

import cn.swingz.domain.Account;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 账户dao接口
 */
@Repository
public interface AccountDao {

    //查询所有账户信息
    @Select("select * from account")
    List<Account> findAll();

    //保存账户信息
    @Insert("insert into account(name,money) values (#{name},#{money})")
    void saveAccount(Account account);


}
