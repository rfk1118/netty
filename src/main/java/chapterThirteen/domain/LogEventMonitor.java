package chapterThirteen.domain;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;

/**
 * 代码清单13-8
 *
 * @author renfakai
 */
public class LogEventMonitor {

    private final EventLoopGroup group;

    private final Bootstrap bootstrap;

    public LogEventMonitor(InetSocketAddress inetSocketAddress) {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();

        bootstrap.group(group)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new LogEventDecoder());
                        pipeline.addLast(new LogEventHandler());

                    }
                })
                .localAddress(inetSocketAddress);
    }


    public Channel bind() {
        return bootstrap.bind().syncUninterruptibly().channel();
    }


    public void stop() {
        group.shutdownGracefully();
    }


    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            throw new IllegalArgumentException("Usage:LogEventMonitor<port>");
        }

        InetSocketAddress address = new InetSocketAddress(Integer.parseInt(args[0]));
        LogEventMonitor monitor = new LogEventMonitor(address);

        try {
            Channel channel = monitor.bind();
            System.out.println("LogEventMonitor running ");

            channel.closeFuture().sync();
        } finally {
            monitor.stop();
        }
    }
}
