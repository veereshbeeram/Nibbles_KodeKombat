

import java.io.IOException;
import java.net.Socket;
import KKMessageProtocol.KKMessage;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KKLowLevelBotProcessor extends Thread {

    Socket client;
    ObjectInputStream in;
    ObjectOutputStream out;
    Boolean isinit = false;
    Boolean isresp = false;
    Boolean isrunning = false;
    Boolean done = false;
    KKBotInterface botinterface;
    int botretvalue;

    public KKLowLevelBotProcessor(String ipaddr, String port, KKBotInterface botinterface) throws IOException {
        int portnum;
        portnum= Integer.parseInt(port);
    	client = new Socket(ipaddr, portnum);
        client.setSoTimeout(500);
        System.out.println("TCP connection established.");

        out = new ObjectOutputStream(client.getOutputStream());
        in = new ObjectInputStream(client.getInputStream());
        this.isinit = true;

        System.out.println("Input1, output streams are opened...");

        this.botinterface = botinterface;
    }

    public void sendResponse(int value) {
        botretvalue = value;
        isresp = true;
    }

    public void closeConnection() {
        done = true;
    }

    @Override
    public void run() {
        //BotProcessor is running
        isrunning = true;

        KKBotExecuter botexec = null;

        // output - response KKMessage to server.
        KKMessage output;
        // done - is set to true to terminate connection.
        done = false;
        // errcount - terminates the connection when errcount is more than 3.
        //            Only simultaneous errors counted.
        int errcount = 0;
        int ptimeout = 3000, stimeout = 1000;

        // Wait till init is actually successful;
        while (!this.isinit);

        System.out.println("About to enter main loop.");

        //Starting main network loop.       

        while (!done && errcount < 4) {
            try {
                KKMessage input = null;

                // Waiting for server response first

                try {
                    input = (KKMessage) in.readObject();
                } catch (SocketTimeoutException ex) {
                    continue;
                }

                switch (input.getKKMessId()) {
                    case 101:
                        /* Perform connection initialization
                         *
                         * Get one time info from the server
                         * -> primary timeout in ms
                         * -> secondary timeout in ms
                         */

                        //Primary timeout variable
                        try {
                        	System.out.println("Received a 101.");

                            ptimeout = (Integer) input.getPayLoad(0);
                            //Secondary timeout variable
                            stimeout = (Integer) input.getPayLoad(1);
                            out.writeObject("OK");
                            System.out.println("Sending the ACK");

                            botexec = new KKBotExecuter(ptimeout, stimeout, this.botinterface, this);
                            botexec.start();

                            System.out.println("Connected to server... and started execution thread of bot");

                        } catch (NumberFormatException ex) {
                            out.writeObject("NOTOK");
                        }

                        break;

                    case 110:
                        //Request for bot name
                        String botname = botinterface.getBotName();
                        out.writeObject(botname);

                        break;

                    case 120:
                        /* Disconnection Request received
                         *
                         * Client will be terminated. All its
                         * resources will be reclaimed.
                         * 
                         */

                        out.writeObject("OK");

                        done = true;
                        System.out.println("Disconnection Request received from server. Processed Ok. Client disconnected.");

                        break;

                    case 200:
                        /*
                         * Start Game Loop Sequence
                         * Map context is received.
                         * response variable set to false
                         */
                        isresp = false;

                        //Construct mapcontext from KKMessage
                        HashMap mapcontext = new HashMap();

                        System.out.println("Received a 200");

                        //Add some data to context
                        mapcontext.put("NROW", input.getPayLoad(0));
                        mapcontext.put("NCOL", input.getPayLoad(1));
                        mapcontext.put("MAP", input.getPayLoad(2));

                        //Initializing and setting up game loop executer
                        botexec.addTask(mapcontext);

                        //polling for changes in isresp's value
                        while (this.isresp == false);
              
                        System.out.println("Done a 200");

                        KKMessage mess = KKMessage.PrepareMessage(220, -1, this.botretvalue, 0);

                        out.writeObject(mess);

                        break;

                    default:
                        /*
                         * Send Error Message Back
                         */
                        out.writeObject("NOTOK");
                }

                /* reset error count number since successfull loop has occcured.
                 * if simultaneous error count exceeds 3 terminate bot's
                 * connection with server.
                 */

                errcount = 0;
            } catch (IOException ex) {
                errcount++;

                //TODO: Notify UI right now and remove this line
                System.out.println("IOException thrown at client end");

                Logger.getLogger(KKLowLevelBotProcessor.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                errcount++;

                //TODO: Notify UI right now and remove this line

                Logger.getLogger(KKLowLevelBotProcessor.class.getName()).log(Level.SEVERE, null, ex);
            }
        //Normal Bot Transfer
        }

        try {
            in.close();
            out.close();
            client.close();
        } catch (Exception ex) {
        	System.out.println("Resources could not be retrieved completely.");
        }

        System.out.println("Bot's connection with server is now disconnected.");
        //Bot Exit Notification

        //Bot Processor is no longer running
        isrunning = false;
    }
}