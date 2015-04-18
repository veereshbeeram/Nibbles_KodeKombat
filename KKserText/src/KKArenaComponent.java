public class KKArenaComponent{
    int arr[][];
    int width,height;
    public KKArenaComponent(int width,int height) {
        this.width=width;
        this.height=height;
    }

    public void setMap(int map[][])
    {
        arr=map;    
    }

   /* public void LoadImages()
    {
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(kodekombatserv.KodeKombatServApp.class).getContext().getResourceMap(KodeKombatServView.class);
        cell=new Image[14];
        cell[0]=resourceMap.getImageIcon("ArenaComponent.EmptyCell").getImage();
        cell[1]=resourceMap.getImageIcon("ArenaComponent.FastFoodCell").getImage();
        cell[2]=resourceMap.getImageIcon("ArenaComponent.FullCourseMealCell").getImage();
        cell[3]=resourceMap.getImageIcon("ArenaComponent.PowerUpCell").getImage();
        cell[4]=resourceMap.getImageIcon("ArenaComponent.ObstacleCell").getImage();
        cell[5]=resourceMap.getImageIcon("ArenaComponent.Bot1").getImage();
        cell[6]=resourceMap.getImageIcon("ArenaComponent.Bot1Head").getImage();
        cell[7]=resourceMap.getImageIcon("ArenaComponent.Bot2").getImage();
        cell[8]=resourceMap.getImageIcon("ArenaComponent.Bot2Head").getImage();
        cell[9]=resourceMap.getImageIcon("ArenaComponent.Bot3").getImage();
        cell[10]=resourceMap.getImageIcon("ArenaComponent.Bot3Head").getImage();
        cell[11]=resourceMap.getImageIcon("ArenaComponent.Bot4").getImage();
        cell[12]=resourceMap.getImageIcon("ArenaComponent.Bot4Head").getImage();
        cell[13]=resourceMap.getImageIcon("ArenaComponent.IllegalValueCell").getImage();
    }

    @Override
    public void paint(Graphics g) {
        if(size.width==0||size.height==0)
            return;
        
        int hstep=this.getWidth()/size.width,vstep=this.getHeight()/size.height;

        for(int i=0;i<this.getHeight();i+=vstep)
            g.drawLine(0,i,this.getWidth(),i);

        for(int i=0;i<this.getWidth();i+=hstep)
            g.drawLine(i, 0, i, this.getHeight());
        
        g.drawRect(0, 0, this.getWidth()-1,this.getHeight()-1);

        if(cell==null||arr==null)
            return;

        //System.out.println(size.height+" "+size.width);
        
        for(int i=0;i<size.height;i++)
            for(int j=0;j<size.width;j++)
            {
                //Draws all the cells necessary.
                Image img;

                switch(arr[i][j])
                {
                    case 0:
                        img=cell[0];
                        break;

                    case 1:
                        img=cell[1];
                        break;

                    case 2:
                        img=cell[2];
                        break;

                    case 3:
                        img=cell[3];
                        break;

                    case 4:
                        img=cell[4];
                        break;

                    case 10:
                        img=cell[5];
                        break;

                    case 11:
                        img=cell[6];
                        break;

                    case 20:
                        img=cell[8];
                        break;

                    case 21:
                        img=cell[8];
                        break;

                    case 30:
                        img=cell[9];
                        break;

                    case 31:
                        img=cell[10];
                        break;

                    case 40:
                        img=cell[11];
                        break;

                    case 41:
                        img=cell[12];
                        break;

                    default:
                        img=cell[13];
                        break;
                }
                g.drawImage(img, j*hstep, i*vstep, hstep, vstep, null);
            }
    }*/
}
