package com.lagou.edu.service.impl;

import com.lagou.edu.dao.AccountDao;
import com.lagou.edu.pojo.Account;
import com.lagou.edu.service.TransferService;
import com.ray.mySpring.annotation.Autowired;
import com.ray.mySpring.annotation.Service;
import com.ray.mySpring.annotation.Transactional;

/**
 * @author Rui Huang
 */
@Service
public class TransferServiceImpl implements TransferService {

    @Autowired
    private AccountDao accountDao;

    @Override
    @Transactional
    public void transfer(String fromCardNo, String toCardNo, int money) throws Exception {
        Account from = accountDao.queryAccountByCardNo(fromCardNo);
        Account to = accountDao.queryAccountByCardNo(toCardNo);

        from.setMoney(from.getMoney()-money);
        to.setMoney(to.getMoney()+money);

        accountDao.updateAccountByCardNo(to);
        accountDao.updateAccountByCardNo(from);
    }
}
