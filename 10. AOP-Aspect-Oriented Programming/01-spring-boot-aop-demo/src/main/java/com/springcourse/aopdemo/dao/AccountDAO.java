package com.springcourse.aopdemo.dao;

import com.springcourse.aopdemo.Account;

public interface AccountDAO {

    void addAccount(Account theAccount, boolean vipFlag);

    boolean doWork();
}
