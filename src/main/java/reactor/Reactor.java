package reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Reactor implements Runnable {

    final Selector selector;
    final ServerSocketChannel serverSocket;

    public Reactor(int port) throws IOException {
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        serverSocket.configureBlocking(false);
        serverSocket.socket().bind(new InetSocketAddress(port));
        SelectionKey register = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        register.attach(new Acceptor());
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    dispatcher(iterator.next());
                }
                selectionKeys.clear();
            }
        } catch (IOException ignore) {
        }
    }

    private void dispatcher(SelectionKey next) {
        Runnable attachment = (Runnable) next.attachment();
        if (null != attachment) {
            attachment.run();
        }
    }

    class Acceptor implements Runnable {
        @Override
        public void run() {
            try {
                SocketChannel connection = serverSocket.accept();
                if (null != connection) {
                    new Handler(selector, connection);
                }
            } catch (IOException ig) {

            }
        }
    }

    final class Handler implements Runnable {
        final SocketChannel socketChannel;
        final SelectionKey sk;
        ByteBuffer input = ByteBuffer.allocate(4096);
        ByteBuffer output = ByteBuffer.allocate(4096);

        static final int READING = 0, SENDING = 1;
        int state = READING;

        public Handler(Selector selector, SocketChannel connection) throws IOException {
            socketChannel = connection;
            connection.configureBlocking(false);
            sk = socketChannel.register(selector, 0);
            sk.attach(this);
            sk.interestOps(SelectionKey.OP_READ);
            selector.wakeup();
        }

        boolean inputIsComplete() {
            return false;
        }

        boolean outputIsComplete() {
            return false;
        }

        void process() {
        }

        @Override
        public void run() {
            try {
                if (state == READING) {
                    read();
                } else if (state == SENDING) {
                    send();
                }
            } catch (IOException ig) {
            }
        }

        void read() throws IOException {
            socketChannel.read(input);
            if (inputIsComplete()) {
                process();
                state = SENDING;
                sk.interestOps(SelectionKey.OP_WRITE);
            }
        }

        void send() throws IOException {
            socketChannel.write(output);
            if (outputIsComplete()) {
                sk.channel();
            }
        }
    }
}
