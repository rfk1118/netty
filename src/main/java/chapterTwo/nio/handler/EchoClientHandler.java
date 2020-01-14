package chapterTwo.nio.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @author renfakai
 * channelActive()在到服务器链接已经建立之后会被调用
 * channelRead0()在从服务器接收到一条消息时被调用
 * exceptionCaught()在处理过程中引发了异常时被调用
 *
 *
 * tips SimpleChannelInboundHandler和ChannelInboundHandlerAdapter
 * 因为客户端在channelRead0方法完成时,可能已经已经不在使用ByteBuf
 * 而服务器wirte依然还需要使用ByteBuf
 *
 *         boolean release = true;
 *         try {
 *             if (acceptInboundMessage(msg)) {
 *                 @SuppressWarnings("unchecked")
 *                 I imsg = (I) msg;
 *                 channelRead0(ctx, imsg);
 *             } else {
 *                 release = false;
 *                 ctx.fireChannelRead(msg);
 *             }
 *         } finally {
 *             if (autoRelease && release) {
 *                 ReferenceCountUtil.release(msg);
 *             }
 *         }
 */
@ChannelHandler.Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 当被通知channel是活跃的时候发送一条消息
        ctx.writeAndFlush(Unpooled.copiedBuffer("NettyRocks!", CharsetUtil.UTF_8));
    }


    /**
     * tips 你重写了channleRead0()方法，每当接收数据时都会调用这个方法,需要注意的是这里会出现粘包和黏包
     * 需要注意的是，由于读服务器发送的消息可能会被分块接收。
     * 也就是说如果服务器发送了5哥字节，那么不能保证这5哥字节会被一次性接收。
     * 即使是对于这么少量的数据，channelRead0()方法也可能会被调用两次，第一次使用一个持有3字节的ByteBuf
     * 第二次使用一个持有2字节的ByteBuf 作为一个面向流的协议，TCP保证了字节数组将会按照服务器发送的顺序被接收
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println("Client receiverd:" + msg.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 在发生异常时,记录错误并关闭Channle
        cause.printStackTrace();
        ctx.close();
    }
}
