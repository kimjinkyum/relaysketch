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

import org.w3c.dom.NameList;

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

   /*communication for server.*/
   BufferedReader in;
   static PrintWriter out;
   OutputStream out2;
   InputStream in2;
   
   /*file transfer.*/
   FileInputStream fin;
   FileOutputStream fin2;
   
   /*GUI frame&panel*/
   JFrame frame = new JFrame();
   JPanel panel = new JPanel();
   JPanel backgrounda = new JPanel();
   JPanel backgroundb = new JPanel();
   static public ImagePanel welcomePanel = new ImagePanel(new ImageIcon("C:\\Users\\������\\eclipse-workspace\\Project_Socket\\src\\image\\R1280x0.jpg").getImage());//game panel 
   static public JPanel resultPanel=new JPanel();//result panel.
   static JFrame resultframe=new JFrame();
   
   JTextField textField = new JTextField(45);// send to message
   JTextArea messageArea = new JTextArea(21, 45);// chatting room
   
   /*specify for user.*/
   JLabel backgrounda_a = new JLabel();
   
   
   JLabel namelist_a =new JLabel("A team");
   JLabel namelist_b = new JLabel("B team");
   JLabel label = new JLabel("WAIT");// label for starter.
   JLabel timerLabel = new JLabel("TIMER");//label for timer
   JLabel label_word = new JLabel("Word");// label for word.(only to first user)
   JLabel label_category;//label for category

   static JLabel label_score = new JLabel("0:0");//label for score
   
   static int PASSPRESSED=0;// check if first user press pass button.
   static int[] length = new int[20];
   static String seqnum; // game sequence.  
   public static int check = 0;//??
   
   static int timercheck = 0;// check if time is over.
   int SEQNUM=0;//??
   
   
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
      resultPanel.setBackground(Color.WHITE);
      String result_end=label_score.getText();   
      JLabel result_score= new JLabel();
      result_score.setSize(130, 88);
      result_score.setLocation(165, 44);
      result_score.setFont(new Font("Calibri", Font.BOLD, 50));
      
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
     resultframe.add(resultPanel);
     resultPanel.add(result_score);
     resultframe.setVisible(true);
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

            "Enter your word you think it's answer ��no capital :",

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

         panel.setBounds(782, 100, 470, 420); //����
         frame.setResizable(false);// ������ ����x

         frame.setLocationRelativeTo(null);

         textField.setEditable(false);

         panel.add(textField, "North");

        
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//����
         messageArea.setEditable(false);
         messageArea.setLineWrap(true);//����
         
         panel.add(new JScrollPane(messageArea)); //����
         
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

         welcomePanel.add(label_category);// ����

         label_word.setForeground(Color.black);

         label_word.setFont(new Font("Calibri", Font.BOLD | Font.ITALIC, 40));

         label_word.setBounds(14, 90, 266, 56);

         welcomePanel.add(label_word);

         welcomePanel.add(backgrounda);
         backgrounda.setBackground(Color.GRAY);
         backgrounda.setBounds(782, 545, 230, 120);
         backgrounda.add(namelist_a);
         namelist_a.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 13));
         namelist_a.setForeground(Color.black);
         
         welcomePanel.add(backgroundb);
         backgroundb.setBackground(Color.WHITE);
         backgroundb.setBounds(1020, 545, 230, 120);
         backgroundb.add(namelist_b);
         namelist_b.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 13));

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
        // welcomePanel.add(PassButton);
         
         label_score.setVisible(false);

         label_word.setVisible(false);// ����


      
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

         if (line.startsWith("<SUBMITNAME>")) {
            while (true) {
               if (RelaySketch.check_name == 1)

               {

                  out.println(RelaySketch.NAME);
                  break;

               } // if(check==name ��

               System.out.print("");

            } // while ��

         } else if (line.startsWith("<TEAMVIEW>")) {
        
        	namelist_a.setText("A Team:");
            namelist_b.setText("B Team:");
           
            namelist_a.setBackground(Color.ORANGE);
            String many = in.readLine();
            int acount = 0, bcount = 0;
            String[] namelist_A = new String[30];
            String[] namelist_B = new String[30];
            System.out.println(line);
            for (int i = 0; i < Integer.parseInt(many); i++) {
               String names = in.readLine();
               if (names.charAt(0) == '<' && names.charAt(2) == '>') {// ó�� ������ ��
                  System.out.println("hihi");
                  if (names.substring(0, 3).equals("<A>")) {
                     namelist_A[i - bcount] = names;
                     acount++;
                  } else if (names.substring(0, 3).equals("<B>")) {
                     namelist_B[i - acount] = names;
                     bcount++;
                  }

                  System.out.println("hihi");
               }
            }
            /*
             * else if(line.matches(".*����.*")) {//�������� �� int index=line.indexOf("��"); String
             * name=line.substring(14,index);
             * 
             * if(line.matches(".*<A>.*")) { for(int i=0;i<namelist_A.size();i++) {
             * if((namelist_A.get(i)).matches(name)) { namelist_A.remove(i); } } //���� �̸��̶�
             * list A�� �ִ� �̸��̶� ���ؼ� ������ //A���� remove } else if(line.matches(".*<B>.*")) {
             * for(int i=0;i<namelist_B.size();i++) { if((namelist_B.get(i)).matches(name))
             * { namelist_B.remove(i); } }
             * 
             * //���� �̸��̶� list B�� �ִ� �̸��̶� ���ؼ� ������ //B���� remove } }
             */
            String astring = "", bstring = "";
            namelist_a.setText("<html>" + namelist_a.getText() + "<br>" + namelist_A[0] + "<br>" + namelist_A[1]
                  + "<br>" + namelist_A[2] + "</html>");

            namelist_b.setText("<html>" + namelist_b.getText() + "<br>" + namelist_B[0] + "<br>" + namelist_B[1]
                  + "<br>" + namelist_B[2] + "</html>");

         } 
         else if(line.startsWith("<USEROUT>"))
         {
             namelist_a.setText("A team");
              namelist_b.setText("B team");
              String many = in.readLine();
              int acount = 0, bcount = 0;
              String[] namelist_A = new String[30];
              String[] namelist_B = new String[30];
              System.out.println(line);
              for (int i = 0; i < Integer.parseInt(many); i++) {
                 String names = in.readLine();
                 if (names.charAt(0) == '<' && names.charAt(2) == '>') {// ó�� ������ ��
                    System.out.println("hihi");
                    if (names.substring(0, 3).equals("<A>")) {
                       namelist_A[i - bcount] = names;
                       acount++;
                    } else if (names.substring(0, 3).equals("<B>")) {
                       namelist_B[i - acount] = names;
                       bcount++;
                    }

                    System.out.println("hihi");
                 }
              }
            
              String astring = "", bstring = "";
              namelist_a.setText("<html>" + namelist_a.getText() + "<br>" + namelist_A[0] + "<br>" + namelist_A[1]
                    + "<br>" + namelist_A[2] + "</html>");

              namelist_b.setText("<html>" + namelist_b.getText() + "<br>" + namelist_B[0] + "<br>" + namelist_B[1]
                    + "<br>" + namelist_B[2] + "</html>");

            
         }
         
         
         
         else if (line.startsWith("<NAMEACCEPTED>")) {

            textField.setEditable(true);

         }

         else if (line.startsWith("<SUBMITTEAM>"))

         {

            while (true)

            {

               if (RelaySketch.check_team == 1)

               {

                  out.println(RelaySketch.TEAM);

                  break;

               }

               System.out.print("");

            }

         }

         else if (line.startsWith("<GAMEFRAME>")) {
        	 /*JPanel imageback = new JPanel();
        	 imageback.setSize(800,600);
        	 imageback.setBounds(20,160,660,310);
        	 imageback.setBackground(Color.ORANGE);
              */
        	 Image showImages = new ImageIcon("C:\\2-2\\client_get.png").getImage();//�ҿ�
               Image scaledImage =showImages.getScaledInstance(700,470,Image.SCALE_DEFAULT);//�ҿ�
               ImagePanel showImage=new ImagePanel(new ImageIcon(scaledImage).getImage());//�ҿ�
               
               //imageback.add(showImage);
               //showImage.setSize(400,500);//�ҿ�
               showImage.setBounds(30, 170, 638, 294);//�ҿ�
               //imagepanel.add(showImage);
               //welcomePanel.add(imagepanel);//�ҿ�
                  welcomePanel.add(showImage);
               showImage.setVisible(true);
               
           
            frame.setVisible(true);

         }

         else if (line.startsWith("MESSAGE")) {

            messageArea.append(line.substring(8) + "\n");

         }

         else if (line.startsWith("<SEND>"))

         {
            if(SEQNUM!=1) {
            Image showImages = new ImageIcon("C:\\2-2\\client_get.png").getImage();//�ҿ�
           Image scaledImage =showImages.getScaledInstance(500,300,Image.SCALE_DEFAULT);//�ҿ�
           ImagePanel showImage=new ImagePanel(new ImageIcon(scaledImage).getImage());//�ҿ�
           //showImage.setSize(400,500);//�ҿ�
           showImage.setBounds(12, 360, 638, 294);//�ҿ�
           welcomePanel.add(showImage);//�ҿ�
           showImage.updateUI();    
           showImage.setVisible(true);
            }
            b = new UserRepaint();

         }

         else if(line.startsWith("<START>"))
         {  



            while(check==0) {

           System.out.print("");
           if(seqnum.equals("1")&&PASSPRESSED==1) {
           String queue=in.readLine();
           out.println("nothing");// prevent waiting input from server
           System.out.println(":"+queue);
           if(queue.startsWith("<GIVEWORD>")) // if pass button pushed

           {

              String word;

              word=in.readLine();
             System.out.println("word : "+word);
              label_word.setText(word);

              label_word.setVisible(true); //change the word in the given category

           }//����
           }
          

             if(check==1) {

                
                System.out.println("in "+check);

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

         else if (line.startsWith("<CANVAS>"))

         {

            Socket soc = new Socket(serverAddress, 11111);

            System.out.println(":start!");

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
         else if (line.startsWith("<BCANVAS>"))

         {

            Socket soc = new Socket(serverAddress, 33333);

            System.out.println(":start!");

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

         else if (line.startsWith("<RECEIVE>"))

         {

            int count = 0;

            Socket soc2 = new Socket(serverAddress, 22222);

            System.out.println(":start!");

            in2 = soc2.getInputStream();

            DataInputStream dout1 = new DataInputStream(in2);

            int len;

            int data = 0;

            data = dout1.readInt();

            fin2 = new FileOutputStream(new File("C:\\2-2\\client_get.png"));

            DataOutputStream din = new DataOutputStream(soc2.getOutputStream());

            byte[] buffer2 = new byte[5000];

            System.out.println(data);

            System.out.println("heyyo!");

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

            System.out.println(count);

            if (count == temp)

            {

               System.out.println(count);

               din.writeInt(1);

               
            }

            else

            {

               din.writeInt(2);

            }

         }
         else if (line.startsWith("<BRECEIVE>"))

         {

            int count = 0;

            Socket soc3 = new Socket(serverAddress, 44444);

            System.out.println(":start!");

            in2 = soc3.getInputStream();

            DataInputStream dout1 = new DataInputStream(in2);

            int len;

            int data = 0;

            data = dout1.readInt();

            fin2 = new FileOutputStream(new File("C:\\2-2\\client_get.png"));

            DataOutputStream din = new DataOutputStream(soc3.getOutputStream());

            byte[] buffer2 = new byte[5000];

            System.out.println(data);

            System.out.println("heyyo!");

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

            System.out.println(count);

            if (count == temp)

            {

               System.out.println(count);

               din.writeInt(1);

               
               
            }

            else

            {

               din.writeInt(2);

            }

         }
         else if (line.startsWith("<OUT>"))

         {

            System.out.println("out");

            out2.close();

            System.out.println("out");

         }

         else if (line.startsWith("<ALLIN>"))

         {

            System.out.println("6 people in");

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

         else if (line.startsWith("<GIVEWORD>")) {

            String word;

            word = in.readLine();

            label_word.setText(word);

            label_word.setVisible(true);

         } // ����
         else if (line.startsWith("<GIVECATEGORY>"))

         {

            String givencategory;

            givencategory = in.readLine();

            label_category.setText(givencategory);

         } // �������� �ܾ�޾ƿ���,//����

         else if (line.startsWith("<SCORE>")) {

            String score;

            score = in.readLine();

            label_score.setText(score);

         }

         else if (line.startsWith("<AANSWERSHEET>"))

         {

            Image showImages = new ImageIcon("C:\\2-2\\client_get.png").getImage();//�ҿ�
             Image scaledImage =showImages.getScaledInstance(500,300,Image.SCALE_DEFAULT);//�ҿ�
             ImagePanel showImage=new ImagePanel(new ImageIcon(scaledImage).getImage());//�ҿ�
             //showImage.setSize(400,500);//�ҿ�
             showImage.setBounds(12, 360, 638, 294);//�ҿ�
             welcomePanel.add(showImage);//�ҿ�
             showImage.updateUI();    
             showImage.setVisible(true);
            System.out.println("END");

            String q = get_answer();

            System.out.println(q);

            out.println("<Aanswer>" + q);

            System.out.println("OUT");

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

         } // ����

         else if (line.startsWith("<BANSWERSHEET>"))

         {
            Image showImages = new ImageIcon("C:\\2-2\\client_get.png").getImage();//�ҿ�
             Image scaledImage =showImages.getScaledInstance(500,300,Image.SCALE_DEFAULT);//�ҿ�
             ImagePanel showImage=new ImagePanel(new ImageIcon(scaledImage).getImage());//�ҿ�
             //showImage.setSize(400,500);//�ҿ�
             showImage.setBounds(12, 360, 638, 294);//�ҿ�
             welcomePanel.add(showImage);//�ҿ�
             showImage.updateUI();    
             showImage.setVisible(true);
            System.out.println("END");

            String q = get_answer();

            System.out.println(q);

            out.println("<Banswer>" + q);

            System.out.println("OUT");

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
            
            
             
              

         } // ����

         else if (line.startsWith("<SEQUENCE>"))

         {

            seqnum = line.substring(10);
            SEQNUM=Integer.parseInt(seqnum);
            System.out.println(seqnum);

            label.setText(seqnum);

         }

      }

      // ����ٰ� ������ ����ִ� â �÷���

   }

   /**
    * 
    * 
    * 
    * Runs the client as an application with a closeable frame.
    * 
    * 
    * 
    */

   public static void main(String[] args) throws Exception {

      Client client = new Client();

      client.run();

   }

}

class TimerThread extends Thread {
  


   private JLabel timerLabel;// Ÿ�̸� ���� ��µ� ���̺�

   public TimerThread(JLabel timerLabel) {

      this.timerLabel = timerLabel; // Ÿ�̸� ī��Ʈ�� ����� ���̺�

   }

   // ������ �ڵ� run()�� �����ϸ� ������ ����

   public void run() {

      int n = 100;

      while (n >= 0) 
      {

         timerLabel.setText(Integer.toString(n));
               
         if (n == 0)

         {
            System.out.println("hello");
            UserRepaint.isend = 1;
            Client.show_result();
            break;
         }

         else

            n--;

         try {

            Thread.sleep(1000);// 1�ʰ���

         } catch (InterruptedException e) {

            e.printStackTrace();

         }

      }
      
      

   }

   

}