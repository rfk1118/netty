package chapterEight;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.oio.OioSocketChannel;
import io.netty.util.concurrent.Future;

import java.net.InetSocketAddress;

/**
 * @author renfakai
 */
public class UdpServer {

    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        OioEventLoopGroup eventExecutors = new OioEventLoopGroup();
        bootstrap.group(eventExecutors)
                .channel(OioSocketChannel.class)
                .handler(new SimpleChannelInboundHandler<DatagramPacket>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
                        // Do something with the packet
                    }
                });

        ChannelFuture bind = bootstrap.bind(new InetSocketAddress(0));

        bind.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("Connection attempt established");
                } else {
                    System.out.println("Connection attempt failed");
                    future.cause().printStackTrace();
                }
            }
        });

        // 代码清单8-9 优雅的关闭
        Future<?> future = eventExecutors.shutdownGracefully();
        future.syncUninterruptibly();
    }
}
