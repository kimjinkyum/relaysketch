import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Container;
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
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.synth.SynthSeparatorUI;

/**
 * 
 * 
 * 
 * A simple Swing-based client for the chat server. Graphically
 * 
 * 
 * 
 * it is a frame with a text field for entering messages and a
 * 
 * 
 * 
 * textarea to see the whole dialog.
 *
 * 
 * 
 * 
 * 
 * 
 * 
 * The client follows the Chat Protocol which is as follows.
 * 
 * 
 * 
 * When the server sends "SUBMITNAME" the client replies with the
 * 
 * 
 * 
 * desired screen name. The server will keep sending "SUBMITNAME"
 * 
 * 
 * 
 * requests as long as the client submits screen names that are
 * 
 * 
 * 
 * already in use. When the server sends a line beginning
 * 
 * 
 * 
 * with "NAMEACCEPTED" the client is now allowed to start
 * 
 * 
 * 
 * sending the server arbitrary strings to be broadcast to all
 * 
 * 
 * 
 * chatters connected to the server. When the server sends a
 * 
 * 
 * 
 * line beginning with "MESSAGE " then all characters following
 * 
 * 
 * 
 * this string should be displayed in its message area.
 * 
 * 
 * 
 */

public class Client 
{
      public static int endcheck=0;//패키지 변수(이름)
      public static int gamestart=0;
      public static int uniqueName=0;
      public static int showend=0;
      //public static int n = 5;

   /*communication for server.*/
   BufferedReader in;
   static PrintWriter out;
   OutputStream out2;
   InputStream in2;
   public static int canvascheck=0;
   /*file transfer.*/
   FileInputStream fin;
   FileOutputStream fin2;
   
   /*GUI frame&panel*/
   JFrame frame = new JFrame();
   JPanel panel = new JPanel();
   JPanel backgrounda = new JPanel();
   JPanel backgroundb = new JPanel();
   static public ImagePanel welcomePanel = new ImagePanel(new ImageIcon("C:\\2-2\\gameframe.jpg").getImage());//game panel 
   static public JPanel resultPanel=new JPanel();//result panel.
   static public JPanel outPanel=new JPanel();

   static JFrame resultframe = new JFrame();
   static JFrame outframe = new JFrame();

   JTextField textField = new JTextField(45);// send to message
   JTextArea messageArea = new JTextArea(21,45);// chatting room
   
   /*specify for user.*/
   JLabel namelist_a = new JLabel("A team");
   JLabel namelist_b = new JLabel("B team");
   static public int timeover=0;
   JLabel label = new JLabel("WAIT");// label for starter.
   JLabel timerLabel = new JLabel("TIMER");//label for timer
   JLabel label_word = new JLabel("Word");// label for word.(only to first user)
   JLabel label_category;//label for category
   
   
   static JLabel label_score = new JLabel("0:0");//label for score

   static int PASSPRESSED=0;// check if first user press pass button.
   static int[] length = new int[20];
   static String seqnum; // game sequence.  
   public static int check = 0;//wait until drawing panel send button pressed.
   
   static int aful=0;
   static int bful=0;
   static int timercheck = 0;// check if time is over.
   int SEQNUM=0;//sequence number
   
   Image showImages2 = new ImageIcon("C:\\2-2\\first.png").getImage();//소영
   Image scaledImage2 =showImages2.getScaledInstance(700,470,Image.SCALE_DEFAULT);//소영
   ImagePanel showImage2=new ImagePanel(new ImageIcon(scaledImage2).getImage());//소영
   
   RelaySketch relay = new RelaySketch();

