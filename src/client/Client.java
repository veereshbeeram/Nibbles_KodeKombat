package client;

import java.io.*;
import java.net.*;
import java.util.*;
import common.*;

public final class Client implements Constants,Runnable{

	public Client(String name, int port1, int port2){
		done		= 0;
		start		= 0;
		result		= 1;
		checker     = 3;
		host		= name;
		port 		= port1;
		sport		= port2;
		bot1queue	= new Vector<Integer>(0);
		bot2queue	= new Vector<Integer>(0);
	}

	public void game() throws Exception{
		try{
			ssocket		= new ServerSocket(sport);
			bot1thread	= new Thread(this,"bot1");
			bot2thread	= new Thread(this,"bot2");			
			socket		= new Socket(InetAddress.getByName(host), port);
			dos			= new DataOutputStream(socket.getOutputStream());							
			ois			= new ObjectInputStream(socket.getInputStream());	
	
			team		= (Integer)ois.readObject();			// getting the team number
			System.out.println("Myteam is"+team);
			bot1thread.start();									//starting the bots
			bot2thread.start();
			while(start != 2)
				Thread.currentThread().sleep(BOTSLEEP);
			dos.flush();
			dos.writeInt(team);									// writing the team number as sign that both bots joined	
			info = (Info)ois.readObject();						// reads the info object and the game starts here
			start++;

			while((result = (Integer)ois.readObject()) == OK){	//receives the signal
				synchronized(this){
					checker = 1;
					dos.flush();
					if(bot1queue.size() > 0){
						dos.writeInt(bot1queue.firstElement());
						bot1queue.remove(0);
					}
					else
						dos.writeInt(0);
					if(bot2queue.size() > 0){
						dos.writeInt(bot2queue.firstElement());
						bot2queue.remove(0);
					}
					else
						dos.writeInt(0);
					info = (Info)ois.readObject();					// updates for t=1
					info = (Info)ois.readObject();					// updates for t=2
				}
				while(checker != 3)
					Thread.currentThread().sleep(BOTSLEEP);
			}
			info.result = result;
			dos.close();
			socket.close();
			System.exit(0);
			while(done < 2)
				Thread.currentThread().sleep(BOTSLEEP);
			ssocket.close();
		}
		catch(Exception e){
			System.out.println(e.toString()+"1");
			System.exit(0);
		}
	}


	
	public void run(){
		int i,j,bot1number,bot2number;
		try{	
			if(Thread.currentThread().getName().equals("bot1")){
				bot1socket 	= ssocket.accept();
				b1os		= new ObjectOutputStream(bot1socket.getOutputStream());
				b1is		= new DataInputStream(bot1socket.getInputStream());
				bot1number	= (team-1)*2+1;
				System.out.println("Bot "+bot1number+" is joined");
				start++;
				while(start != 3)
					bot1thread.sleep(BOTSLEEP);
				b1os.writeObject(new Integer(bot1number));
				try{
					while(result == 1 && info.health[bot1number-1] > 0){
						while(checker != 1)
							bot1thread.sleep(BOTSLEEP);
						synchronized(this){
							checker = 2;
							i = 0;
							while(i<10 && b1is.available() == 0){
								i++;
								bot1thread.sleep(1);
							}
							if(b1is.available() > 0){
								i = b1is.readInt();
								if(i == NOACTION)
									b1os.writeObject(info);
								else{
									//System.out.println("1: "+i);
									bot1queue.addElement(new Integer(i));
								}
							}			
						}
					}
					b1is.close();
					b1os.close();
					bot1socket.close();
				}
				catch(Exception e){
					System.out.println(e.toString()+" early exit 1");
				}
				done++;
				while(true){
					while(checker != 1)
						bot1thread.sleep(BOTSLEEP);
					checker = 2;
				}	
			}
			else{
				bot2socket  = ssocket.accept();
				b2os		= new ObjectOutputStream(bot2socket.getOutputStream());
				b2is		= new DataInputStream(bot2socket.getInputStream());
				bot2number  = (team-1)*2+2;
				System.out.println("Bot "+bot2number+" is joined");
				start++;
				while(start != 3)
					bot2thread.sleep(1);
				b2os.writeObject(new Integer(bot2number));
				try{
					while(result == 1 && info.health[bot2number-1] > 0){
						while(checker != 2)
							bot2thread.sleep(BOTSLEEP);
						synchronized(this){
							checker = 3;
							j = 0;
							while(j<10 && b2is.available() == 0){
								j++;
								bot2thread.sleep(1);
							}
							if(b2is.available() > 0){
								j = b2is.readInt();
								if(j == NOACTION)
									b2os.writeObject(info);
								else{
									//System.out.println("2: "+j);
									bot2queue.addElement(new Integer(j));
								}
							}
						}
					}
					b2is.close();
					b2os.close();
					bot2socket.close();
				}
				catch(Exception e){
					System.out.println(e.toString()+" early exit2");
				}
				done++;
				while(true){
					while(checker != 2)
						bot2thread.sleep(BOTSLEEP);
					checker = 3;
				}				
			}
		}
		catch(Exception e){
			System.out.println(e.toString()+"3");
		}		
	}
	
	public static void main(String args[]){
		try{
			if(args.length == 3){
				Client c	= new Client(args[0],Integer.parseInt(args[1]),Integer.parseInt(args[2]));
				c.game();
			}
			else
				System.out.println("Command line arguments format: hostaddress hostport port");
		}
		catch(Exception e){
			System.out.println(e.toString()+"4");
		}		
	}

private Vector<Integer> bot1queue,bot2queue;
private int i,port,sport,team,done,start,result,checker;
private Info info;
private Thread bot1thread,bot2thread;	
private String host;
private ServerSocket ssocket;	
private Socket socket,bot1socket,bot2socket;
private ObjectInputStream ois;
private DataInputStream	b1is,b2is;
private DataOutputStream dos;
private ObjectOutputStream b1os,b2os;
}