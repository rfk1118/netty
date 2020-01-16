package chapterEleven;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author renfakai
 * 代码清单11-4
 */
public class HttpCompressionInitializer extends ChannelInitializer<Channel> {

    private final boolean isClient;

    public HttpCompressionInitializer(boolean isClient) {
        this.isClient = isClient;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        if (isClient) {
            pipeline.addLast("codec", new HttpClientCodec())
                    // z一处理来自服务器的压缩内容
                    .addLast("decpmpressor", new HttpContentCompressor());
        } else {
            pipeline.addLast("codec", new HttpServerCodec())
                    // 来压缩数据
                    .addLast("compressor", new HttpContentCompressor());
        }

    }
}
