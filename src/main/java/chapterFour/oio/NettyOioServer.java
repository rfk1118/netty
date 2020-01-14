package chapterFour.oio;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * 代码清单4-3使用Netty的阻塞网络处理
 *
 * @author renfakai
 */
public class NettyOioServer {


    public void serve(int port) throws Exception {

        final ByteBuf byteBuf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hi!\r\n", CharsetUtil.UTF_8));

        EventLoopGroup workGroup = new OioEventLoopGroup();

        try {
            // 创建ServerBootstrap
            ServerBootstrap b = new ServerBootstrap();

            b.group(workGroup)
                    // 使用OioEventLoopGroup以阻塞模式
                    .channel(OioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    // add handlr
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    // 将消息写到客户端，并添加ChannelFutureListerner,以便消息一被写完就关闭连接
                                    ctx.writeAndFlush(byteBuf.duplicate())
                                            // not add ChannelFutureListener.CLOSE  终端会等待
                                            // 下面的设置会很宽关闭连接
                                            .addListener(ChannelFutureListener.CLOSE);
                                }
                            });
                        }
                    });

            // 绑定服务器以接收连接
            ChannelFuture future = b.bind().sync();

            future.channel().closeFuture().sync();
        } finally {
            // 释放所有资源
            workGroup.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        new NettyOioServer().serve(8080);
    }
}
