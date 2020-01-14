package chapterFive;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 * copy类似深克隆
 * slice类似浅克隆 可以便面复制的内存开销
 * @author renfakai
 */
public class HeapBufTest {

    public static void main(String[] args) {

        Charset uft8 = Charset.forName("UTF-8");

        // 创建一个用于保存给定字符串的字的ByteBuf
        ByteBuf byteBuf = Unpooled.copiedBuffer("Netty in Action rocks!", uft8);

        // 创建该ByteBuf 从索引0到索引15结束的一个新切片
        ByteBuf sliced = byteBuf.slice(0, 15);
        // 创建该ByteBuf从索引0开始到索引15结束的分段的副本
        ByteBuf copy = byteBuf.copy(0, 15);

        // 将打印"Netty in Action"
        System.out.println(sliced.toString(uft8));
        System.out.println(copy.toString(uft8));

        // 更新索引0处的字节
        byteBuf.setByte(0, (byte) 'J');

        // 将会成功,因为数据是共享的，对其中一个的所做的更改对另外一个是可见的
        assert byteBuf.getByte(0) == sliced.getByte(0);

        assert byteBuf.getByte(0) != copy.getByte(0);

    }
}
