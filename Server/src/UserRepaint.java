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

 JPanel paint_panel; // 그림그릴 판
 JMenuBar menubar; // 메뉴바
 JMenu thickness_mu,figureSelect_mu; //두 개의 메뉴선택 list
 JButton eraser_bt;
 JButton allclean_bt;
 JButton brush_bt;
 JButton colorSelect_bt;
 JButton image_bt;
 JButton fill_bt;
 Graphics graphics; // Graphics2D 클래스 사용을 위한 선언
 Graphics2D g;
 Graphics2D g1; // 이미지 파일 저장을 위한 변수
  //int top = -1 ; // undo & redo를 위한 함수 >> 아무 것도 없을 때 -1이다.


 JMenuItem[] thickness_item = new JMenuItem[5]; // menu에 들어갈 각 항목들
 JMenuItem[] figure_item = new JMenuItem[5];
 public final static int ERASER = 0; // 도형 또는 pencil 종류를 위한 상수
 public final static int PENCIL = 1; 
 public final static int LINE = 2;
 public final static int RECTANGLE = 3;
 public final static int CIRCLE = 4;
 public final static int RESET =5;
 public static int check=0;

 ArrayList<Figure_Info> figureinfo = new ArrayList<Figure_Info>(); // repaint함수 호출 시 이전 정보들을 부르기 위함
 Stack<Figure_Info> trashcanStack = new Stack<Figure_Info>();
 
 
 ArrayList<Integer> startXpoints = new ArrayList<Integer>(); // sketch위한.
 ArrayList<Integer> startYpoints = new ArrayList<Integer>();
 ArrayList<Integer> endXpoints = new ArrayList<Integer>(); 
 ArrayList<Integer> endYpoints = new ArrayList<Integer>();
 BufferedImage imgBuffer = new BufferedImage(1170,600,BufferedImage.TYPE_INT_RGB); // 이미지 저장을 위한

 ////////////////////// 기본 설정을 위한 변수들 /////////////////////


 int mousecount=0; // line을 위한 변수
 int startXforsave;
 int startYforsave;
 int currentthickness;
 Color currentColor;

 boolean isShift = false; // 쉬프트 키가 들어오면, true로 쉬프트 키가 없어지는 순간 > false로 ..!
 boolean ismove = false; // 마우스가 클릭되면 true로 바꾸고 마우스 released되면 false로 바꾸어서
 // mouse Dragged에서 만약 ismove가 true이면, 하도록 한다!
 int drawing_type = PENCIL; 
 Color selectedColor = Color.BLACK; // 어떤 색상이 선택 되면 여기로!
 //Color preSelectedColor = Color.BLACK; // 잔상이 남는 것을 지우고 다시 원색으로 칠하기 위한 함수.
 int thickness = 2; // 기본 선 굵기는 2로 한다
 int startX; // 시작 x좌표의 위치
 int startY; //시작 Y좌표의 위치
 int endX; // 끝 X좌표 위치
 int endY; // 끝 Y 좌표 위치
 boolean isSketch=true;  // 마우스를 따라서 그려지는 버전이냐 아니냐
 // pencil종류 모두는 skecth버전이지만, line이나 도형은 skecth 버전이 아니다.


 //////////////////////////////////2번째 프레임을 위한 설정///////////////////////
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
  super.paint(graphics);
  this.graphics = graphics;
  graphics = getGraphics(); // 그래픽 초기화
  g = (Graphics2D)graphics; // 2D 사용 이유 : 펜의 굵기와 관련..!
  // 기존 graphics 변수를 -> Graphics2D로 변환
  g1 = (Graphics2D)graphics; // 이미지 저장을 위한 변수
  g1 = (Graphics2D) imgBuffer.getGraphics();
  g1.setColor(Color.WHITE);
  g1.fillRect(0, 0, 1170, 600);
  currentColor = selectedColor; // 이 밑에서 모든 것을 다시 그려 주기 때문에 현재의 두께와 색상을 기억해준다.
  currentthickness = thickness;
  g.setColor(selectedColor); // 그려질 선의 색상 = selectedColor로 설정
  g.setStroke(new BasicStroke(thickness,BasicStroke.CAP_ROUND,0));
  g1.setColor(selectedColor); // 그려질 선의 색상 = selectedColor로 설정
  g1.setStroke(new BasicStroke(thickness,BasicStroke.CAP_ROUND,0));
   
  if(drawing_type == CIRCLE){ // 원 그리기
   if(endX-startX<0 && endY-startY<0) { // 사각형이 4사분면에 있을때.
    g.drawOval(endX+10, endY+121, Math.abs(endX-startX)+10, Math.abs(endY-startY)+10);
    g1.drawOval(endX+10, endY+121, Math.abs(endX-startX)+10, Math.abs(endY-startY)+10);
   }
   else if(endX-startX>=0 && endY-startY<0) { // 사각형이 1사분면에 있을 때
    g.drawOval(startX+10, endY+121, Math.abs(endX-startX)+10, Math.abs(endY-startY)+10);
    g1.drawOval(startX+10, endY+121, Math.abs(endX-startX)+10, Math.abs(endY-startY)+10);
   }
   else if(endX-startX<0 && endY-startY>=0) { // 사각형이 3사분면에 있을 때.
    g.drawOval(endX+10, startY+121, Math.abs(endX-startX)+10, Math.abs(endY-startY)+10);
    g1.drawOval(endX+10, startY+121, Math.abs(endX-startX)+10, Math.abs(endY-startY)+10);
   }
   else { // 사각형이 정상적으로 2사분면에 있을 때
    g.drawOval(startX+10, startY+121, Math.abs(endX-startX)+10, Math.abs(endY-startY)+10);
    g1.drawOval(startX+10, startY+121, Math.abs(endX-startX)+10, Math.abs(endY-startY)+10);
   }
  }
  
} // paint 닫는 괄호
 
 
 //------------------------------ constructor ---------------------------------------------//

 public UserRepaint() { //JFrame이다.
  setLayout(null); //JFrame을 어떻게 구성해줄 것인가를 정한다.
  setTitle("그림판"); 
  createMenu();
  setBackground(Color.WHITE);
  setSize(1200,600); // width,heigth
  setLocation(100,50); // main프레임의 위치
  //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // X버튼 누르면 종료하도록
  setVisible(true); // main frame을 보이게 한다!
  
  eraser_bt = new JButton("지우개");
  eraser_bt.setBorderPainted(false);
  //eraser_bt.setContentAreaFilled(false);
  eraser_bt.setFont(new Font("함초롱돋움",Font.PLAIN,10));
  eraser_bt.setBackground(Color.WHITE);
  brush_bt = new JButton("pencil");
  brush_bt.setBorderPainted(false);
  brush_bt.setFont(new Font("함초롱돋움",Font.PLAIN,10));
  brush_bt.setBackground(Color.WHITE);
  colorSelect_bt = new JButton("색상");
  colorSelect_bt.setBorderPainted(false);
  colorSelect_bt.setFont(new Font("함초롱돋움",Font.PLAIN,12));
  colorSelect_bt.setBackground(Color.WHITE);
  allclean_bt = new JButton("All Clean");
  allclean_bt.setForeground(Color.RED);
  allclean_bt.setBorderPainted(false);
  allclean_bt.setFont(new Font("함초롱돋움",Font.PLAIN,12));
  allclean_bt.setBackground(Color.WHITE);
  
  image_bt = new JButton("save");
  image_bt.setBorderPainted(false);
  image_bt.setForeground(Color.BLUE);
  image_bt.setFont(new Font("함초롱돋움",Font.PLAIN,12));
  image_bt.setBackground(Color.WHITE); 

  fill_bt = new JButton("paint");
  fill_bt.setBorderPainted(false);
  fill_bt.setFont(new Font("함초롱돋움",Font.PLAIN,12));
  fill_bt.setBackground(Color.WHITE); 

  paint_panel = new JPanel();
  
  paint_panel.setBackground(Color.WHITE);
  paint_panel.setLayout(null);
  
  
  
      
  
  eraser_bt.setBounds(180,10,70,40);
  brush_bt.setBounds(260,10,70,40);

  allclean_bt.setBounds(500,10,100,40);
  colorSelect_bt.setBounds(610,10,70,40);
  fill_bt.setBounds(690,10,70,40);
  image_bt.setBounds(1100,10,70,40);
  paint_panel.setBounds(10,60,1170,470);
  
  add(eraser_bt);  
  add(brush_bt);
  add(colorSelect_bt);
  add(allclean_bt);
  add(fill_bt);
  add(image_bt);

  add(paint_panel);


  //graphics = getGraphics(); // 그래픽 초기화
  //g = (Graphics2D)graphics; // 2D 사용 이유 : 펜의 굵기와 관련..!
  // 기존 graphics 변수를 -> Graphics2D로 변환
  //g.setColor(selectedColor); // 그려질 선의 색상 = selectedColor로 설정

  //////////////////////////////// 모든 버튼에 actionlistener 추가 /////////////////////////
  addWindowListener(new WindowAdapter() {
   public void windowClosing(WindowEvent e) {
    System.exit(0);
   }
  });
  //**keyboard 리스너
  addKeyListener(new KeyListener() { /// shift 키가 눌러지면 isshift true로 하기위함.
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
  
  

  //**image 버튼 리스너
  image_bt.addActionListener(new ActionListener() {
   @Override
   public void actionPerformed(ActionEvent e) {
    
    //String imagepath = JOptionPane.showInputDialog("저장할 파일 경로와 이름을 입력하세요.");
    File writeFile = new File("C:\\2-2\\kkk.png");
    try {
     ImageIO.write(imgBuffer,"jpg",writeFile);
     Client.check=1;
     dispose();
    } catch (IOException e1) {
     // TODO Auto-generated catch block
     e1.printStackTrace();
    }
    imgBuffer = null;
    imgBuffer = new BufferedImage(1170,600,BufferedImage.TYPE_INT_RGB);
    g1 = (Graphics2D) imgBuffer.getGraphics();
    g1.setColor(Color.WHITE);
   }
  });
  //**allclean 버튼 리스너
  allclean_bt.addActionListener(new ActionListener() {
   public void actionPerformed(ActionEvent e) {
    figureinfo.clear();
    //top=-1;
    drawing_type = RESET;
    repaint();
   }
  });
  //**지우개 버튼 리스너
  eraser_bt.addActionListener(new ActionListener() {
   public void actionPerformed(ActionEvent e) {
    drawing_type= ERASER;
    selectedColor=Color.WHITE;
    isSketch=true; // sketch모드이다.
   }
  });
  //**브러쉬 버튼 리스너 >> 수정 필요 brush 버튼에 추가 사항들이 들어오면..! menu로 옮길 수도 있다!
  brush_bt.addActionListener(new ActionListener() {
   public void actionPerformed(ActionEvent e) {
    drawing_type= PENCIL;
    selectedColor=Color.BLACK;
    isSketch=true;
   }
  });
  //**색상 버튼 리스너
  colorSelect_bt.addActionListener(new ActionListener() { 
   public void actionPerformed(ActionEvent e) {
    JColorChooser chooser = new JColorChooser();
    selectedColor =chooser.showDialog(null,"Color",Color.ORANGE);
   }
  });
  /////////////////////////////paint_panel에 mouseListener와 mouseMotionListener추가 ////////
  PaintMouseListener paintmouselistener = new PaintMouseListener();
  PaintMouseMotionListener paintmousemotionlistener = new PaintMouseMotionListener();
  paint_panel.addMouseListener(paintmouselistener);
  paint_panel.addMouseMotionListener(paintmousemotionlistener);
 }
 //** 메뉴 아이템들 리스너
 public class TextMenuActionListener implements ActionListener{
  @Override
  public void actionPerformed(ActionEvent e) {
   // TODO Auto-generated method stub
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
 
 void createTextMenu() {
  // TODO Auto-generated method stub
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
  //setJMenuBar(Textmenubar);

 }
 void createMenu() { 
  menubar = new JMenuBar(); // 메뉴바 생성
  menubar.setBackground(new Color(213,213,213)); 
  thickness_mu = new JMenu("굵기"); // 메뉴 생성
  thickness_mu.setFont(new Font("함초롬바탕",Font.PLAIN,15));
  figureSelect_mu = new JMenu("도형"); // 메뉴 생성
  figureSelect_mu.setFont(new Font("함초롬바탕",Font.PLAIN,15));

  MenuActionListener menulistener = new MenuActionListener();
  thickness_item[0] = new JMenuItem("2");
  thickness_item[1] = new JMenuItem("4");
  thickness_item[2] = new JMenuItem("8");
  thickness_item[3] = new JMenuItem("10");
  
  for(int i=0 ; i<4 ; i++) {
   thickness_item[i].setFont(new Font("함초롬바탕",Font.PLAIN,15));
   thickness_item[i].addActionListener(menulistener); // actionlistener 달아준다.
   thickness_mu.add(thickness_item[i]); 
   if(i==3) continue;
   thickness_mu.addSeparator(); // 분리선 적어준다.
  }
  
  
  
  figure_item[0] = new JMenuItem("--  line");
  figure_item[1] = new JMenuItem("□  rectangle");
  figure_item[2] = new JMenuItem("○  circle");
  for(int i=0 ; i<3 ; i++) {
   figure_item[i].setFont(new Font("함초롬바탕",Font.PLAIN,15));
   figure_item[i].addActionListener(menulistener);
   figureSelect_mu.add(figure_item[i]);
   figureSelect_mu.addSeparator(); // 분리선 적어준다.
  }
  



  menubar.add(thickness_mu); // 메뉴바에 굵기 메뉴 추가
  menubar.add(figureSelect_mu); // 메뉴바에 도형 메뉴 추가
  setJMenuBar(menubar); // 메뉴바를 세팅
 }
 // ----------------------------- MenuActionListener class -------------------------------------//
 class MenuActionListener implements ActionListener{
  public void actionPerformed(ActionEvent e) {
   String str = e.getActionCommand();
   //System.out.println(str);
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
   case "--  line" :
    drawing_type = LINE;
    if(selectedColor == Color.WHITE) {
     selectedColor = Color.BLACK;
    }
    isSketch=false;
    break;
   case "□  rectangle" :
    drawing_type = RECTANGLE;
    if(selectedColor == Color.WHITE) {
     selectedColor = Color.BLACK;
    }
    isSketch=false;
    break;
  
   case "○  circle" :
    drawing_type = CIRCLE;
    if(selectedColor == Color.WHITE) {
     selectedColor = Color.BLACK;
    }
    isSketch=false;
    break;
   }
   
  }
 }

 // ---------------------------------- PaintMouseListener class  -------------------------------------//
 public class PaintMouseListener implements MouseListener{
  public void mousePressed(MouseEvent e) {
   startX = e.getX(); // 시작 x좌표 지정
   startY = e.getY(); // 시작 y좌표 지정
   //g.setStroke(new BasicStroke(thickness,BasicStroke.CAP_ROUND,0));
   startXforsave = startX; // 눌러질 때, 시작점을 기억한다.
   startYforsave = startY;
   ismove = true;  // 동작하게 한다.
   //Xpoints.clear();
   //Ypoints.clear();
   if(drawing_type == LINE) {
    mousecount++;
   }
   
  
  }
  public void mouseClicked(MouseEvent e) {}
  public void mouseEntered(MouseEvent e) {}
  public void mouseExited(MouseEvent e) {}
  public void mouseReleased(MouseEvent e) {
   ismove = false;
   endX=e.getX(); // 마우스가 떨어지면 마지막 지점을 기억한다.
   endY=e.getY();
   //if(isShift == true && drawing_type == LINE) makeShiftPosition(startX,startY,endX,endY); 
   
   Figure_Info fi = new Figure_Info(); // 들어온 도형 또는 라인 또는 기타 등등의 시작점과 끝점, 타입을 정해주는 함수
   
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
   //System.out.println("    thickness of if" + fi.thickness);
   //System.out.println("    fi.Xpoints size : "+(fi.getXpoints()).size());
   figureinfo.add(fi); // arrayList에 다음을 저장.
   
   //top++; // top 카운트를 1 증가시킨다.
   
   //System.out.println("top: " +top);
   //fi.printall();

   startXpoints.clear();
   startYpoints.clear();
   endXpoints.clear();
   endYpoints.clear();

   //fi.printall();
   //System.out.println("figureinfo of if : " + (figureinfo.get(sum)).thickness);
   //System.out.println("figureinfo of size : "+((figureinfo.get(sum)).Xpoints).size());
  }
 }
 // ---------------------------------- PaintMouseMotionListener class  -------------------------------------//
 public class PaintMouseMotionListener implements MouseMotionListener{
  public void mouseDragged(MouseEvent e) { 
   if(ismove) {
    endX = e.getX();
    endY = e.getY();
 
    
    if(drawing_type == PENCIL || drawing_type == ERASER || drawing_type == LINE ) {
     
     if(isShift==true) {
      makeShiftPosition(startX,startY,endX,endY);
     }
     
     g.setColor(selectedColor);
     g.setStroke(new BasicStroke(thickness,BasicStroke.CAP_ROUND,0));// stroke의 type설정
     g.drawLine(startX+10, startY+121, endX+10, endY+121);
     g1.setColor(selectedColor); 
     g1.setStroke(new BasicStroke(thickness,BasicStroke.CAP_ROUND,0));// stroke의 type설정
     g1.drawLine(startX+10, startY+121, endX+10, endY+121);
     startXpoints.add(startX+10);
     startYpoints.add(startY+121);
     endXpoints.add(endX+10);
     endYpoints.add(endY+121);
     if(drawing_type == LINE) {
      repaint();
     }
     //System.out.println("pencil!");
    }
    
   
    if(drawing_type == RECTANGLE ) { //마우스 이동시 계속 삭제 해준다.
     repaint(); 
    }
    if(drawing_type == CIRCLE ) {
     repaint();
    }
    if(isSketch==true) { // sketch 형태 일 때만 계속 start위치와 end 위치를 바꿔주면서 점을 찍으면서 이어나가도록
     startX = endX;
     startY = endY;
    }
   }
  }
  public void mouseMoved(MouseEvent e) {}
 }
 public void makeShiftPosition(int startX, int startY, int endX, int endY) {
  if(Math.abs(startX-endX)>=Math.abs(startY-endY)) {//X변화율이 Y변화율보다 클 때 >> Y를 고정
   System.out.println("big: X");
   endY=startY;
  }
  else {
   endX=startX;
  }
 }

 // ---------------------------------- main method -------------------------------------//
 public static void main(String[] args) {
  new UserRepaint();
 }

}
