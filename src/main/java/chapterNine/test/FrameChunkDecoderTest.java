package chapterNine.test;

import chapterNine.decoder.FrameChunkDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.TooLongFrameException;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author renfakai
 * 代码清单9-6
 */
public class FrameChunkDecoderTest {

    @Test
    public void testFramesDecoded() {

        ByteBuf byteBuf = Unpooled.buffer();

        for (int i = 0; i < 9; i++) {
            byteBuf.writeByte(i);
        }

        ByteBuf input = byteBuf.duplicate();

        EmbeddedChannel channel = new EmbeddedChannel(new FrameChunkDecoder(3));

        Assert.assertTrue(channel.writeInbound(input.readBytes(2)));

        try {
            channel.writeInbound(input.readBytes(4));
            Assert.fail();
        } catch (TooLongFrameException e) {
            // nothing todo
        }

        Assert.assertTrue(channel.writeInbound(input.readBytes(3)));
        Assert.assertTrue(channel.finish());

        ByteBuf read = channel.readInbound();
        Assert.assertEquals(byteBuf.readSlice(2), read);
        read.release();

        read = channel.readInbound();
        Assert.assertEquals(byteBuf.skipBytes(4).readSlice(3), read);
        read.release();
        byteBuf.release();

    }
}
