package chapterEleven;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * @author renfakai
 * 代码清单11-6
 */
public class WebSocketServerInitializer extends ChannelInitializer<Channel> {


    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(new HttpServerCodec(),
                new HttpObjectAggregator(65536),
                new WebSocketServerProtocolHandler("/websocket"),
                new TextFrameHandlr(),
                new BinaryFrameHandler(),
                new ContinuationFrameHandler()
        );
    }


    public static final class TextFrameHandlr extends SimpleChannelInboundHandler<TextWebSocketFrame> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
            // handler text
        }
    }

    public static final class BinaryFrameHandler extends SimpleChannelInboundHandler<BinaryWebSocketFrame> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, BinaryWebSocketFrame msg) throws Exception {
            // handler binary
        }
    }

    public static final class ContinuationFrameHandler extends SimpleChannelInboundHandler<ContinuationWebSocketFrame> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ContinuationWebSocketFrame msg) throws Exception {
            // handler ContinuationWebSocketFrame
        }
    }

}
