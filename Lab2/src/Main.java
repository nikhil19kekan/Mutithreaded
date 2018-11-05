import java.io.IOException;

public class Main {
	public static ServerWindow serverWindow=null;
	public static void main(String[] args) throws IOException, InterruptedException {
		serverWindow = new ServerWindow();
		System.out.println("MAIN: all clients and server created");
	}
}
