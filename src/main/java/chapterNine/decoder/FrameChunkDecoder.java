package chapterNine.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

/**
 * 代码清单9-5
 *
 * @author renfakai
 */
public class FrameChunkDecoder extends ByteToMessageDecoder {

    private final int maxFrameSize;

    public FrameChunkDecoder(int maxFrameSize) {
        this.maxFrameSize = maxFrameSize;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int readableBytes = in.readableBytes();
        if (readableBytes > maxFrameSize) {
            in.clear();
            // 数据太大,丢出异常
            throw new TooLongFrameException();
        }
        // 否则从byteBuf中读取一个新的桢
        ByteBuf byteBuf = in.readBytes(readableBytes);
        // 将该桢添加到解码消息
        out.add(byteBuf);
    }
}
