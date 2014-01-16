package common;

import java.io.*;
import java.util.*;

public class Info implements Constants, Serializable{

	public Info(int size){
		int i;
		this.size 	= size;
		result		= 1;
		tshield		= new int[2];
		health		= new int[4];
		shield		= new int[4];
		score		= new int[4];
		botpos		= new Point[4];
		grid		= new int[size][size];
		bullets	 	= new Vector<Bullet>(0);
		powerups	= new Vector<Point>(0);
		
		for(i=0;i<4;i++){
			health[i]	= BOTHEALTH;
			shield[i]	= 0;
			score[i]	= 0;
		}
		
		botpos[0] 	= new Point(DOWN,1,size/2-2);
		botpos[1] 	= new Point(DOWN,1,size/2+2);
		botpos[2] 	= new Point(UP,size-2,size/2-2);
		botpos[3] 	= new Point(UP,size-2,size/2+2);
	}
	
	public Info(Info obj){
		int i,j;
		this.size 	= obj.size;
		this.result = obj.result;
		this.health = new int[4];
		this.shield	= new int[4];
		this.score	= new int[4];
		this.botpos	= new Point[4];
		this.grid 	= new int[size][size];
		this.bullets = new Vector<Bullet>(0);
		this.powerups= new Vector<Point>(0);

		for(i=0;i<4;i++){
			this.health[i] 	= obj.health[i];
			this.shield[i] 	= obj.shield[i];
			this.score[i]	= obj.score[i];
			this.botpos[i] 	= new Point(obj.botpos[i]);
		}
		for(i=0;i<size;i++){
			for(j=0;j<size;j++)
				this.grid[i][j] = obj.grid[i][j];
		}
		for(i=0;i<obj.bullets.size();i++)
			this.bullets.add(new Bullet((Bullet)obj.bullets.elementAt(i)));
		for(i=0;i<obj.powerups.size();i++)
			this.powerups.add(new Point((Point)obj.powerups.elementAt(i)));	
	}

	public int				size,result,health[],shield[],score[],tshield[];
	public int 				grid[][];
	public Point 			botpos[];
	public Vector<Bullet>	bullets;
	public Vector<Point>	powerups;	
}