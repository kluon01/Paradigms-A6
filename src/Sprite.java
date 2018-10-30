import java.awt.Graphics;

public abstract class Sprite
{
    // Member variables shared between all sprites
    Model model;
    int x;
    int y;
    int w;
    int h;

    //int coin_count = 0; // Special coin counter variable

    Sprite(Model m)
    {
        model = m;
    }

    Sprite(Sprite copyme, Model newModel)
	{
		x = copyme.x;
		y = copyme.y;
		w = copyme.w;
		h = copyme.h;
		model = newModel;
	}

	abstract Sprite makeDeepCopy(Model newModel);

    // Abstract classes that all/most sprites will need
    abstract void update();

    abstract void draw(Graphics g);

    abstract Json marshal();

    // Override methods
    boolean isBrick() { return false; }

    boolean isCoinBlock() { return false; }

	boolean isMario() { return false; }

    // General collision detection method to check collision between 2 sprites
    boolean doesCollideWith(Sprite a, Sprite b)
    {
        if(a.x + a.w <= b.x)
            return false;
        else if(a.x >= b.x + b.w)
            return false;
        else if(a.y + a.h <= b.y)
            return false;
        else if(a.y >= b.y + b.h)
            return false;
        else
            return true;
    }

    // Collision handling method between mario and another sprite
    void collisionHandle(Mario m, Sprite b)
    {
        if (m.x + m.w >= b.x && m.prev_x + m.w < b.x)
        {
            m.x = b.x - m.w - 1;
        } else if (m.x <= b.x + b.w && m.prev_x > b.x + b.w)
        {
            m.x = b.x + b.w + 1;
        } else if (m.y + m.h >= b.y && m.prev_y < b.y)
        {
            m.y = b.y - m.h - 1;
            m.vert_vel = 0.0;
            m.frames_not_on_ground = 0;
        }
        else if (y <= b.y + b.h && m.prev_y > b.y + b.h)
        {
            if (b.isCoinBlock())
            {
            	CoinBlock c = (CoinBlock)b;
                m.y = b.y + b.h + 1;
                m.vert_vel = 20.8;
                if(c.coin_count < 5)
				{
					c.giveCoin();
					model.mario.coins++;
				}
            }
            else
            {
                m.y = b.y + b.h + 1;
                m.vert_vel = 0;
            }
        }
    }


}
