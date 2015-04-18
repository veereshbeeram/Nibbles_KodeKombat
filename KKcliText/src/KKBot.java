import java.util.*;
public class KKBot implements KKBotInterface{
	public String getBotName() {
        return "Pups Bot";
    }
	 public int executeBotAlgo(HashMap mapcontext, HashMap botcontext) {
		 int count=0,i,j;
		 int kkmap[][]=(int[][]) mapcontext.get("MAP");
		 int nrow=(Integer)mapcontext.get("NROW");
		 int ncol=(Integer)mapcontext.get("NCOL");

		 if (botcontext.containsKey("VAL")) {
	            count = (Integer) botcontext.get("VAL");
		 }
		 botcontext.put("VAL", ++count);
		 
	        if((int)(Math.random()*10) == 4)
	        	return (int)(Math.random()*2+1);
	        else
	        	return 3;
	 }
	 public void onPrimaryTimeout() {
	        //throw new UnsupportedOperationException("Not supported yet.");
		 return;
	    }
	 public void onSecondaryTimeout() {
	        throw new UnsupportedOperationException("Not supported yet.");
	    }

}
