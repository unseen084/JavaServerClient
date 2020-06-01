import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JTextField;



import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client implements MouseListener{

	static JFrame frame;
	static JFrame extraframe;
	static JTextField userNameArea;
	static JTextField IPField;
	static JTextArea textWritingArea;
	static JButton connectButton;
	static JButton disconnectButton;
	static JTextArea conversationArea;
	static JButton msgSendButton;
	static String username="",username1="",chat,checker = "Enter username";
	static final int port = 2000;
	static final String ip = "127.0.0.1";
	//private static final Component Scrollable = null;
	static Socket s;
	static PrintWriter writer;
	static BufferedReader reader;
	//static ArrayList<String> users = new ArrayList();
	static boolean isSocket = false;
	
	static boolean isuserNameTrue = false, isConnect = false;
	private JScrollBar scrollBar;

	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client window = new Client();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
		
	}
	public void ListenThread() 
    {
         Thread IncomingReader = new Thread(new IncomingReader());
         IncomingReader.start();
    }
    
    
    
    
    public void userRemove(String data) 
    {
         conversationArea.append(data + " is now offline.\n");
    }
    
    
    public void sendDisconnect() 
    {
        String bye = (username + ": :Disconnect");
        try
        {
            writer.println(bye); 
            writer.flush(); 
        } catch (Exception e) 
        {
            conversationArea.append("Could not send Disconnect message.\n");
        }
    }

    
    public void Disconnect() 
    {
        try 
        {
            conversationArea.append("Disconnected.\n");
            s.close();
        } catch(Exception ex) {
            conversationArea.append("Failed to disconnect. \n");
        }
        isConnect = false;
        userNameArea.setEditable(true);

    }
    public class IncomingReader implements Runnable
    {
        @Override
        public void run() 
        {
            String[] data;
            String stream, done = "Done", connect = "Connect", disconnect = "Disconnect", chat = "Chat";

            try 
            {
                while ((stream = reader.readLine()) != null) 
                {
                     data = stream.split(":");

                     if (data[2].equals(chat)) 
                     {
                        conversationArea.append(data[0] + ": " + data[1] + "\n");
                        //conversationArea.setCaretPosition(conversationArea.getDocument().getLength());
                     } 
                     else if (data[2].equals(connect))
                     {
                        conversationArea.removeAll();
                        //userAdd(data[0]);
                     } 
                     else if (data[2].equals(disconnect)) 
                     {
                         //userRemove(data[0]);
                     } 
                     else if (data[2].equals(done)) 
                     {
                        //users.setText("");
                        //writeUsers();
                        //users.clear();
                     }
                     
                }
           }catch(Exception ex) { }
        }
    }

	/**
	 * Create the application.
	 */
	public Client() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		
		frame.setResizable(false);
		frame.setBounds(100, 100, 578, 517);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		userNameArea = new JTextField();
		userNameArea.setText("Enter username");
		userNameArea.setBounds(10, 11, 189, 28);
		userNameArea.addMouseListener(this);
		frame.getContentPane().add(userNameArea);
		userNameArea.setColumns(10);
		
		connectButton = new JButton("Connect");
		connectButton.setBounds(209, 14, 89, 23);
		connectButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Start of extra frame work
				
				username = userNameArea.getText();
				if(username != null && username.equals(checker)!=true && username.length()>=4){
					isuserNameTrue = true;
					userNameArea.setEditable(false);
					if(isuserNameTrue == true){
						if(isConnect == false)
							isConnect = true;
					}else{
						extraframe = new JFrame();
						extraframe.setResizable(false);
						extraframe.setBounds(300, 250, 300, 150);
						extraframe.getContentPane().setLayout(new FlowLayout());
						JTextField tf = new JTextField(25);
						tf.setText("Enter username first!");
						tf.setEditable(false);
						extraframe.getContentPane().add(tf);
						extraframe.setVisible(true);
					}
					//End of another frame work
					
					if(isConnect == true ){
						
						
						
						try {
							s = new Socket(ip, port);
			                InputStreamReader streamreader = new InputStreamReader(s.getInputStream());
			                reader = new BufferedReader(streamreader);
			                writer = new PrintWriter(s.getOutputStream());
			                writer.println(username + ":has connected.:Connect");
			                writer.flush(); 
				                 
				            
				            
						} catch (IOException e) {
							extraframe = new JFrame();
							extraframe.setResizable(false);
							extraframe.setBounds(300, 250, 300, 150);
							extraframe.getContentPane().setLayout(new FlowLayout());
							JTextField tf = new JTextField(25);
							tf.setText("Try again! Something went wrong! :/");
							tf.setEditable(false);
							extraframe.getContentPane().add(tf);
							extraframe.setVisible(true);
							
						}
						ListenThread();
						
					}
				}else if(username.length()<4){
					extraframe = new JFrame();
					extraframe.setResizable(false);
					extraframe.setBounds(300, 250, 300, 150);
					extraframe.getContentPane().setLayout(new FlowLayout());
					JTextField tf = new JTextField(25);
					tf.setText("username must be at least 4 letters!");
					tf.setEditable(false);
					extraframe.getContentPane().add(tf);
					extraframe.setVisible(true);
				}else{
					extraframe = new JFrame();
					extraframe.setResizable(false);
					extraframe.setBounds(300, 250, 300, 150);
					extraframe.getContentPane().setLayout(new FlowLayout());
					JTextField tf = new JTextField(25);
					tf.setText("Try again! Something went wrong! :/");
					tf.setEditable(false);
					extraframe.getContentPane().add(tf);
					extraframe.setVisible(true);
				}
				
				
				
			}
		});
		frame.getContentPane().add(connectButton);
		
		disconnectButton = new JButton("Disconnect");
		disconnectButton.setBounds(308, 14, 100, 23);
		disconnectButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sendDisconnect();
				Disconnect();
				
			}
		});
		frame.getContentPane().add(disconnectButton);
		
		conversationArea = new JTextArea();
		conversationArea.setLineWrap(true);
		conversationArea.setBounds(10, 50, 544, 280);
		conversationArea.setEditable(false);
		frame.getContentPane().add(conversationArea);
		
		IPField = new JTextField();
		IPField.setText("localhost");
		IPField.setBounds(10, 438, 189, 28);
		IPField.setEditable(false);
		frame.getContentPane().add(IPField);
		IPField.setColumns(10);
		
		msgSendButton = new JButton("Send");
		msgSendButton.setBounds(360, 438, 194, 28);
		msgSendButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				String nothing = "";
		        if ((textWritingArea.getText()).equals(nothing)) {
		            textWritingArea.setText("");
		            textWritingArea.requestFocus();
		        } else {
		            try {
		               writer.println(username + ":" + textWritingArea.getText() + ":" + "Chat");
		               writer.flush(); // flushes the buffer
		            } catch (Exception ex) {
		                conversationArea.append("Message was not sent. \n");
		            }
		            textWritingArea.setText("");
		            textWritingArea.requestFocus();
		        }

		        textWritingArea.setText("");
		        textWritingArea.requestFocus();
				
				
				
			}
		});
		frame.getContentPane().add(msgSendButton);
		
		textWritingArea = new JTextArea();
		textWritingArea.setText("Write message");
		textWritingArea.setLineWrap(true);
		textWritingArea.setBounds(10, 341, 544, 89);
		textWritingArea.addMouseListener(this);
		textWritingArea.setEditable(true);
		frame.getContentPane().add(textWritingArea);
		
		JLabel lblNewLabel = new JLabel("LAN Chat System");
		lblNewLabel.setBounds(224, 445, 126, 21);
		frame.getContentPane().add(lblNewLabel);
		
		
		
		
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		if(arg0.getSource() == userNameArea){
			if(userNameArea.isEditable() == true){
				userNameArea.setText("");
			}
		}else if(arg0.getSource() == textWritingArea){
			if(textWritingArea.isEditable() == true){
				textWritingArea.setText("");
			}
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
