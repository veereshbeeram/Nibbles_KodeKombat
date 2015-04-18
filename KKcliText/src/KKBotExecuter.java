import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class KKBotExecuter extends Thread implements KKBotStubCallback {

    HashMap botcontext, mapcontext;
    boolean execute = false;
    boolean isclose = false;
    boolean newtask = false;
    KKBotStub botthread;
    Timer primtimer, sectimer;
    TimerTask primtask, sectask;
    long ptimeout, stimeout;
    final KKBotInterface botinterface;
    final KKBotStubCallback itself;
    KKLowLevelBotProcessor botProc;

    public KKBotExecuter(int tptimeout, int tstimeout, final KKBotInterface botinterface, KKLowLevelBotProcessor botProc) {
        botcontext = new HashMap();
        botthread = new KKBotStub(botinterface, this, botcontext);

        this.ptimeout = tptimeout;
        this.stimeout = tstimeout;

        this.resetAllTimers();
        itself = this;

        this.botinterface = botinterface;
        this.botProc = botProc;
    }

    private void resetAllTimers()
    {
        primtimer = new Timer();
        sectimer = new Timer();       
    }

    class sectask extends TimerTask{

        public void run() {
        	sectimer.cancel();
        	System.out.println("Secondary timer triggered.");
            botthread.bot.onSecondaryTimeout();
            botthread.stop();
            botthread = new KKBotStub(botinterface, itself, botcontext);
            processReturnValueFromBot(3);
        }
    }

    class primtask extends TimerTask{

        public void run() {
            primtimer.cancel();
        	System.out.println("Primary timer triggered.");
            execute = false;
            botthread.bot.onPrimaryTimeout();
            sectimer.schedule(new sectask(), stimeout);
        }
    }
    
    @SuppressWarnings("empty-statement")
    public void addTask(HashMap mapcontext) {
        while (newtask);
        this.mapcontext = mapcontext;
        newtask = true;
        execute = false;
        System.out.println("New task added");
    }

    public void close() {
        isclose = true;
    }

    @Override
    @SuppressWarnings("empty-statement")
    public void run() {
        while (!isclose) {
        	System.out.println("Waiting for a new task to be added");
            while (!newtask && !isclose);

            this.resetAllTimers();

            if (isclose) {
                break;
            }

            botthread = new KKBotStub(botinterface, this, botcontext);
            botthread.setMapContext(mapcontext);
            execute = true;
            this.botthread.start();
            this.primtimer.schedule(new primtask(), this.ptimeout);
            newtask = false;
        }

        System.out.println("Closing Bot executer");
    }

    public void processReturnValueFromBot(int retvalue) {
        if (execute == false) {
            //stop secondary timer
            this.sectimer.cancel();

            //Make bot go straight
            this.botProc.sendResponse(3);
        } else {
            execute = false;
            //stop primary timer;
            this.primtimer.cancel();

            //Send
            this.botProc.sendResponse(retvalue);
        }
    }
}