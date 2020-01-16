package chapterTen.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * 代码清单10-1
 *
 * @author renfakai
 *
 * 和之前一样，从ByteBuf中提取的int将会被添加到List中。如果没有足够的字节可用，这个readInt()方法的实现将会抛出一个Error
 *
 * 扩展ReplayingDecoder<Void> 以将字节解码为消息
 */
public class ToIntegerDecoder2 extends ReplayingDecoder<Void> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        out.add(in.readInt());
    }
}
