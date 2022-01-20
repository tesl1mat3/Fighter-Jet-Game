import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class PlayerJet extends Unit{

	Icon planeStable, planeForward, planeRight, planeLeft;
	
	public PlayerJet()	{
	
		planeStable=new ImageIcon(getClass().getResource("PlayerStable.png"));
		planeForward=new ImageIcon(getClass().getResource("PlayerForward.png"));
		planeRight=new ImageIcon(getClass().getResource("PlayerRight.png"));
		planeLeft=new ImageIcon(getClass().getResource("PlayerLeft.png"));
		
		this.unitImage = planeStable;		
		this.xCordinate = 350;
		this.yCordinate = 600;
		this.healthPoint = 50;
		this.unitLabel = new JLabel(planeStable);

		unitLabel.setBounds(350, 600, planeStable.getIconWidth(), planeStable.getIconHeight());
		GameFrame.background.add(this.unitLabel);
	}

	@Override
	public void run() {
	}
}