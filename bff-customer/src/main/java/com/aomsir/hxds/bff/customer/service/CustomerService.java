package com.aomsir.hxds.bff.customer.service;

import com.aomsir.hxds.bff.customer.controller.form.LoginForm;
import com.aomsir.hxds.bff.customer.controller.form.RegisterNewCustomerForm;

public interface CustomerService {
    public long registerNewCustomer(RegisterNewCustomerForm form);

    public Long login(LoginForm form);
}
