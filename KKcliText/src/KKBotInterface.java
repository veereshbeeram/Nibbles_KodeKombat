import java.util.HashMap;

/*
 * This bot interface will be implemented by the programmer of the bot.
 */
public interface KKBotInterface {    
    String getBotName();
    
    int executeBotAlgo(HashMap mapcontext, HashMap botcontext);
    
    void onPrimaryTimeout();
    
    void onSecondaryTimeout();
}
