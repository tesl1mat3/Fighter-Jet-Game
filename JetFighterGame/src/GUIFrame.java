import java.io.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIFrame extends JFrame implements ActionListener{

	static String UserName, PassWord, PlayerScore;
	static int difficulty, requiredEnemySpawn;

	public static void main(String[] args){
			
		JMenuItem register = new JMenuItem("Register");
		JMenuItem play = new JMenuItem("Play Game");
		JMenuItem scoreboard = new JMenuItem("Scoreboard");
		JMenuItem exit = new JMenuItem("Exit");
		JMenuItem credits = new JMenuItem("Credits");
	
		JMenu fileMenu = new JMenu("File");	
		fileMenu.add(register);
		fileMenu.add(play);
		fileMenu.add(scoreboard);
		fileMenu.add(exit);
		
		JMenu HelpMenu = new JMenu("Help");
		HelpMenu.add(credits);
			
		JMenuBar bar = new JMenuBar();
		bar.add(fileMenu);
		bar.add(HelpMenu);
			
		Icon background=new ImageIcon(GUIFrame.class.getResource("LoginScreen.jpg"));
		JLabel backgroundLabel = new JLabel(background);
			
		JFrame loginFrame = new JFrame("Jet Fighter Game Login");
		loginFrame.add(backgroundLabel);
		loginFrame.setVisible(true);
		loginFrame.setJMenuBar(bar);
		loginFrame.setSize(800, 450);
		loginFrame.setResizable(false);
		loginFrame.setLocationRelativeTo(null);
		loginFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	
		register.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent a)
			{
				JTextField ID = new JTextField(5);
				JTextField Password = new JTextField(5);

				JPanel myPanel = new JPanel();
				myPanel.add(new JLabel("Name:"));
				myPanel.add(ID);
				myPanel.add(Box.createHorizontalStrut(50)); // a spacer
				myPanel.add(new JLabel("Password:"));
				myPanel.add(Password);

				int result = JOptionPane.showConfirmDialog(null, myPanel,"Register", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (result == JOptionPane.OK_OPTION) 
				{
					String UserName = ID.getText();
					String PassWord = Password.getText();
					String Score = "0";
	
					try {
						BufferedWriter wr = new BufferedWriter(new FileWriter("Scoreboard.txt", true));
						wr.append(UserName);
						wr.newLine();
						wr.append(PassWord);
						wr.newLine();
						wr.append(Score);
						wr.newLine();
						wr.close();
					}		      
					catch (IOException e) {
						System.out.println("No Such a File");
					}
				    
				}
			}	
		});
	
		play.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent b)
			{
				JTextField ID = new JTextField(5);
				JTextField Password = new JTextField(5);

				JPanel myPanel = new JPanel();
				myPanel.add(new JLabel("Name:"));
				myPanel.add(ID);
				myPanel.add(Box.createHorizontalStrut(50)); // a spacer
				myPanel.add(new JLabel("Password:"));
				myPanel.add(Password);
				    
				int result = JOptionPane.showConfirmDialog(null, myPanel,"Play", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
				    
				if (result == JOptionPane.OK_OPTION) 
				{
					UserName = ID.getText();
					PassWord = Password.getText();		
					String line;
					boolean nameFlag = false;
					boolean passwordFlag = false;
					boolean Authentication = false;
					int counter = 0; 
										
					try {
						BufferedReader reader = new BufferedReader(new FileReader("Scoreboard.txt"));
							
						while((line = reader.readLine()) != null) {
							counter ++;
										
							if ((counter == 1) && (line.equals(UserName)))
								nameFlag = true;
				                	
				            else if ((counter == 2) && (line.equals(PassWord)))
				                passwordFlag = true;
				                
				            else if (counter == 3){
								counter = 0;	
								nameFlag = false;
								passwordFlag = false;
							}
				                
				            if ((nameFlag == true) && (passwordFlag == true)){
				                JOptionPane.showMessageDialog(null, "Authentication Succesfull","WELCOME",JOptionPane.PLAIN_MESSAGE);

				                JTextArea textArea = new JTextArea();      
				                textArea.setText("Yes (Easy)\nNo (Hard)");
	
				          		Object[] options = {"Easy","Hard"};
				                difficulty  = JOptionPane.showOptionDialog(loginFrame, "Please select the difficulty level to enter","Difficulty Selection",JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
				                if (difficulty == 0)
				                	requiredEnemySpawn = 3;	                
				                else
				                    requiredEnemySpawn = 4;
				                PlayerScore = reader.readLine();
				                GameFrame gameFrame = new GameFrame();
				                Authentication = true;
								loginFrame.setVisible(false);
				            }	
						}
						if (!Authentication)
							JOptionPane.showMessageDialog(null, "Authentication Failed.Please register first","Warning",JOptionPane.PLAIN_MESSAGE);
						reader.close();
					}				        
					catch (IOException e){ 
						System.out.println("No Such a File");
					}
				}	     
			}	
		});
	
		scoreboard.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent c)
			{				
				String line, str="";
				int counter=1;
				try {
					BufferedReader rd = new BufferedReader(new FileReader("Scoreboard.txt"));
						
					while((line = rd.readLine()) != null) {
						counter ++;
						if (counter % 3 != 0)
			                str+= line+"\n"; 
					}
					rd.close();
					JOptionPane.showMessageDialog(null, str,"Scoreboard",JOptionPane.PLAIN_MESSAGE);	
				}
				catch (IOException e) {
					System.out.println("No Such a File");
				}
			}	
		});
	
		exit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent d){
				System.exit(0);
			}	
		});
	
		credits.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e){
				JOptionPane.showMessageDialog(null, "Dev Name = Oðuzhan Kaçamak","Credits",JOptionPane.PLAIN_MESSAGE);
			}	
		});

	}
		@Override
		public void actionPerformed(ActionEvent e) 
		{}
}