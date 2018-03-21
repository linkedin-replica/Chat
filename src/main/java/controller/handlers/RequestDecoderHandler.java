package controller.handlers;

import java.net.URISyntaxException;

import java.nio.file.InvalidPathException;
import java.util.LinkedHashMap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

public class RequestDecoderHandler extends ChannelInboundHandlerAdapter{
	StringBuilder builder = new StringBuilder();
	WebSocketServerHandshaker handshaker;
	private final String wsUri;
	
	public RequestDecoderHandler(String wsUri) {
		this.wsUri = wsUri;
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
System.out.println(msg.getClass());
		if ((msg instanceof HttpRequest)) {

						HttpRequest httpRequest = (HttpRequest) msg;
						HttpHeaders headers = httpRequest.headers();
						
						System.out.println("Server recieved an HTTP Request: " + msg.getClass());
						System.out.println("Headers: " + headers);
			System.out.println(((HttpRequest) msg).headers().get("msg"));

			if(wsUri.equalsIgnoreCase( httpRequest.getUri())) {
				
				ctx.pipeline().replace(RequestProcessingHandler.class, "websocketHandler", new WebSocketHandler());
				handleHandshake(ctx, httpRequest);
				WebSocketFrame frame = (WebSocketFrame) msg;
				ctx.fireChannelRead(frame);
			}
		}
		

		if(msg instanceof HttpContent){
			HttpContent httpContent = (HttpContent) msg;
			builder.append(httpContent.content().toString(CharsetUtil.UTF_8));
		}

		if(msg instanceof LastHttpContent){
			//decode request body content collected in builder into request object instance.
			String json = builder.toString();
			Gson gson = new Gson();
			JsonObject body = gson.fromJson(json, JsonObject.class);
			
			//reset builder
			builder = new StringBuilder();
			
			//pass the decoded request to next channel in pipeline
			ctx.fireChannelRead(body);
		}
	}
	 
	protected void handleHandshake(ChannelHandlerContext ctx, HttpRequest req) throws URISyntaxException {
		 System.out.println("in handshake");
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
	 
	   
	//TODO - Construct error messages
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

		LinkedHashMap<String, Object> responseBody = new LinkedHashMap<String, Object>();
		
		//set Http status code
		if(cause instanceof InvalidPathException){
			responseBody.put("code", HttpResponseStatus.NOT_FOUND.code());
			responseBody.put("type", HttpResponseStatus.NOT_FOUND);
		}else{ 
//			if (cause instanceof SearchException){
//				responseBody.put("code", HttpResponseStatus.BAD_REQUEST.code());
//				responseBody.put("type", HttpResponseStatus.BAD_REQUEST);
//			}else{
//				responseBody.put("code", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
//				responseBody.put("type", HttpResponseStatus.INTERNAL_SERVER_ERROR);
//			}
		}
		
		responseBody.put("errMessage1", cause.getMessage());
		ctx.writeAndFlush(responseBody);
	}
}














