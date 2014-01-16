package front;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;

public class Frontend extends Thread implements Constants{

public void init(){	
	flag		= 0;
	f			= new Font("Tahoma",Font.BOLD,12);
	good		= new Color(43,158,43);
	bad			= new Color(190,40,35);
	icon		= Toolkit.getDefaultToolkit().getImage(Frontend.class.getResource("/include/gem.gif"));
	ibmicon		= new ImageIcon(Frontend.class.getResource("/include/ibm.jpg"));
	backicon 	= new ImageIcon(Frontend.class.getResource("/include/back.jpg"));
	brickicon 	= new ImageIcon(Frontend.class.getResource("/include/brick.jpg"));
	halfbrick	= new ImageIcon(Frontend.class.getResource("/include/halfbrick.jpg"));
	bulletfree	= new ImageIcon(Frontend.class.getResource("/include/bulletfree.jpg"));
	bulletwater	= new ImageIcon(Frontend.class.getResource("/include/bulletwater.jpg"));
	eagleicon 	= new ImageIcon(Frontend.class.getResource("/include/eagle.jpg"));
	healthicon	= new ImageIcon(Frontend.class.getResource("/include/health.jpg"));
	botshield	= new ImageIcon(Frontend.class.getResource("/include/botshield.jpg"));
	eagleshield	= new ImageIcon(Frontend.class.getResource("/include/eagleshield.jpg"));
	stoneicon 	= new ImageIcon(Frontend.class.getResource("/include/stone.jpg"));
	watericon 	= new ImageIcon(Frontend.class.getResource("/include/water.jpg"));
	tank11icon 	= new ImageIcon(Frontend.class.getResource("/include/tank11.jpg"));
	tank12icon	= new ImageIcon(Frontend.class.getResource("/include/tank12.jpg"));
	tank13icon 	= new ImageIcon(Frontend.class.getResource("/include/tank13.jpg"));
	tank14icon 	= new ImageIcon(Frontend.class.getResource("/include/tank14.jpg"));
	tank15icon 	= new ImageIcon(Frontend.class.getResource("/include/tank15.jpg"));
	tank16icon	= new ImageIcon(Frontend.class.getResource("/include/tank16.jpg"));
	tank17icon 	= new ImageIcon(Frontend.class.getResource("/include/tank17.jpg"));
	tank18icon 	= new ImageIcon(Frontend.class.getResource("/include/tank18.jpg"));
	tank31icon 	= new ImageIcon(Frontend.class.getResource("/include/tank31.jpg"));
	tank32icon 	= new ImageIcon(Frontend.class.getResource("/include/tank32.jpg"));
	tank33icon 	= new ImageIcon(Frontend.class.getResource("/include/tank33.jpg"));
	tank34icon 	= new ImageIcon(Frontend.class.getResource("/include/tank34.jpg"));
	tank35icon 	= new ImageIcon(Frontend.class.getResource("/include/tank35.jpg"));
	tank36icon 	= new ImageIcon(Frontend.class.getResource("/include/tank36.jpg"));
	tank37icon 	= new ImageIcon(Frontend.class.getResource("/include/tank37.jpg"));
	tank38icon 	= new ImageIcon(Frontend.class.getResource("/include/tank38.jpg"));
	
	ibmpic		= new JLabel(ibmicon,SwingConstants.RIGHT);
	score		= new JLabel("Score");
	score1		= new JLabel("Bot1");
	score2		= new JLabel("Bot2");
	score3		= new JLabel("Bot3");
	score4		= new JLabel("Bot4");
	score5     	= new JLabel("");
	score6     	= new JLabel("");
	score7     	= new JLabel("");
	score8     	= new JLabel("");
	health		= new JLabel("Health");
	health1     = new JLabel("Bot1");
	health2     = new JLabel("Bot2");
	health3     = new JLabel("Bot3");
	health4     = new JLabel("Bot4");
	info		= new JLabel("Info");
	health5		= new JProgressBar(0,BOTHEALTH);
	health6		= new JProgressBar(0,BOTHEALTH);
	health7		= new JProgressBar(0,BOTHEALTH);
	health8		= new JProgressBar(0,BOTHEALTH);
	jta			= new JTextArea("");
	
	gridlabel 	= new JLabel[size][size];
	jp1 		= new JPanel(new GridLayout(size ,size ,0 ,0));
	jp2 		= new JPanel();
	jf1			= new JFrame("Battle City");
		
	drawGrid();
	gl	= new GroupLayout(jp2);
	jp2.setLayout(gl);
	gl.setAutoCreateGaps(true);
	
	GroupLayout.ParallelGroup hGroup = gl.createParallelGroup();
	GroupLayout.SequentialGroup vGroup = gl.createSequentialGroup();

	hGroup.addGroup(gl.createSequentialGroup().
		addComponent(ibmpic));	
	hGroup.addGroup(gl.createSequentialGroup().
		addGroup(gl.createParallelGroup().
			addComponent(score).addComponent(score1).addComponent(score2).addComponent(score3).addComponent(score4).
			addComponent(health).addComponent(health1).addComponent(health2).addComponent(health3).addComponent(health4).
			addComponent(info)).
		addGroup(gl.createParallelGroup().
			addComponent(score5).addComponent(score6).addComponent(score7).addComponent(score8).
			addComponent(health5).addComponent(health6).addComponent(health7).addComponent(health8)));
	hGroup.addGroup(gl.createSequentialGroup().
		addComponent(jta));		
	gl.setHorizontalGroup(hGroup);
	
	vGroup.addGroup(gl.createParallelGroup().
		addComponent(ibmpic));	
	vGroup.addGroup(gl.createParallelGroup().
		addComponent(score));
	vGroup.addGroup(gl.createParallelGroup().
		addComponent(score1).addComponent(score5));
	vGroup.addGroup(gl.createParallelGroup().
		addComponent(score2).addComponent(score6));
	vGroup.addGroup(gl.createParallelGroup().
		addComponent(score3).addComponent(score7));
	vGroup.addGroup(gl.createParallelGroup().
		addComponent(score4).addComponent(score8));
	vGroup.addGroup(gl.createParallelGroup().
		addComponent(health));
	vGroup.addGroup(gl.createParallelGroup().
		addComponent(health1).addComponent(health5));
	vGroup.addGroup(gl.createParallelGroup().
		addComponent(health2).addComponent(health6));
	vGroup.addGroup(gl.createParallelGroup().
		addComponent(health3).addComponent(health7));
	vGroup.addGroup(gl.createParallelGroup().
		addComponent(health4).addComponent(health8));
	vGroup.addGroup(gl.createParallelGroup().
		addComponent(info));	
	vGroup.addGroup(gl.createParallelGroup().
		addComponent(jta));		
	gl.setVerticalGroup(vGroup);
	
	jf1.setLayout(new BorderLayout());
	jf1.add(jp1,BorderLayout.CENTER);
	jf1.add(jp2,BorderLayout.EAST);	

	score.setFont(f);
	health.setFont(f);	
	score.setForeground(Color.BLUE);
	health.setForeground(Color.BLUE);
	health5.setStringPainted(true);
	health6.setStringPainted(true);
	health7.setStringPainted(true);
	health8.setStringPainted(true);
	info.setFont(f);
	jta.setFont(f);
	info.setForeground(Color.BLUE);
	jta.setBackground(new Color(238,238,238));
	jta.setEditable(false);
	
	jf1.setIconImage(icon);
	jf1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	jf1.pack();
	jf1.setResizable(false);
	jf1.setVisible(true);
}


public void setLabelImage(int i, int j){
	switch(grid[i][j]){
	case STONE:
		gridlabel[i][j].setIcon(stoneicon);
		break;
	case FREESPACE:
		gridlabel[i][j].setIcon(backicon);
		break;
	case WATER:
		gridlabel[i][j].setIcon(watericon);
		break;
	case BRICK:
		gridlabel[i][j].setIcon(brickicon);
		break;
	case HALFBRICK:
		gridlabel[i][j].setIcon(halfbrick);
		break;
	case HEALTH:
		gridlabel[i][j].setIcon(healthicon);
		break;
	case BOTSHIELD:
		gridlabel[i][j].setIcon(botshield);
		break;
	case EAGLESHIELD:
		gridlabel[i][j].setIcon(eagleshield);
		break;	
	case TEAM1EAGLE:
	case TEAM2EAGLE:	
		gridlabel[i][j].setIcon(eagleicon);
		break;	
	case BOT1UP:
	case BOT2UP:
		gridlabel[i][j].setIcon(tank11icon);
		break;
	case BOT1RIGHT:
	case BOT2RIGHT:
		gridlabel[i][j].setIcon(tank12icon);
		break;
	case BOT1DOWN:
	case BOT2DOWN:
		gridlabel[i][j].setIcon(tank13icon);
		break;
	case BOT1LEFT:
	case BOT2LEFT:
		gridlabel[i][j].setIcon(tank14icon);
		break;
	case 15:
	case 25:
		gridlabel[i][j].setIcon(tank15icon);
		break;
	case 16:
	case 26:
		gridlabel[i][j].setIcon(tank16icon);
		break;
	case 17:
	case 27:
		gridlabel[i][j].setIcon(tank17icon);
		break;
	case 18:
	case 28:
		gridlabel[i][j].setIcon(tank18icon);
		break;		
	case BOT3UP:
	case BOT4UP:
		gridlabel[i][j].setIcon(tank31icon);
		break;
	case BOT3RIGHT:
	case BOT4RIGHT:
		gridlabel[i][j].setIcon(tank32icon);
		break;
	case BOT3DOWN:
	case BOT4DOWN:
		gridlabel[i][j].setIcon(tank33icon);
		break;
	case BOT3LEFT:
	case BOT4LEFT:
		gridlabel[i][j].setIcon(tank34icon);
		break;
	case 35:
	case 45:
		gridlabel[i][j].setIcon(tank35icon);
		break;
	case 36:
	case 46:
		gridlabel[i][j].setIcon(tank36icon);
		break;
	case 37:
	case 47:
		gridlabel[i][j].setIcon(tank37icon);
		break;
	case 38:
	case 48:
		gridlabel[i][j].setIcon(tank38icon);
		break;		
	default:
		if(grid[i][j]/10 == FREESPACE)
			gridlabel[i][j].setIcon(bulletfree);
		else if(grid[i][j]/10 == WATER)		
			gridlabel[i][j].setIcon(bulletwater);
		break;	
	}
}


public void drawGrid(){
	int i,j;
	for (i=0;i<size;i++){
		for(j=0;j<size;j++){
			gridlabel[i][j] = new JLabel();
			setLabelImage(i,j);
			jp1.add(gridlabel[i][j]);
		}	
	}
}

public void showGame() throws Exception{
	int i,j,k,retval;
	try{
		file	= new File(".");
		jfc 	= new JFileChooser(file);
	    filter 	= new FileNameExtensionFilter("Battle City Files", "bc");
	    jfc.setFileFilter(filter);
		retval = jfc.showOpenDialog(null);
	    if(retval != JFileChooser.APPROVE_OPTION)
			System.exit(0);	
		file 	= jfc.getSelectedFile();
		dis 	= new DataInputStream(new FileInputStream(file));
		size	= dis.readInt();
		grid	= new int[size][size];
		for(i=0;i<size;i++){
			for(j=0;j<size;j++)
				grid[i][j] = dis.readInt();
		}
		init();
		while((i=dis.readInt()) != -4){
			if(i == -1){
				if(flag != 1){
					flag = 1;
					this.sleep(100);
				}	
			}
			else if(i == -2){
				score5.setText((new Integer(dis.readInt())).toString());
				score6.setText((new Integer(dis.readInt())).toString());
				score7.setText((new Integer(dis.readInt())).toString());
				score8.setText((new Integer(dis.readInt())).toString());
				setHealth(health5, dis.readInt());
				setHealth(health6, dis.readInt());
				setHealth(health7, dis.readInt());
				setHealth(health8, dis.readInt());
			}
			else if(i == -3){
				i = dis.readInt();
				byte[] b = new byte[i];
				dis.readFully(b);
				jta.append((new String(b))+"\n");
			}
			else{
				flag = 0;
				j=dis.readInt();
				k=dis.readInt();
				grid[i][j] = k;
				setLabelImage(i,j);
			}			
		}
		dis.close();
	}
	catch(Exception e){
		throw e;
	}
}

public void setHealth(JProgressBar jp, int h){
	jp.setValue(h);
	jp.setString((new Integer(h)).toString());
	if(h > 5)
		jp.setForeground(good);
	else
		jp.setForeground(bad);
}


public static void main(String args[]){
	try{
		Frontend f = new Frontend();
		f.showGame();
	}
	catch(Exception e){
		System.out.println(e.toString());
	}
}

private int size,flag;
private int[][] grid;
private Font f;
private File file;
private Color good,bad;
private Image icon;
private Icon backicon;
private Icon brickicon,halfbrick;
private Icon bulletfree,bulletwater;
private Icon eagleicon;
private Icon healthicon,ibmicon;
private Icon botshield,eagleshield;
private Icon stoneicon;
private Icon tank11icon,tank12icon,tank13icon,tank14icon,tank15icon,tank16icon,tank17icon,tank18icon;
private Icon tank31icon,tank32icon,tank33icon,tank34icon,tank35icon,tank36icon,tank37icon,tank38icon;
private Icon watericon;
private JLabel[][] gridlabel;
private JLabel score,score1,score2,score3,score4,score5,score6,score7,score8,ibmpic;
private JLabel health,health1,health2,health3,health4,info;
private JProgressBar health5,health6,health7,health8;
private JTextArea jta;
private GroupLayout gl;
private JPanel jp1,jp2;
private JFrame jf1;
private JFileChooser jfc;
private FileNameExtensionFilter filter;
private DataInputStream dis;
}