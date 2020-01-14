package chapterFour.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * 代码清单4-1 未使用Netty的阻塞网络编程
 *
 * @author renfakai
 */
public class PlainOioServer {

    public void serve(int port) throws Exception {
        final ServerSocket serverSocket = new ServerSocket(port);

        try {
            for (; ; ) {
                final Socket clientSocket = serverSocket.accept();

                // Accepted connection from Socket[addr=/0:0:0:0:0:0:0:1,port=57825,localport=8080]
                System.out.println("Accepted connection from " + clientSocket);

                new Thread(new Runnable() {
                    public void run() {
                        OutputStream outputStream;

                        try {
                            outputStream = clientSocket.getOutputStream();
                            // 将消息写回到客户端
                            outputStream.write("Hi\r\n".getBytes(Charset.forName("UTF-8")));
                            outputStream.flush();
                            clientSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                clientSocket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception{
        new PlainOioServer().serve(8080);
    }
}
