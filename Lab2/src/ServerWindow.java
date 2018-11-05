import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
/*
 * This is UI for Server
 */
public class ServerWindow {
	private JFrame serverFrame;
	private JButton startServerButton;
	private JButton stopServerButton;
	private JPanel serverPanel;
	private static JTextArea serverTextArea;
	private JScrollPane scroll;
	private static JList<String> connectedClients;
	private static Server server=null;
	private static DefaultListModel<String> clientList;
	
	public ServerWindow() {
		//Creating a Server instance
		server=new Server();
		//create buttons
		startServerButton=new JButton("Start Server");
		startServerButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						server.start_server();
						startServerButton.setEnabled(false);
					}
				}).start();
			}
		});
		stopServerButton=new JButton("Stop Server");
		stopServerButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						try {
							server.stop_server();
							serverTextArea.append("SERVER has stopped"+"\n");
							System.exit(0);
						} catch (IOException e1) {
							System.err.println("error occurred while stopping the server"+e1.getMessage());
							e1.printStackTrace();
						}
					}
				}).start();
			}
		});
		
		//create jtext area
		serverTextArea = new JTextArea(20,30);
		serverTextArea.setEditable(false);
		//create scroll for text area
	    scroll = new JScrollPane(serverTextArea);
	    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		//create a panel
		serverPanel = new JPanel();
		serverPanel.setBorder(new TitledBorder(new EtchedBorder(), "Display Area"));
		serverPanel.setForeground(Color.cyan);
		//Add buttons and scroll to panel
		serverPanel.add(startServerButton);
		serverPanel.add(stopServerButton);
		serverPanel.add(scroll);
		//create jframe
		serverFrame=new JFrame();
		serverFrame.setTitle("Server");
		serverFrame.setSize(500, 430);
		serverFrame.setLocationRelativeTo(null);
		serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		serverFrame.setResizable(false);
		//Add panel to jframe
		serverFrame.add(serverPanel);
		//show jframe in GUI
		serverFrame.setVisible(true);
	}
	public static void write(String str) throws IOException{
		if(str!=null){
			serverTextArea.append("---------------------\n"+str+"\n");
		}
	}
}
