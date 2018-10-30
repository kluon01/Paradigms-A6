import java.util.ArrayList;

class Model
{
	ArrayList<Sprite> sprites; // Member variable for the array list of sprites
	int scrollX; // Scroll position variable
	Mario mario;
	Model()
	{
	    // Initialize sprites arraylist
		sprites = new ArrayList<Sprite>();
		mario = new Mario(this);
		//sprites.add(0,mario);
	}

	// Model Copy Constructor
	Model(Model other)
	{
		sprites = new ArrayList<Sprite>();
		for (int i = 0; i  < other.sprites.size(); i++)
		{
			Sprite s = other.sprites.get(i);
			Sprite clone = s.makeDeepCopy(this);
			sprites.add(clone);
			if(clone.isMario())
			{
				mario = (Mario)clone;
			}
		}
	}

	double evaluateAction(Action action, int depth)
	{
		//mario = (Mario)sprites.get(0);
		int d = 16;
		int k =3;
		// Evaluate the state
		if(depth >= d)
		{
			return 2 * mario.x + 500000 * mario.coins - 4 * mario.jump_count;
		}

		// Simulate the action
		Model copy = new Model(this); // uses the copy constructor
		copy.do_action(action); // like what Controller.update did before
		copy.update(); // advance simulated time

		// Recurse
		if(depth % k != 0)
			return copy.evaluateAction(action, depth + 1);
		else
		{
			double best = copy.evaluateAction(Action.run, depth + 1);
			best = Math.max(best, copy.evaluateAction(Action.jump, depth + 1));
			best = Math.max(best, copy.evaluateAction(Action.jump_and_run, depth + 1));
			best = Math.max(best, copy.evaluateAction(Action.run_and_jump, depth + 1));
			return best;
		}
	}
	void do_action(Action a)
	{
		// Type cast mario Sprite to Mario object
		Mario mario = (Mario)sprites.get(0);
		// Call mario's previous location
		mario.prevLocation();
		if(a == Action.run)
		{
			mario.x+=20; // Screen is tied to mario's x
			mario.frame_update(); // Update mario's frame going right
		}
		if(a == Action.jump)
		{
			// If mario has recently been on the ground, allow the jump
			if(mario.frames_not_on_ground < 5)
			{
				mario.vert_vel -= 20; // Jump height
				mario.jump_count++;
			}
		}
		if(a == Action.jump_and_run)
		{
			// If mario has recently been on the ground, allow the jump
			if(mario.frames_not_on_ground < 5)
			{
				mario.jump_count++;
				mario.vert_vel -= 20; // Jump height
				mario.x+=20;
			}
		}
		if(a == Action.run_and_jump)
		{
			// If mario has recently been on the ground, allow the jump
			if(mario.frames_not_on_ground < 5)
			{
				mario.jump_count++;
				mario.x+=20;
				mario.vert_vel -= 20; // Jump height
			}
		}
	}

	// Add brick to sprites array with controller info
	public void addBrick(int x, int y, int w, int h)
	{
		Brick b = new Brick(x,y,w,h,this);
		sprites.add(b);
	}

    // Add coin block to sprites array with controller info
	public void addCoinBlock(int x, int y, int w, int h)
    {
        CoinBlock c = new CoinBlock(x,y,w,h,1,this);
        sprites.add(c);
    }

    // Generalized model update method
	public void update()
	{
		for(int i = 0; i < sprites.size(); i++)
		{
			Sprite s = sprites.get(i);
			s.update();
		}
	}

	// unmarshal method to load the previously saved map
	void unmarshal(Json ob)
	{
	    // Clear all sprites
		sprites.clear();
		Json j_sprites = ob.get("Sprites");
		Sprite spr;
		for(int i = 0; i < j_sprites.size(); i++)
		{
		    // Get sprite from Json file
			Json j = j_sprites.get(i);
			String s = j.getString("Type");
            // Use type field to determine which sprite to unmarshal
			if(s.equals("Mario"))
			{
				spr = new Mario(j,this);
                sprites.add(0,spr);   // Make sure mario is always in position 0 of the arraylist
            }

            if(s.equals("CoinBlock"))
            {
                spr = new CoinBlock(j,this);
                sprites.add(spr);
            }
			else if(s.equals("Brick"))
			{
				spr = new Brick(j,this);
				sprites.add(spr);
			}
		}
	}

	// marshal method to save model to Json format
	Json marshal()
	{
		Json ob = Json.newObject();
		Json j_sprites = Json.newList();
		ob.add("Sprites", j_sprites);
		for(int i = 0; i < sprites.size(); i++)
		{
			Sprite s = sprites.get(i);
			Json j = s.marshal();
			j_sprites.add(j);
		}
		return ob;
	}

	// Save method that marshals and saves the Json file
	void save(String filename)
	{
		Json ob = marshal();
		ob.save(filename);
	}

	// Load methods that loads Json file and unmarshals to the model
	void load(String filename)
	{
		Json ob = Json.load(filename);
		unmarshal(ob);
	}

}
