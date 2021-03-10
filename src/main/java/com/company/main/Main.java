package com.company.main;

import com.company.dao.inter.UserDaoInter;

public class Main {

    public static void main(String[] args) throws Exception {
        UserDaoInter userDao = Context.instanceUserDao();

//        System.out.println(userDao.getById(1));

    }
}
