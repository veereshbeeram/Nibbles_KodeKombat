/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kodekombatserv;

import java.io.*;
import java.util.Random;
import javax.swing.table.TableModel;

/*
 * This unit contains the business logic of our game. All units of speed and
 * weights to different items like food, power ups can be modified from here.
 */
public class KKGame extends Thread {

    private KKBotManager botManager;
    private KKArena arenaModel;
    private KKArenaComponent arenaComp;
    private TableModel botlist;
    private int map[][];
    private boolean isclose;
    private int tottime;
    private int time_fastfood[];
    private KKLocation loc_fastfood[];
    private int time_fullcoursemeal[];
    private KKLocation loc_fullcoursemeal[];
    private int time_powerup[];
    private KKLocation loc_powerup[];

    public KKGame(KKArena arenaModel,int tottime, KKBotManager botMang, KKArenaComponent arenaComp, TableModel model) {
        //Load the arena from a file.
        this.arenaModel=arenaModel;
        this.arenaComp = arenaComp;
        this.botlist = model;
        botManager = botMang;
        map = this.arenaModel.getMap();
        this.arenaComp.setMap(map);
        this.tottime = tottime;

        isclose = false;
    }

    public void setNumofFastFood(int num) {
        Random rand = new Random();
        time_fastfood = new int[num];
        loc_fastfood = new KKLocation[num];

        for (int i = 0; i < num; i++) {
            int time = (int) (rand.nextDouble() * tottime);

            time_fastfood[i] = time;
            loc_fastfood[i] = new KKLocation(-1, -1);
        }
    }

    public void setNumofFullCourseMeal(int num) {
        Random rand = new Random();
        time_fullcoursemeal = new int[num];
        loc_fullcoursemeal = new KKLocation[num];

        for (int i = 0; i < num; i++) {
            int time = (int) (rand.nextDouble() * tottime);

            time_fullcoursemeal[i] = time;
            loc_fullcoursemeal[i] = new KKLocation(-1, -1);
        }
    }

    public void setNumofPowerUp(int num) {
        Random rand = new Random();
        time_powerup = new int[num];
        loc_powerup = new KKLocation[num];

        for (int i = 0; i < num; i++) {
            int time = (int) (rand.nextDouble() * tottime);

            time_powerup[i] = time;
            loc_powerup[i] = new KKLocation(-1, -1);
        }
    }

    public void close() {
        isclose = true;
    }

