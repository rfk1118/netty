package chapterThirteen.domain;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * 代码清单13-6
 *
 * @author renfakai
 */
public class LogEventDecoder extends MessageToMessageDecoder<DatagramPacket> {


    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List<Object> out) throws Exception {

        ByteBuf byteBuf = msg.content();

        int idx = byteBuf.indexOf(0, byteBuf.readableBytes(), LogEvent.SEPARATOR);

        String filename = byteBuf.slice(0, idx).toString(CharsetUtil.UTF_8);

        String logMsg = byteBuf.slice(idx + 1, byteBuf.readableBytes()).toString(CharsetUtil.UTF_8);

        LogEvent logEvent = new LogEvent(msg.sender(), filename, logMsg, System.currentTimeMillis());

        out.add(logEvent);
    }
}
