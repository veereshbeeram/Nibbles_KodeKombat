import KKMessageProtocol.KKMessage;
import java.io.*;
import java.net.*;
import java.util.*;

public class KKBotManager {
	int height,width;

    class myBool {

        boolean var;

        public myBool(boolean var) {
            this.var = var;
        }
    }
    final myBool istimerelapsed = new myBool(false);
    KKLowLevelServer server;
    ArrayList<KKSnake> list;
    int currid;
    ArrayList<Integer> retvalue;
    ArrayList<Integer> retorder;
    ArrayList<Long> timestamp;
    
    public KKBotManager(int width, int height) {
        currid = 0;
        
        list = new ArrayList<KKSnake>();
        retvalue = new ArrayList<Integer>();
        retorder = new ArrayList<Integer>();
        timestamp = new ArrayList<Long>();
        this.width=width;
        this.height=height;
    }

    public int startServer(int port) {
        if (server != null && !server.isshut) {
            return -1;
        }

        this.server = new KKLowLevelServer(this, port);
        this.server.start();

        System.out.println("Server started...");

        return 0;
    }
    public void closeserver(){
    	server.done=true;
    }
    public int stopServer() {
        if (server == null || server.isshut) {
            return -1;
        }
        server.done=true;
        server = null;
        System.out.println("Server shutting...This may take a while.");
        return 0;
    }

    public int KKAddSnake(KKLowLevelBotProcessor client, String botname) {
        try {
            KKSnake tsnake = null;

            switch (currid) {
                case 0:
                    tsnake = new KKSnake(currid, botname, 3, 0, 0, client);
                    break;

                case 1:
                    tsnake = new KKSnake(currid, botname, 3, this.height - 1, this.width - 1, client);
                    break;

                case 2:
                    tsnake = new KKSnake(currid, botname, 3, this.height - 1, 0, client);
                    break;

                case 3:
                    tsnake = new KKSnake(currid, botname, 3, 0, this.width - 1, client);
                    break;

                default:
                    // More than 4 snakes not supported.
                    return -2;
            }

            list.add(tsnake);
            retvalue.add(-1);
/*
            Object[] obj = new Object[3];
            obj[0] = String.valueOf(tsnake.id);
            obj[1] = tsnake.name;
            obj[2] = tsnake.errorMessage;

            bottable.addRow(obj);*/

            System.out.println("New Bot added by the id " + currid);

        } catch (Exception ex) {
            //Random Error Message
            return -1;
        }
        //Update UI

        int retval = currid++;

        return retval;
    }

