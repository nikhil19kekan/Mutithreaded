package Client;

public class Main {
	public static void main(String[] args) {
		try{
		ClientWindow client = new ClientWindow();
		}catch(Exception e){
			System.err.println("Error occurred while creating client windows");
			e.printStackTrace();
		}
	}

}
