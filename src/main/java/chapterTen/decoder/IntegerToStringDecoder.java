package chapterTen.decoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * @author renfakai
 * 代码清单10-3
 */
public class IntegerToStringDecoder extends MessageToMessageDecoder<Integer> {


    @Override
    protected void decode(ChannelHandlerContext ctx, Integer msg, List<Object> out) throws Exception {
        // 消息转换成为它的string表示,并将其添加到输出的list
        out.add(String.valueOf(msg));
    }
}
