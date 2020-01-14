package chapterOne.nio.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 代码1-2
 * @author renfakai
 * 链接处理器
 * ctx.pipeline().addLast(new ConnectionHandler());
 * 1-3和1-4为伪代码这里不在写入
 */
public class ConnectionHandler extends ChannelInboundHandlerAdapter {


    /**
     * @param ctx 上下文,包含了 channel ,pipeline ,handler
     *            ctx.channel();
     *            ctx.pipeline();
     *            ctx.handler();
     * @throws Exception 异常
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("client" + ctx.channel().remoteAddress() + "connected");
    }
}
