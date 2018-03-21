package controller.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame>{
private final ChannelGroup group;

	public TextWebSocketFrameHandler(ChannelGroup group) {
		this.group = group;
	}
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {

	}

	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		
	}

	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
		group.writeAndFlush(msg.retain());
		
	}
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx,Object evt)throws Exception {
		if(evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
			ctx.pipeline().remove(RequestProcessingHandler.class);
			group.writeAndFlush(new TextWebSocketFrame("Client" + ctx.channel()+"joined"));
			group.add(ctx.channel());
		}
		else {
			super.userEventTriggered(ctx, evt);
		}
	}

}
