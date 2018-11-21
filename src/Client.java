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


    JLabel label = new JLabel("WAIT");
    JLabel label_1;
    BufferedReader in;

    static PrintWriter out;
    
    JFrame frame= new JFrame();

    static ImagePanel welcomePanel=new ImagePanel(new ImageIcon("C:\\2-2\\black.jpg").getImage());

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
    static int timercheck=0;
    /**

     * Constructs the client by laying out the GUI and registering a

     * listener with the textfield so that pressing Return in the

     * listener sends the textfield contents to the server.  Note

     * however that the textfield is initially NOT editable, and

     * only becomes editable AFTER the client receives the NAMEACCEPTED

     * message from the server.

     */
    private String getanswer() {
        return JOptionPane.showInputDialog(
            frame,
            "Enter your word you think it's answer ※no capital :",
            "ANSWER",
            JOptionPane.QUESTION_MESSAGE);
    };//정답판넬

    public Client() {
        // Layout GUI
       
       frame.setSize(1280,720);
       frame.getContentPane().add(welcomePanel, BorderLayout.CENTER);
       
       frame.setResizable(false);//사이즈 조정x

      frame.setLocationRelativeTo(null);
 
        textField.setEditable(false);
 
        panel.add(textField, "North");
       panel.add(messageArea);
       messageArea.setEditable(false);

       panel.add(new JScrollPane(), "Center");

      panel.setBackground(Color.ORANGE);
      panel.setBounds(910, 120, 354, 561);
      welcomePanel.add(panel);
     welcomePanel.add(timerLabel,"NORTH");
          
      timerLabel.setForeground(Color.GRAY);
      timerLabel.setFont(new Font("Arial Rounded MT Bold", Font.BOLD | Font.ITALIC, 54));
      timerLabel.setBounds(554, 10, 462, 82);
      
      label.setForeground(Color.RED);
      label.setFont(new Font("Arial Rounded MT Bold", Font.BOLD | Font.ITALIC, 54));
      label.setBounds(981, 26, 204, 82);
      welcomePanel.add(label);
      
      label_1 = new JLabel("Word");
      label_1.setForeground(Color.GRAY);
      label_1.setFont(new Font("Arial Rounded MT Bold", Font.BOLD | Font.ITALIC, 54));
      label_1.setBounds(114, 53, 226, 82);
     welcomePanel.add(label_1);
     
     

        

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

 

       while(true) 

       {

          if(RelaySketch.check_IP==1) 

          {
        

             serverAddress=RelaySketch.IPADDRESS;

             break;

          }

          System.out.print("");

          

       }
   
        // Make connection and initialize streams

        Socket socket = new Socket(serverAddress, 5880);

        

        in = new BufferedReader(new InputStreamReader(

                socket.getInputStream()));

        out = new PrintWriter(socket.getOutputStream(), true);

        

               
      
       

        // Process all messages from server, according to the protocol.

        while (true) 

        { 
           UserRepaint b=null;
            String line = in.readLine();

            if (line.startsWith("<SUBMITNAME>")) 

            {
            

               while(true) 

               {

                  if(RelaySketch.check_name==1) 

                  {

                     out.println(RelaySketch.NAME);

                     break;

                  }

                  System.out.print("");

               }    

            } else if (line.startsWith("<NAMEACCEPTED>")) {
           
                textField.setEditable(true);

            }

            else if(line.startsWith("<SUBMITTEAM>")) 

            {
         
               while(true) 

               {
                
                  if(RelaySketch.check_team==1) 

                  {
                
                     out.println(RelaySketch.TEAM);

                     break;

                  }

                  System.out.print("");

               }          
                         
                  
            }
            else if(line.startsWith("<GAMEFRAME>")) {
            
               frame.setVisible(true);
                         
            }
            

            else if (line.startsWith("MESSAGE")) {

               messageArea.append(line.substring(8) + "\n");

            }

            else if(line.startsWith("<SEND>"))

            {

               b=new UserRepaint();

            }

            else if(line.startsWith("<START>"))

            {  

               while(check==0) {

           

              System.out.print("");

                if(check==1) {

                   System.out.println("in "+check);

                      

                    out.println("<send>");

                    System.out.print("");

                   

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

               

                  fin= new FileInputStream(new File("C:\\\\2-2\\\\client_paint.png"));

                  

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

                  fin=new FileInputStream("C:\\2-2\\client_paint.png");

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

                 fin2= new FileOutputStream(new File("C:\\2-2\\client_get.png"));

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
            else if(line.startsWith("<ALLIN>"))
            {
               System.out.println("6 people in");
                TimerThread th=new TimerThread(timerLabel);
                  label.setText("3");
                   Thread.sleep(1000);
                   label.setText("2");
                   Thread.sleep(1000);
                   label.setText("1");
                   Thread.sleep(1000);
                   label.setText("START");
                  
                
                 Thread.sleep(1000);
                th.start();
                    
            }
            else if(line.startsWith("<GIVEWORD>"))
                  {
               String givenword =line.substring(10);
               label_1.setText(givenword);
                  }//서버에서 단어받아오기
          
            else if(line.startsWith("<ANSWERSHEET>"))
            {
               out.println(getanswer());
            }
            else if(line.startsWith("<SEQUENCE>"))
            {
               String seqnum=line.substring(10);
               System.out.println(seqnum);
               label.setText(seqnum);
            }
           

           }
          //여기다가 점수들 띄워주는 창 올려봐  
        
        
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
      while(n>=0) {
      
         timerLabel.setText(Integer.toString(n));
        
         if(n==0)
         {   
            System.out.println("hello");
            UserRepaint.isend=1;
            break;
         }
         else
            n--; 
         try {
            Thread.sleep(1000);//1초간격
         }catch(InterruptedException e) {
            e.printStackTrace();
         }
      }
   }
}
