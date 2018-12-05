
import java.awt.Color;

import java.util.ArrayList;

/**
 * UserRePaint class에서 쓰임
 * 들어온 도형, 라인 또는 기타 등등의 시작점과 끝점, 타입을 정해주는 클래스
*/

public class Figure_Info { 

 ArrayList<Integer> startXpoints = new ArrayList<Integer>(); // set X coordinate of the start point

 ArrayList<Integer> startYpoints = new ArrayList<Integer>();//set Y coordinate of the start point

 ArrayList<Integer> endXpoints = new ArrayList<Integer>(); // set X coordinate of the end point

 ArrayList<Integer> endYpoints = new ArrayList<Integer>(); //set Y coordinate of the end point

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