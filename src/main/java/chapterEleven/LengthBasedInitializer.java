package chapterEleven;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author renfakai
 * 代码11-10
 */
public class LengthBasedInitializer extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // 使用LengthFieldBasedFrameDecoder解码将桢长度编码到起始的前8个字节中的消息
        pipeline.addLast(new LengthFieldBasedFrameDecoder(64 * 1024, 0, 8));

        // 添加FrameHandler处理每个桢
        pipeline.addLast(new FrameHandler());

    }

    public static final class FrameHandler extends SimpleChannelInboundHandler<ByteBuf> {


        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
            // Do something with feame
        }
    }
}
