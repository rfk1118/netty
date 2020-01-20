package chapterTwelve;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import java.net.InetSocketAddress;

/**
 * @author renfakai
 * 代码清单12-7 向ChatServer添加加密
 */
public class SecureChatServer extends ChatServer{

    private final SslContext context ;

    public SecureChatServer(SslContext context ){
        this.context =context;
    }


    @Override
    protected ChannelInitializer<Channel> createInitializer(ChannelGroup channels) {
        return new SecureChatServerInitializer(channels,context);
    }


    public static void main(String[] args) throws Exception{
        if (args.length != 1) {
            System.err.println("please give port as argument");
            System.exit(1);
        }

        int prot = Integer.parseInt(args[0]);
        SelfSignedCertificate cert = new SelfSignedCertificate();
        SslContext context = SslContext.newServerContext(cert.certificate(),cert.privateKey());

        final SecureChatServer endpoint = new SecureChatServer(context);

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