   /**
    * 
    * 
    * 
    * Constructs the client by laying out the GUI and registering a
    * 
    * 
    * 
    * listener with the textfield so that pressing Return in the
    * 
    * 
    * 
    * listener sends the textfield contents to the server. Note
    * 
    * 
    * 
    * however that the textfield is initially NOT editable, and
    * 
    * 
    * 
    * only becomes editable AFTER the client receives the NAMEACCEPTED
    * 
    * 
    * 
    * message from the server.
    * 
    * 
    * 
    */
   
   
   public static void show_OutEnd() {
      
     outframe.setSize(400, 160);
     outframe.setLocationRelativeTo(null);
      outPanel.setBackground(Color.WHITE); //은서
      JLabel outlabel= new JLabel("THE GAME IS END.");
      TitledBorder tb = new TitledBorder(new LineBorder(Color.black));
      
      outlabel.setBackground(Color.ORANGE);
      outlabel.setBorder(tb);
      outlabel.setForeground(new Color(255, 0, 0));

      outlabel.setFont(new Font("Calibri", Font.BOLD, 30)); //은서

      outlabel.setHorizontalAlignment(SwingConstants.CENTER);
      outPanel.add(outlabel, BorderLayout.CENTER);

         outframe.add(outPanel, BorderLayout.CENTER);
         outframe.setVisible(true);
         showend=1;
         
   }
   /**show result
    * if game is over, this function start.
    * if user is A team: scoreA: scoreB
    * if user is B team  scoreB: scoreA
    * so first score> second score : show WIN
    *    first score< second score: show LOSE
    *    first score= second score: show DRAW
    * */
   public static void show_result() 
   { 

     
      resultframe.setSize(500, 300);
      resultframe.setLocationRelativeTo(null);
      resultPanel.setBackground(Color.WHITE);
      
      String result_end=label_score.getText();  
      
      JButton yes=new JButton("YES");
	
   yes.addActionListener(new ActionListener() 
  {
      public void actionPerformed(ActionEvent arg0) 
      {
         out.println("<RESTART>YES");
         resultframe.dispose();
       }
    
   });
  
  JButton no=new JButton("NO");
 
  no.addActionListener(new ActionListener() 
  {
      public void actionPerformed(ActionEvent arg0) 
      {
         out.println("<RESTART>NO");
         resultframe.dispose();
         
       }
   });
  
        JLabel question = new JLabel();//은서
        question.setText("Do you want to RESTART the game?"); //은서
        question.setFont(new Font("Calibri", Font.BOLD, 28));//은서
        question.setBounds(41, 100, 431, 81);//은서
  
       JLabel result_score= new JLabel(); //은서
      //result_score.setSize(130, 88);
     // result_score.setLocation(165, 44);
       
      result_score.setFont(new Font("Calibri", Font.BOLD, 50)); //은서
      result_score.setVisible(true);
      label_score.setVisible(true);
      label_score.setText("0:0");
      
      JPanel board = new JPanel();//은서
      board.setBackground(Color.white);//은서
      board.setBounds(12, 10, 460, 100);//은서
      
      TitledBorder tb = new TitledBorder(new LineBorder(Color.black));
      
      resultPanel.setBorder(tb);
      resultPanel.add(board);//은서
      board.setLayout(null);//은서
      
      result_score.setBounds(160, 20, 250, 90);
      board.add(result_score);
     
      board.setVisible(true);//은서
      
      int score_first= Integer.parseInt(result_end.substring(0,result_end.indexOf(":")));
      int score_second= Integer.parseInt(result_end.substring(result_end.indexOf(":")+1));
      if(score_first>score_second) 
      {
         result_score.setText("WIN");
         result_score.setForeground(Color.BLUE);

      }
      else if(score_first<score_second) 
      {
         result_score.setText("LOSE");
         result_score.setForeground(Color.GRAY);

      }
      else if(score_first==score_second) 
      {
         result_score.setText("DRAW");
         result_score.setForeground(Color.GREEN);

      }
     
      yes.setBounds(100, 190, 120, 41);//은서
      yes.setFont(new Font("Arial Black", Font.PLAIN, 22));//은서
      yes.setBackground(Color.WHITE); //은서
      
      no.setBounds(270, 190, 120, 41);//은서
      no.setFont(new Font("Arial Black", Font.PLAIN, 22));//은서
      no.setBackground(Color.WHITE);//은서
      
      
      resultframe.getContentPane().add(resultPanel);//은서
      resultPanel.setLayout(null);//은서
      resultPanel.add(yes);
      resultPanel.add(no);
      resultPanel.add(question);//은서
      resultframe.setVisible(true);
      resultframe.repaint(); //찬빈
      
   }
   /**correct_answer
    * if answer is correct, then the pop up(answer correct) is show to last user. 
    * */
   private void correct_answer()

