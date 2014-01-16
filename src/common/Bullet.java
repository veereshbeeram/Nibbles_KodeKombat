package common;

import java.io.*;

public class Bullet extends Point implements Serializable{
		
	public Bullet(int a, int b, int c, int d){
		super(a,b,c);
		from		= d;
	}
	
	public Bullet(Bullet b){
		super(b);
		this.from		= b.from;
	}
	
	public int from;
}