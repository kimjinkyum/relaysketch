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
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.plaf.synth.SynthSeparatorUI;
/**
 * A simple Swing-based client for the chat server.  Graphically
 * it is a frame with a text field for entering messages and a
 * textarea to see the whole dialog.
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
    JLabel label_category;
    
    BufferedReader in;
    static PrintWriter out;
    
    JFrame frame= new JFrame();

    static ImagePanel welcomePanel=new ImagePanel(new ImageIcon("C:\\Users\\정은서\\eclipse-workspace\\Project_Socket\\src\\image\\black.jpg").getImage());//찬빈
    //static ImagePanel welcomePanel=new ImagePanel(new ImageIcon("C:\\Users\\김진겸\\eclipse-workspace\\Network\\image\\black.jpg").getImage());//진겸


   JLabel timerLabel=new JLabel("TIMER");

   JLabel label_word=new JLabel("Word");//진겸

    JPanel panel = new JPanel();

    JTextField textField = new JTextField(40);

    JTextArea messageArea = new JTextArea(30, 40);
    
   
    
    
    static String seqnum="";

    JButton button2=new JButton("Send");

    JPanel backgrounda = new JPanel();

    

    JPanel backgroundb = new JPanel();

    

    JLabel namelist_a = new JLabel("A team"); 
    JLabel namelist_b = new JLabel("B team"); 
    static int[] length=new int[20];
    OutputStream out2;
    FileInputStream fin;
    FileOutputStream fin2;
    InputStream in2;
    JLabel label_score_a=new JLabel("0:0");
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



    private void correct_answer()
    {
       JOptionPane.showMessageDialog(frame, "The answer is correct!");
    }



    private void wrong_answer(String word) 
    {
       System.out.println("hi");
       JOptionPane.showMessageDialog(frame,"answer is "+word,"The answer is worng",JOptionPane.PLAIN_MESSAGE);

    }

    private String getanswer() {



        return JOptionPane.showInputDialog(
            frame,
            "Enter your word you think it's answer ※no capital :",
            "ANSWER",
            JOptionPane.QUESTION_MESSAGE);

    };//정답판넬//진겸
    
    public Client() {
        // Layout GUI
      frame.setSize(1280,720);
      frame.getContentPane().add(welcomePanel, BorderLayout.NORTH);
      frame.setResizable(false);//사이즈 조정x
      frame.setLocationRelativeTo(null);

      textField.setEditable(false);
      
      
      panel.add(textField, "North");
      panel.add(new JScrollPane(messageArea));//은서
      panel.setBackground(Color.ORANGE);
      panel.setBounds(852, 102, 412, 579);
      
      welcomePanel.add(panel);
      welcomePanel.add(timerLabel,"NORTH");
      messageArea.setEditable(false);
      messageArea.setLineWrap(true); //은서
    
      timerLabel.setForeground(Color.GRAY);
      timerLabel.setFont(new Font("Arial Rounded MT Bold", Font.BOLD | Font.ITALIC, 54));
      timerLabel.setBounds(554, 10, 462, 82);



      



      label.setForeground(Color.RED);



      label.setFont(new Font("Arial Rounded MT Bold", Font.BOLD | Font.ITALIC, 54));



      label.setBounds(981, 26, 204, 82);

      welcomePanel.add(label);

      label_category = new JLabel("Category");
      label_category.setForeground(Color.GRAY);



      label_category.setFont(new Font("Arial Rounded MT Bold", Font.BOLD | Font.ITALIC, 45));



      label_category.setBounds(14, 10, 266, 68);



      welcomePanel.add(label_category);//진겸



      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //은서




      label_word.setForeground(Color.GRAY);



      label_word.setFont(new Font("Arial Rounded MT Bold", Font.BOLD | Font.ITALIC, 40));



      label_word.setBounds(14,90,266,56);



      welcomePanel.add(label_word);

      

      welcomePanel.add(backgrounda);

      backgrounda.setBackground(Color.WHITE);

      backgrounda.setBounds(550,290,250,172);

      backgrounda.add(namelist_a);

      namelist_a.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 13));

      

      welcomePanel.add(backgroundb);

      backgroundb.setBackground(Color.WHITE);

      backgroundb.setBounds(550,490,250,172);

      backgroundb.add(namelist_b);

      namelist_b.setFont(new Font("Arial Rounded MT Bold", Font.BOLD , 13));

      



      label_score_a.setForeground(Color.GRAY);



      label_score_a.setFont(new Font("Arial Rounded MT Bold", Font.BOLD | Font.ITALIC, 50));



      label_score_a.setBounds(547,86,103,56);



      welcomePanel.add(label_score_a);

      

      JButton PassButton = new JButton("PASS");

      PassButton.addActionListener(new ActionListener() {

      	public void actionPerformed(ActionEvent arg0) {

      		out.println("<PASS>");

      	}

      });

      PassButton.setBounds(364, 102, 105, 27);

      welcomePanel.add(PassButton);



      label_score_a.setVisible(false);



      label_word.setVisible(false);//진겸



      



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







            if (line.startsWith("<SUBMITNAME>")) {

             while(true) 

             {

                  if(RelaySketch.check_name==1) 



                  {



                     out.println(RelaySketch.NAME);

                     break;



                  }//if(check==name 끝



                  System.out.print("");



              }//while 끝    

             







            } 

            else if(line.startsWith("<TEAMVIEW>")) //팀보여주기

            {
               namelist_a.setText("A team");
               namelist_b.setText("B team"); 

               String many=in.readLine(); //몇 명들어왔는지
               System.out.println(many+"!!!!!!!!!!!!!!!");
               
               int acount=0,bcount=0;

               String[] namelist_A = {"","",""}; //초기화

               String[] namelist_B = {"","",""};

               System.out.println(line+"------"); //teamview 


               for(int i=0; i< Integer.parseInt(many); i++) {

                  String names=in.readLine();
                  System.out.println(names+"여기");
                  
                  if(names.charAt(0)=='<'&&names.charAt(2)=='>') {//처음 들어왔을 때
                	  
                	  System.out.println("hihi");

                	  if(names.substring(0,3).equals("<A>")) {

                		  namelist_A[i-bcount]=names;      

                		  acount++;
               	  }  

                  else if(names.substring(0,3).equals("<B>")) {

                	  namelist_B[i-acount]=names;

                	  bcount++;

                  }

                    System.out.println("hihi");

                  }

               }//for문 끝

               
               String astring="",bstring="";

                namelist_a.setText("<html>"+namelist_a.getText()+"<br>"+namelist_A[0]+"<br>"+namelist_A[1]+"<br>"+namelist_A[2]+"</html>");
                namelist_b.setText("<html>"+namelist_b.getText()+"<br>"+namelist_B[0]+"<br>"+namelist_B[1]+"<br>"+namelist_B[2]+"</html>");

            } //teamview 끝

          
            else if (line.startsWith("<NAMEACCEPTED>")) {

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
            	
            	if(line.substring(8)==".*.") { //퇴장
            		String name=in.readLine();
            		
            		/*String many=in.readLine(); //몇 명들어왔는지
                    System.out.println(many+"!!!!!!!!!!!!!!!");
                    */
                    
            		/*for(int i=0; i< Integer.parseInt(many); i++) {

                       String names=in.readLine();
                       System.out.println(names+"여기");
                       
                       if(names.charAt(0)=='<'&&names.charAt(2)=='>') {//처음 들어왔을 때
                     	  
                     	  System.out.println("hihi");

                     	  if(names.substring(0,3).equals("<A>")) {

                     		  namelist_A[i-bcount]=names;      

                     		  acount++;
                    	  }  

                       else if(names.substring(0,3).equals("<B>")) {

                     	  namelist_B[i-acount]=names;

                     	  bcount++;

                       }

                         System.out.println("hihi");

                       }

                    }//for문 끝

                    
                    String astring="",bstring="";

                     namelist_a.setText("<html>"+namelist_a.getText()+"<br>"+namelist_A[0]+"<br>"+namelist_A[1]+"<br>"+namelist_A[2]+"</html>");
                     namelist_b.setText("<html>"+namelist_b.getText()+"<br>"+namelist_B[0]+"<br>"+namelist_B[1]+"<br>"+namelist_B[2]+"</html>");
*/
            	}
            }

            else if(line.startsWith("<SEND>")) {

                Image showImages = new ImageIcon("C:\\2-2\\client_get.png").getImage();//소영
                Image scaledImage =showImages.getScaledInstance(400,500,Image.SCALE_DEFAULT);//소영
                ImagePanel showImage=new ImagePanel(new ImageIcon(scaledImage).getImage());//소영



                //showImage.setSize(400,500);//소영

                showImage.setBounds(12, 218, 444, 372);//소영





                welcomePanel.add(showImage);//소영





               b=new UserRepaint();





               //  frame_drawing.setVisible(true);//소영

   

              

                 //frame.add(panel_drawing,"WEST");//소영





            }







            else if(line.startsWith("<START>"))







            {  







               while(check==0) {



              System.out.print("");

              if(seqnum.equals("1")) {

              String queue=in.readLine();

              out.println("nothing");// prevent waiting input from server

              System.out.println(queue);

              if(queue.startsWith("<GIVEWORD>")) // if pass button pushed



              {



                 String word;



                 word=in.readLine();

                System.out.println("word : "+word);

                 label_word.setText(word);



                 label_word.setVisible(true); //change the word in the given category



              }//진겸

              }

             



                if(check==1) {



                   

                   System.out.println("in "+check);



                    out.println("<send>");

                 





                    System.out.print("");







                   







                    break;    







                    }







                else







                   continue;







                }







                  check=0;







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

            else if(line.startsWith("<BCANVAS>"))







            {







              Socket soc=new Socket(serverAddress,33333);







               System.out.println(":start!");







               out2=soc.getOutputStream();







               DataOutputStream dout=new DataOutputStream(out2);







               







                  fin= new FileInputStream(new File("C:\\2-2\\client_paint.png"));







                  







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







            else if(line.startsWith("<BRECEIVE>"))







            {







               int count=0;







                Socket soc2=new Socket(serverAddress,44444);







                System.out.println(":start!");







                in2=soc2.getInputStream();







                DataInputStream dout1=new DataInputStream(in2);







                int len;







                int data=0;







                 data=dout1.readInt();







                 fin2= new FileOutputStream(new File("C:\\2-2\\client_getB.png"));







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



                label_score_a.setVisible(true);



                    



            }



            else if(line.startsWith("<GIVEWORD>")) 



            {



               String word;



               word=in.readLine();

              System.out.println("word : "+word);

               label_word.setText(word);



               label_word.setVisible(true);



            }//진겸



            else if(line.startsWith("<GIVECATEGORY>"))



                  {



                  String givencategory;



                  givencategory=in.readLine();



                     label_category.setText(givencategory);



                  }//서버에서 단어받아오기,//진겸

            

            else if(line.startsWith("<SCORE>")) 

            {

               

               String score;



                score=in.readLine();



                label_score_a.setText(score);



               

            }



          



            else if(line.startsWith("<AANSWERSHEET>"))



            {



               System.out.println("END");



               String q=getanswer();



               System.out.println(q);



               out.println("<Aanswer>"+q);



               System.out.println("OUT");







               String result;



               result=in.readLine();



               if(result.startsWith("<ANSWERCORRECT>")) 



               {



                  correct_answer();

               }



               else if(result.startsWith("<ANSWERWRONG>"))



               {

                  wrong_answer(result.substring(13));



               }







            }//진겸



            else if(line.startsWith("<BANSWERSHEET>"))



            {



               System.out.println("END");



               String q=getanswer();



               System.out.println(q);



               out.println("<Banswer>"+q);



               System.out.println("OUT");







               String result;



               result=in.readLine();



               if(result.startsWith("<ANSWERCORRECT>")) 



               {



                  correct_answer();



               }



               else if(result.startsWith("<ANSWERWRONG>"))



               {



                  wrong_answer(result.substring(13));



                }







            }//진겸







            



            else if(line.startsWith("<SEQUENCE>"))



            {



               seqnum=line.substring(10);



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