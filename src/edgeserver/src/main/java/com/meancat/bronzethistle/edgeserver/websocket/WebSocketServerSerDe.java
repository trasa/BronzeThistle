package com.meancat.bronzethistle.edgeserver.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.spi.ErrorCode;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.UpstreamMessageEvent;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import org.jboss.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import org.jboss.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebSocketServerSerDe extends SimpleChannelHandler {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServerSerDe.class);

    @Autowired
    protected ObjectMapper jsonObjectMapper;


    @Override
    public void writeRequested(ChannelHandlerContext context, MessageEvent e) {
        Object msg = e.getMessage();
        if (msg instanceof WebSocketFrame || msg instanceof HttpResponse) {
            context.sendDownstream(e);
            return;
        }
//        Message message = null;
//        if (e.getMessage() instanceof Message) {
//            message = (Message)e.getMessage();
//        } else {
//            message = new Message(null, e.getMessage());
//        }
//        ChannelBuffer response = ChannelBuffers.wrappedBuffer(jsonObjectMapper.writeValueAsBytes(message));
//        context.getChannel().write(new TextWebSocketFrame(response));
    }

    @Override
    public void messageReceived(ChannelHandlerContext context, MessageEvent e) {
        Channel channel = e.getChannel();
        Object msg = e.getMessage();
        if (msg instanceof HttpRequest) {
            handleHttpRequest(context, (HttpRequest)msg);
        } else if (msg instanceof WebSocketFrame) {
            handleWebSocketFrame(context, e, (WebSocketFrame)msg);
        }
    }


    private void handleWebSocketFrame(ChannelHandlerContext context, MessageEvent e, WebSocketFrame frame) {
        if (frame instanceof CloseWebSocketFrame) {
            // ?
            // TODO
            return;
        } else if (frame instanceof PingWebSocketFrame) {
            context.getChannel().write(new PongWebSocketFrame(frame.getBinaryData()));
            return;
        }

        Channel channel = context.getChannel();
//        try {
//            Message message = jsonObjectMapper.readValue(frame.getBinaryData().toString(CharsetUtil.UTF_8), Message.class);
//            if (message.header == null) {
//                message.header - new MessageHeader();
//            }
//            context.sendUpstream(new UpstreamMessageEvent(channel, message,e.getRemoteAddress()));
//        } catch (JsonProcessingException ex) {
//            if (channel.isOpen()) {
//                channel.write(new Message(new MessageHeader(), new ErrorNotification(ErrorCode.INVALID_MESSAGE, "Message not understood")));
//            }
//        }
    }

    private void handleHttpRequest(ChannelHandlerContext context, HttpRequest request){
        if (request.getMethod() != HttpMethod.GET) {
            sendHttpResponse(context, request, new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN));
            return;
        }

        // handshake
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(getWebSocketLocation(request), null, false);
        WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(request);

        if (handshaker == null) {
            wsFactory.sendUnsupportedWebSocketVersionResponse(context.getChannel());
        } else {
            channelHandshakers.set(ctx.getChannel(), handshaker);

            ChannelFuture future = handshaker.handshake(context.getChannel(), request);

            future.addListener(WebSocketServerHandshaker.HANDSHAKE_LISTENER);
            future.addListener(HANDSHAKE_LISTENER);
        }
    }
    private void sendHttpResponse(ChannelHandlerContext ctx, HttpRequest req, HttpResponse res) {
        // Generate an error page if response status code is not OK (200).
        if (res.getStatus().getCode() != 200) {
            res.setContent(ChannelBuffers.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8));
            HttpHeaders.setContentLength(res, res.getContent().readableBytes());
        }

        // Send the response and close the connection if necessary.
        ChannelFuture f = ctx.getChannel().write(res);
        if (!HttpHeaders.isKeepAlive(req) || res.getStatus().getCode() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }
    private String getWebSocketLocation(HttpRequest req) {
        return "ws://" + req.getHeader(HttpHeaders.Names.HOST) + "/";
    }
}
