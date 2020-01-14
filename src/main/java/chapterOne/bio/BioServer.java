package chapterOne.bio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 代码1-1
 * socket
 * |
 * write or read
 * |
 * Thread
 *
 * @author renfakai
 */
public class BioServer {

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8080);
        Socket sock = serverSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        PrintWriter out = new PrintWriter(sock.getOutputStream());
        String request, response;
        // while(true)
        while ((request = in.readLine()) != null) {
            if ("Done".equals(request)) {
                System.out.println("server receive Done socket will colose");
                break;
            }
            System.out.println(request);
            response = "您刚才说了" + request;
            out.println(response);
            out.flush();
        }
    }
}
