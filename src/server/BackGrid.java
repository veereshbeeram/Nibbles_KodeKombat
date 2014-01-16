package server;

import java.util.*;
import common.*;

public final class BackGrid implements Constants{

	public BackGrid(int size) throws Exception{
		if(size < 9)
			throw new BotException("Grid Size must be atleast 9");
		else if(size%2 == 0)
			throw new BotException("Size should be a odd number");
		GridArray = new int[size][size];
		random	  = new Random();
		this.size = size;
	}

	public void initGrid(){	
		int i,j,k;
		for(j=0;j<size;j++){
			GridArray[0][j] 		= STONE;
			GridArray[size-1][j] 	= STONE;
		}
		for(i=1;i<size/2+1;i++){
			GridArray[i][0]      	= STONE;
			GridArray[size-i][0] 	= STONE;
			for(j=1;j<size-1;j++){
				k = Math.abs(random.nextInt()%7);
				if(k == 3 || k == 4)
					k+=2;
				else if(k > 4)
					k = 5;
				GridArray[i][j]    		= k;
				GridArray[size-i-1][j]	= k;
			}
			GridArray[i][j] 		= STONE;
			GridArray[size-i][j]	= STONE;
		}
		
		GridArray[1][size/2-2]	= BOT1DOWN;
		GridArray[1][size/2-1]	= BRICK;
		GridArray[1][size/2]	= TEAM1EAGLE;
		GridArray[1][size/2+1]	= BRICK;
		GridArray[1][size/2+2]	= BOT2DOWN;
		GridArray[2][size/2-2]	= FREESPACE;
		GridArray[2][size/2-1]	= BRICK;
		GridArray[2][size/2]	= BRICK;
		GridArray[2][size/2+1]	= BRICK;
		GridArray[2][size/2+2]	= FREESPACE;
		GridArray[3][size/2-2]	= FREESPACE;
		GridArray[3][size/2-1]	= FREESPACE;
		GridArray[3][size/2]	= FREESPACE;
		GridArray[3][size/2+1]	= FREESPACE;
		GridArray[3][size/2+2]	= FREESPACE;
		
		GridArray[size-2][size/2-2]	= BOT3UP;
		GridArray[size-2][size/2-1]	= BRICK;
		GridArray[size-2][size/2]	= TEAM2EAGLE;
		GridArray[size-2][size/2+1]	= BRICK;
		GridArray[size-2][size/2+2]	= BOT4UP;
		GridArray[size-3][size/2-2]	= FREESPACE;
		GridArray[size-3][size/2-1]	= BRICK;
		GridArray[size-3][size/2]	= BRICK;
		GridArray[size-3][size/2+1]	= BRICK;
		GridArray[size-3][size/2+2]	= FREESPACE;
		GridArray[size-4][size/2-2]	= FREESPACE;
		GridArray[size-4][size/2-1]	= FREESPACE;
		GridArray[size-4][size/2]	= FREESPACE;
		GridArray[size-4][size/2+1]	= FREESPACE;
		GridArray[size-4][size/2+2]	= FREESPACE;
		
		GridArray[size/2][size/2-2] = STONE;
		GridArray[size/2][size/2]  	= STONE;
		GridArray[size/2][size/2+2] = STONE;
		
		
		for(i=0;i<size;i++){
			for(j=0;j<size;j++)
				System.out.print("-"+GridArray[i][j]+"-");
			System.out.println();	
		}
	}
	
	public int isGridProper(int x,int y,int from){
		int i,j,count=0;
		try{
			if(x == size/2)
				return 1;
			if(GridArray[x+1][y] == FREESPACE || GridArray[x+1][y] == BRICK || GridArray[x+1][y] == HALFBRICK)
				count += isGridProper(x+1,y,0);
			if(y>0 && from !=1 && (GridArray[x][y-1] == FREESPACE || GridArray[x][y-1] == BRICK || GridArray[x][y-1] == HALFBRICK))
				count += isGridProper(x,y-1,2);
			if(y<size-1 && from!=2 && (GridArray[x][y+1] == FREESPACE || GridArray[x][y+1] == BRICK || GridArray[x][y+1] == HALFBRICK))
				count += isGridProper(x,y+1,1);
			return count;		
		}
		catch(Exception e){
			System.out.println(e.toString());
			return 0;
		}		
	}
	
	public int[][] getGrid(){
		do{
			initGrid();
		}while(isGridProper(1,size/2-2,0) == 0);
		return GridArray;
	}

		
int size;
int[][] GridArray;
Random random;
}