package chapterTwelve;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * 代码清单12-2 处理文本桢
 *
 * @author renfakai
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final ChannelGroup channelGroup;

    public TextWebSocketFrameHandler(ChannelGroup channelGroup) {
        this.channelGroup = channelGroup;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        // 增加消息的引用计数，并将它写到channleGroup中所有已经连接的客户端
        channelGroup.writeAndFlush(msg.retain());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
            // 如果该时间表示握手成功,则从该channellipeline中移除HttpRequestHandler 因为将不会收到任务http消息了
            ctx.pipeline().remove(HttpRequestHandler.class);
            // 通知所有的客户新的客户端已经连接上了
            channelGroup.writeAndFlush(new TextWebSocketFrame("client" + ctx.channel() + "join"));
            channelGroup.add(ctx.channel());
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
