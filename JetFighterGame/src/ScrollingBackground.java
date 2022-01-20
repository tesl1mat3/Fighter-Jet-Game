import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ScrollingBackground extends JPanel{

	private Image icon;
	int xStart=0, yStart=-1200;
	
	public ScrollingBackground(LayoutManager l)
	{
		super.setLayout(l);
	}
	
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		icon = new ImageIcon(getClass().getResource("Background.jpg")).getImage();

		g.drawImage(icon, xStart, yStart, icon.getWidth(null), icon.getHeight(null), null);
	}
	
	public void scroll()
	{
		if(yStart == 0){
			yStart=-1200;		
		}
		yStart += 1;
		repaint();
	}
}