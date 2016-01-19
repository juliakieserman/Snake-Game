
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class Game extends JFrame {
	
	public final static int BOARD_DIMENSIONS = 25;
	
	//panel to hold the score of the game
	private ScorePanel scorePanel;
	//panel with start instructions
	private JPanel instructPanel;
	//label with start instructions
	private JLabel instructText;
	
	//chosen cycles per second to animate
	public final static float cyclesPerSecond = 9.0f;
	//calculate milliseconds per cycle based on chosen cycles per second
	public final static float millisPerCycle = (4.0f / cyclesPerSecond) * 1000;
	//count the number of cycles of the game
	private int numRuns;
	//boolean to hold if the game is in progress
	private boolean gameInProgress = false;
	
	//starting size of snake when the game begins
	private static final int MIN_SNAKE_LENGTH = 3;
	
	//maximum number of directions 
	private static final int MAX_DIRECTIONS = 3;
	
	//instance of the board
	private Board board;
	
	//variable to hold score 
	private int score;
	
	//length of time that thread will sleep
	int speed;
	
	//level user is currently on
	private int level;
	
	//random variable to calculate coordinates for fruit and walls
	private Random random;
	
	//linked list to hold each body part of the snake
	private LinkedList<Point> theSnake;
	
	//linked list to hold directions the snake pieces are moving
	public ArrayList<Movement> directArray;
	
	//flag indicates what type of collision has occurred (used for testing)
	private int flag = 0;
	
	
	/**
	 * constructor - calls three initialization functions 
	 * to set up frame and listeners
	 * 
	 * @param none
	 * @return none
	 */
	public Game() {
		//label the JFrame window
		super("Julia's theSnake Game");
		
		//function to create instances of all variables needed for the game
		initVariables();

		//initialize key listeners for snake movement using arrow keys
		initListeners();
		
		//add panels to GUI
		initGUI();
		
		//close JFrame when window is closed
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		//do not allow user to re-size game
		setResizable(false);
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	/**
	 * initialize variables used later in the game
	 * 
	 * @param none
	 * @return none
	 */
	public void initVariables() {
		//create new instances of variables used later
		random = new Random();
		theSnake = new LinkedList<>();
		directArray = new ArrayList<Movement>();
		
		//initialize speed to 100 (length of time thread will sleep)
		speed = 100;
		//start at level 1
		level = 1;
		//start with a score of 0
		setScore(0);
		
		numRuns = 0;
		
		//add board to the game
		board = new Board(this);
		
		//create new panel to hold instructions for how to start game
		instructPanel = new JPanel();
		//create new label to hold instructions for how to start game
		instructText = new JLabel("Press Enter to Play!");
		
		//add score panel to the game
		scorePanel = new ScorePanel(this);
	}
	
	/**
	 * initGUI - add panels to the JFrame in desired layout
	 * @param none
	 * @return none
	 */
	public void initGUI() {
		
		//add game board to the center of the JFrame
		add(board, BorderLayout.CENTER);
		
		//set font preferences for instructions text
		instructText.setFont(new Font("SansSerif", Font.BOLD, 16));
		//add instructions text to instructions panel
		instructPanel.add(instructText);
		//set background color of instructions panel
		instructPanel.setBackground(Color.LIGHT_GRAY);
		
		//add button panel to the bottom of the JFrame
		add(instructPanel, BorderLayout.SOUTH);
		
		//add score panel to the top of the JFrame
		add(scorePanel, BorderLayout.NORTH);
		
		//set dimensions of score panel
		scorePanel.setPreferredSize(new Dimension(40, 40));
		
	}

	/**
	 * A nested class that displays the current score
	 * and current level in response to game changes
	 */
	public class ScorePanel extends JPanel {
		
		Game game;

		public ScorePanel(Game theGame) {
			game = theGame;
			setPreferredSize(new Dimension(30, 40));
		}
		
		@Override 
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			setBackground(Color.LIGHT_GRAY);
			//set font preferences for strings
			g.setFont(new Font("SansSerif", Font.BOLD, 16));
			//string that gets current score of game
			g.drawString("SCORE: " + game.getScore(), 10, 20);
			//string that gets current level of game
			g.drawString("LEVEL: " + game.getLevel(), 420, 20);
		}
	}
	
	/**
	 * getLevel - return current game level
	 * 
	 * @param none
	 * @return level
	 */
	public int getLevel() {
		return level;
	}
	
	/**
	 * setup keyListeners and movements 
	 * associated with each key
	 * 
	 * @param none
	 * @return none
	 */
	public void initListeners() {
		
		KeyListener listener = new KeyListener()  {
			
			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
				int key = e.getKeyCode();
			
				//if the up key is pressed
				if (key == KeyEvent.VK_UP) {
					//if list of movements is less than maximum size (3)
					if(directArray.size() < MAX_DIRECTIONS) {
						//look at the last way that the snake moved
						Movement lastDirection = getDirection();
						//make sure that you are moving a new direction
						//and not a 180 flip before changing directions
						if(lastDirection != Movement.Down && lastDirection != Movement.Up) {
							//add new direction to the list of movements
							directArray.add(Movement.Up);
						}
					}
				} else if (key == KeyEvent.VK_DOWN) {
					if(directArray.size() < MAX_DIRECTIONS) {
						Movement lastDirection = getDirection();
						if(lastDirection != Movement.Up && lastDirection != Movement.Down) {
							directArray.add(Movement.Down);
						}
					}
				} else if (key == KeyEvent.VK_LEFT) {
					if(directArray.size() < MAX_DIRECTIONS) {
						Movement last = getDirection();
						if(last != Movement.Right && last != Movement.Left) {
							directArray.add(Movement.Left);
						}
					}
				} else if (key == KeyEvent.VK_RIGHT) {
					if(directArray.size() < MAX_DIRECTIONS) {
						Movement lastDirection = getDirection();
						if(lastDirection != Movement.Left && lastDirection != Movement.Right) {
							directArray.add(Movement.Right);
						}
					}
				} 
				//start the game by pressing the enter key!
				else if (key == KeyEvent.VK_ENTER) {
					newGame();

				}				
			}
		};

		addKeyListener(listener);
	}
	
	public void startGame() {
		
		gameInProgress = true;

		while(true) {
				
			long beforeTime = System.currentTimeMillis() / 1000000L;
			long calculate = (long) (beforeTime/millisPerCycle);
			
			if (!gameInProgress) {
				//referenced from: http://www.tutorialspoint.com/java/lang/math_floor.htm
				numRuns += Math.floor(calculate);
			}
			
			if (numRuns > 0) 
				collisionCheck();
			
			//Repaint the board and side panel with the new content.
			board.repaint();
			scorePanel.repaint();

			try {
				Thread.sleep(speed);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * collisionCheck - responsible for the main game updates
	 * 
	 * either ends game or increments score and level depending
	 * on what type of collision has occurred
	 * 
	 */
	private void collisionCheck() {
		
		//collision holds the type of cell contents in the tile 
		//that the head of the snake collided with to determine what
		//type of collision occurred 
		CellContents collision = createSnake();
		
		//if the snake collides with the fruit
		if(collision == CellContents.Fruit) {
			//increment the score
			score++;
			//place a new fruit on the board
			createNewObstacle(CellContents.Fruit);
			
			flag = 1;
		
			//LEVEL 2: if score is between 6 and 12
			if (score >= 6 && score < 12) {
				level = 2;
				speed--;
				//create a wall every other fruit consumed
				if (score % 2 == 0)
					createNewObstacle(CellContents.WallBarrier);
			}
			//LEVEL 3: if score is greater than 12
			else if (score >= 12 && score < 18) {
				level = 3;
				//create wall with every fruit consumed
				createNewObstacle(CellContents.WallBarrier);
			}	
			
			else if (score >= 18) {
				level = 4;
				//speed--;
				createNewObstacle(CellContents.WallBarrier);
			}
			
			
		}
		//if snake collides with itself 
		else if (collision == CellContents.SnakeBody) {
			flag = 2;
			gameOver();
		} 
		//if snake collides with a wall 
		else if (collision == CellContents.WallBarrier) {
			flag = 3;
			gameOver();
		}
	}
	
	/**
	 * createSnake() - update the snake head and movement 
	 * of the snake
	 * 
	 * @param none
	 * @return none
	 */
	private CellContents createSnake() {

		//get the next direction the snake should be moving
		Movement currentDirection = getDirection();
		
		if (directArray.size() == 0)
			directArray.add(Movement.Up);
		
		//calculate new head after updating the snake
		Point head = new Point(theSnake.peekFirst());
		
		//move the snake
		if (currentDirection == Movement.Up) {
			head.y--;
		} else if (currentDirection == Movement.Down) {
			head.y++;
		} else if (currentDirection == Movement.Left) {
			head.x--;
		} else if (currentDirection == Movement.Right) {
			head.x++;
		}
		
		//if snake head collides with the wall, indicate game should end
		if (head.x < 0) 
			//same effect as if collided with itself (game ends)
			return CellContents.SnakeBody;
		else if (head.x >= BOARD_DIMENSIONS)
			return CellContents.SnakeBody;
		else if (head.y < 0)
			return CellContents.SnakeBody;
		else if (head.y >= BOARD_DIMENSIONS)
			return CellContents.SnakeBody;
	
		//cell that the head just moved to
		CellContents previousCell = board.getCellContents(head.x, head.y);

		//if that cell was not a fruit or wall barrier (no collision occurred) 
		if(previousCell != CellContents.Fruit && previousCell != CellContents.WallBarrier && theSnake.size() > MIN_SNAKE_LENGTH) {
			//take the last element in the snake (the tail)
			Point tail = theSnake.removeLast();
			//and clear its cell
			board.setCell(tail, null);
			previousCell = board.getCellContents(head.x, head.y);
		}
		
		//if, after moving, the snake has not collided with itself, update the head
		if(previousCell != CellContents.SnakeBody) {
			//make the old head a body
			board.setCell(theSnake.peekFirst(), CellContents.SnakeBody);
			//push the new head cell to the front of the list
			theSnake.push(head);
			//set it as the head of the snake
			board.setCell(head, CellContents.SnakeBody);
			
			if(directArray.size() > 1) {
				directArray.remove(0);
			}
		}
				
		return previousCell;
	}


	/**
	 * newGame - set all initial values to create a new game
	 * @param none
	 * @return none
	 */
	private void newGame() {
	
		score = 0;
		
		//create a new snake and start it in the middle of the board
		Point head = new Point(Board.BOARD_DIMENSIONS / 2, Board.BOARD_DIMENSIONS / 2);
		
		//make sure you are starting with an empty list
		theSnake.clear();
		//add the new snake head to the list
		theSnake.add(head);
		
		//set up board
		board.initializeBoard();
		board.setCell(head, CellContents.SnakeBody);
		
		//make sure you are starting with an empty list
		directArray.clear();
		//add initial movement to directArray so 
		//snake begins moving when game starts
		directArray.add(Movement.Up);
		
		numRuns = 0;
		
		gameInProgress = false;
		
		//add a fruit to the board
		createNewObstacle(CellContents.Fruit);
	}
	
	/**
	 * 
	 * @param type - the type of cellContents that should be placed 
	 * on the board (either a wall piece or a fruit)
	 */
	public void createNewObstacle(CellContents type) {
		
		//get the number of free cells on the gameboard
		int  freeBoardCells = BOARD_DIMENSIONS * Board.BOARD_DIMENSIONS - theSnake.size();
		
		//create a random location from that number
		int loc = random.nextInt(freeBoardCells);

		//flag for if a cell is or is not empty
		int emptyCell = -1;
	
		if (type == CellContents.Fruit) {
			
			//iterate through the board
			for(int x = 0; x < BOARD_DIMENSIONS; x++) {
				for(int y = 0; y < BOARD_DIMENSIONS; y++) {
					//if the cell is empty
					if (board.getCellContents(x, y) == null) {
						//and the cell is in the randomly chosen location
						if (++emptyCell == loc) {
							//set cell to desired type
							board.setCell(x, y, CellContents.Fruit);
							break;
						}
					} else if(board.getCellContents(x, y) == CellContents.Fruit) {
						if (++emptyCell == loc) {
							board.setCell(x, y, CellContents.Fruit);
						}
					}
				}
			}
		} else if (type == CellContents.WallBarrier) {

			//iterate through the board
			for(int x = 0; x < BOARD_DIMENSIONS; x++) {
				for(int y = 0; y < BOARD_DIMENSIONS; y++) {
					//if the cell is empty
					if (board.getCellContents(x, y) == null) {
						if (++emptyCell == loc) {
							board.setCell(x, y, CellContents.WallBarrier);
							break;
						}
					} else if(board.getCellContents(x, y) == CellContents.WallBarrier) {
						if (++emptyCell == loc) {
							board.setCell(x, y, CellContents.WallBarrier);
						}
					}
				}
			}
		} 
	}
	
	/**
	 * getDirection()
	 *
	 * @param none
	 * @return first index of the directions array (a Movement)
	 */
	public Movement getDirection() {
		return directArray.get(0);
		
	}
	
	/**
	 * gameOver - called when the snake collides with a wall or itself
	 * shows the final score and the quits the window 
	 */
	public void gameOver() {
		
		String message = "Game Over!\n Score = " + score;
		JOptionPane.showMessageDialog(this, message, "Game Over", JOptionPane.YES_NO_OPTION);
		System.exit(ABORT);
	}

	/**
	 * @param none
	 * @return score
	 */
	public int getScore() {
		return score;
	}	
	
	public int getFlag() {
		return flag;
	}
	
	public void setScore(int theScore) {
		score = theScore;
	}
	
	public static void main(String[] args) {
		Game theSnake = new Game();
		theSnake.startGame();
	}

}
