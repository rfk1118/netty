package chapterFour.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 代码清单4-2 使用Netty的异步网络编程
 * iterator.remove();
 * 这个是一个不可变的Collection,书中代码remove???
 *
 * @author renfakai
 */
public class PlainNioServer {

    public void serve(int port) throws Exception {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        ServerSocket serverSocket = serverSocketChannel.socket();
        InetSocketAddress address = new InetSocketAddress(port);
        // 将服务器绑定到选定端口
        serverSocket.bind(address);

        // 打开Selector来处理Channel
        Selector selector = Selector.open();

        // 将ServerSocket注册到selector
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        final ByteBuffer msg = ByteBuffer.wrap("Hi!\r\n".getBytes());

        for (; ; ) {
            try {
                // 等待需要处理的新事件;阻塞将一直持续到下一个事件
                selector.select();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }

            // 获取所有接收事件的SelectionKey
            Set<SelectionKey> readyKeys = selector.keys();

            Iterator<SelectionKey> iterator = readyKeys.iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                // Collections$UnmodifiableCollection$1 this is UnmodifiableCollection

                iterator.remove();

                try {
                    // 检查事件是否是一个新的已经就绪可以被接收的连接
                    if (key.isAcceptable()) {
                        ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                        SocketChannel client = channel.accept();
                        client.configureBlocking(false);
                        // 接收客户端,并将它注册到选择器
                        client.register(selector, SelectionKey.OP_READ);
                        System.out.println("Accepted connection from " + client);
                    }


                    // 检查套接字是否已经准备好写数据
                    if (key.isWritable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
                        while (byteBuffer.hasRemaining()) {
                            // 将数据写到已连接的客户端
                            if (client.write(byteBuffer) == 0) {
                                break;
                            }
                        }
                        client.close();
                    }


                } catch (IOException e) {
                    key.channel();
                    try {
                        key.channel().close();
                    } catch (IOException cex) {
                        // ignore on close
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new PlainNioServer().serve(8080);
    }
}
