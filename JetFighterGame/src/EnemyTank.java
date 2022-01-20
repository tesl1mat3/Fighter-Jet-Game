import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class EnemyTank extends Unit implements Runnable {
	
	private static Thread thread;
	
	public EnemyTank(int x, int y){
		
		this.xCordinate = x;
		this.yCordinate = y;	
		this.unitImage = new ImageIcon(getClass().getResource("EnemyTank.png"));
		this.unitLabel = new JLabel(unitImage);
		this.unitLabel.setBounds(xCordinate, yCordinate, unitImage.getIconWidth(), unitImage.getIconHeight());	
		this.setIsAlive(true);
		
		GameFrame.background.add(this.unitLabel);
		
		thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run() {
		while (GameFrame.gameIsRunning && this.getIsAlive())
		{
			try{
				Thread.sleep(50);
				move();
				int fireChance = (int) (Math.random() * 30);
				if (fireChance == 0)
					shoot();	
			}
			catch(InterruptedException exception)
			{}
		}
	}
	
	private void move() throws InterruptedException {
		if (this.yCordinate > 800)
		{
			this.setIsAlive(false);
			this.unitLabel.setIcon(null);
			GameFrame.enemiesArray.remove(this);
			GameFrame.screenEnemyCount--;
		}
		else
		{
			this.yCordinate += 3;
			
			int playerxCordinate = GameFrame.player.xCordinate;
			int playerxEnemyxDifference = Math.abs(playerxCordinate - xCordinate);  
						
			for(int i = 0; i < GameFrame.missileArray.size(); i++)
			{
				Missile missile = GameFrame.missileArray.get(i);
				
				if(missile.firingUnit == GameFrame.player)
				{		
					int playerMissileEnemyDifferenceY = missile.getyCordinate()- this.yCordinate;
					int playerMissileEnemyDifferenceX = this.xCordinate - missile.getxCordinate();
					
					if(playerMissileEnemyDifferenceY < 200)
					{
						if(playerMissileEnemyDifferenceX > 0 && playerMissileEnemyDifferenceX < 40){
							if (xCordinate > 695)
								xCordinate = 700;
							xCordinate += 5;
						}
						if(playerMissileEnemyDifferenceX < 0 && playerMissileEnemyDifferenceX > -40){		
							if (xCordinate < 9)
								xCordinate = 1;
							xCordinate -= 5;
						}					
					}
				}
			}
		//	To activate tank follow up to playerjet
		//	 -----------------------------------------------
		//	if (playerxEnemyxDifference > 100) 
		//	{
		//		if (playerxCordinate < xCordinate) 
		//				xCordinate -= 1;				
		//		else 
		//				xCordinate += 1;
			
			this.unitLabel.setBounds(xCordinate, yCordinate, this.unitImage.getIconWidth(), this.unitImage.getIconHeight());
		}
	}
}