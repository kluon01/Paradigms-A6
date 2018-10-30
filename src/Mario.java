import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;

public class Mario extends Sprite
{
    // Member variables for mario
    int prev_x;
    int prev_y;
    double vert_vel;
    int frame;
    int frames_not_on_ground;
    int jump_count;
    int coins;
    static Image[] mario_images = null; // Mario image array

    Mario(Model m)
    {
        // Initialize member variables
        super(m);
        frame = 0;
        frames_not_on_ground = 0;
        x = 0;
        y = 450;
        w = 61;
        h = 96;
        prev_x = 0;
        prev_y = 450;
        vert_vel = 3.5;
        coins = 0;
        jump_count = 0;
    }

    // Mario Copy Constructor
	Mario(Mario copyme, Model newModel)
	{
		super(copyme, newModel);
		frame = copyme.frame;
		frames_not_on_ground = copyme.frames_not_on_ground;
		prev_x = copyme.prev_x;
		prev_y = copyme.prev_y;
		vert_vel = copyme.vert_vel;
		coins = copyme.coins;
		jump_count = copyme.jump_count;
	}

	Mario makeDeepCopy(Model newModel)
	{
		return new Mario(this, newModel);
	}

    // Mario unmarshal
    Mario(Json ob, Model m)
    {
        super(m);
        // Initialize member variables from Json objects
        x = (int)ob.getLong("x");
        y = (int)ob.getLong("y");
        w = (int)ob.getLong("w");
        h = (int)ob.getLong("h");
        vert_vel = ob.getDouble("v velocity");
        frame = (int)ob.getLong("frame");
        frames_not_on_ground = (int)ob.getLong("frames not on ground");
    }

    // Mario marshal
    Json marshal()
    {
        Json ob = Json.newObject();
        ob.add("Type", "Mario");
        ob.add("x",x);
        ob.add("y",y);
        ob.add("w",w);
        ob.add("h",h);
        ob.add("v velocity", vert_vel);
        ob.add("frames not on ground", frames_not_on_ground);
        ob.add("frame", frame);
        return ob;
    }

    // Method that keeps track of Mario's previous location to be used to determine where Mario is colliding
    void prevLocation()
    {
        prev_x = x;
        prev_y = y;
    }


    // Update mario model
    void update()
    {
        model.scrollX = x - 300; // Tie mario's x with screen scroll
        vert_vel += 5.8; // "Gravity" value on mario
        y += vert_vel; // add gravity to mario

        // When mario is on the ground, stop him and reset frames on ground
        if(y > 687)
        {
            vert_vel = 0.0;
            frames_not_on_ground = 0;
            y = 687;
        }

        // Loop through array of sprites and compare them to mario
        for(int i = 0; i < model.sprites.size(); i++)
        {
            Sprite s = model.sprites.get(i);
            // Only detect and handle collision if mario and brick
            if(s.isBrick())
            {
                if (s != this && doesCollideWith(this,s))
                {
                    collisionHandle(this,s);
                }
            }
        }
        // Increment frames not on the ground
        frames_not_on_ground++;
    }

    public void draw(Graphics g)
    {
        // Lazy loading implementation
        if(mario_images == null) // Load images only once
        {
            // Initialize model and image array
            mario_images = new Image[10];
            try {
                mario_images[0] = ImageIO.read(new File("mario1.png"));
                mario_images[1] = ImageIO.read(new File("mario2.png"));
                mario_images[2] = ImageIO.read(new File("mario3.png"));
                mario_images[3] = ImageIO.read(new File("mario4.png"));
                mario_images[4] = ImageIO.read(new File("mario5.png"));
                mario_images[5] = ImageIO.read(new File("mario6.png"));
                mario_images[6] = ImageIO.read(new File("mario7.png"));
                mario_images[7] = ImageIO.read(new File("mario8.png"));
                mario_images[8] = ImageIO.read(new File("mario9.png"));
                mario_images[9] = ImageIO.read(new File("mario10.png"));
            }
            catch (Exception e)
            {
                System.out.println("Failed to read mario images.");
                System.exit(0);
            }
        }
        // Draw mario
        g.drawImage(mario_images[frame],x - model.scrollX, y,null);
    }

    // Methods to animate mario depending on key pressed
    void frame_update()
    {
        frame++;
        if(frame > 4)
        {
            frame = 0;
        }
    }
    void frame_update_reversed()
    {
        if(frame < 5)
        {
            frame = 5;
        }
        else if(frame > 8)
        {
            frame = 5;
        }
        else
        {
            frame++;
        }
    }

	boolean isMario() { return true; }
}
