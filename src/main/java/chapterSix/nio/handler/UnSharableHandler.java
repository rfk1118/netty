package chapterSix.nio.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author renfakai
 * 错误用法
 * 只应该在确定了你的Channel是线程安全的时候菜使用@Sharable
 * 为何要共享同一个ChannelHandler在多个ChannelPipeline 中安装同一个ChannelHandler的一个常见的原因在于收集跨越多个Channel的统计信息
 */
@ChannelHandler.Sharable
public class UnSharableHandler extends ChannelInboundHandlerAdapter {

    private int count;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        count++;

        // 记录方法调用并转发给下一个ChannelHandler
        System.out.println("ChannelRead(...) called the " + count + "time");

        // call next Chain
        ctx.fireChannelRead(msg);
    }
}
