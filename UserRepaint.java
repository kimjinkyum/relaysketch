import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Stack;
import javax.imageio.ImageIO;
import javax.swing.*;


public class UserRepaint extends JFrame implements Serializable {  

 JPanel paint_panel; // a drawing board
 JMenuBar menubar; // menu bar
 JMenu thickness_mu; //Select two menus list
 JButton eraser_bt;//eraser button
 JButton allclean_bt;//clean all button
 JButton brush_bt;//brush button
 JButton colorSelect_bt;//color button
 JButton image_bt;//send and save button 
 Graphics graphics; // declaring for use Graphics2D class
 Graphics2D g;//Variables for storing image files
 Graphics2D g1; // Variables for storing image files
  //int top = -1 ; // function for undo & redo >> When there is nothing, then -1


 JMenuItem[] thickness_item = new JMenuItem[5]; // lists for thick menu
 JMenuItem[] figure_item = new JMenuItem[5];// lists for figure menu
 public final static int ERASER = 0; // Constant for Shape or Pencil Type
 public final static int PENCIL = 1; //use pencil thickness for drawing
 public final static int RESET =5;//use all clean
 public static int check=0;
 public static int isend=0;
 ArrayList<Figure_Info> figureinfo = new ArrayList<Figure_Info>(); // repaint함수 호출 시 이전 정보들을 부르기 위함
 Stack<Figure_Info> trashcanStack = new Stack<Figure_Info>();
 
 /**
  * for sketching
  */
 ArrayList<Integer> startXpoints = new ArrayList<Integer>();//set startXpoint
 ArrayList<Integer> startYpoints = new ArrayList<Integer>();//set startYpoint
 ArrayList<Integer> endXpoints = new ArrayList<Integer>(); //set endXpoint
 ArrayList<Integer> endYpoints = new ArrayList<Integer>();//set endYpoint
 BufferedImage imgBuffer = new BufferedImage(1170,600,BufferedImage.TYPE_INT_RGB); // for saving image

 ////////////////////// Variables for Default Settings /////////////////////


 int mousecount=0; // variable for line
 int startXforsave;
 int startYforsave;
 int currentthickness;
 Color currentColor;//using color now

 boolean isShift = false; // When the shift key comes in, the moment the shift key disappears to true > false!
 boolean ismove = false; // Change the mouse to true when it is clicked, and change to false when the mouse is reset.
 // If ismove is true in mouse dragged, do it!
 int drawing_type = PENCIL; 
 Color selectedColor = Color.BLACK; // If any color is selected, here!
 int thickness = 4; // The basic line thickness is two.
 int startX; // Location of Start x coordinates
 int startY; //Location of Start Y coordinates
 int endX; // Location of End x coordinates
 int endY; // Location of End y coordinates
 boolean isSketch=true;  // It's the version that follows the mouse.
 // Both types of pencils are sccth version, but lines and shapes are not smart version.


 //////////////////////////////////Settings for second frame///////////////////////
 JMenuBar Textmenubar;
 JTextField font_field;
 JTextField text_field;
 JTextArea output_field;
 JLabel font_label;
 JLabel enter_label;
 JLabel enter_label2;
 JMenu font_mu;
 JButton color_bt, clear_bt;
 JMenuItem[] font_item = new JMenuItem[10];
 Color textColor = Color.BLACK;
 Font selectedFont;
 String fontSize="10";
 String text="";
 String fontType="굴림";
 String text2="";
 //ArrayList<TextInfo> textinfo = new ArrayList<TextInfo>();
 //TextInfo t;
 @Override
 
 public void paint(Graphics graphics) {
    if(isend==1)
      {
         dispose();//if send the drawing, then dispose the drwing panel
      }
  super.paint(graphics);
  this.graphics = graphics;
  graphics = getGraphics(); //reset graphic
  g = (Graphics2D)graphics; //reason for using 2D:the thickness of a pen 
  //Convert existing graphics variables to -> Graphics2D
  g1 = (Graphics2D)graphics; // variable for saving image
  g1 = (Graphics2D) imgBuffer.getGraphics();//buffer for drawing graphic
  g1.setColor(Color.WHITE);//set white for drawing panel background 
  g1.fillRect(0, 0, 1170, 600);//about rectangle
  currentColor = selectedColor; // It re-draws everything from here, so it remembers the current thickness and color.
  currentthickness = thickness;//set thickness used now
  g.setColor(selectedColor); // Color of lines to be drawn = set to selectedColor
  g.setStroke(new BasicStroke(thickness,BasicStroke.CAP_ROUND,0));
  g1.setColor(selectedColor); //Color of lines to be drawn = set to selectedColor
  g1.setStroke(new BasicStroke(thickness,BasicStroke.CAP_ROUND,0));

  
} //paint closing parenthesis
 
 
 //------------------------------ constructor ---------------------------------------------//

