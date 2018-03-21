package controller.handlers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.google.gson.JsonObject;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import models.ErrorResponseModel;
import models.Request;
import models.SuccessResponseModel;
import services.ChatService;


public class RequestProcessingHandler extends ChannelInboundHandlerAdapter{
	private static ChatService service;
	WebSocketServerHandshaker handshaker;
	public  RequestProcessingHandler() throws FileNotFoundException, IOException {
		super();
		service = new ChatService();
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		// JSONObject body that was decoded by RequestDecoderHandler
		JsonObject body = (JsonObject) msg;
		System.out.println("Processing Handler: " + body.toString());
	
		//TODO - map message to commands
		
		
		// create successful response
		LinkedHashMap<String, Object> responseBody = new LinkedHashMap<String, Object>();
		responseBody.put("type", HttpResponseStatus.ACCEPTED);
		responseBody.put("code", HttpResponseStatus.ACCEPTED.code());
		responseBody.put("message", "Changes are applied successfully and configuration files are updated");

		// send response to ResponseEncoderHandler
		ctx.writeAndFlush(responseBody);
	}
	
	   
	protected String getWebSocketURL(HttpRequest req) {
		System.out.println("Req URI : " + req.getUri());
		String url =  "ws://" + req.headers().get("Host") + req.getUri() ;
		System.out.println("Constructed URL : " + url);
		return url;
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		String errorJSON = String.format("\"errMessage\": %s", cause.getMessage());
		
		// construct Error Response
		ErrorResponseModel response = new ErrorResponseModel();
		response.setCode(HttpResponseStatus.BAD_REQUEST.code());
		response.setMessage(errorJSON);
		
		// send response to ResponseEncoderHandler
		ctx.writeAndFlush(response);
	}
	
	
	
}