package chapterEight;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * 代码清单8-1 引导一个客户端
 *
 * @author renfakai
 */
public class BootstrapClient {


    public static void main(String[] args) {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        // 创建一个Bootstrap 类的实力以创建和连接新的客户端
        Bootstrap bootstrap = new Bootstrap();

        // 设置EventGroup
        bootstrap.group(eventLoopGroup)
                // 指定使用的Channel实现
                .channel(NioSocketChannel.class)
                .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                        System.out.println("Received data");
                    }
                });

        final ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress("www.manning.com", 80));

        // 连接到远程主机

        channelFuture.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                if (channelFuture.isSuccess()) {
                    System.out.println("Connection attempt established");
                } else {
                    System.out.println("Connection attempt failed");
                    channelFuture.cause().printStackTrace();
                }
            }
        });
    }
}
