package chapterSix.nio.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author renfakai
 * 代码清单6-12 基本的入账异常处理
 * 默认实现是简单的将当前异常转发给ChannelPipeline中的下一个ChannelHandler
 *
 * 如果异常到达了ChannelPipiline的尾端,它菜会被记录为为处理
 */
public class InboundExceptionHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
