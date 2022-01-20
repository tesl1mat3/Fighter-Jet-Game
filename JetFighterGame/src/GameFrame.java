import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.JFrame;
import javax.swing.Icon;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;


public class GameFrame implements KeyListener,Runnable{

	static Icon explosion0 = new ImageIcon(GameFrame.class.getResource("Explosion0.png"));
	static Icon explosion1 = new ImageIcon(GameFrame.class.getResource("Explosion1.png"));
	static Icon explosion2 = new ImageIcon(GameFrame.class.getResource("Explosion2.png"));
	static Icon explosion3 = new ImageIcon(GameFrame.class.getResource("Explosion3.png"));
	static Icon explosion4 = new ImageIcon(GameFrame.class.getResource("Explosion4.png"));
	static Icon explosion5 = new ImageIcon(GameFrame.class.getResource("Explosion5.png"));
	static Icon explosion6 = new ImageIcon(GameFrame.class.getResource("Explosion6.png"));
	static Icon explosion7 = new ImageIcon(GameFrame.class.getResource("Explosion7.png"));
	static Icon explosion8 = new ImageIcon(GameFrame.class.getResource("Explosion8.png"));
	static Icon explosion9 = new ImageIcon(GameFrame.class.getResource("Explosion9.png"));
	static Icon healthBarHalf = new ImageIcon(GameFrame.class.getResource("HealthBarHalf.png")); 
	static Icon playerJet=new ImageIcon(GameFrame.class.getResource("PlayerStable.png"));
	
	private Icon shadowImage = new ImageIcon(getClass().getResource("JetShadow.png"));
	private Icon livesLeftImage = new ImageIcon(getClass().getResource("LivesLeft.png"));
	private static Icon healthBarFull = new ImageIcon(GameFrame.class.getResource("HealthBarFull.png"));
	
	public static JLabel scoreLabel, healthbarlabel, livesLabel1, livesLabel2, livesLabel3, laserCooldownLabel, startCountdownLabel, playerShadowLabel;
	
	private int enemySpawnChance, enemySpawnxCordinate;
	
	static ArrayList <Missile> missileArray = new ArrayList<Missile>();
	static ArrayList <Unit> enemiesArray = new ArrayList<Unit>();
	
	static ScrollingBackground background;
	static PlayerJet player;
	
	static int screenEnemyCount=0, playerScore=0, livesLeft=3, laserCooldown=30, seconds=0;
	
	static boolean gameIsRunning=true, laserRemains=false,  laserReady=true;
	private boolean leftFlag, rightFlag, upFlag, downFlag, gameStart=true;
	
	private Thread thread;
	private Timer timer;
	

