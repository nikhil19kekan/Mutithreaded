package Client;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;
/*
 * This is UI for Client
 */
public class ClientWindow{
	public JFrame clientFrame;
	public JButton sendRandomNumberButton;
	public JButton sendClientNameButton;
	public JButton killClient;
	public JPanel clientPanel;
	public JTextPane clientPane;
	public static JTextArea clientTextArea;
	public JTextArea clientNameTextArea;
	public JTextPane clientMessagePane;
	public String client_name;
	public Client client=null;
	public Random rand=null;
	public ClientWindow() throws UnknownHostException, IOException, InterruptedException{
		rand=new Random();
		client=new Client();
		//Client Text Area
		clientTextArea = new JTextArea(20,50);
		clientTextArea.setEditable(false);
		clientTextArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);	
	
		clientNameTextArea = new JTextArea(5,50);
		clientNameTextArea.setEditable(true);
		clientNameTextArea.setText("");
		clientNameTextArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);		
		//create buttons for sending message
		sendRandomNumberButton=new JButton("Send Random Number");	
		sendRandomNumberButton.setEnabled(false);
		sendRandomNumberButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							if(client!=null && client.clientSocket.isConnected()){
								int n=rand.nextInt(11)+5;
								write("Requested server to SLEEP for:"+n);
								client.send(Integer.toString(n));
							}
						} catch (IOException e) {
							write("Error while sending random number to server");
							System.err.println("ClientWindow:Error while sending random number to server"+e.getMessage());
						}
					}
				}).start();
			}
		});
		sendClientNameButton=new JButton("Send Client Name");
		sendClientNameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try{
							if(clientNameTextArea.getText()=="" || clientNameTextArea.getText()==null){
								ClientWindow.write("Empty Name");
							}else{
								clientNameTextArea.setEditable(false);
								sendClientNameButton.setEnabled(false);
								sendRandomNumberButton.setEnabled(true);
								String name=clientNameTextArea.getText();
								client.tryConnecting(client.getHttpMessage(name));
							}
						}catch(Exception ex){
							write("Error in sending client name to server");
							System.err.println("ClientWindow:Error in sending client name to server"+ex.getMessage());
						}
					}
				}).start();
			}
		});
		//Create button
		killClient=new JButton("Kill Client");
		killClient.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					client.clientSocket.close();
				} catch (IOException e1) {
					System.err.println("error in killing client"+e1.getMessage());
				}
				System.exit(0);
			}
		});
		//create a panel
		clientPanel = new JPanel();
		clientPanel.setForeground(Color.cyan);
		//add buttons to panel
		clientPanel.add(clientTextArea);
		clientPanel.add(clientNameTextArea);
		clientPanel.add(sendRandomNumberButton);
		clientPanel.add(sendClientNameButton);
		clientPanel.add(killClient);
		//create jframe
		clientFrame=new JFrame();
		clientFrame.setTitle(this.client_name);
		clientFrame.setSize(600, 500);
		clientFrame.setLocationRelativeTo(null);
		clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		clientFrame.setResizable(false);
		//add panel
		clientFrame.add(clientPanel);
		clientFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		//show jframe in GUI
		clientFrame.setVisible(true);
		//start reading from connected server as a separate thread
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					try{
						Thread.sleep(2000);
						if(client!=null){
							write(client.read());
						}
					}catch (InterruptedException e) {
						System.err.println("ClientWindow:Error while reading from server"+e.getMessage());
					}
				}
			}
		}).start();
	}
	public static void write(String str){
		if(str!=null){
			clientTextArea.append(str+"\n");
		}
	}
}