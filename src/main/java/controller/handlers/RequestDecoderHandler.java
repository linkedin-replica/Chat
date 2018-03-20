package controller.handlers;

import java.net.URISyntaxException;

import com.google.gson.Gson;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;
import models.Request;

public class RequestDecoderHandler extends ChannelInboundHandlerAdapter{
	StringBuilder builder = new StringBuilder();
	WebSocketServerHandshaker handshaker;
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		
		/*
		 * HttpContent holds the request body content. A request may have more than HttpContent block so
		 * builder will collect all HttpContents.
		 */
		
		if (msg instanceof HttpRequest) {
			HttpRequest httpRequest = (HttpRequest) msg;
			 HttpHeaders headers = httpRequest.headers();
			 if (headers.get("Connection").equalsIgnoreCase("Upgrade") || headers.get("Upgrade").equalsIgnoreCase("WebSocket")) {
				 
				//Adding new handler to the existing pipeline to handle WebSocket Messages
	                ctx.pipeline().replace(this, "websocketHandler", new WebSocketHandler());
	                // handshake upgrades the connection from HTTP to WebSocket connection
	                handleHandshake(ctx, httpRequest);

				 
			 }
		}
		if(msg instanceof HttpContent){
			HttpContent httpContent = (HttpContent) msg;
			builder.append(httpContent.content().toString(CharsetUtil.UTF_8));
		}
		/*
		 * LastHttpContent has trailing headers which indicates the end of request.
		 */
		if(msg instanceof LastHttpContent){
			// decode request body content collected in builder into request object instance.
			String json = builder.toString();
			Gson gson = new Gson();
			Request request = gson.fromJson(json, Request.class);
			// pass the decoded request to next channel in pipeline
			ctx.fireChannelRead(request);
		}
	}
	
	 
	protected void handleHandshake(ChannelHandlerContext ctx, HttpRequest req) throws URISyntaxException {
		 
		 WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(getWebSocketURL(req), null, true);
		 handshaker = wsFactory.newHandshaker(req);
		 if (handshaker == null) {
	            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
	        } else {
	            handshaker.handshake(ctx.channel(), req);
	        }
	}
	   protected String getWebSocketURL(HttpRequest req) {
	        System.out.println("Req URI : " + req.getUri());
	        String url =  "ws://" + req.headers().get("Host") + req.getUri() ;
	        System.out.println("Constructed URL : " + url);
	        return url;
	    }
	 
}














