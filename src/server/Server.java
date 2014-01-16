package server;

import java.io.*;
import java.net.*;
import java.util.*;
import common.*;

public final class Server implements Constants,Runnable{
	
	public Server(int size,String name, int num) throws Exception{
		try{
			this.size	= size;						// no. of rows/column
			loop		= 0;
			port		= num;						// server  port
			botmove		= new int[4];				// the moves of bot is stored here to use it in next slot
			file		= new File(name+".bc");		// creating the file object to  write the output
			backgrid	= new BackGrid(size);		// creating the object of backgrid to create the background
			info		= new Info(size);			// creatintg the info object to take care the grid and bullets
			random		= new Random();
		}
		catch(Exception e){
			System.out.println(e.toString()+"\nError Code: 1");
			System.exit(0);
		}
	}
	
	
	public void init() throws Exception{
		int i,j;
		try{
			info.grid	= backgrid.getGrid();				// instantiating the grid
			dos			= new DataOutputStream(new FileOutputStream(file)); // opening stream to the file
			dos.writeInt(size);								// writing the size to file
			for(i=0;i<size;i++){
				for(j=0;j<size;j++)
					dos.writeInt(info.grid[i][j]);			// writing the grid to file
			}
			writeBlockEnd();
			
			thread 	= new Thread(this);
			sock	= new ServerSocket(port);
			client1 = sock.accept();
			client2	= sock.accept();
			c1oos	= new ObjectOutputStream(client1.getOutputStream());
			c2oos	= new ObjectOutputStream(client2.getOutputStream());
			c1dis	= new DataInputStream(client1.getInputStream());
			c2dis	= new DataInputStream(client2.getInputStream());

			c1oos.writeObject(new Integer(TEAM1));							// writing the number 1 to team1			
			c2oos.writeObject(new Integer(TEAM2));							// writing the number 2 to team2			
			System.out.println("Team "+c1dis.readInt()+" is ready");
			System.out.println("Team "+c2dis.readInt()+" is ready");	
			System.out.println("All the bots joined: Game Starting Now");
			writeGrid();	
			thread.start();
		}
		catch(Exception e){
			System.out.println(e.toString()+"2");
		}
	}
	
	
	public void run(){
		int i,j,stop,nextx,nexty;
		Bullet point = null;
		try{
			loop = 1;
			while(loop <= LOOPTIME){
				c1oos.writeObject(new Integer(OK));							// sends the signal
				c2oos.writeObject(new Integer(OK));
				botmove[BOT1-1]	= c1dis.readInt();
				botmove[BOT2-1]	= c1dis.readInt();
				botmove[BOT3-1]	= c2dis.readInt();
				botmove[BOT4-1]	= c2dis.readInt();
								
				i = loop%4;
				stop = i;
				do{
					if(info.health[i] > 0 && botmove[i] == SHOOT){			// shoot action is performed
						info.bullets.addElement(new Bullet(info.botpos[i].direction,info.botpos[i].x,info.botpos[i].y,i+1));
						botmove[i] = NOACTION;								// adding the bullet to bullets vector and making the move to 0
					}
					i = (i+1)%4;
				}while(i!=stop);
				timeslot = 1;
				checkBullet();										// processing the bullets for t=1
				thread.sleep(SERVERSLEEP);
				i = loop%4;
				stop = i;
				do{													//processing the moves
					if(info.health[i] > 0){
						if(botmove[i] > 0 && botmove[i] < MOVE){	// phase change is performed
							info.grid[info.botpos[i].x][info.botpos[i].y] = (i+1)*10 + botmove[i];
							info.botpos[i].direction = botmove[i];
							if(info.shield[i] > 0)
								writeFile(info.botpos[i].x, info.botpos[i].y,(i+1)*10 + botmove[i] + 4);
							else
								writeFile(info.botpos[i].x, info.botpos[i].y,(i+1)*10 + botmove[i]);
						}
						else if(botmove[i] == MOVE){				// move action is performed
							nextx = info.botpos[i].x;
							nexty = info.botpos[i].y;
							if(info.botpos[i].direction == UP)
								nextx--;
							else if(info.botpos[i].direction == RIGHT)
								nexty++;
							else if(info.botpos[i].direction == DOWN)
								nextx++;
							else
								nexty--;
							if(info.grid[nextx][nexty]/10 == FREESPACE){							
								for(j=0;j<info.bullets.size();j++){
									Bullet bullet = (Bullet)info.bullets.elementAt(j);
									if(bullet.x == nextx && bullet.y == nexty && bullet.direction == info.grid[nextx][nexty]%10){
										point = new Bullet(info.bullets.elementAt(j));
										info.bullets.removeElementAt(j);
										break;
									}
								}
								info.grid[info.botpos[i].x][info.botpos[i].y] = FREESPACE;
								writeFile(info.botpos[i].x,info.botpos[i].y,FREESPACE);
								if(info.shield[i] == 0){	
									info.health[i]--;
									if(info.health[i] <= 0){
										info.grid[nextx][nexty] = FREESPACE;
										writeFile(nextx,nexty,FREESPACE);
										info.botpos[i].x = DEAD;
										info.botpos[i].y = DEAD;
										info.botpos[i].direction = DEAD;
										if(point.from == BOT1 || point.from == BOT2){
											if(i == BOT1-1 || i == BOT2-1)
												info.score[point.from-1] += TEAMKILL;
											else
												info.score[point.from-1] += KILL;
										}
										else{
											if(i == BOT3-1 || i == BOT4-1)
												info.score[point.from-1] += TEAMKILL;
											else
												info.score[point.from-1] += KILL;
										}
										writeMessage("Bot"+(i+1)+" killed by Bot"+point.from);
									}
								}
								if(info.health[i] > 0){
									info.grid[nextx][nexty] = (i+1)*10+info.botpos[i].direction;
									info.botpos[i].x = nextx;
									info.botpos[i].y = nexty;
									if(info.shield[i] > 0)
										writeFile(nextx, nexty, (i+1)*10 + info.botpos[i].direction + 4);
									else
										writeFile(nextx, nexty, (i+1)*10 + info.botpos[i].direction);
								}
							}
							else if(info.grid[nextx][nexty] == FREESPACE || info.grid[nextx][nexty] == HEALTH || info.grid[nextx][nexty] == BOTSHIELD || info.grid[nextx][nexty] == EAGLESHIELD){
								if(info.grid[nextx][nexty] == HEALTH){
									info.health[i] += INCHEALTH;
									if(info.health[i] > BOTHEALTH)
										info.health[i] = BOTHEALTH;
								}	
								else if(info.grid[nextx][nexty] == BOTSHIELD)
									info.shield[i] += SHIELDDURATION;
								else if(info.grid[nextx][nexty] == EAGLESHIELD){
									info.tshield[i/2] += SHIELDDURATION;
									makeShield(i/2,1);
								}	
								if(info.grid[nextx][nexty] == HEALTH || info.grid[nextx][nexty] == BOTSHIELD || info.grid[nextx][nexty] == EAGLESHIELD){
									for(j=0;j<info.powerups.size();j++){
										pointer = (Point) info.powerups.elementAt(j);
										if(pointer.x == nextx && pointer.y ==nexty){
											info.powerups.removeElementAt(j);
											break;
										}	
									}
								}
								info.grid[info.botpos[i].x][info.botpos[i].y] = FREESPACE;
								writeFile(info.botpos[i].x, info.botpos[i].y, FREESPACE);
								info.grid[nextx][nexty] = (i+1)*10+info.botpos[i].direction;
								info.botpos[i].x = nextx;
								info.botpos[i].y = nexty;
								if(info.shield[i] > 0)
									writeFile(nextx, nexty, (i+1)*10 + info.botpos[i].direction + 4);
								else
									writeFile(nextx, nexty, (i+1)*10 + info.botpos[i].direction);	
							}		
						}
					}
					i = (i+1)%4;
				}while(i!=stop);
				
				timeslot = 2;
				checkBullet();								// processing the bullets for t = 2	
				thread.sleep(SERVERSLEEP);
				loop++;
			}
			endGame(NORESULT);
		}
		catch(Exception e){
			System.out.println(e.toString()+"3");
		}
	}
	
