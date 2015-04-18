/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kodekombatserv;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/*
 * This unit contains the business logic of our game. All units of speed and
 * weights to different items like food, power ups can be modified from here.
 */
public class KKPlayBack extends Thread {

    private KKArenaComponent arenaComp;
    private Timer timer;
    BufferedReader reader;
    FileReader f;
    String filename;
    int height, width;
    int[][] tmap;
    int done=0;
    int i;
    
    
    public KKPlayBack(KKArenaComponent arenaComp) {
        this.arenaComp = arenaComp;
        filename = "gameOP";
        i=0;
    }
    
    public void run(){
        try {
        	f = new FileReader(filename);
	        reader = new BufferedReader(f);
	
	        height = Integer.parseInt(reader.readLine());
	        width = Integer.parseInt(reader.readLine());
	        tmap = new int[height][width];
	        KodeKombatServApp.TerminalOutput(String.valueOf(height));
	        KodeKombatServApp.TerminalOutput(String.valueOf(width));
	        timer = new Timer();
	        timer.schedule(new task(), 500);
	
	    } catch (FileNotFoundException e) {
	        // TODO Auto-generated catch block
	        KodeKombatServApp.TerminalOutput("Map file could not be located");
	        return;
	    } catch (NumberFormatException e) {
	        // TODO Auto-generated catch block
	    	KodeKombatServApp.TerminalOutput("Number Format exception has occured.");
	        return;
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    	KodeKombatServApp.TerminalOutput("Map file could not be parsed.");
	        return;
	    }
    }
    
    class task extends TimerTask{
        public void run() {
        	try {
        		timer.cancel();
        	//	KodeKombatServApp.TerminalOutput("triggered");
        		String tmp = reader.readLine();
				if(tmp.equals("1")){
	        	//	KodeKombatServApp.TerminalOutput("ready");
				    for (int i = 0; i < height; i++) {
				        String line = reader.readLine();
				 //       KodeKombatServApp.TerminalOutput("L"+String.valueOf(i)+": "+line+"d");
				        String args[] = line.split(" ", width);
				        args[width-1]=args[width-1].substring(0, args[width-1].length()-1);
				        for (int j = 0; j < width; j++) {
				 //           KodeKombatServApp.TerminalOutput(":"+args[j] + ": "+tmap);
				        	tmap[i][j] = Integer.parseInt(args[j]);
				 //           KodeKombatServApp.TerminalOutput(":");
				        }
				    }
				    i++;
				    KodeKombatServApp.TerminalOutput("Frame "+String.valueOf(i));
				   // this.arenaModel.map = tmap;
				    KKPlayBack.this.arenaComp.setMap(tmap);
			 //       KodeKombatServApp.TerminalOutput(arenaComp.cell[1].toString());
			 //       KodeKombatServApp.TerminalOutput(arenaComp.cell[0].toString());
			        KKPlayBack.this.arenaComp.repaint();
				    timer = new Timer();
				    timer.schedule(new task(), 600);
				}else{
					done=1;
					timer.cancel();
	        //		KodeKombatServApp.TerminalOutput("dne");
			        reader.close();
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				KodeKombatServApp.TerminalOutput("numformat exception");
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
}