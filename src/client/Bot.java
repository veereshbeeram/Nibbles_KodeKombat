package client;

import java.io.*;
import java.net.*;
import common.*;

public class Bot implements Constants{
	
	public Bot(String address, int port){
		try{
			socket 	= new Socket(address, port);
			dos		= new DataOutputStream(socket.getOutputStream());
			ois		= new ObjectInputStream(socket.getInputStream());
			mynumber = (Integer)ois.readObject();
			System.out.println("Iam Bot"+mynumber);
			getInfo();
			size	= info.size;
		}		
		catch(Exception e){
			System.out.println(e.toString());
		}		
	}
	
	public void changePhase(int side){
		try{
			if(side > 0 && side < 5)
				dos.writeInt(side);
			else
				dos.writeInt(info.botpos[mynumber-1].direction);
		}
		catch(Exception e){
			printException(e);
		}
	}
	
	public void move(){
		try{
			dos.writeInt(MOVE);
		}
		catch(Exception e){
			printException(e);
		}
	}
	
	public void shoot(){
		try{
			dos.writeInt(SHOOT);
		}
		catch(Exception e){
			printException(e);
		}
	}
	
	public int getMyNumber(){
		return mynumber;
	}
	
	public int getMyTeamNumber(){
		return (mynumber-1)/2+1;
	}
	
	public int getMyHealth(){
		return getBotHealth(mynumber);
	}
	
	public int getBotHealth(int number){
		getInfo();
		if(number > 0 && number < 5)
			return info.health[number-1];
		else
			return -1;
	}
	
	public boolean amIShielded(){
		return isBotShielded(mynumber);
	}
	
	public boolean isBotShielded(int number){
		getInfo();
		if(number > 0 && number < 5 && info.shield[number-1] > 0)
			return true;
		return false;
	}	
	
	public int isBulletAt(int x, int y){
		getInfo();
		if(x >= 0 && x < info.size && y >= 0 && y < info.size && info.grid[x][y] > 50 && info.grid[x][y] < 70)
			return (info.grid[x][y]%10);
		return 0;
	}
	
	public int elementAt(int x,int y){
		getInfo();
		if(x >= 0 && x < info.size && y >= 0 && y < info.size)
			return info.grid[x][y];
		return -1;	
	}
	
	public int[] getMyPosition(){
		return getBotPosition(mynumber);
	}
	
	public int[] getBotPosition(int number){
		int ret[] = new int[3];
		getInfo();
		if(number > 0 && number < 5){
			ret[0] = info.botpos[number-1].direction;
			ret[1] = info.botpos[number-1].x;
			ret[2] = info.botpos[number-1].y;
		}
		return ret;
	}

	public int[][] getBonusPositions(){
		int i,j,ret[][] = new int[4][3];
		getInfo();
		for(i=0;i<info.powerups.size();i++){
			Point p	= (Point)info.powerups.elementAt(i);
			ret[i][0] = p.direction;
			ret[i][1] = p.x;
			ret[i][2] = p.y;
		}
		for(;i<4;i++){
			ret[i][0] = -1;
			ret[i][1] = -1;
			ret[i][2] = -1;
		}
		return ret;
	}	
	
	public int[][] getBoard(){
		getInfo();
		return info.grid;
	}
	
	public void getInfo(){
		try{
			dos.writeInt(NOACTION);
			info = (Info)ois.readObject();
			if(info.health[mynumber-1] <= 0 || info.result != 1){
				ois.close();
				dos.close();
				socket.close();	
				if(info.health[mynumber-1] <= 0)
					throw new BotException("Iam Dead");
				if(info.result == 30)
					throw new BotException("Match Draw");
				else if(info.result == (mynumber-1)/2+1*10)
					throw new BotException("We Won");
				else
					throw new BotException("We Lost");
			}
		}
		catch(Exception e){
			printException(e);
		}		
	}
	
	public void printException(Exception e){
		if(e instanceof BotException)
			System.out.println(e.toString());
		else
			System.out.println("Game Over: Result Unknown");
		System.exit(0);
	}
	
public  int size;	
private int mynumber;
private Info info;
private Socket socket;	
private ObjectInputStream ois;
private DataOutputStream dos;	
}
