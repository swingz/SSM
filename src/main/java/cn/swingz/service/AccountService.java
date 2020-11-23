package cn.swingz.service;

import cn.swingz.domain.Account;

import java.util.List;

/**
 * Account的业务层
 */
public interface AccountService {
    //查询所有账户信息
    List<Account> findAll();

    //保存账户信息
    void saveAccount(Account account);
}
