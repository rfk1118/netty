package chapterNine.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author renfakai
 * 代码清单9-1
 */
public class FixedLengthFrameDecoder extends ByteToMessageDecoder {

    private final int frameLength;

    public FixedLengthFrameDecoder(int frameLength) {
        if (frameLength < 0) {
            throw new IllegalArgumentException("frameLength must be a positive integer" + frameLength);
        }

        this.frameLength = frameLength;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 检查是否有足够的字节可以读取,以生成下一个新桢
        while (in.readableBytes() >= frameLength) {
            // 读取一个新桢
            ByteBuf byteBuf = in.readBytes(frameLength);
            // 将该桢添加到已被解码的消息列表中
            out.add(byteBuf);
        }
    }
}
