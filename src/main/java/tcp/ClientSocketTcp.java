package tcp;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class ClientSocketTcp {

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("localhost", 4444));
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("helloServer".getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        socket.shutdownOutput();
        byte b[] = new byte[1024];
        while (inputStream.read(b) > 0) {
            System.out.println(new String(b));
            break;
        }

        socket.close();
    }
}
