package com.aomsir.hxds.cst.db.dao;


import com.aomsir.hxds.cst.db.pojo.CustomerCarEntity;

import java.util.ArrayList;
import java.util.HashMap;

public interface CustomerCarDao {

    public int insert(CustomerCarEntity entity);

    public ArrayList<HashMap> searchCustomerCarList(long customerId);

    public int deleteCustomerCarById(long id);
}




