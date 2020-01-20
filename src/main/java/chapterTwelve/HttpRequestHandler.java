package chapterTwelve;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * 代码清单12-1 HTTPRequestHandler
 *
 * @author renfakai
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {


    private final String wsUri;

    private static final File INDEX;

    static {
        URL location = HttpRequestHandler.class.getProtectionDomain().getCodeSource().getLocation();

        try {
            String path = location.toURI() + "index.html";
            path = !path.contains("file:") ? path : path.substring(5);
            INDEX = new File(path);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Unable to localte index.html", e);
        }

    }

    public HttpRequestHandler(String wsUri) {
        this.wsUri = wsUri;
    }


    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        if (wsUri.equalsIgnoreCase(request.getUri())) {
            // 如果请求了websocket协议升级,则增加引用计数(调用retain方法)
            // 并将它传递给下一个channelInboundHandler
            ctx.fireChannelRead(request.retain());
        } else {
            if (HttpHeaders.is100ContinueExpected(request)) {
                // 处理100 continue 请求符合Http 1.1 规范
                send100Cotinue(ctx);
            }

            // 读取index.html
            RandomAccessFile file = new RandomAccessFile(INDEX, "r");

            HttpResponse response = new DefaultHttpResponse(request.getProtocolVersion(), HttpResponseStatus.OK);

            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html;charset=UTF-8");
            boolean keepAlive = HttpHeaders.isKeepAlive(request);

            if (keepAlive) {
                // 如果去请求了keep-alive 则添加所需要的http头信息
                response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, file.length());
                response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            }

            ctx.write(response);

            if (ctx.pipeline().get(SslHandler.class) == null) {
                ctx.writeAndFlush(new DefaultFileRegion(file.getChannel(), 0, file.length()));
            } else {
                ctx.writeAndFlush(new ChunkedNioFile(file.getChannel()));
            }

            ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);

            if (!keepAlive) {
                future.addListener(ChannelFutureListener.CLOSE);
            }
        }
    }


    private static void send100Cotinue(ChannelHandlerContext channelHandlerContext) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        channelHandlerContext.writeAndFlush(response);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

