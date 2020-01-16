package chapterTen.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 代码清单10-5
 * @author renfakai
 * 编码器
 */
public class ShortToByteEncoder extends MessageToByteEncoder<Short> {


    protected void encode(ChannelHandlerContext ctx, Short msg, ByteBuf out) throws Exception {
        out.writeShort(msg);
    }
}
