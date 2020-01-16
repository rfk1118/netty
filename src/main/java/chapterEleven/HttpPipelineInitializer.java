package chapterEleven;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * @author renfakai
 * 代码清单11-2 添加http支持
 */
public class HttpPipelineInitializer extends ChannelInitializer<Channel> {

    private final boolean client;

    public HttpPipelineInitializer(boolean client) {
        this.client = client;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (client) {
            // 如果是客户端,则添加HttpResponseDecoder 以处理来自服务器的响应
            pipeline.addLast("decoder", new HttpResponseDecoder())
                    // 如果是客户端,则添加HttpResponseEncoder以向服务器发送请求
                    .addLast("encoder", new HttpResponseEncoder());
        } else {
            pipeline.addLast("decoder", new HttpResponseDecoder())
                    .addLast("encoder", new HttpResponseEncoder());
        }

    }
}