 public UserRepaint() { //JFrame for using drawing
    
  setLayout(null); 
  setTitle("그림판"); 
  createMenu();
  setBackground(Color.WHITE);
  setSize(1035,540); // width,heigth
  setLocation(100,50); // location of main frame
  setVisible(true); //show main frame
  
  eraser_bt = new JButton("Eraser");
  eraser_bt.setBorderPainted(false);
  //eraser_bt.setContentAreaFilled(false);
  eraser_bt.setFont(new Font("굴림",Font.PLAIN,22));
  eraser_bt.setBackground(Color.WHITE);
  brush_bt = new JButton("Pencil");
  brush_bt.setBorderPainted(false);
  brush_bt.setFont(new Font("함초롱돋움",Font.PLAIN,22));
  brush_bt.setBackground(Color.WHITE);
  colorSelect_bt = new JButton("COLOR");
  colorSelect_bt.setBorderPainted(false);
  colorSelect_bt.setFont(new Font("함초롱돋움",Font.PLAIN,22));
  colorSelect_bt.setBackground(Color.WHITE);
  allclean_bt = new JButton("All Clean");
  allclean_bt.setForeground(Color.RED);
  allclean_bt.setBorderPainted(false);
  allclean_bt.setFont(new Font("함초롱돋움",Font.PLAIN,22));
  allclean_bt.setBackground(Color.WHITE);
  
  image_bt = new JButton("Save");
  image_bt.setBorderPainted(false);
  image_bt.setForeground(Color.BLUE);
  image_bt.setFont(new Font("함초롱돋움",Font.BOLD,22));
  image_bt.setBackground(Color.WHITE); 

  
  paint_panel = new JPanel();
  
  paint_panel.setBackground(Color.WHITE);//set background of panel white 
  paint_panel.setLayout(null);
  
  eraser_bt.setBounds(35,10,150,40);
  brush_bt.setBounds(195,10,150,40);

  allclean_bt.setBounds(355,10,150,40);
  colorSelect_bt.setBounds(515,10,150,40);

  image_bt.setBounds(835,10,150,40);
  paint_panel.setBounds(10,60,1170,470);
  paint_panel.setSize(1000,400);
  add(eraser_bt);  
  add(brush_bt);
  add(colorSelect_bt);
  add(allclean_bt);

  add(image_bt);

  add(paint_panel);

  //////////////////////////////// Add actionlistener to all buttons /////////////////////////
  addWindowListener(new WindowAdapter() {
   public void windowClosing(WindowEvent e) {
    System.exit(0);
   }
  });
  //**keyboard listener
  addKeyListener(new KeyListener() { //When shift key is pressed, isshift is true.
   @Override
   public void keyPressed(KeyEvent e) {
    if(e.getModifiers()==1) isShift=true;
    //System.out.println(isShift);
   }
   @Override
   public void keyReleased(KeyEvent e) {
    isShift = false;
   }
   @Override
   public void keyTyped(KeyEvent e) {}
  });
  
  //**image button listener
  image_bt.addActionListener(new ActionListener() {
   @Override
   public void actionPerformed(ActionEvent e) {
	  
      if(isend==1)
      {  dispose();//if image is saved, then close the frame.
      }else {
    File writeFile = new File("C:\\2-2\\client_paint.png");//save drawing image to C:\\2-2\\client_paint.png.
    try {
     ImageIO.write(imgBuffer,"jpg",writeFile);
     Client.check=1;
     dispose();//if image is saved, then close the frame.
    } catch (IOException e1) {
     e1.printStackTrace();
    }
    imgBuffer = null;
    imgBuffer = new BufferedImage(1170,600,BufferedImage.TYPE_INT_RGB);
    g1 = (Graphics2D) imgBuffer.getGraphics();
    g1.setColor(Color.WHITE);
   }}
  });
  //**allclean button listener
  allclean_bt.addActionListener(new ActionListener() {
   public void actionPerformed(ActionEvent e) {
    figureinfo.clear();
    //top=-1;
    drawing_type = RESET;
    repaint();
   }
  });
  //**eraser button listenr
  eraser_bt.addActionListener(new ActionListener() {
   public void actionPerformed(ActionEvent e) {
    drawing_type= ERASER;
    selectedColor=Color.WHITE;
    isSketch=true; // sketch mode
   }
  });
  //**brush button listner
  brush_bt.addActionListener(new ActionListener() {
   public void actionPerformed(ActionEvent e) {
    drawing_type= PENCIL;
    selectedColor=Color.BLACK;
    isSketch=true;
   }
  });
  //**color button listenr
  colorSelect_bt.addActionListener(new ActionListener() { 
   public void actionPerformed(ActionEvent e) {
      if(isend==1)
      {
         dispose();//if image is saved, then close the frame.
      }
    JColorChooser chooser = new JColorChooser();
    selectedColor =chooser.showDialog(null,"Color",Color.ORANGE);
   }
  });
  ///////////add mouseListener and mouseMotionListener to paint_panel///////////
  PaintMouseListener paintmouselistener = new PaintMouseListener();
  PaintMouseMotionListener paintmousemotionlistener = new PaintMouseMotionListener();
  paint_panel.addMouseListener(paintmouselistener);
  paint_panel.addMouseMotionListener(paintmousemotionlistener);
 }
 
 
 //** menu items listener
 public class TextMenuActionListener implements ActionListener{
  @Override
  public void actionPerformed(ActionEvent e) {
     if(isend==1)
  {
      dispose();//if image is saved, then close the frame.
   }
   //select font type
   String str = e.getActionCommand();
   switch(str) {
   case "굴림" :
    fontType = "굴림";
    break;
   case "궁서" :
    fontType = "궁서";
    break;
   case "맑은고딕" :
    fontType = "맑은고딕";
    break;
   case "돋움" :
    fontType = "돋움";
    break;
   case "함초롬돋움" :
    fontType = "함초롬돋움";
    break;
   case "나눔스퀘어라운드" :
    fontType = "나눔스퀘어라운드";
    break;
   }
  }
 }
 /**
  * GUI for text menu
  */
 void createTextMenu() {
  Textmenubar = new JMenuBar();
  Textmenubar.setBackground(Color.WHITE);
  font_mu = new JMenu("Font");
  font_mu.setFont(new Font("함초롱돋움",Font.PLAIN,15));
  TextMenuActionListener textmenulistener = new TextMenuActionListener();
  font_item[0] = new JMenuItem("굴림");
  font_item[1] = new JMenuItem("궁서");
  font_item[2] = new JMenuItem("맑은고딕");
  font_item[3] = new JMenuItem("돋움");
  font_item[4] = new JMenuItem("함초롬돋움");
  font_item[5] = new JMenuItem("나눔스퀘어라운드");
  
  for(int i=0 ; i<6 ; i++) {
   font_item[i].addActionListener(textmenulistener);
   font_item[i].setBackground(Color.WHITE);
   font_item[i].setFont(new Font("함초롱돋움",Font.PLAIN,12));
   font_mu.add(font_item[i]);
   if(i==5) continue;
   font_mu.addSeparator();
  }
  
  Textmenubar.add(font_mu);
 }
 /**
  * GUI for menu
  */
 void createMenu() { 
  menubar = new JMenuBar(); // create menu bar
  menubar.setBackground(new Color(213,213,213)); 
  thickness_mu = new JMenu("굵기"); // create "굵기"menu
  thickness_mu.setFont(new Font("함초롬바탕",Font.PLAIN,15));
  

  MenuActionListener menulistener = new MenuActionListener();
  thickness_item[0] = new JMenuItem("2");
  thickness_item[1] = new JMenuItem("4");
  thickness_item[2] = new JMenuItem("8");
  thickness_item[3] = new JMenuItem("10");
  
  for(int i=0 ; i<4 ; i++) {
   thickness_item[i].setFont(new Font("함초롬바탕",Font.PLAIN,15));
   thickness_item[i].addActionListener(menulistener);
   thickness_mu.add(thickness_item[i]); 
   if(i==3) continue;
   thickness_mu.addSeparator(); // separation line
  }

  menubar.add(thickness_mu); // Add a thickness menu to the menu Bar
  setJMenuBar(menubar); //setting menu bar
 }
 // ----------------------------- MenuActionListener class -------------------------------------//
 class MenuActionListener implements ActionListener{
  public void actionPerformed(ActionEvent e) {
   //select thickness
   String str = e.getActionCommand();
   switch(str) {
   case "2" :
    thickness = 2;
    break;
   case "4" :
    thickness = 4;
    break;
   case "8" :
    thickness = 8;
    break;
   case "10" :
    thickness = 10;
    break;
    //select figure type
   }
   
  }
 }

