package com.douweikang.demo.demorpc.microservice.protocol;

public class TransferInfo {

    private  String InterfaceName;

    public String getInterfaceName() {
        return InterfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        InterfaceName = interfaceName;
    }

    public String getMethodName() {
        return MethodName;
    }

    public void setMethodName(String methodName) {
        MethodName = methodName;
    }

    public Object[] getArgs() {
        return Args;
    }

    public void setArgs(Object[] args) {
        Args = args;
    }

    public  TransferInfo()
    {

    }

    public TransferInfo(String interfaceName, String methodName, Object[] args) {
        InterfaceName = interfaceName;
        MethodName = methodName;
        Args = args;
    }

    private String MethodName;

    private Object[] Args;

}
