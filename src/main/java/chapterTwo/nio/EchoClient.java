package chapterTwo.nio;

import chapterTwo.nio.handler.EchoClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;


public class EchoClient {

    private final String host;

    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }


    public void start() throws Exception {
        // work
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 创建Bootstrap
            Bootstrap b = new Bootstrap();

            // 指定EventLoopGroup 以处理客户端事件，需要适于NIO实现
            b.group(group)
                    .channel(NioSocketChannel.class)
                    // 设置绑定 或者使用 b.connect()
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        // 在创建Channel,向ChannelPipeline添加EchoClientHandler实例
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });

            // 链接到远程节点,阻塞等待直到连接完成
            ChannelFuture sync = b.connect().sync();
            // 阻塞,直到Channel关闭
            sync.channel().closeFuture().sync();
        } finally {
            // 关闭线程池并且释放所有资源
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage:" + EchoClient.class.getSimpleName() + "<host><port>");
            return;
        }
        String host = args[0];
        int prot = Integer.parseInt(args[1]);
        new EchoClient(host, prot).start();
    }
}
