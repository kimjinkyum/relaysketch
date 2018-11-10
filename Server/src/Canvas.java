import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Canvas {
	int x=50, y_s=80, y_b=90;
	JPanel can;
	public Canvas(BufferedImage k) throws IOException, InterruptedException {
		
     JLabel paint=new JLabel(new ImageIcon(k));
     can.add(paint);
     can.setVisible(true);
	}

	
	
	
	
	
	
	
	
}
