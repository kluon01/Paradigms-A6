import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;

public class Coin extends Sprite
{
    // Member varaibles of Coin class
    double v_vel;
    double h_vel;
    Image coin_img = null;

    // Coin constructor when giveCoin is called
    Coin(int b_x, int b_y, int b_width, double vert_vel, double horiz_vel, Model m)
    {
        super(m);
        try
        {
            coin_img = ImageIO.read(new File("coin.png"));
        }
        catch (Exception e)
        {
            System.out.println("Failed to read coin image.");
            System.exit(0);
        }
        x = b_x + (b_width / 5);
        y = b_y;
        w = 70;
        h = 70;
        v_vel = vert_vel;
        h_vel = horiz_vel;
    }

	// Coin Copy Constructor
	Coin(Coin copyme, Model newModel)
	{
		super(copyme, newModel);
		v_vel = copyme.v_vel;
		h_vel = copyme.h_vel;
	}

	Coin makeDeepCopy(Model newModel)
	{
		return new Coin(this, newModel);
	}

    // Coin update
    void update()
    {
        y += v_vel;
        v_vel += 1.8;
        x += h_vel;

        // After coin falls off screen, remove coin from arraylist
        if(y > 1600)
        {
            model.sprites.remove(this);
        }
    }

    void draw(Graphics g)
    {
        g.drawImage(coin_img,x - model.scrollX,y,w,h,null);
    }

    Json marshal()
    {
        return null;
    }
}
