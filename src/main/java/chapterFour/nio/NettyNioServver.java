package chapterFour.nio;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * 代码清单4-4 使用Netty的异步网络处理
 * 4-5是伪代码不做处理
 *
 * @author renfakai
 */
public class NettyNioServver {


    public void serve(int port) throws Exception {
        final ByteBuf byteBuf = Unpooled.copiedBuffer("Hi!\r\n", CharsetUtil.UTF_8);

        // 使用非阻塞模式
        // Caused by: java.lang.IllegalStateException: Only supported on Linux
//        EventLoopGroup workGroup = new EpollEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            // 创建ServerBootstrap
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(workGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    // 指定ChannelInitializer,如果区分boss和work的话,boss只负责连接，work使用
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 添加handler以接收和处理事件
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {

                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    // 将消息写到客户端,并添加ChannelFutureListener,以便消息一被写完就关闭连接
                                    ctx.writeAndFlush(byteBuf.duplicate())
                                            .addListener(ChannelFutureListener.CLOSE);
                                }
                            });
                        }
                    });

            // bind server以接受连接
            ChannelFuture sync = bootstrap.bind().sync();
            sync.channel().closeFuture().sync();

        } finally {
            // 释放所有的资源
            workGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new NettyNioServver().serve(8080);
    }

}
