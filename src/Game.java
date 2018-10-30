import javax.swing.*;
import java.awt.*;
import java.awt.Robot;

public class Game extends JFrame
{
	// Member variables for Model, Controller, and View objects taht comprise the game
	Model model;
	Controller controller;
	View view;
	Mario mario;
	Robot bot;
	// Constructor that initializes all the necessary elements of the game
	public Game()
	{
		model = new Model();
		//mario = new Mario(model);
		//model.sprites.add(0,mario); // Create mario and add to arraylist so mario is always in position 0
		//model.mario = (Mario)model.sprites.get(0);
		controller = new Controller(model,bot);
		view = new View(controller, model);
        model.load("map.json");    // Load pre made map when building model
		this.setTitle("Mario");
		this.setSize(1600, 900);
		this.setFocusable(true);
		this.getContentPane().add(view);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		view.addMouseListener(controller);
		this.addKeyListener(controller);
	}

    // Main method to run game
	public static void main(String[] args)
	{
		Game g = new Game();
		g.run();
	}

	public void run()
	{
		while(true)
		{
			controller.update();
			model.update();
			view.repaint(); // Indirectly calls View.paintComponent
			Toolkit.getDefaultToolkit().sync(); // Updates screen

			// Go to sleep for 50 miliseconds
			try
			{
				Thread.sleep(50);
			} catch(Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
}
