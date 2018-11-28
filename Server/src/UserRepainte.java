import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


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


	public class UserRepainte extends JFrame implements Serializable {  

	 JPanel paint_panel; // �׸��׸� ��
	 JMenuBar menubar; // �޴���
	 JMenu thickness_mu,figureSelect_mu; //�� ���� �޴����� list
	 JButton eraser_bt;
	 JButton allclean_bt;
	 JButton brush_bt;
	 JButton colorSelect_bt;
	 JButton image_bt;
	 JButton fill_bt;
	 Graphics graphics; // Graphics2D Ŭ���� ����� ���� ����
	 Graphics2D g;
	 Graphics2D g1; // �̹��� ���� ������ ���� ����
	  //int top = -1 ; // undo & redo�� ���� �Լ� >> �ƹ� �͵� ���� �� -1�̴�.


	 JMenuItem[] thickness_item = new JMenuItem[5]; // menu�� �� �� �׸��
	 JMenuItem[] figure_item = new JMenuItem[5];
	 public final static int ERASER = 0; // ���� �Ǵ� pencil ������ ���� ���
	 public final static int PENCIL = 1; 
	 public final static int LINE = 2;
	 public final static int RECTANGLE = 3;
	 public final static int CIRCLE = 4;
	 public final static int RESET =5;
	 public static int check=0;

	 ArrayList<Figure_Info> figureinfo = new ArrayList<Figure_Info>(); // repaint�Լ� ȣ�� �� ���� �������� �θ��� ����
	 Stack<Figure_Info> trashcanStack = new Stack<Figure_Info>();
	 
	 
	 ArrayList<Integer> startXpoints = new ArrayList<Integer>(); // sketch����.
	 ArrayList<Integer> startYpoints = new ArrayList<Integer>();
	 ArrayList<Integer> endXpoints = new ArrayList<Integer>(); 
	 ArrayList<Integer> endYpoints = new ArrayList<Integer>();
	 BufferedImage imgBuffer = new BufferedImage(1170,600,BufferedImage.TYPE_INT_RGB); // �̹��� ������ ����

	 ////////////////////// �⺻ ������ ���� ������ /////////////////////


	 int mousecount=0; // line�� ���� ����
	 int startXforsave;
	 int startYforsave;
	 int currentthickness;
	 Color currentColor;

	 boolean isShift = false; // ����Ʈ Ű�� ������, true�� ����Ʈ Ű�� �������� ���� > false�� ..!
	 boolean ismove = false; // ���콺�� Ŭ���Ǹ� true�� �ٲٰ� ���콺 released�Ǹ� false�� �ٲپ
	 // mouse Dragged���� ���� ismove�� true�̸�, �ϵ��� �Ѵ�!
	 int drawing_type = PENCIL; 
	 Color selectedColor = Color.BLACK; // � ������ ���� �Ǹ� �����!
	 //Color preSelectedColor = Color.BLACK; // �ܻ��� ���� ���� ����� �ٽ� �������� ĥ�ϱ� ���� �Լ�.
	 int thickness = 2; // �⺻ �� ����� 2�� �Ѵ�
	 int startX; // ���� x��ǥ�� ��ġ
	 int startY; //���� Y��ǥ�� ��ġ
	 int endX; // �� X��ǥ ��ġ
	 int endY; // �� Y ��ǥ ��ġ
	 boolean isSketch=true;  // ���콺�� ���� �׷����� �����̳� �ƴϳ�
	 // pencil���� ��δ� skecth����������, line�̳� ������ skecth ������ �ƴϴ�.


	 //////////////////////////////////2��° �������� ���� ����///////////////////////
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
	 String fontType="����";
	 String text2="";
	 //ArrayList<TextInfo> textinfo = new ArrayList<TextInfo>();
	 //TextInfo t;
	 @Override
	 public void paint(Graphics graphics) {
	  super.paint(graphics);
	  this.graphics = graphics;
	  graphics = getGraphics(); // �׷��� �ʱ�ȭ
	  g = (Graphics2D)graphics; // 2D ��� ���� : ���� ����� ����..!
	  // ���� graphics ������ -> Graphics2D�� ��ȯ
	  g1 = (Graphics2D)graphics; // �̹��� ������ ���� ����
	  g1 = (Graphics2D) imgBuffer.getGraphics();
	  g1.setColor(Color.WHITE);
	  g1.fillRect(0, 0, 1170, 600);
	  currentColor = selectedColor; // �� �ؿ��� ��� ���� �ٽ� �׷� �ֱ� ������ ������ �β��� ������ ������ش�.
	  currentthickness = thickness;
	  g.setColor(selectedColor); // �׷��� ���� ���� = selectedColor�� ����
	  g.setStroke(new BasicStroke(thickness,BasicStroke.CAP_ROUND,0));
	  g1.setColor(selectedColor); // �׷��� ���� ���� = selectedColor�� ����
	  g1.setStroke(new BasicStroke(thickness,BasicStroke.CAP_ROUND,0));
	   
	  if(drawing_type == CIRCLE){ // �� �׸���
	   if(endX-startX<0 && endY-startY<0) { // �簢���� 4��и鿡 ������.
	    g.drawOval(endX+10, endY+121, Math.abs(endX-startX)+10, Math.abs(endY-startY)+10);
	    g1.drawOval(endX+10, endY+121, Math.abs(endX-startX)+10, Math.abs(endY-startY)+10);
	   }
	   else if(endX-startX>=0 && endY-startY<0) { // �簢���� 1��и鿡 ���� ��
	    g.drawOval(startX+10, endY+121, Math.abs(endX-startX)+10, Math.abs(endY-startY)+10);
	    g1.drawOval(startX+10, endY+121, Math.abs(endX-startX)+10, Math.abs(endY-startY)+10);
	   }
	   else if(endX-startX<0 && endY-startY>=0) { // �簢���� 3��и鿡 ���� ��.
	    g.drawOval(endX+10, startY+121, Math.abs(endX-startX)+10, Math.abs(endY-startY)+10);
	    g1.drawOval(endX+10, startY+121, Math.abs(endX-startX)+10, Math.abs(endY-startY)+10);
	   }
	   else { // �簢���� ���������� 2��и鿡 ���� ��
	    g.drawOval(startX+10, startY+121, Math.abs(endX-startX)+10, Math.abs(endY-startY)+10);
	    g1.drawOval(startX+10, startY+121, Math.abs(endX-startX)+10, Math.abs(endY-startY)+10);
	   }
	  }
	  
	} // paint �ݴ� ��ȣ
	 
	 
	 //------------------------------ constructor ---------------------------------------------//

	 public UserRepainte() { //JFrame�̴�.
	  getContentPane().setLayout(null); //JFrame�� ��� �������� ���ΰ��� ���Ѵ�.
	  setTitle("�׸���"); 
	  createMenu();
	  setBackground(Color.WHITE);
	  setSize(1200,600); // width,heigth
	  setLocation(100,50); // main�������� ��ġ
	  //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // X��ư ������ �����ϵ���
	  setVisible(true); // main frame�� ���̰� �Ѵ�!
	  
	  eraser_bt = new JButton("���찳");
	  eraser_bt.setBorderPainted(false);
	  //eraser_bt.setContentAreaFilled(false);
	  eraser_bt.setFont(new Font("���ʷյ���",Font.PLAIN,10));
	  eraser_bt.setBackground(Color.WHITE);
	  brush_bt = new JButton("pencil");
	  brush_bt.setBorderPainted(false);
	  brush_bt.setFont(new Font("���ʷյ���",Font.PLAIN,10));
	  brush_bt.setBackground(Color.WHITE);
	  colorSelect_bt = new JButton("����");
	  colorSelect_bt.setBorderPainted(false);
	  colorSelect_bt.setFont(new Font("���ʷյ���",Font.PLAIN,12));
	  colorSelect_bt.setBackground(Color.WHITE);
	  allclean_bt = new JButton("All Clean");
	  allclean_bt.setForeground(Color.RED);
	  allclean_bt.setBorderPainted(false);
	  allclean_bt.setFont(new Font("���ʷյ���",Font.PLAIN,12));
	  allclean_bt.setBackground(Color.WHITE);
	  
	  image_bt = new JButton("save");
	  image_bt.setBorderPainted(false);
	  image_bt.setForeground(Color.BLUE);
	  image_bt.setFont(new Font("���ʷյ���",Font.PLAIN,12));
	  image_bt.setBackground(Color.WHITE); 

	  fill_bt = new JButton("paint");
	  fill_bt.setBorderPainted(false);
	  fill_bt.setFont(new Font("���ʷյ���",Font.PLAIN,12));
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
	  
	  getContentPane().add(eraser_bt);  
	  getContentPane().add(brush_bt);
	  getContentPane().add(colorSelect_bt);
	  getContentPane().add(allclean_bt);
	  getContentPane().add(fill_bt);
	  getContentPane().add(image_bt);

	  getContentPane().add(paint_panel);


	  //graphics = getGraphics(); // �׷��� �ʱ�ȭ
	  //g = (Graphics2D)graphics; // 2D ��� ���� : ���� ����� ����..!
	  // ���� graphics ������ -> Graphics2D�� ��ȯ
	  //g.setColor(selectedColor); // �׷��� ���� ���� = selectedColor�� ����

	  //////////////////////////////// ��� ��ư�� actionlistener �߰� /////////////////////////
	  addWindowListener(new WindowAdapter() {
	   public void windowClosing(WindowEvent e) {
	    System.exit(0);
	   }
	  });
	  //**keyboard ������
	  addKeyListener(new KeyListener() { /// shift Ű�� �������� isshift true�� �ϱ�����.
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
	  
	  

	  //**image ��ư ������
	  image_bt.addActionListener(new ActionListener() {
	   @Override
	   public void actionPerformed(ActionEvent e) {
	    
	    //String imagepath = JOptionPane.showInputDialog("������ ���� ��ο� �̸��� �Է��ϼ���.");
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
	  //**allclean ��ư ������
	  allclean_bt.addActionListener(new ActionListener() {
	   public void actionPerformed(ActionEvent e) {
	    figureinfo.clear();
	    //top=-1;
	    drawing_type = RESET;
	    repaint();
	   }
	  });
	  //**���찳 ��ư ������
	  eraser_bt.addActionListener(new ActionListener() {
	   public void actionPerformed(ActionEvent e) {
	    drawing_type= ERASER;
	    selectedColor=Color.WHITE;
	    isSketch=true; // sketch����̴�.
	   }
	  });
	  //**�귯�� ��ư ������ >> ���� �ʿ� brush ��ư�� �߰� ���׵��� ������..! menu�� �ű� ���� �ִ�!
	  brush_bt.addActionListener(new ActionListener() {
	   public void actionPerformed(ActionEvent e) {
	    drawing_type= PENCIL;
	    selectedColor=Color.BLACK;
	    isSketch=true;
	   }
	  });
	  //**���� ��ư ������
	  colorSelect_bt.addActionListener(new ActionListener() { 
	   public void actionPerformed(ActionEvent e) {
	    JColorChooser chooser = new JColorChooser();
	    selectedColor =chooser.showDialog(null,"Color",Color.ORANGE);
	   }
	  });
	  /////////////////////////////paint_panel�� mouseListener�� mouseMotionListener�߰� ////////
	  PaintMouseListener paintmouselistener = new PaintMouseListener();
	  PaintMouseMotionListener paintmousemotionlistener = new PaintMouseMotionListener();
	  paint_panel.addMouseListener(paintmouselistener);
	  paint_panel.addMouseMotionListener(paintmousemotionlistener);
	 }
	 //** �޴� �����۵� ������
	 public class TextMenuActionListener implements ActionListener{
	  @Override
	  public void actionPerformed(ActionEvent e) {
	   // TODO Auto-generated method stub
	   String str = e.getActionCommand();
	   switch(str) {
	   case "����" :
	    fontType = "����";
	    break;
	   case "�ü�" :
	    fontType = "�ü�";
	    break;
	   case "�������" :
	    fontType = "�������";
	    break;
	   case "����" :
	    fontType = "����";
	    break;
	   case "���ʷҵ���" :
	    fontType = "���ʷҵ���";
	    break;
	   case "�������������" :
	    fontType = "�������������";
	    break;
	   }
	  }
	 }
	 
	 void createTextMenu() {
	  // TODO Auto-generated method stub
	  Textmenubar = new JMenuBar();
	  Textmenubar.setBackground(Color.WHITE);
	  font_mu = new JMenu("Font");
	  font_mu.setFont(new Font("���ʷյ���",Font.PLAIN,15));
	  TextMenuActionListener textmenulistener = new TextMenuActionListener();
	  font_item[0] = new JMenuItem("����");
	  font_item[1] = new JMenuItem("�ü�");
	  font_item[2] = new JMenuItem("�������");
	  font_item[3] = new JMenuItem("����");
	  font_item[4] = new JMenuItem("���ʷҵ���");
	  font_item[5] = new JMenuItem("�������������");
	  
	  for(int i=0 ; i<6 ; i++) {
	   font_item[i].addActionListener(textmenulistener);
	   font_item[i].setBackground(Color.WHITE);
	   font_item[i].setFont(new Font("���ʷյ���",Font.PLAIN,12));
	   font_mu.add(font_item[i]);
	   if(i==5) continue;
	   font_mu.addSeparator();
	  }
	  
	  Textmenubar.add(font_mu);
	  //setJMenuBar(Textmenubar);

	 }
	 void createMenu() { 
	  menubar = new JMenuBar(); // �޴��� ����
	  menubar.setBackground(new Color(213,213,213)); 
	  thickness_mu = new JMenu("����"); // �޴� ����
	  thickness_mu.setFont(new Font("���ʷҹ���",Font.PLAIN,15));
	  figureSelect_mu = new JMenu("����"); // �޴� ����
	  figureSelect_mu.setFont(new Font("���ʷҹ���",Font.PLAIN,15));

	  MenuActionListener menulistener = new MenuActionListener();
	  thickness_item[0] = new JMenuItem("2");
	  thickness_item[1] = new JMenuItem("4");
	  thickness_item[2] = new JMenuItem("8");
	  thickness_item[3] = new JMenuItem("10");
	  
	  for(int i=0 ; i<4 ; i++) {
	   thickness_item[i].setFont(new Font("���ʷҹ���",Font.PLAIN,15));
	   thickness_item[i].addActionListener(menulistener); // actionlistener �޾��ش�.
	   thickness_mu.add(thickness_item[i]); 
	   if(i==3) continue;
	   thickness_mu.addSeparator(); // �и��� �����ش�.
	  }
	  
	  
	  
	  figure_item[0] = new JMenuItem("--  line");
	  figure_item[1] = new JMenuItem("��  rectangle");
	  figure_item[2] = new JMenuItem("��  circle");
	  for(int i=0 ; i<3 ; i++) {
	   figure_item[i].setFont(new Font("���ʷҹ���",Font.PLAIN,15));
	   figure_item[i].addActionListener(menulistener);
	   figureSelect_mu.add(figure_item[i]);
	   figureSelect_mu.addSeparator(); // �и��� �����ش�.
	  }
	  



	  menubar.add(thickness_mu); // �޴��ٿ� ���� �޴� �߰�
	  menubar.add(figureSelect_mu); // �޴��ٿ� ���� �޴� �߰�
	  setJMenuBar(menubar); // �޴��ٸ� ����
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
	   case "��  rectangle" :
	    drawing_type = RECTANGLE;
	    if(selectedColor == Color.WHITE) {
	     selectedColor = Color.BLACK;
	    }
	    isSketch=false;
	    break;
	  
	   case "��  circle" :
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
	   startX = e.getX(); // ���� x��ǥ ����
	   startY = e.getY(); // ���� y��ǥ ����
	   //g.setStroke(new BasicStroke(thickness,BasicStroke.CAP_ROUND,0));
	   startXforsave = startX; // ������ ��, �������� ����Ѵ�.
	   startYforsave = startY;
	   ismove = true;  // �����ϰ� �Ѵ�.
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
	   endX=e.getX(); // ���콺�� �������� ������ ������ ����Ѵ�.
	   endY=e.getY();
	   //if(isShift == true && drawing_type == LINE) makeShiftPosition(startX,startY,endX,endY); 
	   
	   Figure_Info fi = new Figure_Info(); // ���� ���� �Ǵ� ���� �Ǵ� ��Ÿ ����� �������� ����, Ÿ���� �����ִ� �Լ�
	   
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
	   figureinfo.add(fi); // arrayList�� ������ ����.
	   
	   //top++; // top ī��Ʈ�� 1 ������Ų��.
	   
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
	     g.setStroke(new BasicStroke(thickness,BasicStroke.CAP_ROUND,0));// stroke�� type����
	     g.drawLine(startX+10, startY+121, endX+10, endY+121);
	     g1.setColor(selectedColor); 
	     g1.setStroke(new BasicStroke(thickness,BasicStroke.CAP_ROUND,0));// stroke�� type����
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
	    
	   
	    if(drawing_type == RECTANGLE ) { //���콺 �̵��� ��� ���� ���ش�.
	     repaint(); 
	    }
	    if(drawing_type == CIRCLE ) {
	     repaint();
	    }
	    if(isSketch==true) { // sketch ���� �� ���� ��� start��ġ�� end ��ġ�� �ٲ��ָ鼭 ���� �����鼭 �̾������
	     startX = endX;
	     startY = endY;
	    }
	   }
	  }
	  public void mouseMoved(MouseEvent e) {}
	 }
	 public void makeShiftPosition(int startX, int startY, int endX, int endY) {
	  if(Math.abs(startX-endX)>=Math.abs(startY-endY)) {//X��ȭ���� Y��ȭ������ Ŭ �� >> Y�� ����
	   System.out.println("big: X");
	   endY=startY;
	  }
	  else {
	   endX=startX;
	  }
	 }

	 // ---------------------------------- main method -------------------------------------//
	 public static void main(String[] args) {
	  new UserRepainte();
	 }
	}

