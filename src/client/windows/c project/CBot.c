#include <stdio.h>
#include "CBot.h"

int game(int len){
	int mynumber;
	int myposition[3];
	int board[len][len];
	
    mynumber = getMyNumber();
	while(1){
		getBoard((int *)board);
		getMyPosition(myposition);
		move();
    	shoot();
        /* The above lines inside while loop are sample lines */
        /* You are required to implement your code here */	
	}
}