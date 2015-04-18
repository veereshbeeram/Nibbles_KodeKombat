import java.io.IOException;
import java.io.DataInputStream;


public class clientMain {
	private static KKLowLevelBotProcessor botProc;
    //private static KKBot botinterface;
    private static KKBot botclass;

	public static void main(String[] args){
		botclass=new KKBot();
		System.out.println("enter Ipaddress:");
		String ipadd="127.0.0.1";
		java.io.DataInputStream in =new java.io.DataInputStream(System.in);
		try{
		ipadd = in.readLine();
		}
		catch(Exception e){
			
		}
		
		try{
			
			System.out.println("Bot by the name \""+botclass.getBotName()+"\" is loaded.");
			botProc = new KKLowLevelBotProcessor(ipadd,"20000",botclass);
			botProc.start();
			System.out.println("Connected...\n");
        } /*catch (InstantiationException ex) {
        	System.out.println("Bot class could not be instantiated.");
        } catch (IllegalAccessException ex) {
        	System.out.println("Illegal access exception in accessing class");
        }*/ catch (NumberFormatException ex) {
        	System.out.println("Port number is not in correct format.");
        } catch (IOException ex){
        	System.out.println("Could not connect to server. IOException thrown.");
        }
		}
	/*private static void Initiategame(Class botcls){
		botclass=botcls;
	}*/
	}
	
