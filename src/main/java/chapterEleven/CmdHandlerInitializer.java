package chapterEleven;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.LineBasedFrameDecoder;

/**
 * @author renfakai
 * 代码清单11-9 使用ChannelInitializer安装解码器
 */
public class CmdHandlerInitializer extends ChannelInitializer<Channel> {

    //
    private final static byte SPACE = (byte) ' ';

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // 添加CmdDecoder 以提取Cmd对象,并将它转发给下一个ChannelInboundHandler
        pipeline.addLast(new CmdDecoder(64 * 1024));
        // 添加CmdHandler以接收和处理Cmd对象
        pipeline.addLast(new CmdHandler());
    }


    public static final class Cmd {

        private final ByteBuf name;

        private final ByteBuf args;

        public Cmd(ByteBuf name, ByteBuf args) {
            this.name = name;
            this.args = args;
        }

        public ByteBuf getName() {
            return name;
        }

        public ByteBuf getArgs() {
            return args;
        }
    }

    public static final class CmdDecoder extends LineBasedFrameDecoder {


        public CmdDecoder(int maxLength) {
            super(maxLength);
        }

        @Override
        protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {

            ByteBuf frame = (ByteBuf) super.decode(ctx, buffer);

            if (null == frame) {
                return null;
            }

            int index = frame.indexOf(frame.readerIndex(), frame.writerIndex(), SPACE);

            return new Cmd(frame.slice(frame.readerIndex(), index),
                    frame.slice(index + 1, frame.writerIndex()));
        }
    }


    public static final class CmdHandler extends SimpleChannelInboundHandler<Cmd> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Cmd msg) throws Exception {
            // Do something with the command
        }
    }
}
