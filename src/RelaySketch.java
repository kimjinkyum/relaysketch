import java.awt.Dimension;







import java.awt.EventQueue;







import java.awt.Graphics;







import java.awt.Image;







import java.awt.Window;















import javax.swing.ImageIcon;







import javax.swing.JFrame;







import javax.swing.JPanel;







import javax.swing.JLabel;







import java.awt.Font;







import javax.swing.JTextField;



import javax.swing.SwingConstants;



import javax.swing.border.EmptyBorder;



import javax.swing.JButton;







import javax.swing.JComboBox;















import java.awt.event.ActionListener;







import java.awt.event.ActionEvent;







import java.awt.Color;







import java.awt.BorderLayout;

public class RelaySketch {
   public static String IPADDRESS;
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
    */
   public void initialize() {
      JFrame frame_ip=new JFrame();
      frame_ip.setSize(300,100);
      frame_ip.setResizable(false);//사이즈 조정x
      frame_ip.setLocationRelativeTo(null);
      
      JFrame frame_name=new JFrame();
      frame_name.setSize(300,100);   
      frame_name.setResizable(false);//사이즈 조정x
      frame_name.setLocationRelativeTo(null);

      
      JPanel contentPane1;
      contentPane1 = new JPanel();
      contentPane1.setBorder(new EmptyBorder(5, 5, 5, 5));
      contentPane1.setLayout(new BorderLayout(0, 0));

      JLabel lblIpChecked = new JLabel("IP checked!");
      lblIpChecked.setBackground(new Color(102, 51, 51));
      //lblIpChecked.setForeground(new Color(255, 0, 0));
      lblIpChecked.setForeground(Color.RED);
      lblIpChecked.setFont(new Font("Calibri", Font.BOLD, 30));
      lblIpChecked.setHorizontalAlignment(SwingConstants.CENTER);

      
      contentPane1.add(lblIpChecked, BorderLayout.CENTER);
      contentPane1.setBackground(Color.white);
      frame_ip.add(contentPane1, BorderLayout.CENTER);
      JPanel contentPane2;
      contentPane2 = new JPanel();
      contentPane2.setBorder(new EmptyBorder(5, 5, 5, 5));
      contentPane2.setLayout(new BorderLayout(0, 0));

      JLabel lblIpChecked2 = new JLabel("Name checked!");
      lblIpChecked2.setBackground(new Color(102, 51, 51));
      lblIpChecked2.setForeground(new Color(255, 0, 0));
      lblIpChecked2.setFont(new Font("Calibri", Font.BOLD, 30));
      lblIpChecked2.setHorizontalAlignment(SwingConstants.CENTER);

      
      contentPane2.add(lblIpChecked2, BorderLayout.CENTER);
      contentPane2.setBackground(Color.white);
      frame_name.add(contentPane2, BorderLayout.CENTER);


      frame=new JFrame();

      
      ImagePanel welcomePanel=new ImagePanel(new ImageIcon("C:\\Users\\정은서\\eclipse-workspace\\Project_Socket\\src\\image\\draw.jpg").getImage());

      
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
      JButton submit_ip=new JButton("Submit");
      submit_ip.setFont(new Font("Arial Black",Font.PLAIN,22));
      submit_ip.setBounds(375, 168, 137, 50);
      submit_ip.setBackground(Color.WHITE);
      
      welcomePanel.add(submit_ip);

      submit_ip.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) 
            {
               IPADDRESS= txtEnterIpAddress.getText();

               check_IP=1;

               frame_ip.setVisible(true);
               
            }

         });


      JLabel lblName = new JLabel("Name:");
      lblName.setFont(new Font("Calibri", Font.BOLD, 22));
      lblName.setBounds(70, 234, 137, 45);
      welcomePanel.add(lblName);



      txtEnterYourName = new JTextField();
      txtEnterYourName.setToolTipText("Enter your name");
      txtEnterYourName.setText("Enter your name");
      txtEnterYourName.setBounds(161, 238, 200, 50);
      welcomePanel.add(txtEnterYourName);
      txtEnterYourName.setColumns(10);


      JButton submit_name = new JButton("Submit");

      submit_name.setFont(new Font("Arial Black",Font.PLAIN,22));
      submit_name.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) 
         {
            NAME=txtEnterYourName.getText();

            check_name=1;



            frame_name.setVisible(true);

         }

      });

      submit_name.setBackground(Color.WHITE);
      submit_name.setBounds(375, 238, 137, 50);

      welcomePanel.add(submit_name);

      JLabel lblTeam = new JLabel("Team:");

      lblTeam.setFont(new Font("Calibri", Font.BOLD, 22));
      lblTeam.setBounds(70, 305, 137, 50);
      welcomePanel.add(lblTeam);



      JButton btnNewButton = new JButton("A");

      btnNewButton.setFont(new Font("Agency FB", Font.BOLD, 52));
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



            frame.setVisible(false);
         }

      });







      btnNewButton.setBounds(161, 302, 80, 65);







      welcomePanel.add(btnNewButton);







   







      JButton btnB = new JButton("B");

      btnB.setFont(new Font("Agency FB", Font.BOLD, 52));
      btnB.setBackground(Color.WHITE);






      btnB.setBounds(281, 302, 80, 65);

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



            frame.setVisible(false);

         }
         });

      welcomePanel.add(btnB);
      JLabel lblRelaySketch = new JLabel("RELAY SKETCH");
      lblRelaySketch.setForeground(Color.WHITE);
      lblRelaySketch.setFont(new Font("Abadi", Font.BOLD| Font.ITALIC , 54));
      lblRelaySketch.setBounds(400, 24, 470, 82);
      //lblRelaySketch.fillRect()
      welcomePanel.add(lblRelaySketch);

      frame.setResizable(false);//사이즈 조정x

      frame.setLocationRelativeTo(null);

      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   }
}

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