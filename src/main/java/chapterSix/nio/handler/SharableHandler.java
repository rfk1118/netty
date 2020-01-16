package chapterSix.nio.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 代码清单6-10 可共享的ChannelHandler
 */
@ChannelHandler.Sharable
public class SharableHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Channel read message :" + msg);
        // 记录方法调用,并转发给下一个ChannelHandler
        ctx.fireChannelRead(msg);
    }
}
