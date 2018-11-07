
import java.awt.Color;
import java.util.ArrayList;
public class Figure_Info {
 
 ArrayList<Integer> startXpoints = new ArrayList<Integer>(); // sketch위한.
 ArrayList<Integer> startYpoints = new ArrayList<Integer>();
 ArrayList<Integer> endXpoints = new ArrayList<Integer>(); // sketch위한.
 ArrayList<Integer> endYpoints = new ArrayList<Integer>();
 
 int startX;
 int startY;
 int endX;
 int endY;
 int thickness;
 Color selectedcolor;
 int drawing_type;
 
 
 Color textColor=Color.BLACK;
 String fontSize;
 String text="";
 String fontType;
 

 public void printall() {
  System.out.println("Thickness "+thickness);
  System.out.println("Color " +selectedcolor);
  System.out.println("size : "+startXpoints.size());
  System.out.println("type : "+drawing_type);
 }
}