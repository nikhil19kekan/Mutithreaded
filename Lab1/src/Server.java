import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server{
	private ServerSocket server=null;
	Map<Socket,String> clientsConnected=null;
	private ExecutorService executor=null;
	/*
	 * Contructor
	 */
	Server(){
		clientsConnected=new HashMap<Socket,String>();
	}
	/*
	 * Accepts connections from clients continually till the server is UP(max 10 clients)
	 */
	public void acceptConnection(){
		Socket client=null;
		try{
			executor=Executors.newFixedThreadPool(10);
			while(server.isBound()){
				client=server.accept();
				DataInputStream di= new DataInputStream(client.getInputStream());
				String msg=di.readUTF();
				clientsConnected.put(client, getMessage(msg));
				ServerWindow.write(msg);
				Runnable service= new ServerService(client,getMessage(msg));
				executor.execute(service);
			}
		}catch(Exception e){
			System.err.println("Server:error while accepting connections"+e.getMessage());
		}
	}
	/*
	 * This method takes out actual message from http format
	 */
	public String getMessage(String str){
		return str.substring(str.indexOf("message:")+8, str.length());
	}
	/*
	 * Starts the server listening to port 4000
	 */
	public void start_server(){
		try{
			if(server==null || !server.isBound()){
				server = new ServerSocket(4000);
			}
			acceptConnection();
		}catch(Exception e){
			System.err.println("Server:error occurred while server start"+e.getMessage());
		}
	}
	/*
	 * Closes client sockets of every connected client, shuts down the thread executor that serves clients
	 */
	public void stop_server() throws IOException{
		Iterator it=clientsConnected.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry e= (Map.Entry)it.next();
			Socket toBeClosed=(Socket)e.getKey();
			toBeClosed.close();
		}
		executor.shutdownNow();
		server.close();
	}
}