    @Override
    public void run() {
    //    try {
            Random rand = new Random();
            int currtime = 0;

            KodeKombatServApp.TerminalOutput("Kode kombat game simulation has begun.");

            //About to initialize connection for all bots.

            this.botManager.initBots(2000, 500);
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter("gameOP"));
                out.write(String.valueOf(arenaComp.size.height));
                out.write("\n");
                out.write(String.valueOf(arenaComp.size.width));
                out.write("\n");
                for(int l=0;l<arenaComp.size.height; l++)
                {
                	for(int m = 0; m< arenaComp.size.width; m++)
                	{
                		out.write(String.valueOf(map[l][m]));
                		out.write(" ");
                	}
                	out.write("\n");
                }
	       //     setNumofFastFood(20);
	      //      setNumofFullCourseMeal(10);
	      //      setNumofPowerUp(5);
	            while (!isclose && currtime++ < tottime) {
	      //          KKGame.sleep(50);
	
	                //Check if fastfood timings are matching global time
	                if (time_fastfood != null) {
	                    for (int i = 0; i < time_fastfood.length; i++) {
	                        if (currtime == time_fastfood[i]) {
	                            //Create fastfood
	
	                            boolean done = false;
	
	                            while (!done) {
	                                int ti = (int) (rand.nextDouble() * arenaComp.size.height);
	                                int tj = (int) (rand.nextDouble() * arenaComp.size.width);
	
	                                if (map[ti][tj] == 0) {
	                                    map[ti][tj] = 1;
	                                    loc_fastfood[i].i = ti;
	                                    loc_fastfood[i].j = tj;
	                                    done = true;
	                                }
	                            }
	                        }
	
	                        //TODO: Change the time of lasting of food
	                        if (currtime == time_fastfood[i] + 20) {
	                            //Remove fastfood
	
	                            if (map[loc_fastfood[i].i][loc_fastfood[i].j] == 1) {
	                                map[loc_fastfood[i].i][loc_fastfood[i].j] = 0;
	                            }
	                        }
	                    }
	                }
	
	                //Check if fullcoursemeal timings are matching global time
	                if (time_fullcoursemeal != null) {
	                    for (int i = 0; i < time_fullcoursemeal.length; i++) {
	                        if (currtime == time_fullcoursemeal[i]) {
	                            //Create fullcoursemeal
	
	                            boolean done = false;
	
	                            while (!done) {
	                                int ti = (int) (rand.nextDouble() * arenaComp.size.height);
	                                int tj = (int) (rand.nextDouble() * arenaComp.size.width);
	
	                                if (map[ti][tj] == 0) {
	                                    map[ti][tj] = 2;
	                                    loc_fullcoursemeal[i].i = ti;
	                                    loc_fullcoursemeal[i].j = tj;
	                                    done = true;
	                                }
	                            }
	                        }
	
	                        //TODO: Change the time of lasting of fullcoursemeal
	                        if (currtime == time_fullcoursemeal[i] + 20) {
	                            //Remove fullcoursemeal
	
	                            if (map[loc_fullcoursemeal[i].i][loc_fullcoursemeal[i].j] == 2) {
	                                map[loc_fullcoursemeal[i].i][loc_fullcoursemeal[i].j] = 0;
	                            }
	                        }
	                    }
	                }
	
	                //Check if powerups timings are matching global time
	                if (time_powerup != null) {
	                    for (int i = 0; i < time_powerup.length; i++) {
	                        if (currtime == time_powerup[i]) {
	                            //Create powerup
	
	                            boolean done = false;
	
	                            while (!done) {
	                                int ti = (int) (rand.nextDouble() * arenaComp.size.height);
	                                int tj = (int) (rand.nextDouble() * arenaComp.size.width);
	
	                                if (map[ti][tj] == 0) {
	                                    map[ti][tj] = 3;
	                                    loc_powerup[i].i = ti;
	                                    loc_powerup[i].j = tj;
	                                    done = true;
	                                }
	                            }
	                        }
	
	                        //TODO: Change the time of lasting of powerups
	                        if (currtime == time_powerup[i] + 20) {
	                            //Remove powerup
	
	                            if (map[loc_powerup[i].i][loc_powerup[i].j] == 2) {
	                                map[loc_powerup[i].i][loc_powerup[i].j] = 0;
	                            }
	                        }
	                    }
	                }
	
	                //Comment This portion - starting here
	
	 //               int i = (int) (rand.nextDouble() * arenaComp.size.height);
	 //               int j = (int) (rand.nextDouble() * arenaComp.size.height);
	
	//                map[i][j] = 1;
	
	                //Comment This portion - ending here
	
	                //Call botmanager's run gameloop method
	                this.botManager.performGameLoop(map);
	
	                this.arenaComp.repaint();

	                for(int l=0;l<arenaComp.size.height; l++)
                    {
                    	for(int m = 0; m< arenaComp.size.width; m++)
                    	{
                    		out.append(String.valueOf(map[l][m]));
                    		out.append(" ");
                    	}
                    	out.append("\n");
                    }
	            }
                out.close();
            } catch (IOException e) {
            }

            KodeKombatServApp.TerminalOutput("Kode kombat game simulation has ended.");
            
     //   } catch (InterruptedException ex) {
     //       KodeKombatServApp.TerminalOutput("KKGames sleep interruption. Terminating the game.");
     //       return;
     //   }
    }
}