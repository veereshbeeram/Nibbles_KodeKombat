/*
 * KodeKombatServApp.java
 */
package kodekombatserv;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class KodeKombatServApp extends SingleFrameApplication {

    private static KodeKombatServView view;

    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup() {
        view = new KodeKombatServView(this);
        show(view);
    }

    public static void TerminalOutput(String str) {
        view.addText(str);
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override
    protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of KodeKombatServApp
     */
    public static KodeKombatServApp getApplication() {
        return Application.getInstance(KodeKombatServApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(KodeKombatServApp.class, args);
    /*double k;

    for(int i=0;i<10000;i++)
    for(int j=0;j<10000;j++)
    k=Math.sqrt(i*j);

    System.out.println("Hello");*/
    }
}
