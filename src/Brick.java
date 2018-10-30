import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;

// Brick class to store and set brick coordinates
class Brick extends Sprite
{
    Image brick_texture = null;

    Brick(int x_pos, int y_pos, int width, int height, Model m)
    {
        super(m);
        x = x_pos;
        y = y_pos;
        w = width;
        h = height;
    }

    Brick(Json ob, Model m)
    {
        super(m);
        // Initialize member variables from Json objects
        x = (int)ob.getLong("x");
        y = (int)ob.getLong("y");
        w = (int)ob.getLong("w");
        h = (int)ob.getLong("h");
    }

	// Brick Copy Constructor
	Brick(Brick copyme, Model newModel)
	{
		super(copyme, newModel);
	}

	Brick makeDeepCopy(Model newModel)
	{
		return new Brick(this, newModel);
	}


	Json marshal()
    {
        // Convert bricks to Json
        Json ob = Json.newObject();
        ob.add("Type", "Brick");
        ob.add("x",x);
        ob.add("y",y);
        ob.add("w",w);
        ob.add("h",h);
        return ob;
    }

    boolean isBrick()
    {
        return true;
    }

    void update()
    {
    }

    void draw(Graphics g)
    {
        // Lazy loading for brick texture
        if(brick_texture == null)
        {
            try
            {
                brick_texture = ImageIO.read(new File("brick.png"));
            }
            catch (Exception e)
            {
                System.out.println("Failed to read brick image.");
                System.exit(0);
            }
        }
        g.drawImage(brick_texture,x-model.scrollX,y,w,h,null);
    }
}
