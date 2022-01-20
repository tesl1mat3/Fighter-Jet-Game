import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Rectangle;
import java.io.IOException;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class Missile implements Runnable {
	
	int xCordinate, yCordinate;

	Icon missileImage;
	JLabel missileLabel;
	Unit firingUnit;
	AudioClip hurtSound;

	boolean missileHit=false;
	
	private static Thread thread;
	
	public Missile(int xCoordinate, int yCoordinate, Unit shooter){
			
		URL hurt = GameFrame.class.getResource("PlayerHurtSound.wav");
		hurtSound = Applet.newAudioClip(hurt);	
		
		this.firingUnit = shooter;
		
		if (shooter instanceof EnemyJet)
		{
			this.missileImage = new ImageIcon(getClass().getResource("EnemyMissile.png"));
			this.missileLabel = new JLabel(missileImage);
			this.xCordinate = xCoordinate + 28;
			this.yCordinate = yCoordinate + this.firingUnit.unitImage.getIconHeight() + 10;
		}
		else if (shooter instanceof EnemyTank)
		{
			this.missileImage = new ImageIcon(getClass().getResource("EnemyMissile.png"));
			this.missileLabel = new JLabel(missileImage);
			this.xCordinate = xCoordinate + 15;
			this.yCordinate = yCoordinate + this.firingUnit.unitImage.getIconHeight() + 6;
		}
		else if (shooter instanceof PlayerJet)
		{
			if (GameFrame.laserRemains == false){
				this.missileImage = new ImageIcon(getClass().getResource("PlayerMissile.png"));
				this.missileLabel = new JLabel(missileImage);
				this.xCordinate = xCoordinate +18;
				this.yCordinate = yCoordinate - 30;	
			}
			else if (GameFrame.laserRemains == true){
				this.missileImage = new ImageIcon(getClass().getResource("Laser.png"));
				this.missileLabel = new JLabel(missileImage);
				this.xCordinate = 0;
				this.yCordinate = yCoordinate - 30;	
			}
			
		}
		GameFrame.background.add(missileLabel);
		GameFrame.missileArray.add(this);
		
		thread=new Thread(this);
		thread.start();
	}
	
	public int getyCordinate(){
		return this.yCordinate;
	}
	
	public int getxCordinate(){
		return this.xCordinate;
	}
	
	@Override
	public void run()
	{
		while (GameFrame.gameIsRunning && !this.missileHit)
		{
			try{
				Thread.sleep(50);
				move();
			}
			catch(InterruptedException exception)
			{} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void move() throws InterruptedException, IOException 
	{ 	
		if (this.firingUnit instanceof PlayerJet)
		{
			if (this.yCordinate < 0)
			{
				this.missileLabel.setIcon(null);
				this.missileHit = true;
				GameFrame.missileArray.remove(this);
				
			}
			else{
				this.yCordinate -=  10;
				checkForEnemyCollision();
			}
		}
		else
		{	
			if (this.yCordinate > 800)
			{
				this.missileLabel.setIcon(null);
				this.missileHit = true;
				GameFrame.missileArray.remove(this);
				
			}
			else{
				this.yCordinate += 10;
				checkForPlayerCollision();
			}
		}
		this.missileLabel.setBounds(this.xCordinate, this.yCordinate, missileImage.getIconWidth(), missileImage.getIconHeight());
	}
	
	private void checkForPlayerCollision() throws InterruptedException, IOException	
	{
		PlayerJet player = GameFrame.player;
		
		for(int i = 0; i < GameFrame.missileArray.size(); i++)
		{
			Missile missile = GameFrame.missileArray.get(i);	
			Rectangle missileRectangle = new Rectangle(missile.xCordinate, missile.yCordinate, missile.missileImage.getIconWidth(), missile.missileImage.getIconHeight());
			Rectangle playerRectangle = new Rectangle(player.xCordinate, player.yCordinate, player.unitImage.getIconWidth(), player.unitImage.getIconHeight());
				
			if (missileRectangle.intersects(playerRectangle) && !GameFrame.player.getisImmune())
			{
				this.missileHit = true;
				GameFrame.missileArray.remove(i);
				this.missileLabel.setIcon(null);
				GameFrame.player.healthPoint -= 25;				
				
				if (GameFrame.player.healthPoint == 25)
					GameFrame.healthbarlabel.setIcon(GameFrame.healthBarHalf);
					
				if (GameFrame.player.healthPoint <= 0)			
					GameFrame.updateLivesLeft();
				
				hurtSound.play();	
				Thread.sleep(500);
				hurtSound.stop();	
				break;	
			}
		}
	}
	
	private void checkForEnemyCollision() throws InterruptedException 
	{		
		Rectangle missileRectangle = new Rectangle(this.xCordinate, this.yCordinate, this.missileImage.getIconWidth(), this.missileImage.getIconHeight());
		for(int i = 0; i < GameFrame.enemiesArray.size(); i++)
		{
			Unit enemy = GameFrame.enemiesArray.get(i);
			Rectangle enemyRectangle = new Rectangle(enemy.xCordinate, enemy.yCordinate,enemy.unitImage.getIconWidth(), enemy.unitImage.getIconWidth());
			
			if (missileRectangle.intersects(enemyRectangle))
			{
				enemy.setIsAlive(false);
				GameFrame.enemiesArray.remove(enemy);
				GameFrame.screenEnemyCount--;
				
				if (!GameFrame.laserRemains){
					this.missileHit = true;	
					this.missileLabel.setIcon(null);
					GameFrame.missileArray.remove(this);
				}
				GameFrame.explosion(enemy);						
				GameFrame.playerScore += 100;
				GameFrame.scoreLabel.setText("Score : "+GameFrame.playerScore);
				break;
			}	
		}
	}
}