	public void endGame(int status) throws Exception{
		int team1score,team2score;
		try{
			c1oos.writeObject(new Integer(status));
			c2oos.writeObject(new Integer(status));
			c1dis.close();									//closing the streams
			c2dis.close();
			c1oos.close();
			c2oos.close();
			client1.close();
			client2.close();
			sock.close();
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
		finally{
			team1score = info.score[0] + info.score[1];
			team2score = info.score[2] + info.score[3];
			System.out.println("Score Bot1:"+info.score[0]+" Bot2:"+info.score[1]+" Bot3:"+info.score[2]+" Bot4:"+info.score[3]);
			writeMessage("Game Over\nTeam1:"+team1score+" Team2:"+team2score);
			if(status == TEAM1WON)
				writeMessage("Team 1 won the game");
			else if(status == TEAM2WON)
				writeMessage("Team 2 won the game");
			else if(team1score > team2score)
				writeMessage("Team 1 won the game");
			else if(team2score > team1score)
				writeMessage("Team 2 won the game");
			else	
				writeMessage("Game Drawn");
			dos.writeInt(-4);
			dos.close();	
			System.exit(0);
		}
	}
	
	public void checkBullet(){
		try{
			int i,j,nextx,nexty,meetbot,endstatus = 0;
			for(i=0;i<info.bullets.size();i++){					// calculating the next move
				point = (Bullet)info.bullets.elementAt(i);
				nextx = point.x;
				nexty = point.y;
				if(point.direction == UP)
					nextx--;
				else if(point.direction == RIGHT)
					nexty++;
				else if(point.direction == DOWN)
					nextx++;
				else
					nexty--;
				
				if(info.grid[point.x][point.y]/10 == FREESPACE){		// bullet is in free space so making it free space again
					info.grid[point.x][point.y] = FREESPACE;
					writeFile(point.x, point.y, FREESPACE);
				}
				else if(info.grid[point.x][point.y]/10 == WATER){	// bullet is in water so making it water again
					info.grid[point.x][point.y] = WATER;
					writeFile(point.x, point.y, WATER);
				}
				point.x = nextx;
				point.y = nexty;
				info.bullets.setElementAt(point,i);	
			}
			for(i=0;i<info.bullets.size();i++){
				point = (Bullet)info.bullets.elementAt(i);
				switch(info.grid[point.x][point.y]){
					case STONE:										// bullet goes to unbreakable brick
						info.bullets.removeElementAt(i);
						i--;
						break;
					case HEALTH:
						info.bullets.removeElementAt(i);
						i--;
						break;
					case BOTSHIELD:
						info.bullets.removeElementAt(i);
						i--;
						break;
					case EAGLESHIELD:	
						info.bullets.removeElementAt(i);
						i--;
						break;
					case FREESPACE:									// bullet goes to free space
						info.grid[point.x][point.y] = (FREESPACE*10) + point.direction;
						writeFile(point.x, point.y, (FREESPACE*10) + point.direction);
						break;
					case WATER:										// bullet goes to water
						info.grid[point.x][point.y] = (WATER*10) + point.direction;
						writeFile(point.x, point.y, (WATER*10) + point.direction);
						break;
					case BRICK:										// bullet goes to new brick
						info.grid[point.x][point.y] = HALFBRICK;
						writeFile(point.x, point.y, HALFBRICK);
						info.score[point.from-1] += BRICKBREAK;
						info.bullets.removeElementAt(i);
						i--;
						break;
					case HALFBRICK:									// bullet goes to half brick
						info.grid[point.x][point.y] = FREESPACE;
						writeFile(point.x, point.y, FREESPACE);
						info.score[point.from-1] += BRICKBREAK;			// updating the score
						info.bullets.removeElementAt(i);
						i--;
						break;
					case TEAM1EAGLE:								// bullet goes to team 1 eagle
						endstatus += TEAM2EAGLE;
						info.grid[point.x][point.y] = FREESPACE;
						writeFile(point.x, point.y, FREESPACE);
						if((point.from-1)/2 == 0)
							info.score[point.from-1] += SELFKILL;
						else
							info.score[point.from-1] += EAGLEKILL;
						writeMessage("Bot"+point.from+" killed Team 1 eagle");
						info.bullets.removeElementAt(i);
						i--;
						break;
					case TEAM2EAGLE:								// bullet goes to team 2 eagle
						endstatus += TEAM1EAGLE;
						info.grid[point.x][point.y] = FREESPACE;
						writeFile(point.x, point.y, FREESPACE);
						if((point.from-1)/2 == 1)
							info.score[point.from-1] += SELFKILL;
						else
							info.score[point.from-1] += EAGLEKILL;
						writeMessage("Bot"+point.from+" killed Team 2 eagle");	
						info.bullets.removeElementAt(i);
						i--;
						break;
					default:
						mustdo = false;
						if(info.grid[point.x][point.y]/10 < FREESPACE){		//bullet meets a bot
							if(info.grid[point.x][point.y]/10 == BOT1)
								meetbot = BOT1-1;
							else if(info.grid[point.x][point.y]/10 == BOT2)
								meetbot = BOT2-1;
							else if(info.grid[point.x][point.y]/10 == BOT3)
								meetbot = BOT3-1;
							else if(info.grid[point.x][point.y]/10 == BOT4)
								meetbot = BOT4-1;
							else{
								info.bullets.removeElementAt(i);
								i--;
								break;
							}
							if(info.shield[meetbot] == 0){	
								info.health[meetbot]--;
								if(info.health[meetbot] <= 0){
									info.grid[info.botpos[meetbot].x][info.botpos[meetbot].y] = FREESPACE;
									writeFile(info.botpos[meetbot].x,info.botpos[meetbot].y,FREESPACE);
									info.botpos[meetbot].x = DEAD;
									info.botpos[meetbot].y = DEAD;
									info.botpos[meetbot].direction = DEAD;
									if(point.from == BOT1 || point.from == BOT2){
										if(meetbot == BOT1-1 || meetbot == BOT2-1)
											info.score[point.from-1] += TEAMKILL;
										else
											info.score[point.from-1] += KILL;
									}
									else{
										if(meetbot == BOT3-1 || meetbot == BOT4-1)
											info.score[point.from-1] += TEAMKILL;
										else
											info.score[point.from-1] += KILL;
									}
									writeMessage("Bot"+(meetbot+1)+" killed by Bot"+point.from);
								}
							}		
						}
						else if(info.grid[point.x][point.y]/10 == FREESPACE){	// bullet meets another bullet in free space
							info.grid[point.x][point.y] = FREESPACE;
							writeFile(point.x, point.y, FREESPACE);
							mustdo =  true;
						}
						else if(info.grid[point.x][point.y]/10 == WATER){	// bullet meets anthoer bullet in water
							info.grid[point.x][point.y] = WATER;
							writeFile(point.x, point.y, WATER);
							mustdo = true;
						}
						for(j=0;j<i && mustdo;j++){
							oldpoint = (Bullet)info.bullets.elementAt(j);
							if(point.x == oldpoint.x && point.y == oldpoint.y){
								info.bullets.removeElementAt(j);
								i--;
								break;
							}	
						}
						info.bullets.removeElementAt(i);
						i--;
						break;	
				}	
			}
			for(i=0;i<4;i++){
				if(info.shield[i] > 0)
					info.shield[i]--;
			}
			for(i=0;i<2;i++){
				if(info.tshield[i] > 0){
					info.tshield[i]--;
					if(info.tshield[i] == 0)
						makeShield(i,2);
				}	
			}			
			writeGrid();
			generateBonus();
			printScore();
			writeBlockEnd();
			if(endstatus == 0){
				if(info.health[BOT1-1] <=0 && info.health[BOT2-1] <=0)
					endstatus += TEAM2WON;
				if(info.health[BOT3-1] <=0 && info.health[BOT4-1] <=0)
					endstatus += TEAM1WON;	
			}
			if(endstatus != 0){
				if(timeslot == 1)
					writeGrid();
				endGame(endstatus);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println(e.toString()+"4");
		}
	}
	
	public void generateBonus() throws Exception{
		int i,j,k;
		if(timeslot == 2 && loop%POWERTIME == 0 && info.powerups.size() < 4){
			for(k=0;k<10;k++){
				i = Math.abs(random.nextInt())%(size-4) + 2;
				j =	Math.abs(random.nextInt())%(size-2) + 1;
				if(info.grid[i][j] == FREESPACE){
					if(!((i == 1 || i == 2 || i == size-2 || i== size-3) && j > size/2-2 && j < size/2+2)){
						if(loop%(POWERTIME*3) == POWERTIME){
							info.grid[i][j] = HEALTH;
							writeFile(i,j,HEALTH);
							info.powerups.addElement(new Point(HEALTH,i,j));
						}
						else if(loop%(POWERTIME*2) == POWERTIME){
							info.grid[i][j] = BOTSHIELD;
							writeFile(i,j,BOTSHIELD);
							info.powerups.addElement(new Point(BOTSHIELD,i,j));
						}
						else{
							info.grid[i][j] = EAGLESHIELD;
							writeFile(i,j,EAGLESHIELD);
							info.powerups.addElement(new Point(EAGLESHIELD,i,j));
						}
						break;
					}
				}		
			}	
		}
	}
	
	public void makeShield(int number, int mode) throws Exception{
		int i,j,k,from,to;
		try{
			if(mode == 1){
				from = BRICK;
				to	 = STONE;	
			}
			else{
				from = STONE;
				to	 = BRICK;
			}
			if(number == 0)
				j = 1;
			else
				j = size-3;
			for(i=0;i<2;i++,j++){
				for(k=size/2-1;k<=size/2+1;k++){
					if(info.grid[j][k] == from){
						info.grid[j][k] = to;
						writeFile(j,k,to);
					}					
				}
			}
		}
		catch(Exception e){
			throw e;
		}		
	}
	
	public void writeGrid() throws Exception{
		try{
			c1oos.flush();
			c2oos.flush();
			c1oos.writeObject(new Info(info));		
			c2oos.writeObject(new Info(info));	
		}
		catch(Exception e){
			throw e;
		}
	}
	
	public void writeFile(int x, int y, int element) throws Exception{
		try{
			dos.writeInt(x);
			dos.writeInt(y);
			dos.writeInt(element);
		}
		catch(Exception e){
			throw e;
		}
	}
	
	public void writeBlockEnd() throws Exception{
		try{
			dos.writeInt(-1);
		}
		catch(Exception e){
			throw e;
		}
	}
	
	public void writeMessage(String s) throws Exception{
		try{
			System.out.println(s);
			dos.writeInt(-3);
			byte[] b = s.getBytes();
			dos.writeInt(b.length);
			dos.write(b,0,b.length);
		}
		catch(Exception e){
			throw e;
		}
	}
	
	public void printScore() throws Exception{
		int i;
		try{
			dos.writeInt(-2);
			for(i=0;i<4;i++){
				dos.writeInt(info.score[i]);
			}
			for(i=0;i<4;i++){
				dos.writeInt(info.health[i]);
			}	
		}
		catch(Exception e){
			throw e;
		}		
	}
	
	public static void main(String args[]){
		try{
			if(args.length == 3){
				Server server	= new Server(Integer.parseInt(args[0]),args[1],Integer.parseInt(args[2]));
				server.init();
			}
			else
				System.out.println("Command line arguments format: size filename port");
		}
		catch(Exception e){
			System.out.println(e.toString()+"5");
		}
	}
	
	
private int 	size,port,timeslot,loop,botmove[];
private boolean	mustdo;	
private File	file;
private Info	info;
private Point	pointer;
private Bullet 	point,oldpoint;
private Thread 	thread;
private Random	random;
private Socket client1,client2;
private ServerSocket sock;
private BackGrid backgrid;
private DataInputStream 	c1dis,c2dis;
private DataOutputStream	dos,c1dos,c2dos;
private ObjectOutputStream 	c1oos,c2oos;
}