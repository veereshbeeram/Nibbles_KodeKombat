/*
 * 
 * public int moveStraight() : moves the head one unit in the direction of the head of the snake.
 * public int moveLeft() : moves the head one unit to the left.
 * public int moveRight() : moves the head one unit to the right.
 *
 * Each of these function calls should cause the main map to be modified
 * immediately.
 * 
 * Return values
 * 0 - if move is successfull
 * -1 - move fails due to hitting an obstacle.
 *
 * Implement the necessary data structures like linked lists.
 * Do necessary modifications in KKArena and KKArenaComponent.
 *
 * Use Random pics to show movement of the snake on calling these functions.
 * 
 */
package kodekombatserv;

import java.util.LinkedList;

public class KKSnake {

    int id;
    String name;
    LinkedList<KKLocation> body;
    int moves;
    int length;
    int dir; //0,1,2,3 for up, right, down, left    
    int errorStat;
    int npower;
    KKLocation startloc;
    String errorMessage;
    KKLowLevelBotProcessor client;

    public KKSnake(int id, String name, int len, int i, int j, KKLowLevelBotProcessor sock) {
        moves = len-1;

        switch (id) {
            case 0:
                dir = 2;
                break;

            case 1:
                dir = 0;
                break;

            case 2:
                dir = 2;
                break;

            case 3:
                dir = 0;
                break;
        }

        this.id = id;
        this.name = name;

        this.client = sock;

        this.npower = 0;

        startloc = new KKLocation(i, j);
        body = new LinkedList<KKLocation>();
        body.add(startloc);

        errorStat = 0;
        errorMessage = "OK";
    }

    public void ReportError(int err, String mess) {
        this.errorStat = err;
        this.errorMessage = mess;
    }

    private KKLocation step() {
        switch (dir) {
            case 0:
                return new KKLocation(body.getLast().i, body.getLast().j - 1);
            case 1:
                return new KKLocation(body.getLast().i + 1, body.getLast().j);
            case 2:
                return new KKLocation(body.getLast().i, body.getLast().j + 1);
            case 3:
                return new KKLocation(body.getLast().i - 1, body.getLast().j);
            default:
                KodeKombatServApp.TerminalOutput("Not Supported direction " + dir);
                return new KKLocation(body.getLast().i, body.getLast().j + 1);
        }
    }

    public void move(int i, int j, int map[][]) {
        KodeKombatServApp.TerminalOutput("Size of body: " + body.size());
        KKLocation head = body.getLast();

        body.add(new KKLocation(i, j));

        //Sets current head as body        
        map[head.i][head.j]--;

        //Sets the new location as its head
        map[i][j] = (id + 1) * 10 + 1;

        if (moves == 0) {
            KKLocation loc = body.removeFirst();
            map[loc.i][loc.j] = 0;
        } else {
            moves--;
        }

        KodeKombatServApp.TerminalOutput("Size of body at the end: " + body.size());
    }

    public KKLocation turnLeft() {
        dir = (dir + 3) % 4;
        return step();
    }

    public KKLocation turnRight() {
        dir = (dir + 1) % 4;
        return step();
    }

    public KKLocation Forward() {
        return step();
    }

    public void crash(int map[][]) {
    	int n = body.size() + moves + 3;
    	if(n%2 == 1)
    		n++;
        n = (int)((double)(n)/2);
        moves = n;

        while (body.size() > 0) {
            KKLocation loc = body.removeLast();
            //Clears the map
            map[loc.i][loc.j] = 0;
        }

        //Bot loses power up on crashing
        npower = 0;

        body.add(startloc);
        moves--;
        map[startloc.i][startloc.j] = (this.id + 1) * 10 + 1;

        KodeKombatServApp.TerminalOutput("Bot " + this.id + " has crashed into some obstacle or another bot");
    }

    public void eat(int weight) {
        if (npower != 0) {
            weight *= 2;
            npower--;
        }

        moves += weight;
    }

    public void power(int weight) {
        //TODO: Change response to power up may be increment the npower up

        npower += weight;
    }
}