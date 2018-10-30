import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CoinBlock extends Sprite
{
    // Coin Block member variables
    Image[] coinblock_textures = null;
    //static Random rand = new Random();
    int t = 1; // Indicates what type of coin block to draw, 1 for active or 2 for spent
	int coin_count;

    CoinBlock(int x_pos, int y_pos, int width, int height, int type, Model m)
    {
        super(m);
        x = x_pos;
        y = y_pos;
        w = width;
        h = height;
        t = type;
        coin_count = 0;
    }

	// Coin Block Copy Constructor
	CoinBlock(CoinBlock copyme, Model newModel)
	{
		super(copyme, newModel);
		t = copyme.t;
		coin_count = copyme.coin_count;
	}

	CoinBlock makeDeepCopy(Model newModel)
	{
		return new CoinBlock(this, newModel);
	}

	// Method to give mario a coin from a coin block
	void giveCoin()
	{
		double h_vel = ThreadLocalRandom.current().nextDouble(-5,5);
		double v_vel = -23.8;

		Coin c = new Coin(x,y,w,v_vel,h_vel,model);
		model.sprites.add(c);
		coin_count++;
		if(coin_count > 4)
		{
			drawSpent();
		}
	}

	// Method to draw a spent coin block after 5 coins
	void drawSpent()
	{
		CoinBlock b = new CoinBlock(x,y,w,h,2,model);
		model.sprites.add(b);
	}

    CoinBlock(Json ob, Model m)
    {
        super(m);
        // Initialize member variables from Json objects
        x = (int)ob.getLong("x");
        y = (int)ob.getLong("y");
        w = (int)ob.getLong("w");
        h = (int)ob.getLong("h");
        t = (int)ob.getLong("type");
    }

    Json marshal()
    {
        // Convert bricks to Json
        Json ob = Json.newObject();
        ob.add("Type", "CoinBlock");
        ob.add("x",x);
        ob.add("y",y);
        ob.add("w",w);
        ob.add("h",h);
        ob.add("type", t);
        return ob;
    }

    void update()
    {
        Mario m = (Mario)model.sprites.get(0); // Casts the first sprite to be mario.
        // Loop through arraylist of sprites and compare coin blocks to mario
        for(int i = 0; i < model.sprites.size(); i++)
        {
            if(doesCollideWith(m,this))
            {
                collisionHandle(m,this);
            }
        }
    }

    void draw(Graphics g)
    {
        // Lazy loading for coin block textures
        if(coinblock_textures == null)
        {
            coinblock_textures = new Image[2];
            try
            {
                coinblock_textures[0] = ImageIO.read(new File("coinblockactive.png"));
                coinblock_textures[1] = ImageIO.read(new File("coinblockspent.png"));
            }
            catch (Exception e)
            {
                System.out.println("Failed to read coin block image.");
                System.exit(0);
            }
        }
        // Draw the appropriate coinblock
        if(t == 1)
        {
            g.drawImage(coinblock_textures[0],x-model.scrollX,y,w,h,null);
        }
        else
        {
            g.drawImage(coinblock_textures[1],x-model.scrollX,y,w,h,null);
        }
    }

    boolean isCoinBlock()
    {
        return true;
    }
}
