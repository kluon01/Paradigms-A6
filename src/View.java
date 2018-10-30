import javax.swing.JPanel;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

class View extends JPanel
{
	Model model; // Model member variable
	BufferedImage background = null; // Background and brick sprites
	// View constructor to initialize model
	View(Controller c, Model m)
	{
	    model = m;
	    // Load background image
		try
		{
			background = ImageIO.read(new File("background.png"));
		}
		catch (Exception e)
		{
            System.out.println("Failed to read background image.");
            System.exit(0);
		}
	}
	// Draw what user sees
	public void paintComponent(Graphics g)
	{
		// Set backgroundcolor to light blue
		g.setColor(new Color(80, 182, 255));
		g.fillRect(0,0,this.getWidth(),this.getHeight());
		// Draw background, scrolls at fraction of mario's speed
		g.drawImage(background,-150 - (model.scrollX/4),0,3000,this.getHeight(),null);
		// General draw loop to draw sprites
		for(int i = 0; i < model.sprites.size(); i++)
		{
			Sprite s = model.sprites.get(i);
			s.draw(g);
		}
	}
}
