import javax.swing.Icon;
import javax.swing.JLabel;

public abstract class Unit implements Runnable
{ 
	Icon unitImage;
	JLabel unitLabel;
	
	int xCordinate, yCordinate, healthPoint;

	private boolean isAlive, isImmune;

	public void shoot(){
		Missile missile = new Missile(this.xCordinate, this.yCordinate, this);
	}
	
	public boolean getIsAlive() {
		return this.isAlive;
	}
	
	public void setIsAlive(boolean alive) {
		this.isAlive = alive;
	}
	
	public boolean getisImmune() {
		return this.isImmune;
	}
	
	public void setisImmune(boolean immune) {
		this.isImmune = immune;
	}
}