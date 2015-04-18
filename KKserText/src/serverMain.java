


public class serverMain {
	//private KKArenaComponent arena; -- Used to display the map and hence not required
    private static KKArena arenaModel;
    private KKArenaComponent arena;
    private static KKBotManager botManager;
    KKGame game;
	public static void main(String[] args){
		arenaModel=KKArena.LoadMap("map.kkmap");
		if(arenaModel==null)
        {
            arenaModel=new KKArena(40,40);
            System.out.println("Error Loading map file.");
   //         KodeKombatServApp.TerminalOutput("Map could not be loaded. Default open map loaded.");
        }
		botManager=new KKBotManager(arenaModel.width,arenaModel.height);
	botManager.startServer(20000);
	}
	public void startGame(){
		this.arena=new KKArenaComponent(arenaModel.width,arenaModel.height);
        this.arena.setMap(arenaModel.map);
		 game=new KKGame(arenaModel,75,botManager,this.arena);
	        game.start();
	}
	
}
