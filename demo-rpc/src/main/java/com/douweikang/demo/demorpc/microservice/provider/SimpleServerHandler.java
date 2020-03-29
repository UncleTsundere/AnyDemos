package com.douweikang.demo.demorpc.microservice.provider;

import com.douweikang.demo.demorpc.microservice.nio.Providers;
import com.douweikang.demo.demorpc.microservice.protocol.TransferInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleServerHandler extends ChannelInboundHandlerAdapter {

    private Providers providers;

    private ConcurrentHashMap<String, Object> serviceMaps;

    public SimpleServerHandler(Providers providers, ConcurrentHashMap<String, Object> serviceMaps) {
        this.providers = providers;
        this.serviceMaps = serviceMaps;
    }

    public ConcurrentHashMap<String, Object> getServiceMaps() {
        return serviceMaps;
    }

    public Providers getProviders() {
        return providers;
    }

    public SimpleServerHandler(Providers providers) {
        this.providers = providers;
    }

    /**
     * 读取客户端通道的数据
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //可以在这里面写一套类似SpringMVC的框架
        //让SimpleServerHandler不跟任何业务有关，可以封装一套框架

        String requestString = "";

        if (msg instanceof ByteBuf) {
            requestString = ((ByteBuf) msg).toString(Charset.defaultCharset());
        }

        try {

            ObjectMapper mapper = new ObjectMapper();

            TransferInfo info = mapper.readValue(requestString, TransferInfo.class);

            Object returnObj = null;

            if (!serviceMaps.containsKey(info.getInterfaceName())) {

                Class<?> aClass = providers.getClass();

                Field[] fields = aClass.getDeclaredFields();

                for (Field field : fields) {

                    if (field.getGenericType().getTypeName().equals(info.getInterfaceName())) {

                        field.setAccessible(true);

                        Object resultValue = field.get(providers);

                        serviceMaps.put(info.getInterfaceName(), resultValue);
                    }
                }
            }

            Method currentMethod = null;

            if (serviceMaps.containsKey(info.getInterfaceName())) {

                Object obj = serviceMaps.get(info.getInterfaceName());

                Class<?> aClass = obj.getClass();

                Method[] methods = aClass.getMethods();

                for (Method method : methods) {

                    if (method.getName().equals(info.getMethodName())) {
                        currentMethod = method;
                        break;
                    }
                }

                if (currentMethod != null) {

                    if (!currentMethod.getAnnotatedReturnType().equals(Void.TYPE)) {
                        currentMethod.setAccessible(true);
                        returnObj = currentMethod.invoke(obj, info.getArgs());
                        //返回给客户端的数据，告诉我已经读到你的数据了
                        String result = mapper.writeValueAsString(returnObj);
                        ByteBuf buf = Unpooled.buffer();
                        buf.writeBytes(result.getBytes());
                        ctx.channel().writeAndFlush(buf);
                    } else {
                        currentMethod.invoke(obj, info.getArgs());
                    }
                }
            }

        } catch (Exception ex) {

            //返回给客户端的数据
            String result = "Fail";
            ByteBuf buf = Unpooled.buffer();
            buf.writeBytes(result.getBytes());
            ctx.channel().writeAndFlush(buf);
        }
    }
}
