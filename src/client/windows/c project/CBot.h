#ifndef _Included_CBot
#define _Included_CBot

/* Bot number */
int TEAM1		= 1;
int TEAM2		= 2;	
int BOT1		= 1;
int BOT2		= 2;
int BOT3		= 3;
int BOT4		= 4;

/* Grid's attributes */
int STONE 		= 0;
int BRICK 		= 1;
int HALFBRICK 	= 2;
int FREESPACE 	= 5;
int WATER 		= 6;
int HEALTH		= 7;
int BOTSHIELD	= 8;
int EAGLESHIELD = 9;
int TEAM1EAGLE 	= 100;
int TEAM2EAGLE 	= 200;
int BOT1UP 		= 11;
int BOT1RIGHT 	= 12;
int BOT1DOWN 	= 13;
int BOT1LEFT 	= 14;
int BOT2UP 		= 21;
int BOT2RIGHT 	= 22;
int BOT2DOWN 	= 23;
int BOT2LEFT 	= 24;
int BOT3UP 		= 31;
int BOT3RIGHT 	= 32;
int BOT3DOWN 	= 33;
int BOT3LEFT 	= 34;
int BOT4UP 		= 41;
int BOT4RIGHT 	= 42;
int BOT4DOWN	= 43;
int BOT4LEFT 	= 44;

/* Bot actions */
int NOACTION 	= 0;
int UP 			= 1;
int RIGHT 		= 2; 
int DOWN 		= 3;
int LEFT 		= 4;
int MOVE 		= 5;
int SHOOT 		= 6;

/* Result decider */
int TEAM1WON	= 100;
int TEAM2WON	= 200;
int NORESULT	= 300;

/* Functions */
void changePhase(int);
void move();
void shoot();
int getMyNumber();
int getMyTeamNumber();
int getMyHealth();
int getBotHealth(int);
int amIShielded();
int isBotShielded(int);
int isBulletAt(int ,int);
int elementAt(int , int);
void getMyPosition(int *);
void getBotPosition(int ,int *);
void getBonusPositions(int *);
void getBoard(int *);
int game(int);

#endif