    public int KKRemSnake(int id) {
        try {
        	System.out.println("Reached removing snake " + id + " " + list.get(0).id);

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).id == id) {
                    //Sending disconnection message to the client.
                    list.get(i).client.sendMessage(KKMessage.PrepareMessage(120, id));

                    list.remove(i);
                    System.out.println("Removed snake");
                }
            }
        } catch (Exception ex) {
            //Random Error Message;
        	System.out.println("Some Crash has occured at KKRemSnake.");
            return -1;
        }

        //Update UI
        /*
        this.bottable.setRowCount(0);

        for (int i = 0; i < list.size(); i++) {
            Object[] obj = new Object[3];
            obj[0] = String.valueOf(list.get(i).id);
            obj[1] = list.get(i).name;
            obj[2] = list.get(i).errorMessage;

            bottable.addRow(obj);
        }*/

        return 0;
    }

    public void addResponse(int id, long time, int dir) {
        //Check if responses are accepted now??? Otherwise reject calls
        if (this.istimerelapsed.var == true) {
            return;
        }

        boolean found = false;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).id == id) {
                if (retvalue.get(i) != -1) {
                    //Ignore duplicate responses.
                    return;
                } else {
                    retvalue.set(i, dir);
                }

                found = true;
                break;
            }
        }

        if (!found) {
            //Bot with given id does not exist
            return;
        }

        if (timestamp.size() == 0) {
            timestamp.add(time);
            retorder.add(id);
            return;
        }

        for (int i = 0; i < timestamp.size(); i++) {
            if (timestamp.get(i) > time) {
                timestamp.add(i, time);
                retorder.add(i, id);
                break;
            }
        }
    }

    public void initBots(int ptimeout,int stimeout)
    {
        for(int i=0;i<list.size();i++)
        {
            KKMessage mess=KKMessage.PrepareMessage(101, list.get(i).id, ptimeout,stimeout);
            list.get(i).client.sendMessage(mess);
        }

        System.out.println("Sent initialization code to all bots");
    }

    @SuppressWarnings("empty-statement")
    public int performGameLoop(int map[][]) {
    	System.out.println("Entered perform game loop " + map[0][1] + " " + this.width + " " + this.height);

        //Clear the queue.
        for (int i = 0; i < retvalue.size(); i++) {
            retvalue.set(i, -1);
        }

        if (timestamp.size() > 0) {
            timestamp.clear();
        }


        if (retorder.size() > 0) {
            retorder.clear();
        }

        this.istimerelapsed.var = false;

        System.out.println("Cleared all game loop variables.");

        //First send an async start signal to all clients.

        for (int i = 0; i < list.size(); i++) {
        	System.out.println("About to send message to bot with id " + list.get(i).id);

            //Send all the necessary details across to the client.
            KKMessage mess = KKMessage.PrepareMessage(200, list.get(i).id,this.width,this.height,map);

            list.get(i).client.sendMessage(mess);
            System.out.println("Sending message to bot with id " + list.get(i).id);
        }

        //TODO: change server time - 7 seconds - presume the remaining bots are disconnected.
        class task extends TimerTask{

            @Override
            public void run() {
                istimerelapsed.var = true;
            }
        }

        Timer timer = new Timer();
        timer.schedule(new task(), 10000);

        //Wait for all responses from all clients
        /*boolean looper=true;
        while (looper){
        	if(retorder.size() < list.size() && !istimerelapsed.var){
        		try{
        		Thread.sleep(10);
        		}
        	catch(Exception e){
        		System.out.println("Interuppt occured");
        	}
        }
        }*/
        while (retorder.size() < list.size() && !istimerelapsed.var);

        System.out.println("Received all responses from the bots " + istimerelapsed.var + " " + retorder.size());

        if (!istimerelapsed.var) {
            timer.cancel();

            //addResponse no longer shall accept responses from the clients.

            this.istimerelapsed.var = true;

            //Process elements based on their location in the queue.

            for (int i = 0; i < retorder.size(); i++) {
                //Perform operation of list.
                int retval = retvalue.get(retorder.get(i));
                int botid = retorder.get(i);
                KKLocation loc;

                switch (retval) {
                    case 1:
                        //Move left                        
                        loc = list.get(botid).turnLeft();

                        break;

                    case 2:
                        //Move right
                        loc = list.get(botid).turnRight();

                        break;

                    case 3:
                    //Move straight

                    default:
                        //Move straight only
                        loc = list.get(botid).Forward();

                    System.out.println("About to move straight " + loc.i + " " + loc.j);

                        break;
                }

                if (loc.i < 0) {
                    loc.i = this.height - 1;
                }

                loc.i = loc.i % this.height;

                if (loc.j < 0) {
                    loc.j = this.width - 1;
                }

                loc.j = loc.j % this.width;

                switch (map[loc.i][loc.j]) {
                    case 0:
                        //Move normally
                        list.get(botid).move(loc.i, loc.j, map);

                        break;

                    case 1:
                        //Eat fast food
                        //TODO: CHECK WEIGHTAGE OF FASTFOOD
                        list.get(botid).eat(1);
                        list.get(botid).move(loc.i, loc.j, map);

                        break;

                    case 2:
                        //Eat full course meal
                        //TODO: CHECK WEIGHTAGE OF FULL COURSE MEAL
                        list.get(botid).eat(2);
                        list.get(botid).move(loc.i, loc.j, map);

                        break;

                    case 3:
                        //Power Up
                        //TODO: CHECK WEIGHTAGE OF POWER UP
                        list.get(botid).power(3);
                        list.get(botid).move(loc.i, loc.j, map);

                        break;

                    default:
                        //Bot has hit an obstacle or some bot.
                    	System.out.println("New Location is (" + loc.i + "," + loc.j + ") -> " + map[loc.i][loc.j]);
                        list.get(botid).crash(map);
                        break;
                }
            }
        }

        /* 
         * if timer elapses before receiving response from all
         * delete all the other snakes from the list and remove them
         * from the table.
         */

        //Report error for those bots that have not sent their responses.
        for (int i = 0; i < this.retvalue.size(); i++) {
            if (retvalue.get(i) == -1) {
                //No response received. Bot is removed from the game.
                list.get(i).ReportError(100, "Bot has crashed or is unreachable.");
            }
        }

        // On receiving all responses. Process them in the order of timestamp.

        return 0;
    }

    /*

    public int KKMoveLeft(int id) {
    for (int i = 0; i < list.size(); i++) {
    if (list.get(i).id == id) {
    KKLocation next = list.get(i).turnLeft();
    list.get(i).move(next.i, next.j);

    }

    }



    return 0;
    }

    public int KKMoveRight(int id) {
    for (int i = 0; i <
    list.size(); i++) {
    if (list.get(i).id == id) {
    KKLocation next = list.get(i).turnRight();
    list.get(i).move(next.i, next.j);
    }

    }

    return 0;
    }

    public int KKMoveForward(int id) {
    for (int i = 0; i <
    list.size(); i++) {
    if (list.get(i).id == id) {
    KKLocation next = list.get(i).Forward();
    list.get(i).move(next.i, next.j);
    }

    }
    return 0;
    }*/
    public int KKErrorCallback(int id, int errid) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int KKControlMessage(int id, Object data) {
        //No response

        return 0;
    }
}

class KKLowLevelServer extends Thread {

    KKBotManager botmanager;
    int sport;
    boolean done;
    boolean isshut;

    public KKLowLevelServer(KKBotManager botmanager, int port) {
        this.botmanager = botmanager;
        this.sport = port;
        done = false;
        isshut = false;
    }

    
    @Override
    @SuppressWarnings("empty-statement")
    public void run() {

        try {
            ServerSocket serv = new ServerSocket(this.sport);

            System.out.println("Server is created on port: " + this.sport);

            serv.setSoTimeout(500);

            while (!done) {
                try {
                    Socket client = serv.accept();

                    System.out.println("Client connected...");

                    KKLowLevelBotProcessor botproc = new KKLowLevelBotProcessor(client, this.botmanager);
                    Object obj= botproc.sendMessage(KKMessage.PrepareMessage(110, -1));
                    botproc.botname = obj.toString();

                    botproc.start();
                } catch (SocketTimeoutException ex) {
                }
            }
        } catch (IOException ex) {
        	System.out.println("Server has crashed.");
            botmanager.KKControlMessage(-100, "KKLowLevelServer has crashed. No new connections can be made.");
        }

        System.out.println("Server is shut");
        isshut = true;
    }
}