package chapterNine.test;

import chapterNine.encoder.AbsIntegerEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;
import org.junit.Test;

/**
 * 代码清单9-4
 *
 * @author renfakai
 */
public class AbsIntegerEncoderTest {


    @Test
    public void testEncoded() {

        ByteBuf byteBuf = Unpooled.buffer();

        for (int i = 0; i < 10; i++) {
            byteBuf.writeInt(i * -1);
        }

        EmbeddedChannel channel = new EmbeddedChannel(new AbsIntegerEncoder());

        Assert.assertTrue(channel.writeOutbound(byteBuf));

        Assert.assertTrue(channel.finish());

        for (int i = 0; i < 10; i++) {
            int abi = channel.readOutbound();
            System.out.println(abi);
            Assert.assertEquals(i, abi);
        }

        Assert.assertNull(channel.readOutbound());
    }
}
