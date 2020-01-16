package chapterTen.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.*;

import java.util.List;

/**
 *
 * 这样处理的话不违反单一原则吗？
 * @author renfakai
 * 代码清单10-7
 */
public class WebSocketConvertHandler extends MessageToMessageCodec<WebSocketFrame, WebSocketConvertHandler.MyWebSocketFrame> {

    @Override
    protected void encode(ChannelHandlerContext ctx, MyWebSocketFrame msg, List<Object> out) throws Exception {

        ByteBuf plyload = msg.getData().duplicate().retain();

        switch (msg.getType()) {
            case BINARY:
                out.add(new BinaryWebSocketFrame(plyload));
                break;
            case TEXT:
                out.add(new TextWebSocketFrame(plyload));
                break;
            case CLOSE:
                out.add(new CloseWebSocketFrame(true, 0, plyload));
                break;
            case CONTINUATION:
                out.add(new ContinuationWebSocketFrame(plyload));
                break;
            case PONG:
                out.add(new PongWebSocketFrame(plyload));
                break;
            case PING:
                out.add(new PingWebSocketFrame(plyload));
                break;
            default:
                throw new IllegalStateException("Unsupported websocket msg" + msg);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) throws Exception {

        ByteBuf payload = msg.content().duplicate().retain();

        // 这里处理的话不违反单一职责？
        if (msg instanceof BinaryWebSocketFrame) {
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.BINARY, payload));
        } else if (msg instanceof CloseWebSocketFrame) {
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.CLOSE, payload));
        } else if (msg instanceof PingWebSocketFrame) {
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.PING, payload));
        } else if (msg instanceof PongWebSocketFrame) {
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.PONG, payload));
        } else if (msg instanceof TextWebSocketFrame) {
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.TEXT, payload));
        } else if (msg instanceof ContinuationWebSocketFrame) {
            out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.CONTINUATION, payload));
        } else {
            throw new IllegalStateException("Unsupported websocket msg" + msg);
        }
    }


    /**
     * inner class
     */
    public static final class MyWebSocketFrame {

        public enum FrameType {
            BINARY,

            CLOSE,

            PING,

            PONG,

            TEXT,

            CONTINUATION
        }


        private final FrameType type;

        private final ByteBuf data;

        public MyWebSocketFrame(FrameType type, ByteBuf data) {
            this.type = type;
            this.data = data;
        }


        public FrameType getType() {
            return type;
        }

        public ByteBuf getData() {
            return data;
        }
    }
}
