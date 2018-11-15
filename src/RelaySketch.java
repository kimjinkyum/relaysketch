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
import javax.swing.JButton;
import javax.swing.JComboBox;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.BorderLayout;


public class RelaySketch {

	public static String IPADDRESS;
	public static int check_IP=0;
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
		frame=new JFrame();
		ImagePanel welcomePanel=new ImagePanel(new ImageIcon("C:\\Users\\김진겸\\eclipse-workspace\\Network\\src\\image\\pencil.jpg").getImage());
		frame.setSize(welcomePanel.getWidth(),welcomePanel.getHeight());
		frame.getContentPane().add(welcomePanel, BorderLayout.EAST);
		
		JLabel lblIpAddress = new JLabel("IP Address");
		lblIpAddress.setFont(new Font("Arial Black", Font.PLAIN, 22));
		lblIpAddress.setBounds(12, 165, 137, 45);
		welcomePanel.add(lblIpAddress);
		
		txtEnterIpAddress = new JTextField();
		txtEnterIpAddress.setText("Enter IP Address");
		txtEnterIpAddress.setToolTipText("Enter IP Address");
		txtEnterIpAddress.setBounds(161, 167, 200, 50);
		welcomePanel.add(txtEnterIpAddress);
		txtEnterIpAddress.setColumns(10);
		
		JButton submit_ip=new JButton("Submit");
		submit_ip.setFont(new Font("Arial Black",Font.PLAIN,22));
		submit_ip.setBounds(375, 168, 137, 37);
		
		welcomePanel.add(submit_ip);
		submit_ip.addActionListener(new ActionListener() 
	      {

	         @Override
	         public void actionPerformed(ActionEvent e) 
	         {
	        	 
	        	
	        	 IPADDRESS= txtEnterIpAddress.getText();
	        	 System.out.println(IPADDRESS);
	        	 check_IP=1;
	         }

	      });
		
		
		
		
		JLabel lblName = new JLabel("Name");
		lblName.setFont(new Font("Arial Black", Font.PLAIN, 22));
		lblName.setBounds(12, 234, 137, 45);
		welcomePanel.add(lblName);
		
		txtEnterYourName = new JTextField();
		txtEnterYourName.setToolTipText("Enter your name");
		txtEnterYourName.setText("Enter your name");
		txtEnterYourName.setBounds(161, 238, 200, 50);
		welcomePanel.add(txtEnterYourName);
		txtEnterYourName.setColumns(10);
		
		JLabel lblTeam = new JLabel("Team");
		lblTeam.setFont(new Font("Arial Black", Font.PLAIN, 22));
		lblTeam.setBounds(12, 305, 137, 50);
		welcomePanel.add(lblTeam);
		
		JButton btnNewButton = new JButton("A");
		btnNewButton.setFont(new Font("Agency FB", Font.BOLD, 52));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnNewButton.setBounds(161, 302, 80, 65);
		welcomePanel.add(btnNewButton);
	
	  JButton btnB = new JButton("B");
		btnB.setFont(new Font("Agency FB", Font.BOLD, 52));
		btnB.setBounds(281, 302, 80, 65);
		welcomePanel.add(btnB);
		
		JLabel lblRelaySketch = new JLabel("RELAY SKETCH");
		lblRelaySketch.setForeground(Color.GRAY);
		lblRelaySketch.setFont(new Font("Arial Rounded MT Bold", Font.BOLD | Font.ITALIC, 54));
		lblRelaySketch.setBounds(407, 24, 462, 82);
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
		setSize(new Dimension(img.getWidth(null),img.getHeight(null)));
		setPreferredSize(new Dimension(img.getWidth(null),img.getHeight(null)));
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