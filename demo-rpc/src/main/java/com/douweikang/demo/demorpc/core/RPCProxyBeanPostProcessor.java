package com.douweikang.demo.demorpc.core;

import com.douweikang.demo.demorpc.customannotation.RPCService;
import com.douweikang.demo.demorpc.microservice.consumer.RemoteServiceProxy;
import com.douweikang.demo.demorpc.singleton.RPCInstances;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class RPCProxyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        Class<?> cls = bean.getClass();
        for (Field field : cls.getDeclaredFields()) {
            if (field.isAnnotationPresent(RPCService.class)) {

                System.out.println("自定义注解：beanName=" + beanName + ";FieldName=" + field.getName());

                try {
                    field.setAccessible(true);
                    field.set(bean, RemoteServiceProxy.getProxy(field.getType(), "127.0.0.1", RPCInstances.Instance.getPort()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                return bean;
            }
        }
        return bean;
    }
}
