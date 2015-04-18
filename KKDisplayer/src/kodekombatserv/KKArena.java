/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kodekombatserv;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class KKArena {

    int map[][];
    int width, height;
    String mapfile;

    public KKArena() {
        width = 0;
        height = 0;
        mapfile = "";
    }

    public KKArena(int width, int height) {
        map = new int[width][height];
        this.width = width;
        this.height = height;
    }
    
    public int[][] getMap()
    {
    	return map;
    }

    public static KKArena LoadMap(String filename) {
        KKArena model = null;

        //Implement a parser to read map file and load necessary details.

        try {
            model = new KKArena();
            model.mapfile = filename;

            BufferedReader reader = new BufferedReader(new FileReader(filename));

            model.height = Integer.parseInt(reader.readLine());
            model.width = Integer.parseInt(reader.readLine());
            model.map = new int[model.height][model.width];

            for (int i = 0; i < model.height; i++) {
                String line = reader.readLine();
                String args[] = line.split(" ", model.width);
                for (int j = 0; j < model.width; j++) {
                    model.map[i][j] = Integer.parseInt(args[j]);
                }
            }

            reader.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
//            KodeKombatServApp.TerminalOutput("Map file could not be located");
            return null;
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            //           KodeKombatServApp.TerminalOutput("Number Format exception has occured.");
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
//            KodeKombatServApp.TerminalOutput("Map file could not be parsed.");
            return null;
        }

        return model;
    }
}
