import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.BorderLayout;
/**
 * RelaySketch class is about frame that is show before game start
 * contains
 * 1. server IP input
 * 2. name input
 * 3. team select
 */
public class RelaySketch {
   public static String IPADDRESS;
   public static int afu=0;
   public static int bfu=0;
   public static int check_IP=0; 
   public static String NAME;
   public static int check_name=0;
   public static String TEAM;
   public static int check_team;
   private JFrame frame;
   private JTextField txtEnterIpAddress;
   private JTextField txtEnterYourName;

   /**

    * Launch the application.

    */


   public static void main(String[] args) {

      new RelaySketch();

   }

   /**

    * Create the application.

    */

   public RelaySketch() {

      initialize();
      frame.setVisible(true);

   }
   /**
    * Initialize the contents of the frame.
    * GUI for frame that is showed before the game
    */

   public void initialize() {

      JFrame frame_ip=new JFrame();//if user enter the IP,then show user IP checked pop up
      frame_ip.setSize(300,100);
      JFrame frame_name=new JFrame();//if user enter the name,then show user name checked pop up
      JFrame frame_nameDU = new JFrame();////if user enter the duplicated name, then show user name duplicated pop up
      frame_name.setSize(300,100);   
      frame_nameDU.setSize(300,100);
      JFrame frame_A=new JFrame();
      JFrame frame_B = new JFrame();
      frame_A.setSize(600,100);   
      frame_B.setSize(600,100);
      frame_ip.setResizable(false);//사이즈 조정x
      frame_ip.setLocationRelativeTo(null);
      frame_name.setResizable(false);//사이즈 조정x
      frame_name.setLocationRelativeTo(null);
      frame_nameDU.setResizable(false);//사이즈 조정x
      frame_nameDU.setLocationRelativeTo(null);
      frame_A.setResizable(false);//사이즈 조정x
      frame_A.setLocationRelativeTo(null);//가운데에 뜨기
      frame_B.setResizable(false);//사이즈 조정x
      frame_B.setLocationRelativeTo(null);
      JPanel contentPane1;
      contentPane1 = new JPanel();
      contentPane1.setBorder(new EmptyBorder(5, 5, 5, 5));
      contentPane1.setLayout(new BorderLayout(0, 0));
      
      JLabel lblIpChecked = new JLabel("IP checked!");
      lblIpChecked.setBackground(new Color(102, 51, 51));
      lblIpChecked.setForeground(Color.RED);
      lblIpChecked.setFont(new Font("Calibri", Font.BOLD, 30));
      lblIpChecked.setHorizontalAlignment(SwingConstants.CENTER);
      contentPane1.add(lblIpChecked, BorderLayout.CENTER);
      
      frame_ip.add(contentPane1, BorderLayout.CENTER);
      
      JPanel contentPane2;
      contentPane2 = new JPanel();
      contentPane2.setBorder(new EmptyBorder(5, 5, 5, 5));
      contentPane2.setLayout(new BorderLayout(0, 0));
      
      JPanel contentPane2DU;
      contentPane2DU = new JPanel();
      contentPane2DU.setBorder(new EmptyBorder(5, 5, 5, 5));
      contentPane2DU.setLayout(new BorderLayout(0, 0));
      
      JLabel lblIpChecked2 = new JLabel("Name checked!");
      lblIpChecked2.setBackground(new Color(102, 51, 51));
      lblIpChecked2.setForeground(new Color(255, 0, 0));
      lblIpChecked2.setFont(new Font("Calibri", Font.BOLD, 30));
      lblIpChecked2.setHorizontalAlignment(SwingConstants.CENTER);
      
      //if name is duplicated, then infrom user
      JLabel lblIpChecked2DU = new JLabel("Name is duplicated!");
      lblIpChecked2DU.setBackground(new Color(102, 51, 51));
      lblIpChecked2DU.setForeground(new Color(255, 0, 0));
      lblIpChecked2DU.setFont(new Font("Calibri", Font.BOLD, 30));
      lblIpChecked2DU.setHorizontalAlignment(SwingConstants.CENTER);
      
      //if team is full, then inform user 
      JLabel lblIpChecked4 = new JLabel("A team is full! You are team B");
      lblIpChecked4.setBackground(new Color(102, 51, 51));
      lblIpChecked4.setForeground(new Color(255, 0, 0));
      lblIpChecked4.setFont(new Font("Calibri", Font.BOLD, 30));
      lblIpChecked4.setHorizontalAlignment(SwingConstants.CENTER);
      
      JLabel lblIpChecked5 = new JLabel("B team is full! You are team A");
      lblIpChecked5.setBackground(new Color(102, 51, 51));
      lblIpChecked5.setForeground(new Color(255, 0, 0));
      lblIpChecked5.setFont(new Font("Calibri", Font.BOLD, 30));
      lblIpChecked5.setHorizontalAlignment(SwingConstants.CENTER);
      
      contentPane2.add(lblIpChecked2, BorderLayout.CENTER);
      contentPane2DU.add(lblIpChecked2DU, BorderLayout.CENTER);
      
      frame_name.add(contentPane2, BorderLayout.CENTER);
      frame_nameDU.add(contentPane2DU, BorderLayout.CENTER);   
      
      frame_A.add(lblIpChecked4, BorderLayout.CENTER);
      frame_B.add(lblIpChecked5, BorderLayout.CENTER);
      
      frame=new JFrame();
      
      ImagePanel welcomePanel=new ImagePanel(new ImageIcon("C:\\2-2\\beforegame.jpg").getImage());

      frame.setSize(1280,720);
      frame.getContentPane().add(welcomePanel, BorderLayout.EAST);
      
      JLabel lblIpAddress = new JLabel("IP Address:");

      lblIpAddress.setFont(new Font("Calibri", Font.BOLD, 22));
      lblIpAddress.setBounds(30, 165, 137, 45);

      welcomePanel.add(lblIpAddress);

      txtEnterIpAddress = new JTextField();
      txtEnterIpAddress.setText("Enter IP Address");
      txtEnterIpAddress.setToolTipText("Enter IP Address");
      txtEnterIpAddress.setBounds(161, 167, 200, 50); 

      welcomePanel.add(txtEnterIpAddress);
 
      txtEnterIpAddress.setColumns(10);
      /*
       * button about team A
       */
      JButton btnNewButton = new JButton("A");
      btnNewButton.setFont(new Font("Agency FB", Font.BOLD, 52));
      btnNewButton.setEnabled(false);
      btnNewButton.setBackground(Color.WHITE); 
 
      btnNewButton.addActionListener(new ActionListener() {
    	  
         public void actionPerformed(ActionEvent arg0) 

         {
            TEAM="A";
            check_team=1;
            try {
               Thread.sleep(500);
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
            //a팀이 꽉 찼을경우
            for(int i=0; i<1000; i++)
            {
            	if(afu==1) {
            		frame_A.setVisible(true);
            	}
            }
            frame.setVisible(false);
         }
      });

      
      btnNewButton.setBounds(161, 302, 80, 65);
      welcomePanel.add(btnNewButton);
      /*
       * button about team B
       */
      JButton btnB = new JButton("B");
      btnB.setFont(new Font("Agency FB", Font.BOLD, 52));
      btnB.setBackground(Color.WHITE);
      btnB.setBounds(281, 302, 80, 65);
      btnB.setEnabled(false);
     
      btnB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) 
         {
            TEAM="B";
            check_team=1;
            try {
               Thread.sleep(500);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          //b팀이 꽉 찼을경우
            for(int i=0; i<1000; i++)
            {
               if(bfu==1)
                   frame_B.setVisible(true);
            }
            frame.setVisible(false);
         }
         });

