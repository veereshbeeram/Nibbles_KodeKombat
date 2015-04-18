/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kodekombatserv;

public class KKLocation {
    public int i,j;

    public KKLocation(){
        i=-1;j=-1;
    }

    public KKLocation(int i,int j){
        this.i=i;
        this.j=j;
    }

    public int geti(){
        return i;
    }

    public int getj(){
        return j;
    }
}
