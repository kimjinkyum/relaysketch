import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Canvas extends JFrame {
	int x=50, y_s=80, y_b=90;
	 
	public Canvas(Image k) throws IOException {
		 setBounds(10,60,1500,1000);
		 getContentPane().setBounds(10,60,1500,1000);
		 	
     JLabel paint=new JLabel(new ImageIcon(k));
     paint.setBounds(10,60,1500,1000);
     getContentPane().add(paint);
     setVisible(true);
	}
}
