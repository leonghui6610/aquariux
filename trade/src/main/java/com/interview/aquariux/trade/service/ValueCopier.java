package com.interview.aquariux.trade.service;

import org.apache.commons.beanutils.BeanUtilsBean;

import java.lang.reflect.InvocationTargetException;

public class ValueCopier extends BeanUtilsBean {
    @Override
    public void copyProperty(Object bean, String name, Object value)
            throws IllegalAccessException, InvocationTargetException {
        if (value == null)
            return;
        super.copyProperty(bean, name, value);
    }
}
