package com.douweikang.demo.demorpc.microservice.consumer;

import com.douweikang.demo.demorpc.microservice.protocol.TransferInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetAddress;
import java.net.Socket;


public class RemoteServiceProxy<T> {

    public static Object getProxy(final Class classType, final String host, final int port) {

        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {

                TransferInfo transferInfo = new TransferInfo();

                transferInfo.setInterfaceName(classType.getName());

                transferInfo.setMethodName(method.getName());

                transferInfo.setArgs(objects);

                ObjectMapper mapper = new ObjectMapper();

                //接受返回结果
                String result = "";

                try {

                    String input = mapper.writeValueAsString(transferInfo);
                    InetAddress localHost = InetAddress.getLocalHost();
                    Socket socket = new Socket(localHost, 10000);
                    OutputStream outputStream = socket.getOutputStream();
                    InputStream inputStream = socket.getInputStream();
                    PrintWriter printWriter = new PrintWriter(outputStream);
                    printWriter.println(input);
                    printWriter.flush();

                    byte tmpb = (byte) inputStream.read();
                    byte[] bc = new byte[inputStream.available() + 1];
                    bc[0] = tmpb;
                    inputStream.read(bc, 1, inputStream.available());

                    //接受返回结果
                    result = new String(bc);

                    //先关闭Socket连接
                    inputStream.close();
                    outputStream.close();
                    socket.close();

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {

                }

                return mapper.readValue(result, method.getReturnType());
            }
        };

        return Proxy.newProxyInstance(classType.getClassLoader(),
                new Class[]{classType}, handler);
    }
}
