package chapterEight;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.oio.OioSocketChannel;

import java.net.InetSocketAddress;

/**
 * 不兼容的channel和EventLoopGroup
 *
 * @author renfakai
 * <p>
 * Exception
 * Exception in thread "main" java.lang.IllegalStateException: incompatible event loop type: io.netty.channel.nio.NioEventLoop
 * at io.netty.channel.AbstractChannel$AbstractUnsafe.register(AbstractChannel.java:469)
 * at io.netty.channel.SingleThreadEventLoop.register(SingleThreadEventLoop.java:80)
 * at io.netty.channel.SingleThreadEventLoop.register(SingleThreadEventLoop.java:74)
 * at io.netty.channel.MultithreadEventLoopGroup.register(MultithreadEventLoopGroup.java:86)
 * at io.netty.bootstrap.AbstractBootstrap.initAndRegister(AbstractBootstrap.java:333)
 * at io.netty.bootstrap.Bootstrap.doResolveAndConnect(Bootstrap.java:163)
 * at io.netty.bootstrap.Bootstrap.connect(Bootstrap.java:145)
 * at chapterEight.NoAdaptBootstrapClient.main(NoAdaptBootstrapClient.java:36)
 */
public class NoAdaptBootstrapClient {


    public static void main(String[] args) {
        // 创建一个新的Boostrap类的实力,来创建新的客户端Channel
        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(group)
                .channel(OioSocketChannel.class)
                .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                        System.out.println("Received data");
                    }
                });

        ChannelFuture connect = bootstrap.connect(new InetSocketAddress("https://www.baidu.com", 80));

        // 尝试连接到远程节点
        connect.syncUninterruptibly();

    }
}
