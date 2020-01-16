package chapterEleven;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * @author renfakai
 * 代码清单11-1 添加ssl/tls支持
 */
public class SslChannelInitializer extends ChannelInitializer<Channel> {


    private final SslContext context;

    private final boolean startTls;

    public SslChannelInitializer(SslContext context, boolean startTls) {
        this.context = context;
        // 如果设置为true,第一个写入的消息将不会被加密(客户端应该设置为true)
        this.startTls = startTls;
    }


    @Override
    protected void initChannel(Channel ch) throws Exception {
        SSLEngine engine = context.newEngine(ch.alloc());
        ch.pipeline().addFirst("ssl", new SslHandler(engine, startTls));
    }
}
