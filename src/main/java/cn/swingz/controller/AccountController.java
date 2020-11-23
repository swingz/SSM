package cn.swingz.controller;

import cn.swingz.domain.Account;
import cn.swingz.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 账户web
 */
@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @RequestMapping("/findAll")
    public String findAll(){
        System.out.println("表现层:查询所有用户信息");
        List<Account> all = accountService.findAll();
        for (Account account : all) {
            System.out.println(account);
        }
        return "list";
    }

    @RequestMapping("/save")
    public String save(Account account){
        accountService.saveAccount(account);
        return "list";
    }
}
