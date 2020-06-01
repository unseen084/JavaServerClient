import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.net.*;
import java.io.*;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.util.*;
public class Server implements MouseListener {

	static JFrame frmServer;
	static JFrame extraframe;
	static final int port = 2000;
	static JTextArea conversationArea;
	static JLabel lblNewLabel;
	static JTextArea OnlineUserArea;
	static String check="Enter Port",username1,chat;
	static BufferedReader br ;
	static ServerSocket ss;
	static Socket s;
	static BufferedWriter bw;
	static boolean isSocket = false;
	
	static boolean isPort = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Server server = new Server();
				server.start();
				
				
			}
		});
		
		
	}
	ArrayList clientOutputStreams;
	ArrayList<String> users;
	private JButton btnNewButton;

	public class ClientHandler implements Runnable	
	{
	       BufferedReader reader;
	       Socket sock;
	       PrintWriter client;

	       public ClientHandler(Socket clientSocket, PrintWriter user) 
	       {
	            client = user;
	            try 
	            {
	                sock = clientSocket;
	                InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
	                reader = new BufferedReader(isReader);
	            }
	            catch (Exception ex) 
	            {
	                conversationArea.append("Unexpected error... \n");
	            }

	       }

	       @Override
	       public void run() 
	       {
	            String message, connect = "Connect", disconnect = "Disconnect", chat = "Chat" ;
	            String[] data;

	            try 
	            {
	                while ((message = reader.readLine()) != null) 
	                {
	                    data = message.split(":");
	                    
	                    for (String token:data) 
	                    {
	                        conversationArea.append(token + "\n");
	                    }

	                    if (data[2].equals(connect)) 
	                    {
	                        tellEveryone((data[0] + ":" + data[1] + ":" + chat));
	                        userAdd(data[0]);
	                    } 
	                    else if (data[2].equals(disconnect)) 
	                    {
	                        tellEveryone((data[0] + ":has disconnected." + ":" + chat));
	                        userRemove(data[0]);
	                    } 
	                    else if (data[2].equals(chat)) 
	                    {
	                        tellEveryone(message);
	                    } 
	                    else 
	                    {
	                        conversationArea.append("No Conditions were met. \n");
	                    }
	                    onlineusers();
	                } 
	             } 
	             catch (Exception ex) 
	             {
	                conversationArea.append("Lost a connection. \n");
	                ex.printStackTrace();
	                clientOutputStreams.remove(client);
	             }
	            
	       } 
	}
	public void start(){
		Thread starter = new Thread(new ServerStart());
        starter.start();
	}
	
	public Server() {
		
		initialize();
		
	}
	public class ServerStart implements Runnable 
    {
        @Override
        public void run() 
        {
            clientOutputStreams = new ArrayList();
            users = new ArrayList();  

            try 
            {
                ServerSocket serverSock = new ServerSocket(port);

                while (true) 
                {
				Socket clientSock = serverSock.accept();
				PrintWriter writer = new PrintWriter(clientSock.getOutputStream());
				clientOutputStreams.add(writer);

				Thread listener = new Thread(new ClientHandler(clientSock, writer));
				listener.start();
				conversationArea.append("Got a connection. \n");
                }
            }
            catch (Exception ex)
            {
                conversationArea.append("Error making a connection. \n");
            }
        }
        
    }
    
    public void userAdd (String data) 
    {
        String message, add = ": :Connect", done = "Server: :Done", name = data;
        users.add(name);
        String[] tempList = new String[(users.size())];
        users.toArray(tempList);

        for (String token:tempList) 
        {
            message = (token + add);
            tellEveryone(message);
        }
        tellEveryone(done);
    }
    
    public void userRemove (String data) 
    {
        String message, add = ": :Connect", done = "Server: :Done", name = data;
        users.remove(name);
        String[] tempList = new String[(users.size())];
        users.toArray(tempList);

        for (String token:tempList) 
        {
            message = (token + add);
            tellEveryone(message);
        }
        tellEveryone(done);
    }
    
    public void tellEveryone(String message) 
    {
	Iterator it = clientOutputStreams.iterator();

        while (it.hasNext()) 
        {
            try 
            {
                PrintWriter writer = (PrintWriter) it.next();
                writer.println(message);
                writer.flush();
                
            } 
            catch (Exception ex) 
            {
            	//conversationArea.append("Error telling everyone. \n");
            }
        } 
    }
    public void onlineusers(){
    	OnlineUserArea.setText("");
    	for(int i=0; i<users.size() ; i++){
    		OnlineUserArea.append(users.get(i)+"\n");
    	}
    }
	private void initialize() {
		frmServer = new JFrame();
		frmServer.setTitle("Server");
		frmServer.setResizable(false);
		frmServer.setBounds(100, 100, 655, 466);
		frmServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmServer.getContentPane().setLayout(null);
		frmServer.setVisible(true);
		
		conversationArea = new JTextArea();
		conversationArea.setBounds(23, 43, 412, 331);
		conversationArea.setEditable(false);
		frmServer.getContentPane().add(conversationArea);
		
		lblNewLabel = new JLabel("Online Users");
		lblNewLabel.setBounds(491, 18, 89, 14);
		frmServer.getContentPane().add(lblNewLabel);
		
		OnlineUserArea = new JTextArea();
		OnlineUserArea.setBounds(445, 43, 182, 331);
		OnlineUserArea.setEditable(false);
		frmServer.getContentPane().add(OnlineUserArea);
		
		btnNewButton = new JButton("Clear Text");
		btnNewButton.setBounds(23, 390, 142, 36);
		btnNewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				conversationArea.setText("");
				
			}
		});
		frmServer.getContentPane().add(btnNewButton);
		
		
		
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
