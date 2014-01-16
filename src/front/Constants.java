package front;

public interface Constants{

/* Bot number */
public final int TEAM1		= 1;
public final int TEAM2		= 2;	
public final int BOT1		= 1;
public final int BOT2		= 2;
public final int BOT3		= 3;
public final int BOT4		= 4;

/* Bot's attributes */
public final int DEAD 		= -1;

/* Grid's attributes */
public final int STONE 		= 0;
public final int BRICK 		= 1;
public final int HALFBRICK 	= 2;
public final int FREESPACE 	= 5;
public final int WATER 		= 6;
public final int HEALTH		= 7;
public final int BOTSHIELD	= 8;
public final int EAGLESHIELD= 9;
public final int TEAM1EAGLE = 100;
public final int TEAM2EAGLE = 200;
public final int BOT1UP 	= 11;
public final int BOT1RIGHT 	= 12;
public final int BOT1DOWN 	= 13;
public final int BOT1LEFT 	= 14;
public final int BOT2UP 	= 21;
public final int BOT2RIGHT 	= 22;
public final int BOT2DOWN 	= 23;
public final int BOT2LEFT 	= 24;
public final int BOT3UP 	= 31;
public final int BOT3RIGHT 	= 32;
public final int BOT3DOWN 	= 33;
public final int BOT3LEFT 	= 34;
public final int BOT4UP 	= 41;
public final int BOT4RIGHT 	= 42;
public final int BOT4DOWN	= 43;
public final int BOT4LEFT 	= 44;

/* Bot actions */
public final int NOACTION 	= 0;
public final int UP 		= 1;
public final int RIGHT 		= 2; 
public final int DOWN 		= 3;
public final int LEFT 		= 4;
public final int MOVE 		= 5;
public final int SHOOT 		= 6;

/* Server-Client Interaction */
public final int OK			= 1;
public final int TEAM1WON	= 100;
public final int TEAM2WON	= 200;
public final int NORESULT	= 300;

/* Client Constants */
public final int BOTSLEEP	 = 5;

/* Server Constants */
public final int BOTHEALTH 	= 10;
public final int INCHEALTH	= 5;

/* Scoring Info */
public final int BRICKBREAK = 1;
public final int KILL		= 50;
public final int TEAMKILL	= -50;
public final int EAGLEKILL	= 100;
public final int SELFKILL	= -100;

/* Duration Info */
public final int POWERTIME 		= 30;
public final int SERVERSLEEP 	= 0;	 
public final int SHIELDDURATION	= 60;
public final int LOOPTIME		= 1000;
}