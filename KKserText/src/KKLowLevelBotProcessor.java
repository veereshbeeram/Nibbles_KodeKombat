

import KKMessageProtocol.KKMessage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KKLowLevelBotProcessor extends Thread {

    KKBotManager botmanager;
    Socket client;
    ObjectOutputStream out;
    ObjectInputStream in;
    int id;
    String botname;
    boolean isgameloop;
    boolean isclose;
    serverMain smain=new serverMain();

    public KKLowLevelBotProcessor(Socket client, KKBotManager botmanager) throws IOException {
        this.botmanager = botmanager;
        this.client = client;
        this.client.setSoTimeout(500);
        out = new ObjectOutputStream(client.getOutputStream());
        in = new ObjectInputStream(client.getInputStream());
        id = -1;
        isclose=false;
        isgameloop=false;
    }

    public void close()
    {
        isclose=true;
    }

    @Override
    public void run() {
        //Add new snake
        this.id = this.botmanager.KKAddSnake(this, botname);

        //New snake has been added to the game
System.out.println("New Snake by the name ... has been added.");






//------------------------------------------Kuarc---------------------------------------------
	int choice=1; 
	System.out.println("1: add new bot\n2:Close Server\n3:Simulate");
	java.io.DataInputStream ins =new java.io.DataInputStream(System.in);
	try{
		choice= Integer.parseInt(ins.readLine());
	}
			catch(Exception e){
				
			}
			if(choice==3)
			{
				System.out.println("starting game");
				/*try {
					//sleep(1000);
				} catch (InterruptedException e) {
					System.out.println("some exeption");
	// 	TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				smain.startGame();
			}
			else if(choice==2)
			{
				/*try {
					sleep(10000);
				} catch (InterruptedException e) {
					System.out.println("some exeption");
		// 	TODO Auto-generated catch block
					
				}*/
				botmanager.closeserver();
				
			}
//----------------------------------kuarc-----------------------------------------------------
     
 
 
 
 
 
 while (!isclose) {
	 if(isgameloop){
            
                try {
                    KKMessage mess = (KKMessage) in.readObject();
                    botmanager.addResponse(this.id, (Integer) mess.getPayLoad(1), (Integer) mess.getPayLoad(0));
                    isgameloop = false;
                    System.out.println("Received a message from bot " + this.id + ". Bot dir is " + mess.getPayLoad(0));
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(KKLowLevelBotProcessor.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                	System.out.println("Error receiving directions from bot " + this.id);
                    botmanager.addResponse(this.id, 10000, 3);
                    isgameloop=false;
                }
	 }
	 /*else{
		 try {
				sleep(10);
			} catch (InterruptedException e) {
				System.out.println("exeption in sleep @ while(!(!isclose&&isgameloop))");
//	 	TODO Auto-generated catch block
				
			}
	 }*/
	 
   }
        //run your protocol checks

        //incase crash happens notify Botmanager
        System.out.println("bot " + this.id + " : read thread shutting down.");
    }

    synchronized public Object sendMessage(KKMessage mess) {
        try {
            //Change this to a text based protocol to support C, C++            

            switch (mess.getKKMessId()) {
                case 200:
                    isgameloop = true;
                    out.writeObject(mess);
                    return 0;

                case 110:
                    out.writeObject(mess);
                    try {
                        String resp = (String) in.readObject();
                        return resp;
                    } catch (ClassNotFoundException ex) {
                        return "Unknown Bot name";
                    }                    

                default:
                	System.out.println("About to send message.");                    
                    out.writeObject(mess);
                    
                    System.out.println("About to receive message.");

                    String resp = (String) in.readObject();

                    if (resp.equals("OK")) {
                        return 0;
                    }
                    break;
            }
        } catch (ClassNotFoundException ex) {
            return -3;  //Class error.... wont arise mostly.
        } catch (IOException ex) {
        	System.out.println("IOException thrown will sending message with id " + mess.getKKMessId() + " from bot " + mess.getKKBotId());
            return -2;  //Communication error. This is mostly due to disconnection with client.
        }

        //Unexpected message.
        return -1;
    }
}