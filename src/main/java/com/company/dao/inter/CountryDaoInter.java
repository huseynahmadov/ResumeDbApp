package com.company.dao.inter;

import com.company.entity.Country;

import java.util.List;

public interface CountryDaoInter {


    List<Country> getAll();
     
    public Country getById(int id);
     
    boolean updateCountry(Country u);
     
    boolean removeCountry(int id);
}
