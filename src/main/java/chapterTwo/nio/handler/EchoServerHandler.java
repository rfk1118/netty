package chapterTwo.nio.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

/**
 * @author renfakai
 * channelRead 对于每个传入的消息都要调用
 * channelReadComplete 通知ChannelInboundHandler最后一次对channelRead的调用是当前批量读取中的最后一条消息
 * exceptionCaught 在读取操作期间，有异常抛出时会调用
 * ChannelHandler.Sharable 表示一个ChannelHandler可以被channel安全的共享
 */
@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        super.channelRead(ctx, msg);
        ByteBuf in = (ByteBuf) msg;

        // print message in terminal
        System.out.println("server receiverd" + in.toString(Charset.defaultCharset()));

        // 将收到的消息发给发送者,而不冲刷出站消息
        //   ctx.writeAndFlush(in);
        ctx.write(in);

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 将未决消息刷到远程节点,并且关闭channle
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }


    /**
     * tips  每个Channel都有一个与之相关联的ChannelPipeline,其持有一个ChannelHandler实力链
     * 在默认的情况下,ChannelHandler会把对它的方法调用转发给链中的下一个ChannelHandler.
     * 因此,如果exceptionCaught方法没有被该链中的某处实现，那么所接受的异常将会被传递到ChannelPipeline的尾端并被记录
     * 因此，你的应用程序应该提供至少一个实现了exceptionCaught方法的ChannelHandler
     *
     * @param ctx   上下文
     * @param cause exception
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        // 打印异常栈轨迹
        cause.printStackTrace();
        // 关闭channel
        ctx.close();
    }
}
