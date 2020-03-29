package com.douweikang.demo.demorpc.microservice.provider;

import com.douweikang.demo.demorpc.microservice.nio.Providers;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;

import java.util.concurrent.ConcurrentHashMap;

public class NettyProvider implements Runnable {

    private int Port;

    public NettyProvider(int port, Providers providers) {
        Port = port;
        this.providers = providers;
    }

    public int getPort() {
        return Port;
    }

    public Providers getProviders() {
        return providers;
    }

    private Providers providers;


    public void Bind() throws Exception {

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);

        EventLoopGroup workerGroup = new NioEventLoopGroup();

        //支持的服务列表
        ConcurrentHashMap<String, Object> serviceMaps = new ConcurrentHashMap<String, Object>();

        try {

            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .handler(new ServerHandler())// 服务端启动过程中有什么逻辑
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                            System.out.println("client initChannel success , address : " + socketChannel.localAddress());

                            socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Delimiters.lineDelimiter()[0]));

                            socketChannel.pipeline().addLast(new SimpleServerHandler(getProviders(), serviceMaps));
                        }
                    });

            // 绑定端口，同步等待成功
            ChannelFuture future = bootstrap.bind(getPort()).sync();
            // 等待服务端监听端口关闭
            future.channel().closeFuture().sync();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void run() {

        try {
            Bind();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class ServerHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            System.out.println("Register Success");
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
        }

        @Override
        public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        }
    }
}
