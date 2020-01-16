package chapterTen.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 代码清单10-1
 *
 * @author renfakai
 */
public class ToIntegerDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() >= 4) {
            // byteBuf中读取一个int,将其添加到解码消息的list中
            out.add(in.readInt());
        }
    }
}
