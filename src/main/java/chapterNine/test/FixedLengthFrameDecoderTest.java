package chapterNine.test;

import chapterNine.decoder.FixedLengthFrameDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author renfakai
 * 代码清单9-2
 */
public class FixedLengthFrameDecoderTest {


    // 使用了注解@Test 因此Junit将会执行该方法
    @Test
    public void testFramesDecoded() {
        // 创建一个ByteBuf ,并存储9个字节
        ByteBuf byteBuf = Unpooled.buffer();

        for (int i = 0; i < 9; i++) {
            byteBuf.writeByte(i);
        }

        ByteBuf input = byteBuf.duplicate();

        // 创建一个EmbeddedChannel并添加一个FixedLengthFrameDecoder，其将以3字节的桢长度被测试
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));

        Assert.assertTrue(embeddedChannel.writeInbound(input.retain()));
        Assert.assertTrue(embeddedChannel.finish());

        ByteBuf read = (ByteBuf) embeddedChannel.readInbound();
        Assert.assertEquals(byteBuf.readSlice(3), read);
        read.release();

        read = embeddedChannel.readInbound();
        Assert.assertEquals(byteBuf.readSlice(3), read);
        read.release();

        read = embeddedChannel.readInbound();
        Assert.assertEquals(byteBuf.readSlice(3), read);
        read.release();

        Assert.assertNull(embeddedChannel.readInbound());
        input.release();

    }


    @Test
    public void testFrmesDecode2() {
        ByteBuf byteBuf = Unpooled.buffer();

        for (int i = 0; i < 9; i++) {
            byteBuf.writeByte(i);
        }

        ByteBuf input = byteBuf.duplicate();

        // 创建一个EmbeddedChannel并添加一个FixedLengthFrameDecoder，其将以3字节的桢长度被测试
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));

        // 返回一个false 因为没有一个完整的可读取的桢
        Assert.assertFalse(embeddedChannel.writeInbound(input.readBytes(2)));


        Assert.assertTrue(embeddedChannel.writeInbound(input.readBytes(7)));


        Assert.assertTrue(embeddedChannel.finish());


        ByteBuf read = (ByteBuf) embeddedChannel.readInbound();
        Assert.assertEquals(input.readSlice(3), read);
        read.release();


        read = (ByteBuf) embeddedChannel.readInbound();
        Assert.assertEquals(input.readSlice(3), read);
        read.release();

        read = (ByteBuf) embeddedChannel.readInbound();
        Assert.assertEquals(input.readSlice(3), read);
        read.release();

        Assert.assertNull(embeddedChannel.readInbound());
        input.release();


    }
}
