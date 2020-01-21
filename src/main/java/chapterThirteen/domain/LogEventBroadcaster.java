package chapterThirteen.domain;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;

/**
 * 代码清单13-3
 *
 * @author renfakai
 */
public class LogEventBroadcaster {


    private final EventLoopGroup group;


    private final Bootstrap bootstrap;


    private final File file;

    public LogEventBroadcaster(InetSocketAddress address, File file) {
        group = new NioEventLoopGroup();

        bootstrap = new Bootstrap();

        bootstrap.group(group).channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST,true)
                .handler(new LogEventEncoder(address));

        this.file = file;
    }


    public void run() throws Exception {
        Channel channel = bootstrap.bind(0).sync().channel();

        long pointer = 0;

        for (; ; ) {
            long len = file.length();

            if (len < pointer) {
                pointer = len;
            } else if (len > pointer) {
                RandomAccessFile raf = new RandomAccessFile(file, "r");
                // 设置当前文件的指针，以确保没有任何旧日志被发送
                raf.seek(pointer);
                String line;

                while ((line = raf.readLine()) != null) {
                    channel.writeAndFlush(new LogEvent(file.getAbsolutePath(), line));
                }

                pointer = raf.getFilePointer();
                raf.close();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.interrupted();
                break;
            }
        }
    }


    public void stop() {
        group.shutdownGracefully();
    }


    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            throw new IllegalArgumentException();
        }

        InetSocketAddress address = new InetSocketAddress("225.225.225.225", Integer.parseInt(args[0]));
        LogEventBroadcaster broadcaster = new LogEventBroadcaster(address, new File(args[1]));

        try {
            broadcaster.run();
        } finally {
            broadcaster.stop();
        }
    }
}

