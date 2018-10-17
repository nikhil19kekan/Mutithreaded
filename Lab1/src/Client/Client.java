package Client;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

public class Client{
	static Socket clientSocket=null;
	DataInputStream di=null;
	DataOutputStream dout=null;
	public String clientName=null;
	/*
	 * This Method continually try's to establish connection with server
	 */
	public void tryConnecting(String name){
		ClientWindow.write("Trying to Establish Connection....");
		while(clientSocket==null || !clientSocket.isConnected()){
			try{
				clientSocket=new Socket("localhost", 4000);
				ClientWindow.write("Connected with server");
				dout=new DataOutputStream(clientSocket.getOutputStream());
				clientName=name;
				dout.writeUTF(clientName);
				dout.flush();
				ClientWindow.write("Client Name sent to server");
				di=new DataInputStream(clientSocket.getInputStream());
			}catch(Exception e){
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e1) {
					System.err.println("client:Error while trying connection to server"+e.getMessage());
				}
			}
		}
	}
	/*
	 * Reads from server
	 */
	public String read(){
		String str=null;
		try{
			str=di.readUTF();
		}catch(Exception e){
			System.err.println("error occurred while reading msg from server"+e.getMessage());
		}
	return str;
	}
	/*
	 * converts a normal string to a http formatted string
	 */
	public String getHttpMessage(String msg){
		String str="POST Http/1.1 \n" + "Host: www.uta.com \n" + "User-Agent: Mozilla/5.0 \n"
				+ "Content=type: application/x-www-form-urlencoded \n" + "Content-Length: " + msg.length() + " \n"
				+ "Date:" + new Date() + " \n" + "message:" + msg;
		return str;
	}
	/*
	 * Sends the message to server
	 */
	public void send(String str) throws IOException{
		if(clientSocket.isConnected()){
			dout.writeUTF(getHttpMessage(str));
			dout.flush();
		}else{
			ClientWindow.write("Server is DOWN:");
		}
	}
}