	public GameFrame(){
		
		background = new ScrollingBackground(null);	
			
		playerShadowLabel = new JLabel(shadowImage);
		background.add(playerShadowLabel);
		playerShadowLabel.setBounds(400, 650, shadowImage.getIconWidth(), shadowImage.getIconHeight());

		scoreLabel = new JLabel("Score = 0");
		background.add(scoreLabel);
		scoreLabel.setBounds(580, 20, 200, 30);
		scoreLabel.setFont(new Font("Helvetica", Font.PLAIN, 30));
		scoreLabel.setForeground(Color.WHITE);
		
		laserCooldownLabel = new JLabel("Laser Cooldown = 0");
		background.add(laserCooldownLabel);
		laserCooldownLabel.setBounds(550, 725, 200, 20);
		laserCooldownLabel.setFont(new Font("Helvetica", Font.PLAIN, 20));
		laserCooldownLabel.setForeground(Color.WHITE);
		
		startCountdownLabel = new JLabel("READY!!");
		background.add(startCountdownLabel);
		startCountdownLabel.setBounds(200, 300, 400, 100);
		startCountdownLabel.setFont(new Font("Helvetica", Font.PLAIN, 80));
		startCountdownLabel.setForeground(Color.BLACK);
		
		healthbarlabel = new JLabel(healthBarFull);
		background.add(healthbarlabel);
		healthbarlabel.setBounds(0, 710, healthBarFull.getIconWidth(), healthBarFull.getIconHeight());
			
		livesLabel1 = new JLabel(livesLeftImage);
		background.add(livesLabel1);
		livesLabel1.setBounds(4, 10, livesLeftImage.getIconWidth(), livesLeftImage.getIconHeight());

		livesLabel2 = new JLabel(livesLeftImage);
		background.add(livesLabel2);
		livesLabel2.setBounds(58, 10, livesLeftImage.getIconWidth(), livesLeftImage.getIconHeight());
		
		livesLabel3 = new JLabel(livesLeftImage);
		background.add(livesLabel3);
		livesLabel3.setBounds(112, 10, livesLeftImage.getIconWidth(), livesLeftImage.getIconHeight());
	
		JFrame gameScene = new JFrame("Jet Fighter Game");
		
		gameScene.add(background);
		gameScene.setVisible(true);
		gameScene.setSize(760, 800);
		gameScene.setResizable(false);
		gameScene.addKeyListener(this);
		gameScene.setLocationRelativeTo(null);
		gameScene.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		player = new PlayerJet();
		
		thread = new Thread(this);
		thread.start();	
		
		timer = new Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				seconds++;
				laserCooldown--;
				laserCooldownLabel.setText("Laser Cooldown = "+laserCooldown);
				
				if(seconds % 5 == 0)
					laserRemains = false;
				
				if(seconds == 30){
					timer.stop();
					laserReady = true;
					laserCooldown = 30;
					seconds = 0;	
				}	
			}
		});
	}
	
	public void playerJetMovement() {		
		if (leftFlag && !player.getisImmune()){
			if (player.xCordinate < 9)
				player.xCordinate = 3;
			player.xCordinate -= 8;
			player.unitLabel.setIcon(player.planeLeft);
		}
		if (rightFlag && !player.getisImmune()){
			if (player.xCordinate > 696)
				player.xCordinate = 702;
			player.xCordinate += 8;
			player.unitLabel.setIcon(player.planeRight);
		}
		if (upFlag && !player.getisImmune()){
			if (player.yCordinate < 78)
				player.yCordinate = 70;
			player.yCordinate -= 8;
			player.unitLabel.setIcon(player.planeForward);
		}
		if (downFlag && !player.getisImmune()){
			if (player.yCordinate > 640)
				player.yCordinate = 646;
			player.yCordinate += 8;
			player.unitLabel.setIcon(player.planeStable);
		}	
		playerShadowLabel.setBounds(player.xCordinate+50, player.yCordinate+50, shadowImage.getIconWidth(), shadowImage.getIconHeight());
		player.unitLabel.setBounds(player.xCordinate, player.yCordinate, player.planeLeft.getIconWidth(), player.planeLeft.getIconHeight());
	}
	
	public void keyPressed(KeyEvent event) {
		int key = event.getKeyCode();	
		if(key == KeyEvent.VK_LEFT && !gameStart)
			leftFlag = true;
		if(key == KeyEvent.VK_RIGHT && !gameStart)
			rightFlag = true;
		if(key == KeyEvent.VK_UP && !gameStart)
			upFlag = true;	
		if(key == KeyEvent.VK_DOWN && !gameStart)
			downFlag = true;		
		playerJetMovement();
	}

	public void keyReleased(KeyEvent event) {
		int key = event.getKeyCode();		
		if(key == KeyEvent.VK_LEFT)
			leftFlag = false;
		if(key == KeyEvent.VK_RIGHT)
			rightFlag = false;
		if(key == KeyEvent.VK_UP)
			upFlag = false;
		if(key == KeyEvent.VK_DOWN)
			downFlag = false;
		if((key == KeyEvent.VK_SPACE && !player.getisImmune()) && !laserRemains)// locks missile fire when laser used for 5 sec.
			player.shoot();
		if((key == KeyEvent.VK_CONTROL && !player.getisImmune())&& laserReady){
			laserRemains=true;
			player.shoot();
			timer.start();
			laserReady = false;
		}
		player.unitLabel.setIcon(player.planeStable);
	}

	public void keyTyped(KeyEvent event) 
	{}

	@Override
	public void run() {			 
			while (gameIsRunning)
			{			
				playerJetMovement();
				try {
					if(gameStart)
						startCountDown();
				} 
				catch (InterruptedException e1) {
						e1.printStackTrace();
				}			
				gameStart=false;
				enemySpawnxCordinate = (int) (Math.random() * 500);
				enemySpawnChance = (int) (Math.random() * 1000);
				try {		
					//difficulty Easy=0, Hard=1
					if (GUIFrame.difficulty == 0){
						background.scroll();
						Thread.sleep(30);			
						if(screenEnemyCount < GUIFrame.requiredEnemySpawn){ //requiredEnemySpawn=3
							if (enemySpawnChance < 8){
								EnemyJet enemyJet = new EnemyJet(enemySpawnxCordinate, 0);
								enemiesArray.add(enemyJet);
								screenEnemyCount++;
							}
						}		
					}
					else{
						if(screenEnemyCount < GUIFrame.requiredEnemySpawn){ //requiredEnemySpawn=4
							if (enemySpawnChance < 8){
								EnemyJet enemyJet = new EnemyJet(enemySpawnxCordinate, 0);
								enemiesArray.add(enemyJet);
								screenEnemyCount++;
							}
							else if (enemySpawnChance < 10){
								EnemyTank enemyTank = new EnemyTank(enemySpawnxCordinate, 0);
								enemiesArray.add(enemyTank);
								screenEnemyCount++;
							}
						}
						else{
							background.scroll();
							Thread.sleep(30);
							if (enemySpawnChance < 8){
								EnemyJet enemyJet = new EnemyJet(enemySpawnxCordinate, 0);
								enemiesArray.add(enemyJet);
								screenEnemyCount++;
							}
							else if (enemySpawnChance < 10){
								EnemyTank enemyTank = new EnemyTank(enemySpawnxCordinate, 0);
								enemiesArray.add(enemyTank);
								screenEnemyCount++;
							}
						}
					}
 				}
				catch(InterruptedException e)
				{}
		}
	}
	
	public static void updateLivesLeft() throws InterruptedException, IOException{
		livesLeft--;
		healthbarlabel.setIcon(healthBarFull);
		if(livesLeft==2)
			livesLabel3.setIcon(null);
		else if(livesLeft==1)
			livesLabel2.setIcon(null);
		else if(livesLeft==0)
			livesLabel1.setIcon(null);
		else if(livesLeft < 0){
			gameIsRunning = false;
			JOptionPane.showMessageDialog(null, "GAME IS OVER!!\nYour Score is : "+playerScore,"Credits",JOptionPane.PLAIN_MESSAGE);	
			writeScore();
			System.exit(0);	
		}
		immune();
	}
	
	public static void explosion(Unit enemy) throws InterruptedException{
		AudioClip explosionSound;
		URL explosion = GameFrame.class.getResource("explosionSound.wav");
		explosionSound = Applet.newAudioClip(explosion);	
		explosionSound.play();	
		enemy.unitLabel.setIcon(GameFrame.explosion0);
		Thread.sleep(50);
		enemy.unitLabel.setIcon(GameFrame.explosion1);
		Thread.sleep(50);
		enemy.unitLabel.setIcon(GameFrame.explosion2);
		Thread.sleep(50);
		enemy.unitLabel.setIcon(GameFrame.explosion3);
		Thread.sleep(50);
		enemy.unitLabel.setIcon(GameFrame.explosion4);
		Thread.sleep(50);
		enemy.unitLabel.setIcon(GameFrame.explosion5);
		Thread.sleep(50);
		enemy.unitLabel.setIcon(GameFrame.explosion6);
		Thread.sleep(50);
		enemy.unitLabel.setIcon(GameFrame.explosion7);
		Thread.sleep(50);
		enemy.unitLabel.setIcon(GameFrame.explosion8);
		Thread.sleep(50);
		enemy.unitLabel.setIcon(GameFrame.explosion9);
		Thread.sleep(50);
		explosionSound.stop();	
		enemy.unitLabel.setIcon(null);
	}
	
	public static void immune() throws InterruptedException{	
		Icon playerJet;		
		playerJet=new ImageIcon(GameFrame.class.getResource("PlayerStable.png"));
		player.unitLabel.setIcon(null);
		player.xCordinate = 350;
		player.yCordinate = 600;
		player.unitLabel.setBounds(player.xCordinate, player.yCordinate, playerJet.getIconWidth(), playerJet.getIconHeight());
		player.setisImmune(true);
		Thread.sleep(300);
		player.unitLabel.setIcon(playerJet);
		Thread.sleep(300);
		player.unitLabel.setIcon(null);
		Thread.sleep(300);
		player.unitLabel.setIcon(playerJet);	
		Thread.sleep(300);
		player.unitLabel.setIcon(null);
		Thread.sleep(300);
		player.unitLabel.setIcon(playerJet);	
		Thread.sleep(300);
		player.unitLabel.setIcon(playerJet);	
		player.healthPoint = 50;
		player.setisImmune(false);	
	}
		
	public void startCountDown() throws InterruptedException{	
		Thread.sleep(1000);
		startCountdownLabel.setBounds(350, 300, 400, 100);
		startCountdownLabel.setText("3");
		Thread.sleep(1000);
		startCountdownLabel.setText("2");
		Thread.sleep(1000);
		startCountdownLabel.setText("1");
		Thread.sleep(1000);
		startCountdownLabel.setBounds(300, 300, 400, 100);
		startCountdownLabel.setText("GO!!");
		Thread.sleep(1000);
		startCountdownLabel.setText(null);
	}
	
	public static void writeScore() throws IOException{
		Path path = Paths.get("Scoreboard.txt");
		Charset charset = StandardCharsets.UTF_8;

		String content = new String(Files.readAllBytes(path), charset);
		String willChange,willReplace;
		willChange = GUIFrame.UserName+"\r\n"+GUIFrame.PassWord+"\r\n"+GUIFrame.PlayerScore;
		willReplace = GUIFrame.UserName+"\r\n"+GUIFrame.PassWord+"\r\n"+playerScore;
		content = content.replace(willChange,willReplace );
		Files.write(path, content.getBytes(charset));
	}
}