package chapterEight;

import chapterEight.handler.ChannelInitializerImpl;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * 代码清单8-6 引导和使用ChannelInitializer
 *
 * @author renfakai
 */
public class UseChannelInitializer {

    public static void main(String[] args) throws Exception{
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(new NioEventLoopGroup(),new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializerImpl());

        ChannelFuture bind = serverBootstrap.bind(new InetSocketAddress(8080));
        bind.sync();
    }
}
