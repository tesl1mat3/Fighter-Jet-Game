import java.awt.Rectangle;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class EnemyJet extends Unit implements Runnable {
	
	private static Thread thread;

	public EnemyJet(int x, int y){
		 
		this.xCordinate = x;
		this.yCordinate = y;
		this.unitImage = new ImageIcon(getClass().getResource("EnemyJet.png"));
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
				int fireChance = (int) (Math.random() * 50);		
				if (fireChance == 0)
					shoot();	
			}
			catch(InterruptedException exception)
			{} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void move() throws InterruptedException, IOException {
		if (this.yCordinate > 800)
		{
			this.setIsAlive(false);
			this.unitLabel.setIcon(null);
			GameFrame.enemiesArray.remove(this);
			GameFrame.screenEnemyCount--;
		}
		else
		{
			this.yCordinate += 4;
			
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
						if(playerMissileEnemyDifferenceX > -20 && playerMissileEnemyDifferenceX < 40){
							if (xCordinate > 695)
								xCordinate = 700;
							xCordinate += 5;
						}
						if(playerMissileEnemyDifferenceX < -20 && playerMissileEnemyDifferenceX > -40){		
							if (xCordinate < 9)
								xCordinate = 1;
							xCordinate -= 10;
						}					
					}
				}
			}
			if (playerxEnemyxDifference > 100) 
			{
				if (playerxCordinate < xCordinate) 
						xCordinate -= 1;				
				else 
						xCordinate += 1;
			}
			this.unitLabel.setBounds(xCordinate, yCordinate, this.unitImage.getIconWidth(), this.unitImage.getIconHeight());
			checkForBodyCollisionWithPlayer();	
		}
	}

	private void checkForBodyCollisionWithPlayer() throws InterruptedException, IOException 
	{
		PlayerJet player = GameFrame.player;
		Rectangle playerRectangle = new Rectangle(player.xCordinate,player.yCordinate,player.unitImage.getIconWidth(),player.unitImage.getIconHeight());	
		Rectangle enemyRectangle = new Rectangle(this.xCordinate,this.yCordinate,this.unitImage.getIconWidth(),this.unitImage.getIconHeight());

		if (enemyRectangle.intersects(playerRectangle) && !GameFrame.player.getisImmune())
		{
			GameFrame.player.healthPoint -= 25;
			GameFrame.enemiesArray.remove(this);	
			GameFrame.screenEnemyCount--;
			
			this.setIsAlive(false);
			GameFrame.explosion(this);
			
			if (GameFrame.player.healthPoint == 25)
				GameFrame.healthbarlabel.setIcon(GameFrame.healthBarHalf);	
			
			if (GameFrame.player.healthPoint <= 0)	
				GameFrame.updateLivesLeft();	
		}
	}
}