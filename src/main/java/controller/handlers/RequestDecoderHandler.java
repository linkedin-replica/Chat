package controller.handlers;


import com.google.gson.Gson;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.CharsetUtil;
import models.Request;

public class RequestDecoderHandler extends ChannelInboundHandlerAdapter{
	StringBuilder builder = new StringBuilder();
	
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		
		/*
		 * HttpContent holds the request body content. A request may have more than HttpContent block so
		 * builder will collect all HttpContents.
		 */
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
}
