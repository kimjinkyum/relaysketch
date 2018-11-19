import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import java.awt.image.BufferedImage;

import java.io.BufferedInputStream;

import java.io.BufferedOutputStream;

import java.io.BufferedReader;

import java.io.DataInputStream;

import java.io.DataOutputStream;

import java.io.File;

import java.io.FileInputStream;

import java.io.FileOutputStream;

import java.io.IOException;

import java.io.InputStream;

import java.io.InputStreamReader;

import java.io.ObjectInputStream;

import java.io.ObjectOutputStream;

import java.io.OutputStream;

import java.io.PrintWriter;

import java.io.Serializable;

import java.net.Socket;

import java.net.UnknownHostException;

import java.util.HashSet;



import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
    
    JFrame frame= new JFrame();

   ImagePanel welcomePanel=new ImagePanel(new ImageIcon("C:\\Users\\user\\eclipse-workspace\\projectre\\src\\image\\black.jpg").getImage());
   

   JLabel timerLabel=new JLabel("TIMER");


    JPanel panel = new JPanel();

    JTextField textField = new JTextField(40);

    JTextArea messageArea = new JTextArea(30, 40);

    JButton button2=new JButton("Send");

    static int[] length=new int[20];

    OutputStream out2;

    FileInputStream fin;

    FileOutputStream fin2;

    InputStream in2;

    int wow=0;

    RelaySketch relay=new RelaySketch();

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
       
       frame.setSize(1280,720);
       frame.getContentPane().add(welcomePanel, BorderLayout.CENTER);
       
       frame.setResizable(false);//사이즈 조정x

      frame.setLocationRelativeTo(null);

        textField.setEditable(false);
        messageArea.setEditable(false);
 
        panel.add(textField, "North");

       panel.add(new JScrollPane(messageArea), "Center");

      panel.setBackground(Color.ORANGE);
      panel.setBounds(852, 102, 412, 579);
      welcomePanel.add(panel);
     welcomePanel.add(timerLabel,"NORTH");
          
      timerLabel.setForeground(Color.GRAY);
      timerLabel.setFont(new Font("Arial Rounded MT Bold", Font.BOLD | Font.ITALIC, 54));
      timerLabel.setBounds(555, 10, 462, 82);
      TimerThread th=new TimerThread(timerLabel);


         th.start();
      

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

     * Connects to the server then enters the processing loop.

     * @return 

     * @throws IOException 

     * @throws ClassNotFoundException 

     * @throws InterruptedException 

     * @throws UnknownHostException 

     */
  

    public void run() throws IOException, ClassNotFoundException, InterruptedException{

   
      
       String serverAddress=null ; 

             //getServerAddress();

       while(true) 

       {

          if(RelaySketch.check_IP==1) 

          {
        

             serverAddress=RelaySketch.IPADDRESS;

             break;

          }

          System.out.println();

          

       }
   
        // Make connection and initialize streams

        Socket socket = new Socket(serverAddress, 5880);

        

        in = new BufferedReader(new InputStreamReader(

                socket.getInputStream()));

        out = new PrintWriter(socket.getOutputStream(), true);

        

        //out.println(serverAddress);

               
      
       

        // Process all messages from server, according to the protocol.

        while (true) 

        { 

            String line = in.readLine();

            if (line.startsWith("SUBMITNAME")) 

            {
            

               while(true) 

               {

                  if(RelaySketch.check_name==1) 

                  {

                     out.println(RelaySketch.NAME);

                     break;

                  }

                  System.out.println();

               }    

            } else if (line.startsWith("NAMEACCEPTED")) {
           
                textField.setEditable(true);

            }

            else if(line.startsWith("SUBMITTEAM")) 

            {
         
               while(true) 

               {
                
                  if(RelaySketch.check_team==1) 

                  {
                
                     out.println(RelaySketch.TEAM);

                     break;

                  }

                  System.out.println();

               }          
                         
                  
            }
            else if(line.startsWith("GAMEFRAME")) {
            
               frame.setVisible(true);
                         
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

            else if(line.startsWith("<CANVAS>"))

            {

              Socket soc=new Socket(serverAddress,11111);

               System.out.println(":start!");

               out2=soc.getOutputStream();

               DataOutputStream dout=new DataOutputStream(out2);

               

                  fin= new FileInputStream(new File("C:\\2-2\\kkk.png"));

                  

                  byte[] buffer= new byte[5000];

                  int len;

                  int data=0;

                   int datas;

                     

                  while((len=fin.read(buffer))>0)

                  {

                     data++;

                  }

                  dout.writeInt(data);

                  datas=data;

                  System.out.println(data);

                  fin.close();

                  fin=new FileInputStream("C:\\2-2\\kkk.png");

                  len=0;

                  int i=0;

                  for(;data>0; data--)

                  {

                     len=fin.read(buffer);

                     out2.write(buffer,0,len);

                     out.println(len);

                    

                   }

               

               

            }

            else if(line.startsWith("<RECEIVE>"))

            {

               int count=0;

            Socket soc2=new Socket(serverAddress,22222);

                System.out.println(":start!");

                in2=soc2.getInputStream();

                DataInputStream dout1=new DataInputStream(in2);

                int len;

                int data=0;

                 data=dout1.readInt();

                 fin2= new FileOutputStream(new File("C:\\2-2\\kkkkk.png"));

                      DataOutputStream din=new DataOutputStream(soc2.getOutputStream());

                   byte[] buffer2= new byte[5000];

                  

                   System.out.println(data);

                   System.out.println("heyyo!");

                   int temp=data;

                   len=0;

                   for(;data>0; data--)

                   {

                      len=in2.read(buffer2);

                      fin2.write(buffer2,0,len);

                      String queue=in.readLine();

                      System.out.println("receive : "+len);

                      System.out.println("!");

                      System.out.println(queue);

                      if(Integer.toString(len).equals(queue))

                      {

                         count++;

                      }

                   }

                   System.out.println(count);

                   if(count==temp)

                  {   
                     System.out.println(count);

                    din.writeInt(1);

                    System.out.println("wow");                    

                  }

                   else

                   {

                      din.writeInt(2);

                   }

               }
            

            else if(line.startsWith("<out>"))

            {
               System.out.println("out");

               out2.close();

               System.out.println("out");

            }

            

           }

        }
    /**

     * Runs the client as an application with a closeable frame.

     */

    public static void main(String[] args) throws Exception {

        Client client = new Client();
     
     
        client.run();

    }

}
class TimerThread extends Thread{
   private JLabel timerLabel;//타이머 값이 출력될 레이블
   
   public TimerThread(JLabel timerLabel) {
      this.timerLabel = timerLabel; //타이머 카운트를 출력할 레이블
   }
   //스레드 코드 run()이 종료하면 스레드 종료
   public void run() {
      int n = 100;
      while(true) {
      
         timerLabel.setText(Integer.toString(n));
         n--;
         try {
            Thread.sleep(1000);//1초간격
         }catch(InterruptedException e) {
            e.printStackTrace();
         }
      }
   }
}