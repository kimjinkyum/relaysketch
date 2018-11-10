import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.plaf.synth.SynthSeparatorUI;

/**
 * A simple Swing-based client for the chat server.  Graphically
 * it is a frame with a text field for entering messages and a
 * textarea to see the whole dialog.
 *
 * The client follows the Chat Protocol which is as follows.
 * When the server sends "SUBMITNAME" the client replies with the
 * desired screen name.  The server will keep sending "SUBMITNAME"
 * requests as long as the client submits screen names that are
 * already in use.  When the server sends a line beginning
 * with "NAMEACCEPTED" the client is now allowed to start
 * sending the server arbitrary strings to be broadcast to all
 * chatters connected to the server.  When the server sends a
 * line beginning with "MESSAGE " then all characters following
 * this string should be displayed in its message area.
 */

public class Client {

    BufferedReader in;
    PrintWriter out;
    JFrame frame = new JFrame("Chatter");
    JTextField textField = new JTextField(40);
    JTextArea messageArea = new JTextArea(8, 40);
    JButton button2=new JButton("Send");

    public static int check=0;
    /**
     * Constructs the client by laying out the GUI and registering a
     * listener with the textfield so that pressing Return in the
     * listener sends the textfield contents to the server.  Note
     * however that the textfield is initially NOT editable, and
     * only becomes editable AFTER the client receives the NAMEACCEPTED
     * message from the server.
     */
    public Client() {

        // Layout GUI
        textField.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField, "North");
       frame.getContentPane().add(new JScrollPane(messageArea), "Center");
        frame.pack();
        
        
        // Add Listeners
        textField.addActionListener(new ActionListener() {
            /**
             * Responds to pressing the enter key in the textfield by sending
             * the contents of the text field to the server.    Then clear
             * the text area in preparation for the next message.
             */
            public void actionPerformed(ActionEvent e) 
            {
                out.println(textField.getText());
                textField.setText("");
            }
        });
       
    }
	   
    /**
     * Prompt for and return the address of the server.
     */
    private String getServerAddress() {
        return JOptionPane.showInputDialog(
            frame,
            "Enter IP Address of the Server:",
            "Welcome to the Chatter",
            JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * Prompt for and return the desired screen name.
     */
    private String getName() {
        return JOptionPane.showInputDialog(
            frame,
            "Choose a screen name:",
            "Screen name selection",
            JOptionPane.PLAIN_MESSAGE);
    }
    
    
    private String getTeam() 
    {
        return JOptionPane.showInputDialog(
                frame,
                "Choose a team name(A,B):",
                "Screen team selection",
                JOptionPane.PLAIN_MESSAGE);
     }
    /**
     * Prompt for and return the desired name to whisper.
     */
    private String getWhisperName() {
        return JOptionPane.showInputDialog(
            frame,
            "Choose a name to whisper:",
            "whisper",
            JOptionPane.PLAIN_MESSAGE);
    }
    
    /**
     * Connects to the server then enters the processing loop.
     * @return 
     * @throws IOException 
     * @throws ClassNotFoundException 
     * @throws UnknownHostException 
     */

    public void run() throws IOException, ClassNotFoundException{

        // Make connection and initialize streams
        String serverAddress = getServerAddress();
        
        Socket socket = new Socket(serverAddress, 5880);
        in = new BufferedReader(new InputStreamReader(
            socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        
      	       
        
        // Process all messages from server, according to the protocol.
        while (true) 
        {  
        
        
            String line = in.readLine();
            
        
        
            if (line.startsWith("SUBMITNAME")) {
                out.println(getName());
            } else if (line.startsWith("NAMEACCEPTED")) {
                textField.setEditable(true);
            }
            else if(line.startsWith("SUBMITTEAM")) 
            {
            	out.println(getTeam());
            }
            
            else if (line.startsWith("MESSAGE")) {
            	messageArea.append(line.substring(8) + "\n");
            }
            else if(line.startsWith("SEND"))
            {
            	UserRepaint b=new UserRepaint();
            }
            else if(line.startsWith("<START>"))
            {  
            	while(check==0) {
           
              System.out.println(check);
                if(check==1) {
                	System.out.println("in "+check);
                		
                    out.println("<send>");
                    System.out.println(check);
                   
                    break;    
                 	}
                else
                	continue;
                }
               	
            }
           }
        
        }

    
   

    /**
     * Runs the client as an application with a closeable frame.
     */
    public static void main(String[] args) throws Exception {
        Client client = new Client();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();
    }
}