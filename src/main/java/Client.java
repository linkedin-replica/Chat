import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public class Client {
	
	private final String host;
	private final int port;
	
	public Client(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public void run() throws InterruptedException, IOException {
	
		EventLoopGroup group = new NioEventLoopGroup();
		
		try {
			Bootstrap bootstrap = new Bootstrap()
					.group(group)
					.channel(NioSocketChannel.class)
					.handler(new ChatClientInitilaizer());
			
			Channel channel = bootstrap.connect(host, port).sync().channel();
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			URL obj = new URL("http://localhost:8080/ws");
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			//	if(in.readLine() != null) {
				
				con.setRequestMethod("POST");
				con.setDoOutput(true);
				String msg = in.readLine();
				
				con.setRequestProperty("msg", msg);
				con.setRequestProperty("Connection", "Upgrade");
				con.setRequestProperty("Upgrade", "websocket");
				con.setRequestProperty("Content-Type", "text/plain");
			//}
				
	
				// success
				int responseCode = con.getResponseCode();
				System.out.println("POST Response Code :: " + responseCode);
				
				if (responseCode == HttpURLConnection.HTTP_OK) { 
					BufferedReader in11 = new BufferedReader(new InputStreamReader(
							con.getInputStream()));
					String inputLine1;
					StringBuffer response1 = new StringBuffer();

					while ((inputLine1 = in11.readLine()) != null) {
						response1.append(inputLine1);
						channel.write(in.readLine() + "\r\n");
					}
					
					in.close();
					//System.out.println(response.toString());
				} 
				else {
					System.out.println("POST request error");
				}
			//}
			
			
//			URL obj = new URL("http://localhost:8080/ws");
//			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//			con.setRequestMethod("GET");
//			con.addRequestProperty("Connection", "Upgrade");
//			con.addRequestProperty("Upgrade", "websocket");
//			con.setRequestProperty("Content-Type", "text/plain"); 
			
		}
		
		finally {
			group.shutdownGracefully();
		}
		
	}
	
	public static void main(String [] args) {
		try {
			new Client("localhost", 8080).run();
		} catch (InterruptedException e) {
			System.out.println("InterruptedException: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("InterruptedException: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	
}
