import java.awt.*;
import javax.print.DocFlavor.URL;
import javax.swing.*;

class GUI{
   
   public static void main(String[] args) {
      ImageIcon image[]= {new ImageIcon("C:\\Users\\정은서\\eclipse-workspace\\Figure_Info\\room_Image.png")};
      JFrame frame=new JFrame("relay sketch");
      frame.setIconImage(image[0].getImage());
      Toolkit toolkit=frame.getToolkit();
        Dimension dimension = toolkit.getScreenSize();
        
        // frame에 가로,세로 길이를 설정 한다.
        frame.setSize(1000,700);
       // Toolkit Class에서 얻어온 Screen의 크기를 알맞게 구하여 frame이 스크린에
       //  보여질 위치를 설정 한다.
       frame.setLocation((int)(dimension.getWidth()/2)-500,(int)(dimension.getHeight()/2)-350);
      // frame을 모니터 띄운다.
      frame.show();
   }
}