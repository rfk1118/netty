package chapterThirteen.domain;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.List;


/**
 * @author renfakai
 * 代码清单13-2
 */
public class LogEventEncoder extends MessageToMessageEncoder<LogEvent> {

    private final InetSocketAddress remoteAddress;


    public LogEventEncoder(InetSocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, LogEvent logEvent, List<Object> out) throws Exception {

        byte[] file = logEvent.getLogfile().getBytes(CharsetUtil.UTF_8);

        byte[] msg = logEvent.getMsg().getBytes(CharsetUtil.UTF_8);

        ByteBuf buf = ctx.alloc().buffer(file.length + msg.length + 1);

        // 将文件名写入到ByteBuf中
        buf.writeBytes(file);

        // 添加分隔符
        buf.writeByte(LogEvent.SEPARATOR);

        // 将日志消息写入到butebuf
        buf.writeBytes(msg);

        out.add(new DatagramPacket(buf, remoteAddress));

    }
}
