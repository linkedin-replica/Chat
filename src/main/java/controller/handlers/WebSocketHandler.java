package controller.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public class WebSocketHandler extends ChannelInboundHandlerAdapter{
	 @Override
	    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		  if (msg instanceof WebSocketFrame) {
	            System.out.println("Client Channel : " + ctx.channel());
	            
	            if (msg instanceof BinaryWebSocketFrame) {
	                System.out.println( ((BinaryWebSocketFrame) msg).content() );
	            } 
	            else if (msg instanceof TextWebSocketFrame) {
	                ctx.channel().write(new TextWebSocketFrame("Message recieved : " + ((TextWebSocketFrame) msg).text()));
	                System.out.println(((TextWebSocketFrame) msg).text());
	            } 
	            else if (msg instanceof PingWebSocketFrame) {
	               //TODO - Handle ping requests
	            } 
	            else if (msg instanceof PongWebSocketFrame) {
	            	//TODO - Handle pong requests
	            } 
	            else if (msg instanceof CloseWebSocketFrame) {
	            	//TODO - Handle close socket requests
	                System.out.println("ReasonText :" + ((CloseWebSocketFrame) msg).reasonText());
	                System.out.println("StatusCode : " + ((CloseWebSocketFrame) msg).statusCode());
	            } 
	            else {
	            	//TODO - Handle unsupported websocketframe
	                System.out.println("Unsupported WebSocketFrame");
	            }
		  }
		  else {
			  //TODO - Handle unsupported frame
			  System.out.println("Unsupported Frame");
		  }
		 
		  
		  
	 }
}
