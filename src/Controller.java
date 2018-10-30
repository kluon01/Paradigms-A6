import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.Robot;

class Controller implements ActionListener, MouseListener, KeyListener
{
	// Member variables for View and Model Objects
	View view;
	Model model;
	//Robot robot;

	// Boolean variables for key presses
	boolean keyLeft;
	boolean keyRight;
	boolean keyUp;
	boolean keyDown;
	boolean keySpace;
	// Position of initial mouse press x and y
	int mousePressX, mousePressY, button;

	Controller(Model m, Robot r)
	{
		//robot = r;
		model = m;
	}
	public void setView(View v)
	{
		view = v;
	}
	public void actionPerformed(ActionEvent e)
	{
		//robot.mouseWheel(0);
	}
	public void mousePressed(MouseEvent e)
	{
		// On mouse press, save X and Y starting position
        button = e.getButton();
		mousePressX = e.getX();
		mousePressY = e.getY();
	}
	// Mouse release event class
	public void mouseReleased(MouseEvent e)
	{
		// Save positions of mouse pressed and released
		int x1 = mousePressX;
		int y1 = mousePressY;
		int x2 = e.getX();
		int y2 = e.getY();
		int left = Math.min(x1,x2);
		int right = Math.max(x1,x2);
		int top = Math.min(y1,y2);
		int bottom = Math.max(y1,y2);
		// Draw brick using difference in order to allow for any direction of drawing, add view scroll to draw in correct view
        if(button == 1)
        {
            model.addBrick(left+model.scrollX, top, right-left, bottom-top); // Draw regular brick if left clicking
        }
        else if(button == 3)
        {
            model.addCoinBlock(left+model.scrollX, top, right-left, bottom-top); // Draw coin block if right clicking
        }
	}
	public void mouseEntered(MouseEvent e) {    }
	public void mouseExited(MouseEvent e) {    }
	public void mouseClicked(MouseEvent e) {    }

	// Get event of a key press and do an action when key is pressed
	public void keyPressed(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_RIGHT: keyRight = true; break;
			case KeyEvent.VK_LEFT: keyLeft = true; break;
			case KeyEvent.VK_UP: keyUp = true; break;
			case KeyEvent.VK_DOWN: keyDown = true; break;
			case KeyEvent.VK_SPACE: keySpace = true; break;
			case KeyEvent.VK_S: model.save("map.json"); break; // Call model save method to save Json file
			case KeyEvent.VK_L: model.load("map.json"); break; // Call model load method to load last saved Json file
		}
	}

	// Get event of key release
	public void keyReleased(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_RIGHT: keyRight = false; break;
			case KeyEvent.VK_LEFT: keyLeft = false; break;
			case KeyEvent.VK_UP: keyUp = false; break;
			case KeyEvent.VK_DOWN: keyDown = false; break;
			case KeyEvent.VK_SPACE: keySpace = false; break;
		}
	}
	public void keyTyped(KeyEvent e)
	{
	}
	// Update mario depending on key pressed
	void update()
	{
	    // Type cast mario Sprite to Mario object
		Mario mario = (Mario)model.sprites.get(0);

        // Call mario's previous location
		mario.prevLocation();
		if(keyRight)
		{
			mario.x+=25; // Screen is tied to mario's x
			mario.frame_update(); // Update mario's frame going right
		}
		if(keyLeft)
		{
			mario.x-=25;
			mario.frame_update_reversed(); // Update mario's frame going left
		}
		if(keySpace)
		{
			// If mario has recently been on the ground, allow the jump
			if(mario.frames_not_on_ground < 5)
			{
				mario.jump_count++;
				mario.vert_vel -= 18.8; // Jump height
			}
		}

		// Evaluate each possible action
		double score_run = model.evaluateAction(Action.run, 0);
		double score_jump = model.evaluateAction(Action.jump, 0);
		double score_jump_and_run =
				model.evaluateAction(Action.jump_and_run, 0);


		// Do the best one
		if(score_jump_and_run > score_jump &&
				score_jump_and_run > score_run)
			model.do_action(Action.jump_and_run);
		else if(score_jump > score_run)
			model.do_action(Action.jump);
		else
			model.do_action(Action.run);
	}
}