   {

      JOptionPane.showMessageDialog(frame, "The answer is correct!");

   }
      
   /**wrong_answer
    * if answer is wrong, then pop up(answer is wrong + original answer) show to last user.
    * */
   private void wrong_answer(String word)

   {
      JOptionPane.showMessageDialog(frame, "answer is " + word, "The answer is worng", JOptionPane.PLAIN_MESSAGE);
   }

   /**get_answer
    * To last user, send answer panel.
    * if enter answer then send to server.
    * */
   private String get_answer() 
   {

      return JOptionPane.showInputDialog(

            frame,

            "Enter your word you think it's answer ※no capital :",

            "ANSWER",

            JOptionPane.QUESTION_MESSAGE);

   };
   
   /**set_GUI
    * set GUI for game panel.
    * */
   private void set_GUI() 

   {
          frame.setSize(1280, 720);
         frame.getContentPane().add(welcomePanel, BorderLayout.NORTH);
         panel.setBounds(782, 100, 470, 420); //은서
         frame.setResizable(false);// 사이즈 조정x
         frame.setLocationRelativeTo(null);
         textField.setEditable(false);
         panel.add(textField, "North");       
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//은서
         
         messageArea.setEditable(false);
         messageArea.setLineWrap(true);//은서        
         panel.add(new JScrollPane(messageArea)); //은서        
         panel.setBackground(Color.GRAY);     
         welcomePanel.add(panel);
         welcomePanel.add(timerLabel, "NORTH");
         
         timerLabel.setForeground(Color.black);
         timerLabel.setFont(new Font("Calibri", Font.BOLD | Font.ITALIC, 54));
         timerLabel.setBounds(554, 10, 462, 82);
         label.setForeground(Color.RED);
         label.setFont(new Font("Calibri", Font.BOLD | Font.ITALIC, 54));
         label.setBounds(981, 26, 204, 82);
         welcomePanel.add(label);
         
         label_category = new JLabel("Category");
         label_category.setForeground(Color.black);
         label_category.setFont(new Font("Calibri", Font.BOLD | Font.ITALIC, 45));
         label_category.setBounds(14, 10, 266, 68);
         welcomePanel.add(label_category);// 진겸
         
         label_word.setForeground(Color.black);
         label_word.setFont(new Font("Calibri", Font.BOLD | Font.ITALIC, 28));
         label_word.setBounds(14, 90, 266, 56);
         welcomePanel.add(label_word);
         
         welcomePanel.add(backgrounda);
         backgrounda.setBackground(Color.GRAY);
         backgrounda.setBounds(782, 545, 230, 120);
         backgrounda.add(namelist_a);
         namelist_a.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 13));
         namelist_a.setForeground(Color.white);      
         
         welcomePanel.add(backgroundb);  
         backgroundb.setBackground(Color.GRAY);
         backgroundb.setBounds(1020, 545, 230, 120);
         backgroundb.add(namelist_b);
         namelist_b.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 13));
         namelist_b.setForeground(Color.white);      
         
      
         label_score.setForeground(Color.BLACK);
         label_score.setFont(new Font("Arial Rounded MT Bold", Font.BOLD | Font.ITALIC, 50));
         label_score.setBounds(547, 86, 103, 56);
         welcomePanel.add(label_score);
         JButton PassButton = new JButton("PASS");
         PassButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
               out.println("<PASS>");
               PASSPRESSED=1;
            }

         });

         PassButton.setBounds(364, 102, 105, 27);
         label_score.setVisible(true); //은서
         //label_score.setVisible(false); //진겸
         label_word.setVisible(false);// 진겸

   }
   
   public Client() {

      set_GUI();
      textField.addActionListener(new ActionListener() {

         /**
          * 
          * 
          * 
          * Responds to pressing the enter key in the textfield by sending
          * 
          * 
          * 
          * the contents of the text field to the server. Then clear
          * 
          * 
          * 
          * the text area in preparation for the next message.
          * 
          * 
          * 
          */

         public void actionPerformed(ActionEvent e)

         {

            out.println(textField.getText());

            textField.setText("");

         }

      });

   }
   
   

   /**
    * 
    * 
    * 
    * Connects to the server then enters the processing loop.
    * 
    * 
    * 
    * @return
    * 
    * 
    * 
    * @throws IOException
    * 
    * 
    * 
    * @throws ClassNotFoundException
    * 
    * 
    * 
    * @throws InterruptedException
    * 
    * 
    * 
    * @throws UnknownHostException
    * 
    * 
    * 
    */

   public void run() throws IOException, ClassNotFoundException, InterruptedException {

      String serverAddress = null;

      while (true)

      {

         if (RelaySketch.check_IP == 1)

         {

            serverAddress = RelaySketch.IPADDRESS;

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

         UserRepaint b = null;

         String line = in.readLine();
         
        if (line.startsWith("<NAMEACCEPTED>")) {
        	 // 서버에게 name accepted메시지가 오면 이름 중복이 아니라는 의미
            
             uniqueName=1;// unique name을 1로 변경->릴레이스케치클래스에서 중복인지 아닌지 판명
             
             System.out.print("");
             textField.setEditable(true);// 채팅창을 실행할 수 있도록


          }
     // 서버에게 norestart라는 메시지 오면 게임이 더 이상 중지 하는 것이 아니기 때문에 show_Out_End 끝났다는 메소드 불러옴
        else if(line.startsWith("<NORESTART>"))
        {
        	if(showend==0){
        	show_OutEnd();// 게임이 끝났을 때의 메시지(비정상적, 혹은 재시작 NO

        	}
        }
        /* 서버에게 user out이라는 메시지가 오면 네임 리스트에서 한명이 빠지는 것이기 때문에 네임리스트 업데이트 */
        else if(line.startsWith("<USEROUT>"))
         {
            
            if(gamestart==1) { 
                // n=0; //찬빈
                 
                 endcheck=1;
             }
             namelist_a.setText("A team");
              namelist_b.setText("B team");
              String many = in.readLine();
              int acount = 0, bcount = 0;
              String[] namelist_A = new String[30];
              String[] namelist_B = new String[30];
              System.out.print("");
              for (int i = 0; i < Integer.parseInt(many); i++) {
                 String names = in.readLine();
                 if (names.charAt(0) == '<' && names.charAt(2) == '>') {// 처음 들어왔을 때
                    System.out.print("");
                    if (names.substring(0, 3).equals("<A>")) {
                       namelist_A[i - bcount] = names;
                       acount++;
                    } else if (names.substring(0, 3).equals("<B>")) {
                       namelist_B[i - acount] = names;
                       bcount++;
                    }

                    System.out.print("");
                 }
              }
            
              String astring = "", bstring = "";
              namelist_a.setText("<html>" + namelist_a.getText() + "<br>" + namelist_A[0] + "<br>" + namelist_A[1]
                    + "<br>" + namelist_A[2] + "</html>");

              namelist_b.setText("<html>" + namelist_b.getText() + "<br>" + namelist_B[0] + "<br>" + namelist_B[1]
                    + "<br>" + namelist_B[2] + "</html>");

            
         }
        /*
      		 * 서버에게 이름을 보내라는 메시지가 오면 이름을 입력하게 되는데 다른 클래스이기 때문에 package 변수를 사용 서버는 이름이 중복이
      		 * 아니라는 것을 확인할 떄까지 submitname이라는 메시지를 보냄
      		 */
         else if (line.startsWith("<SUBMITNAME>")) {
            while (true) {
               if (RelaySketch.check_name == 1)

               {

                  out.println(RelaySketch.NAME);
                  break;

               } // if(check==name 끝

               System.out.print("");

            } // while 끝

         } 
        /*
		 * 새로운 사람이 들어올때마다 서버는 team view라는 메시지 를 주기 떄문에 team view라는 메시지를 받으면 namelist업데이트
		 */
         else if (line.startsWith("<TEAMVIEW>")) {
            
            namelist_a.setText("A team");
            namelist_b.setText("B team");
            String many = in.readLine();
            int acount = 0, bcount = 0;
            String[] namelist_A = new String[30];
            String[] namelist_B = new String[30];
            System.out.print("");
            for (int i = 0; i < Integer.parseInt(many); i++) {
               String names = in.readLine();
               if (names.charAt(0) == '<' && names.charAt(2) == '>') {// 처음 들어왔을 때
                  System.out.print("");
                  if (names.substring(0, 3).equals("<A>")) {
                     namelist_A[i - bcount] = names;
                     acount++;
                  } else if (names.substring(0, 3).equals("<B>")) {
                     namelist_B[i - acount] = names;
                     bcount++;
                  }

                  System.out.print("");
               }
            }
            String astring = "", bstring = "";
            namelist_a.setText("<html>" + namelist_a.getText() + "<br>" + namelist_A[0] + "<br>" + namelist_A[1]
                  + "<br>" + namelist_A[2] + "</html>");

            namelist_b.setText("<html>" + namelist_b.getText() + "<br>" + namelist_B[0] + "<br>" + namelist_B[1]
                  + "<br>" + namelist_B[2] + "</html>");

        
         } 
 
         else if(line.startsWith("<STOP>"))
         {
             if(gamestart==1) { 
                // n=0; //찬빈 
                 
                 endcheck=1;
             }
         }
         
         
         

   
        
        /*
		 * 이름이 accepted되면 서버는 team을 고르라는 메시지 만약 한팀이 적정인원수를 넘게 되면, 메시지를 띄우고 상대팀으로 자동 배정
		 */
         else if (line.startsWith("<SUBMITTEAM>"))

         {

            while (true)

            {

               if (RelaySketch.check_team == 1)

               {

                  out.println(RelaySketch.TEAM);
                   String ateamnum=in.readLine();
                   String bteamnum=in.readLine();
                   /*
					 * 제한 인원수가 넘으면 메시지를 보내주는것이 Relay sketch 클래스에 있기 때문에 package변수를 사용해서 메시지 보냄.
					 */
                   if((ateamnum).equals("3"))
                   {
                      RelaySketch.afu=1;
                      System.out.print("");
                   }
                   if((bteamnum.equals("3")))
                   {
                      {
                          RelaySketch.bfu=1;
                          System.out.print("");
                       }  
                   }
                  break;

               }

               System.out.print("");

            }

         }
        /* 만약 닉네임과 팀이 다 입력되었으면 게임판을 띄우라는 메시지 */
		
         else if (line.startsWith("<GAMEFRAME>")) {

              showImage2.setBounds(30, 170, 700, 470);//소영
              welcomePanel.add(showImage2);//소영         
            showImage2.setVisible(true);
            frame.setVisible(true);

         }
        /*
		 * server 메시지가 아니라 유저 채팅 메시지 나는 것을 의미해서 채팅방에 메시지를 띄워줌 서버가 같은 팀멤버들에게만 이 메시지를
		 * 보내기때문에 본인과 같은 팀원인 채팅밖에 보지 못함.
		 */
         else if (line.startsWith("MESSAGE")) {
 
            messageArea.append(line.substring(8) + "\n");

         }
        /*
		 * 본격적인 릴레이 스케치 게임을 위한 메시지를 보내게 됨 릴레이 스케치 게임 메시지: start,canvas
		 * 
		 */
         else if(line.startsWith("<START>"))
         {  

           
            while(check==0) 
            {
             if(timeover==1) 
             {
               break;   
             }
             if(endcheck==1)
                break;
           System.out.print("");
           if(seqnum.equals("1")&&PASSPRESSED==1) {
           String queue=in.readLine();
           out.println("nothing");// prevent waiting input from server
           System.out.print("");
           if(queue.startsWith("<GIVEWORD>")) // if pass button pushed

           {


              String word;

              word=in.readLine();
             System.out.print("");
              label_word.setText(word);

              label_word.setVisible(true); //change the word in the given category
              


           }//진겸
           }
          
        // 그림판에서 save버튼 누르면 check변수가 1로 바뀜== 그림을 다 그렸다는 뜻으로 서버에게 메시지 보내줌.
           
             if(check==1) {

                
                System.out.print("");

                 out.println("<send>");
              
                 PASSPRESSED=0;

                 System.out.print("");


                 break;    



                 }



             else



                continue;



             }



               check=0;



         }
        /* A팀이고, 서버와 소켓통신을 한번 더 연 후 본인이 그린 그림을 전송 시퀀스 넘버가 1또는 2인 사람만 */
        
         else if (line.startsWith("<ACANVAS>"))

         {

            Socket soc = new Socket(serverAddress, 11111);

            System.out.print("");

            out2 = soc.getOutputStream();

            DataOutputStream dout = new DataOutputStream(out2);

            fin = new FileInputStream(new File("C:\\2-2\\client_paint.png"));

            byte[] buffer = new byte[5000];

            int len;

            int data = 0;

            int datas;

            while ((len = fin.read(buffer)) > 0)

            {

               data++;

            }

            dout.writeInt(data);

            datas = data;

            System.out.print("");

            fin.close();

            fin = new FileInputStream("C:\\2-2\\client_paint.png");

            len = 0;

            int i = 0;

            for (; data > 0; data--)

            {

               len = fin.read(buffer);

               out2.write(buffer, 0, len);

               out.println(len);

            }
         
         }
        /* B팀에 해당하는 것으로 A팀과 동일하게 진행한다.(소켓열고 본인이 그린 그림 보내줌) */
    	
         else if (line.startsWith("<BCANVAS>"))

         {

            Socket soc = new Socket(serverAddress, 33333);

            System.out.print("");

            out2 = soc.getOutputStream();

            DataOutputStream dout = new DataOutputStream(out2);

            fin = new FileInputStream(new File("C:\\2-2\\client_paint.png"));

            byte[] buffer = new byte[5000];

            int len;

            int data = 0;

            int datas;

            while ((len = fin.read(buffer)) > 0)

            {

               data++;

            }

            dout.writeInt(data);

            datas = data;

            System.out.println(data);

            fin.close();

            fin = new FileInputStream("C:\\2-2\\client_paint.png");

            len = 0;

            int i = 0;

            for (; data > 0; data--)

            {

               len = fin.read(buffer);

               out2.write(buffer, 0, len);

               out.println(len);

            }
           

         }
        // AFULL이라는 메시지는 A팀이 제한인원을 초과했다라는 뜻으로 aful을 1로 변경해주어서 , 팝업 창(A팀이 꽉찼다는)을 띄울 수 있게
		// 해줌
         else if(line.startsWith("<AFULL>"))
         {
           aful=1;
           System.out.print("");
         }
         else if(line.startsWith("<BFULL>"))
         {
          bful=1;

         System.out.print("");
         }
        /*
		 * 서버가 이전 그림을 잘 받고, 클라이언트에게 이전 사람의 그림을 넘겨 주는 것 그림을 전송할 때와 마찬가지로 소켓을 열어서 서버에게
		 * 그림전송을 받고, 그 그림을 clinet_get.png로 저장해서 그림판넬에 띄워준다.
		 */
         else if (line.startsWith("<ARECEIVE>"))

         {

            int count = 0;

            Socket soc2 = new Socket(serverAddress, 22222);

            System.out.print("");

            in2 = soc2.getInputStream();

            DataInputStream dout1 = new DataInputStream(in2);

            int len;

            int data = 0;

            data = dout1.readInt();

            fin2 = new FileOutputStream(new File("C:\\2-2\\client_get.png"));

            DataOutputStream din = new DataOutputStream(soc2.getOutputStream());

            byte[] buffer2 = new byte[5000];

            System.out.print("");

            System.out.print("");
            

            int temp = data;

            len = 0;

            for (; data > 0; data--)

            {

               len = in2.read(buffer2);

               fin2.write(buffer2, 0, len);

               String queue = in.readLine();

               System.out.println("receive : " + len);

               System.out.println("!");

               System.out.println(queue);

               if (Integer.toString(len).equals(queue))

               {

                  count++;

               }

            }

            System.out.print("");

            if (count == temp)

            {

               System.out.print("");

               din.writeInt(1);

               
            }

            else

            {

               din.writeInt(2);

            }
            
          

         }
        /* B팀에게 그림을 전송하는 것으로 ARECIVE와 똑같이 작동 */
    	
         else if (line.startsWith("<BRECEIVE>"))

         {

            int count = 0;

            Socket soc3 = new Socket(serverAddress, 44444);

            System.out.print("");

            in2 = soc3.getInputStream();

            DataInputStream dout1 = new DataInputStream(in2);

            int len;

            int data = 0;

            data = dout1.readInt();

            fin2 = new FileOutputStream(new File("C:\\2-2\\client_get.png"));

            DataOutputStream din = new DataOutputStream(soc3.getOutputStream());

            byte[] buffer2 = new byte[5000];

            System.out.print("");

            System.out.print("");

            int temp = data;

            len = 0;

            for (; data > 0; data--)

            {

               len = in2.read(buffer2);

               fin2.write(buffer2, 0, len);

               String queue = in.readLine();


               System.out.print("");

               if (Integer.toString(len).equals(queue))

               {

                  count++;

               }

            }

            System.out.print("");

            if (count == temp)

            {

               System.out.print("");

               din.writeInt(1);

               
               
            }

            else

            {

               din.writeInt(2);

            }
           
         }
    	/* 서버 쪽에서 그림 전송을 위한 소켓을 다 썼다는 메시지로, 소켓 연결을 끊는다. */
        
         else if (line.startsWith("<OUT>"))

         {

            System.out.print("");

            out2.close();

            System.out.print("");

         }
        /*
  		 * 게임 인원인 6명이 다들어 오게 되면 서버는 ALLIN이라는 메세지를 주게되고 클라이언트들은 3초를 기달렸다가 본인의 순서를받게 되고 바로
  		 * 게임이 시작된다.
  		 */
         else if (line.startsWith("<ALLIN>"))

         {
            timeover=0; //찬빈
            
            gamestart=1;
            System.out.print("");

            TimerThread th = new TimerThread(timerLabel);

            label.setText("3");

            Thread.sleep(1000);

            label.setText("2");

            Thread.sleep(1000);

            label.setText("1");

            Thread.sleep(1000);

            label.setText("START");

            Thread.sleep(1000);

            th.start();

            label_score.setVisible(true);
      

         }
        /* 제시어를 게임 판넬에 띄워준다(첫번째 사람에게만 AB순서 동일) */
    	
         else if (line.startsWith("<GIVEWORD>")) {

            String word;

            word = in.readLine();

            label_word.setText(word);

            label_word.setVisible(true);
          
         } // 진겸
        /* 제시어의 카테고리를 게임판넬에 띄워준다.(모든 사람, AB동일) */
        
         else if (line.startsWith("<GIVECATEGORY>"))

         {

            String givencategory;

            givencategory = in.readLine();

            label_category.setText(givencategory);

         } // 서버에서 단어받아오기,//진겸
        /*서버 쪽에서 점수 변동(즉 3번째 사람이 정답을 입력한 것으로 점수를 업데이트 해준다.
		 * 서버가 A팀에게는 A:B
		 * B팀이게는 B:A로 보내주기때문에 본인의 팀과 관계 없이 읽은 그대로 게임 판넬에 띄워준다.*/
        
         else if (line.startsWith("<SCORE>")) {
            

          

            String score;

            score = in.readLine();

            label_score.setText(score);

         

         }
        /*서버에게 정답을 입력하라는 메시지(A팀)
		 * 이 메시지를 받으면 정답을 입력하라는 textfiled가 뜨게 되고 그 string을 서버에게 보내준다.*/
        
         else if (line.startsWith("<AANSWERSHEET>"))

         {
             Image showImages = new ImageIcon("C:\\2-2\\client_get.png").getImage();//소영
             Image scaledImage =showImages.getScaledInstance(700,470,Image.SCALE_DEFAULT);//소영
             ImagePanel showImage=new ImagePanel(new ImageIcon(scaledImage).getImage());//소영
             
             showImage.setBounds(0, 0, 700, 470);//소영
             showImage2.add(showImage);//소영
             showImage.updateUI();    
             showImage.setVisible(true);
     
             
             System.out.print("");

             String q = get_answer();

             System.out.print("");

             out.println("<Aanswer>" + q);

             System.out.print("");

             String result;

             result = in.readLine();
             for(int i=0;i<1000;i++) {
            	 System.out.print(" ");
             }
             
             if (result.startsWith("<ANSWERCORRECT>"))

             {

                correct_answer();
             }

             else if (result.startsWith("<ANSWERWRONG>"))

             {
                wrong_answer(result.substring(13));

             }
             
     
             showImage.setVisible(false);
         
         } // 진겸
        /*B팀 마지막사람에게 정답판넬을 띄우는 것으로 A팀과 동일하게 작동*/
    	
         else if (line.startsWith("<BANSWERSHEET>"))

         {
             Image showImages = new ImageIcon("C:\\2-2\\client_get.png").getImage();//소영
             Image scaledImage =showImages.getScaledInstance(700,470,Image.SCALE_DEFAULT);//소영
             ImagePanel showImage=new ImagePanel(new ImageIcon(scaledImage).getImage());//소영
             
             showImage.setBounds(0, 0, 700, 470);//소영
             showImage2.add(showImage);//소영
             showImage.updateUI();    
             showImage.setVisible(true);
                                  
            System.out.print("");

            String q = get_answer();

            System.out.print("");

            out.println("<Banswer>" + q);

            System.out.print("");

            String result;

            result = in.readLine();

            if (result.startsWith("<ANSWERCORRECT>"))
            {
               correct_answer();
            }

            else if (result.startsWith("<ANSWERWRONG>"))
            {
               wrong_answer(result.substring(13));

            }

         showImage.setVisible(false);
         } // 진겸
         
         else if (line.startsWith("<SEQUENCE>"))
         {
            seqnum = line.substring(10);
            SEQNUM=Integer.parseInt(seqnum);
            System.out.print("");

            label.setText(seqnum);
                    }  
      //그림판을 띄우라는 메세지로 SEQNUM이 1일때는 처음사람으로 받아온 그림을 띄워줄 필요가 없고 처음사람이 아닐 경우 위에서 받아온 그림을 화면에 띄워준다.
        
         else if (line.startsWith("<SEND>"))

         { //찬빈 
            if(SEQNUM!=1) {
            Image showImages = new ImageIcon("C:\\2-2\\client_get.png").getImage();//소영
           Image scaledImage =showImages.getScaledInstance(700,470,Image.SCALE_DEFAULT);//소영
           ImagePanel showImage=new ImagePanel(new ImageIcon(scaledImage).getImage());//소영
           
           showImage.setBounds(0, 0, 700, 470);//소영
           showImage2.add(showImage);//소영
           showImage.updateUI();    
           showImage.setVisible(true);
           
           Thread.sleep(3000);
             showImage.setVisible(false);
             b = new UserRepaint();
            }
            if(SEQNUM==1) {
               b = new UserRepaint();
            }
         }
         }
      // 여기다가 점수들 띄워주는 창 올려봐
   }
   /**
    * Runs the client as an application with a closeable frame.
    */

   public static void main(String[] args) throws Exception {

      Client client = new Client();

      client.run();
   }
}

