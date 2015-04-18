/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kodekombatserv;

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
        KodeKombatServApp.TerminalOutput("New Snake by the name ... has been added.");

        while (!isclose) {
            if (isgameloop) {
                try {
                    KKMessage mess = (KKMessage) in.readObject();
                    botmanager.addResponse(this.id, (Integer) mess.getPayLoad(1), (Integer) mess.getPayLoad(0));
                    isgameloop = false;
                    KodeKombatServApp.TerminalOutput("Received a message from bot " + this.id + ". Bot dir is " + mess.getPayLoad(0));
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(KKLowLevelBotProcessor.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    KodeKombatServApp.TerminalOutput("Error receiving directions from bot " + this.id);
                    botmanager.addResponse(this.id, 10000, 3);
                    isgameloop=false;
                }
            }
        }
        //run your protocol checks

        //incase crash happens notify Botmanager
        KodeKombatServApp.TerminalOutput("bot " + this.id + " : read thread shutting down.");
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
                    KodeKombatServApp.TerminalOutput("About to send message.");                    
                    out.writeObject(mess);
                    
                    KodeKombatServApp.TerminalOutput("About to receive message.");

                    String resp = (String) in.readObject();

                    if (resp.equals("OK")) {
                        return 0;
                    }
                    break;
            }
        } catch (ClassNotFoundException ex) {
            return -3;  //Class error.... wont arise mostly.
        } catch (IOException ex) {
            KodeKombatServApp.TerminalOutput("IOException thrown will sending message with id " + mess.getKKMessId() + " from bot " + mess.getKKBotId());
            return -2;  //Communication error. This is mostly due to disconnection with client.
        }

        //Unexpected message.
        return -1;
    }
}