import java.awt.*;
import javax.print.DocFlavor.URL;
import javax.swing.*;

class GUI{
   
   public static void main(String[] args) {
      ImageIcon image[]= {new ImageIcon("C:\\Users\\������\\eclipse-workspace\\Figure_Info\\room_Image.png")};
      JFrame frame=new JFrame("relay sketch");
      frame.setIconImage(image[0].getImage());
      Toolkit toolkit=frame.getToolkit();
        Dimension dimension = toolkit.getScreenSize();
        
        // frame�� ����,���� ���̸� ���� �Ѵ�.
        frame.setSize(1000,700);
       // Toolkit Class���� ���� Screen�� ũ�⸦ �˸°� ���Ͽ� frame�� ��ũ����
       //  ������ ��ġ�� ���� �Ѵ�.
       frame.setLocation((int)(dimension.getWidth()/2)-500,(int)(dimension.getHeight()/2)-350);
      // frame�� ����� ����.
      frame.show();
   }
}