class TimerThread extends Thread {  
 
   private JLabel timerLabel;// 타이머 값이 출력될 레이블

   public TimerThread(JLabel timerLabel) {

      this.timerLabel = timerLabel; // 타이머 카운트를 출력할 레이블

   }

   // 스레드 코드 run()이 종료하면 스레드 종료

   public void run() {
         UserRepaint.isend = 0;
         int n=100;
         
         while (n >= 0) //찬빈
         {
            timerLabel.setText(Integer.toString(n)); //찬빈                  
                     
            if (n == 0) //찬빈
            {
               if(Client.endcheck!=1) {
                  System.out.print("");
                  UserRepaint.isend=1;//찬빈
                  Client.timeover=1;
                  Client.show_result();
                  break;
               }               
            }

            else {
                if(Client.endcheck==1) {
          
                    break;
                    }
                else 
                {
                   n--;//찬빈
                 }
            try {

               Thread.sleep(1000);// 1초간격

            } catch (InterruptedException e) {

               e.printStackTrace();
            }
            }
         }
         if(Client.endcheck==1||n>0) { //찬빈 
            UserRepaint.isend = 1;
           timerLabel.setText(Integer.toString(n));
             try {
                  Thread.sleep(1000);// 1초간격

               } catch (InterruptedException e) {

                  e.printStackTrace();
               }              
             if(Client.showend==0) {
             Client.show_OutEnd();
             }
         }
      }

}
