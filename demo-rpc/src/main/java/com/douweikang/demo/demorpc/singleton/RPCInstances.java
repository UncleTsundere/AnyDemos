package com.douweikang.demo.demorpc.singleton;

import java.util.concurrent.ConcurrentHashMap;

public enum RPCInstances {

    Instance;

    private int Port = 10000;

    public int getPort() {
        return Port;
    }

    private ConcurrentHashMap<String, Object> instance;

    public ConcurrentHashMap<String, Object> getInstance() {
        return instance;
    }
}
