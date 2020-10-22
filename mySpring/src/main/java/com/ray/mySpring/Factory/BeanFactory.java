package com.ray.mySpring.Factory;

import com.ray.mySpring.annotation.Autowired;
import com.ray.mySpring.annotation.Component;
import com.ray.mySpring.annotation.Service;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeanFactory {
    private List<Object> components;
    private Object service;

    private Map<String, Object> beanQualifiedNameToBeanMap;
    private Map<String, Object> interfaceToBeanMap;

    private BeanFactory(List<Object> components) {
        this.components = components;
        this.beanQualifiedNameToBeanMap = new HashMap<>();
        this.interfaceToBeanMap = new HashMap<>();
    }

    public static BeanFactory build() {
        try {
            List<Object> components = getAnnotatedBeans("com.ray.mySpring", Component.class);
            return new BeanFactory(components);
        } catch (Exception e) {

        }
        return null;
    }

    public BeanFactory init(String packageToScan) {
        try {
            List<Object> userDefinedComponents = getAnnotatedBeans(packageToScan, Component.class);
            this.components.addAll(userDefinedComponents);

            List<Object> userDefinedServices = getAnnotatedBeans(packageToScan, Service.class);
            this.service = userDefinedServices.get(0);

            buildQualifiedNameToBeanMap();
            buildInterfaceToBeanMap();

            assembleBeans();
            return this;
        } catch (Exception e) {

        }
        return null;
    }

    private void assembleBeans() throws IllegalAccessException {
        for (Object bean : components) inject(bean);
        inject(service);
    }

    private void inject(Object bean) throws IllegalAccessException {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Autowired.class)) {
                field.setAccessible(true);
                if (field.getType().isInterface()) {
                    field.set(bean, interfaceToBeanMap.get(field.getType().getName()));
                } else {
                    field.set(bean, beanQualifiedNameToBeanMap.get(field.getType().getName()));
                }
            }
        }
    }

    private void buildQualifiedNameToBeanMap() {
        for (Object bean : components) {
            beanQualifiedNameToBeanMap.put(bean.getClass().getName(), bean);
        }
        beanQualifiedNameToBeanMap.put(service.getClass().getName(), service);
    }

    private void buildInterfaceToBeanMap() {
        for (Object bean : components) {
            if (bean.getClass().getInterfaces().length == 1) {
                String interfaceQualifiedName = bean.getClass().getInterfaces()[0].getName();
                interfaceToBeanMap.put(interfaceQualifiedName, bean);
            }
        }

        interfaceToBeanMap.put(this.service.getClass().getInterfaces()[0].getName(), service);
    }

    public Object getBean(String beanName) {
        Object bean = null;
        if (beanQualifiedNameToBeanMap.keySet().contains(beanName)) {
            bean = beanQualifiedNameToBeanMap.get(beanName);
        } else if (interfaceToBeanMap.keySet().contains(beanName)) {
            bean = interfaceToBeanMap.get(beanName);
        }

        if (bean.getClass().isAnnotationPresent(Service.class)) {
            ProxyFactory proxyFactory = (ProxyFactory) beanQualifiedNameToBeanMap.get(ProxyFactory.class.getName());
            bean = proxyFactory.getJdkProxy(bean);
        }
        return bean;
    }

    private static List<Object> getAnnotatedBeans(String packageToScan, Class<?> annotation) throws IllegalAccessException, InstantiationException {
        Reflections reflections = new Reflections(packageToScan, new SubTypesScanner(), new TypeAnnotationsScanner(), new FieldAnnotationsScanner());
        Set<Class<?>> beanClass = new HashSet<>();
        if (annotation.getName().equals(Component.class.getName())) {
            beanClass = reflections.getTypesAnnotatedWith(Component.class);
        } else if (annotation.getName().equals(Service.class.getName())) {
            beanClass = reflections.getTypesAnnotatedWith(Service.class);
        }
        List<Object> components = new ArrayList<>();
        for (Class<?> aClass : beanClass) {
            Object component = aClass.newInstance();
            components.add(component);
        }
        return components;
    }
}
