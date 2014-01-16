package common;

import java.io.*;

public class Point implements Serializable{
		
	public Point(int a, int b, int c){
		direction 	= a;
		x			= b;
		y			= c;
	}
	
	public Point(Point p){
		this.direction 	= p.direction;
		this.x			= p.x;
		this.y			= p.y;
	}
	
	public int x,y,direction;
}