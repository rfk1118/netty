package chapterFive;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 * 5-10  5-11测试
 *
 * @author renfakai
 */
public class ReadAndWriteTest {

    public static void main(String[] args) {
        m512();
        m513();
    }


    public static void m512() {
        Charset uft8 = Charset.forName("UTF-8");

        // 创建一个用于保存给定字符串的字的ByteBuf
        ByteBuf byteBuf = Unpooled.copiedBuffer("Netty in Action rocks!", uft8);

        System.out.println((char) byteBuf.getByte(0));

        int readerIndex = byteBuf.readerIndex();

        System.out.println("readerIndex:" + readerIndex);

        int writeIndex = byteBuf.writerIndex();

        System.out.println("writeIndex:" + writeIndex);

        // 将索引0处的字节更新为字符'B'
        byteBuf.setByte(0, (byte) 'B');

        System.out.println((char) byteBuf.getByte(0));

        assert readerIndex == byteBuf.readerIndex();

        assert writeIndex == byteBuf.writerIndex();
    }


    public static void m513() {
        Charset uft8 = Charset.forName("UTF-8");

        // 创建一个用于保存给定字符串的字的ByteBuf
        ByteBuf byteBuf = Unpooled.copiedBuffer("Netty in Action rocks!", uft8);

        // 打印第一个字符'N'
        System.out.println((char) byteBuf.readByte());

        // current readindex
        int readerIndex = byteBuf.readerIndex();

        // current writeIndex
        int writerIndex = byteBuf.writerIndex();

        byteBuf.writerIndex((byte) '?');

        assert readerIndex == byteBuf.readerIndex();

        assert writerIndex != byteBuf.writerIndex();
    }
}