      welcomePanel.add(btnB);    
      /*
       * submit button for ip,name
       * name submit button is used after checking IP
       * team button is used after checking name
       */

      JButton submit_ip=new JButton("Submit");
      submit_ip.setFont(new Font("Calibri",Font.BOLD,25));
      submit_ip.setBounds(375, 168, 137, 50);
      submit_ip.setBackground(Color.WHITE);
      welcomePanel.add(submit_ip);

      JLabel lblName = new JLabel("Name:");
      lblName.setFont(new Font("Calibri", Font.BOLD, 22));
      lblName.setBounds(70, 234, 137, 45);
      welcomePanel.add(lblName);
      txtEnterYourName = new JTextField();
      txtEnterYourName.setToolTipText("Enter your name");
      txtEnterYourName.setText("Enter your name");
      txtEnterYourName.setBounds(161, 238, 200, 50);
      txtEnterYourName.setColumns(10);
      welcomePanel.add(txtEnterYourName);

      JButton submit_name = new JButton("Submit");
      submit_name.setFont(new Font("Calibri",Font.BOLD,25));
      submit_name.setEnabled(false);

      submit_ip.addActionListener(new ActionListener(){

    	  @Override
            public void actionPerformed(ActionEvent e) 
            {
               submit_name.setEnabled(true);//ip가 check되었을때 이름 submit버튼을 누를수 있게함
               IPADDRESS= txtEnterIpAddress.getText();
               check_IP=1;
               frame_ip.setVisible(true);
            }
         });

      submit_name.addActionListener(new ActionListener() {
    	  
         public void actionPerformed(ActionEvent arg0) 
         {
            NAME=txtEnterYourName.getText();
            check_name=1;
            try {
                Thread.sleep(100);
             } catch (InterruptedException e) {
                e.printStackTrace();
             }     
            
             if(Client.uniqueName==1) {
            	 //이름이 중복된게 아닐때에 팀버튼을 선택할수 있게 함
                 btnNewButton.setEnabled(true); 
                 btnB.setEnabled(true); 
                 frame_name.setVisible(true);
             }
             else {//중복됐다는 프레임 띄우기!
                frame_nameDU.setVisible(true);
             }
          }         
      });

      submit_name.setBackground(Color.WHITE); 
      submit_name.setBounds(375, 238, 137, 50);
      welcomePanel.add(submit_name);
      JLabel lblTeam = new JLabel("Team:");
      lblTeam.setFont(new Font("Calibri", Font.BOLD, 22));
      lblTeam.setBounds(70, 305, 137, 50);
      welcomePanel.add(lblTeam);

      welcomePanel.add(btnB);
      JLabel lblRelaySketch = new JLabel("RELAY SKETCH");
      lblRelaySketch.setForeground(Color.WHITE);
      lblRelaySketch.setFont(new Font("Abadi", Font.BOLD| Font.ITALIC , 54));
      lblRelaySketch.setBounds(400, 24, 470, 82);
      welcomePanel.add(lblRelaySketch);

      frame.setResizable(false);
      frame.setLocationRelativeTo(null);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      }
}      

/**
 * to declare panel for image
 * this class set size
 */
class ImagePanel extends JPanel{
   private Image img;
   public ImagePanel(Image img) {

	  this.img=img;
      setSize(new Dimension(1280,720));
      setPreferredSize(new Dimension(1280,720));
      setLayout(null);
   }

   public int getWidth() {
      return img.getWidth(null);
   }

   public int getHeight() {
      return img.getHeight(null);
   }

   public void paintComponent(Graphics g) {
      g.drawImage(img, 0,0,null);
   }

}
