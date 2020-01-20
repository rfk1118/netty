package chapterTwelve;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.ImmediateEventExecutor;

import java.net.InetSocketAddress;

/**
 * @author renfakai
 * 代码清单12-4 引导服务器
 */
public class ChatServer {


    /**
     * 创建defaultChannleGroup 将其保存所有已经连接的websocket channel
     */
    private final ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);


    private final EventLoopGroup group = new NioEventLoopGroup();

    private Channel channel;


    public ChannelFuture start(InetSocketAddress address) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(group)
                .channel(NioServerSocketChannel.class)
                .childHandler(createInitializer(channelGroup));
        ChannelFuture channelFuture = serverBootstrap.bind(address);
        channelFuture.syncUninterruptibly();
        channel = channelFuture.channel();
        return channelFuture;
    }

    protected ChannelInitializer<Channel> createInitializer(ChannelGroup channels) {
        return new ChatServerInitializer(channels);
    }

    public void destory() {
        if (channel != null) {
            channel.close();
        }
        channelGroup.close();
        group.shutdownGracefully();
    }


    public static void main(String[] args) throws Exception {
        {
            if (args.length != 1) {
                System.err.println("please give port as argument");
                System.exit(1);
            }

            int prot = Integer.parseInt(args[0]);

            final ChatServer endpoint = new ChatServer();

            ChannelFuture future = endpoint.start(new InetSocketAddress(prot));

            Runtime.getRuntime().addShutdownHook(new Thread() {

                @Override
                public void run() {
                    endpoint.destory();
                }
            });

            future.channel().closeFuture().syncUninterruptibly();
        }


    }
}
