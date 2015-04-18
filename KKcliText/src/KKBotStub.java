import java.awt.EventQueue;
import java.util.HashMap;

public class KKBotStub extends Thread {

    KKBotInterface bot;
    HashMap botcontext;
    HashMap mapcontext;
    KKBotStubCallback callback;

    public KKBotStub(KKBotInterface bot,KKBotStubCallback callback, HashMap botcontext) {
        this.bot = bot;
        this.botcontext = botcontext;
        this.callback=callback;
    }

    public String getBotName() {
        return bot.getBotName();
    }

    public HashMap getBotContext() {
        return this.botcontext;
    }

    public void setMapContext(HashMap mapcontext) {
        this.mapcontext = mapcontext;
    }

    @Override
    public void run() {
        final int value = bot.executeBotAlgo(mapcontext, botcontext);
        EventQueue.invokeLater(new Runnable(){

            public void run() {
                callback.processReturnValueFromBot(value);
            }
        });
    }
}