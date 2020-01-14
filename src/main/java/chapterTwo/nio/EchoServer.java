package chapterTwo.nio;

import chapterTwo.nio.handler.EchoServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class EchoServer {

    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage" + EchoServer.class.getSimpleName() + "");
        }

        // 设置端口号,如果端口参数格式不正确,则 抛出一个异常
        int port = Integer.parseInt(args[0]);

        // 调用服务器的start()方法
        new EchoServer(port).start();

    }

    public void start() throws Exception {
        // 自定义ChannelHandler
        final EchoServerHandler echoServerHandler = new EchoServerHandler();

        // 其实大家更喜欢使用father=bossGroup  和 child =workGroup
        // 创建Event-LoopGroup
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            // 创建ServerBootstrap
            ServerBootstrap b = new ServerBootstrap();

            b.group(group)

                    // 指定所使用的NIO传输Channel
                    .channel(NioServerSocketChannel.class)
                    // 使用指定的端口设置套接字地址
                    .localAddress(new InetSocketAddress(port))

                    // workGroup channelHandler
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(echoServerHandler);
                        }
                    });

            // 异步的绑定服务器,调用sync()方法阻塞等待直到绑定完成
            ChannelFuture sync = b.bind().sync();

            // 获取Channel的CloseFuther,并且阻塞当前线程知道它完成
            sync.channel().closeFuture().sync();
        } finally {
            // 关闭EventLoopGroup,释放所有资源
            group.shutdownGracefully().sync();
        }

    }
}
