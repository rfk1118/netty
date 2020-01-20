package chapterTwelve;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * 代码清单12-6 为channelPipeline添加加密
 * @author renfakai
 */
public class SecureChatServerInitializer extends ChatServerInitializer{


    private final SslContext context;

    public SecureChatServerInitializer(ChannelGroup channels,SslContext context){
        super(channels);
        this.context = context;
    }


    @Override
    public void initChannel(Channel ch) throws Exception {
        super.initChannel(ch);

        SSLEngine engine = context.newEngine(ch.alloc());
        engine.setUseClientMode(false);
        ch.pipeline().addLast(new SslHandler(engine));
    }
}
