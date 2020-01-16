package chapterSix.nio.handler;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author renfakai
 * 缓存ChannelHandlerContext
 */
public class WriteHandler extends ChannelHandlerAdapter {


    // 组合
    private ChannelHandlerContext ctx;


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // 存储ChannelHandlerContext的引用来发送消息
        this.ctx = ctx;
    }


    /**
     * 使用之前存储的ChannelHandlerContext的引用来发送消息
     *
     * @param msg 需要发送的消息
     */
    public void send(String msg) {
        ctx.writeAndFlush(msg);
    }
}