 // ---------------------------------- PaintMouseListener class  -------------------------------------//
 public class PaintMouseListener implements MouseListener{
  public void mousePressed(MouseEvent e) {
     if(isend==1)
      {
         dispose();//if image is saved, then close the frame.
      }
   startX = e.getX(); // set start x point
   startY = e.getY(); // set start y point
   startXforsave = startX; // When pressed, remember the starting point.
   startYforsave = startY;
   ismove = true;  // for move
  }
  public void mouseClicked(MouseEvent e) {}
  public void mouseEntered(MouseEvent e) {}
  public void mouseExited(MouseEvent e) {}
  public void mouseReleased(MouseEvent e) {
   ismove = false;
   endX=e.getX(); // When the mouse falls, remember the last point.
   endY=e.getY();
 
   Figure_Info fi = new Figure_Info(); 
   
   fi.drawing_type = drawing_type; 

    fi.startXpoints = (ArrayList<Integer>)startXpoints.clone();
    fi.startYpoints = (ArrayList<Integer>)startYpoints.clone();
    fi.endXpoints = (ArrayList<Integer>)endXpoints.clone();
    fi.endYpoints = (ArrayList<Integer>)endYpoints.clone();
    fi.startX = startXforsave;
    fi.startY = startYforsave;
    fi.endX = endX;
    fi.endY = endY;
    fi.selectedcolor = selectedColor;
    fi.thickness = thickness;
    
   figureinfo.add(fi); // save to arraylist
   startXpoints.clear();
   startYpoints.clear();
   endXpoints.clear();
   endYpoints.clear();
  }
 }
 // ---------------------------------- PaintMouseMotionListener class  -------------------------------------//
 public class PaintMouseMotionListener implements MouseMotionListener{
  public void mouseDragged(MouseEvent e) { 
   if(ismove) {
    endX = e.getX();
    endY = e.getY();
 
    
    if(drawing_type == PENCIL || drawing_type == ERASER ) {
     
     if(isShift==true) {
      makeShiftPosition(startX,startY,endX,endY);
     }
     
     g.setColor(selectedColor);
     g.setStroke(new BasicStroke(thickness,BasicStroke.CAP_ROUND,0));//set type of stroke
     g.drawLine(startX+10, startY+121, endX+10, endY+121);
     g1.setColor(selectedColor); 
     g1.setStroke(new BasicStroke(thickness,BasicStroke.CAP_ROUND,0));//set type of stroke
     g1.drawLine(startX+10, startY+121, endX+10, endY+121);
     startXpoints.add(startX+10);
     startYpoints.add(startY+121);
     endXpoints.add(endX+10);
     endYpoints.add(endY+121);
    }
    
  //Only when it's in the shape of a sketch, change the position of the start and end to continue with the dot.
    if(isSketch==true) { 
     startX = endX;
     startY = endY;
    }
   }
  }
  public void mouseMoved(MouseEvent e) {}
 }
 public void makeShiftPosition(int startX, int startY, int endX, int endY) {
  if(Math.abs(startX-endX)>=Math.abs(startY-endY)) {//When X change rate is greater than Y change rate >>fix Y
   System.out.println("big: X");
   endY=startY;
  }
  else {
   endX=startX;
  }
 }

 // ---------------------------------- main method -------------------------------------//
 public static void main(String[] args) {
  UserRepaint q=new UserRepaint();  
 }

}