package chapterSix.nio.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;

/**
 * 代码清单6-4
 * @author renfakai
 */
@ChannelHandler.Sharable
public class DiscardOutboundHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        // 通过使用ReferenceCountUtil方法释放资源
        ReferenceCountUtil.release(msg);
        // 通知ChannelPromise数据已经处理了
        promise.setSuccess();
    }
}
