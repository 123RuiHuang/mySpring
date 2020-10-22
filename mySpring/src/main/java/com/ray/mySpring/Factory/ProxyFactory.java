package com.ray.mySpring.Factory;

import com.ray.mySpring.annotation.Component;
import com.ray.mySpring.TransactionManager.TransactionManager;
import com.ray.mySpring.annotation.Autowired;
import com.ray.mySpring.annotation.Transactional;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Component
public class ProxyFactory {

    @Autowired
    private TransactionManager transactionManager;

    public Object getJdkProxy(Object obj) {

        return  Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Object result = null;
                        try{
                            if (method.isAnnotationPresent(Transactional.class)) {
                                transactionManager.beginTransaction();
                                result = method.invoke(obj, args);
                                transactionManager.commit();
                            } else {
                                result = method.invoke(obj, args);
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                            transactionManager.rollback();
                            throw e;
                        }
                        return result;
                    }
                });
    }
}
