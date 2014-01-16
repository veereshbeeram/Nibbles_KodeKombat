import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

class Runner implements ActionListener{
	public Runner(){
		try{
			icon	= Toolkit.getDefaultToolkit().getImage(Runner.class.getResource("gem.gif"));
			l1 		= new JLabel("Client1",SwingConstants.CENTER);
			l2  	= new JLabel("Client2",SwingConstants.CENTER);
			l3 		= new JLabel("C1-Bot1",SwingConstants.CENTER);
			l4 		= new JLabel("C1-Bot2",SwingConstants.CENTER);
			l5 		= new JLabel("C2-Bot1",SwingConstants.CENTER);
			l6 		= new JLabel("C2-Bot2",SwingConstants.CENTER);
			l7		= new JLabel("",SwingConstants.CENTER);
			l8		= new JLabel("Host Address",SwingConstants.CENTER);
			l9		= new JLabel("Host Port",SwingConstants.CENTER);
			l10		= new JLabel("Port",SwingConstants.CENTER);
			l11		= new JLabel("",SwingConstants.CENTER);
			l12		= new JLabel("Bot Name",SwingConstants.CENTER);
			l13		= new JLabel("Client Address",SwingConstants.CENTER);
			l14		= new JLabel("Client Port",SwingConstants.CENTER);
			c1t1	= new JTextField();
			c1t2	= new JTextField();
			c1t3	= new JTextField();
			c2t1	= new JTextField();
			c2t2	= new JTextField();
			c2t3	= new JTextField();
			t1b11	= new JTextField();
			t1b12	= new JTextField();
			t1b13	= new JTextField();
			t1b21	= new JTextField();
			t1b22	= new JTextField();
			t1b23	= new JTextField();			
			t2b11	= new JTextField();
			t2b12	= new JTextField();
			t2b13	= new JTextField();			
			t2b21	= new JTextField();
			t2b22	= new JTextField();
			t2b23	= new JTextField();
			b1		= new JButton("Run Clients");
			b2		= new JButton("Run Bots");
			b3		= new JButton("Kill All");
			p1		= new JPanel(new GridLayout(8,4,5,5));
			p2		= new JPanel();
			p3		= new JPanel(new BorderLayout()); 
			f1		= new JFrame("Runner");
			p1.add(l7);
			p1.add(l8);
			p1.add(l9);
			p1.add(l10);
			p1.add(l1);	
			p1.add(c1t1);	
			p1.add(c1t2);	
			p1.add(c1t3);	
			p1.add(l2);	
			p1.add(c2t1);	
			p1.add(c2t2);	
			p1.add(c2t3);
			p1.add(l11);
			p1.add(l12);
			p1.add(l13);
			p1.add(l14);
			p1.add(l3);	
			p1.add(t1b11);	
			p1.add(t1b12);	
			p1.add(t1b13);
			p1.add(l4);	
			p1.add(t1b21);	
			p1.add(t1b22);	
			p1.add(t1b23);
			p1.add(l5);	
			p1.add(t2b11);	
			p1.add(t2b12);	
			p1.add(t2b13);
			p1.add(l6);	
			p1.add(t2b21);	
			p1.add(t2b22);	
			p1.add(t2b23);
			p2.add(b1);			
			p2.add(b2);
			p2.add(b3);
			p3.add(p1,BorderLayout.CENTER);
			p3.add(p2,BorderLayout.SOUTH);
			f1.add(p3);
			f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			f1.pack();
			f1.setVisible(true);
			f1.setResizable(false);
			f1.setIconImage(icon);
			b1.addActionListener(this);
			b2.addActionListener(this);
			b3.addActionListener(this);
		}
		catch(Exception e){
			System.out.println(e.toString());
		}	
	}

	public void actionPerformed(ActionEvent ae){
		String command = (String)ae.getActionCommand();
		try{
			if(command.equals("Run Clients")){
				if(c1t1.getText().equals("") || c1t2.getText().equals("") || c1t3.getText().equals("") || c2t1.getText().equals("") || c2t2.getText().equals("") || c2t3.getText().equals(""))
					JOptionPane.showMessageDialog(f1,"Required field is empty","Error",JOptionPane.WARNING_MESSAGE);
				else{
					pr1 = Runtime.getRuntime().exec("java -jar client.jar "+c1t1.getText()+" "+c1t2.getText()+" "+c1t3.getText());
					pr2 = Runtime.getRuntime().exec("java -jar client.jar "+c2t1.getText()+" "+c2t2.getText()+" "+c2t3.getText());
				}	
			}
			else if(command.equals("Run Bots")){
				if(t1b11.getText().equals("") || t1b12.getText().equals("") || t1b13.getText().equals("")|| t1b21.getText().equals("") || t1b22.getText().equals("") || t1b23.getText().equals("")|| t2b11.getText().equals("") || t2b12.getText().equals("") || t2b13.getText().equals("")|| t2b21.getText().equals("") || t2b22.getText().equals("") || t2b23.getText().equals(""))
					JOptionPane.showMessageDialog(f1,"Required field is empty","Error",JOptionPane.WARNING_MESSAGE);
				else{
					pr3 = Runtime.getRuntime().exec("java "+t1b11.getText()+" "+t1b12.getText()+" "+t1b13.getText());
					pr4 = Runtime.getRuntime().exec("java "+t1b21.getText()+" "+t1b22.getText()+" "+t1b23.getText());
					pr5 = Runtime.getRuntime().exec("java "+t2b11.getText()+" "+t2b12.getText()+" "+t2b13.getText());
					pr6 = Runtime.getRuntime().exec("java "+t2b21.getText()+" "+t2b22.getText()+" "+t2b23.getText());
				}				
			}
			else if(command.equals("Kill All")){
				if(pr1 != null)
					pr1.destroy();
				if(pr2 != null)
					pr2.destroy();
				if(pr3 != null)
					pr3.destroy();
				if(pr4 != null)
					pr4.destroy();
				if(pr5 != null)
					pr5.destroy();
				if(pr6 != null)
					pr6.destroy();
			}	
		}
		catch(Exception e){
			System.out.println(e.toString());
		}	
	}
	
	public static void main(String args[]){
		Runner t = new Runner();
	}

Image		icon;	
Process		pr1,pr2,pr3,pr4,pr5,pr6;	
JLabel 		l1,l2,l3,l4,l5,l6,l7,l8,l9,l10,l11,l12,l13,l14;
JTextField 	c1t1,c1t2,c1t3,c2t1,c2t2,c2t3,t1b11,t1b12,t1b13,t1b21,t1b22,t1b23,t2b11,t2b12,t2b13,t2b21,t2b22,t2b23;
JButton 	b1,b2,b3;	
JPanel 		p1,p2,p3;
JFrame 		f1;
}