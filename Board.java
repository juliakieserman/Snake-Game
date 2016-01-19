import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.Random;

import javax.swing.JPanel;

public class Board extends JPanel {
	
	//dimensions for board 
	public static final int BOARD_DIMENSIONS = 25;
	public static final int CELL_SIZE = 20;

	private CellContents[] cells;

	public Board(Game game) {
		//create new array of type cell contents with the dimmensions of the board
		cells = new CellContents[BOARD_DIMENSIONS * BOARD_DIMENSIONS];

		//set dimensions of panel
		setPreferredSize(new Dimension(BOARD_DIMENSIONS * CELL_SIZE, BOARD_DIMENSIONS * CELL_SIZE));
		setBackground(Color.DARK_GRAY);
	}
	
	/**
	 * initialize the board
	 * 
	 * @param none
	 * @return none
	 * 
	 */
	public void initializeBoard() {
		for(int i = 0; i < cells.length; i++) {
			/*initially set all cells to null
			//null used as default value
			to check if cell is empty */
			cells[i] = null;
		}
	}
	
	/**
	 * set value of cell to given type 
	 * in given location
	 * 
	 * @param point - (x, y) location of cell to be modified 
	 * @param type
	 * 
	 * @return none
	 */
	public void setCell(Point point, CellContents type) {
		setCell(point.x, point.y, type);
	}
	
	/**
	 * 
	 * place a type of object in a specific cell location 
	 * 
	 * @param x - x-coordinate of cell
	 * @param y y-coordinate of cell
	 * @param type - type of object desired in cell
	 */
	public void setCell(int x, int y, CellContents type) {
		cells[y * BOARD_DIMENSIONS + x] = type;
	}
	
	/**
	 * Find out what type of item is located in 
	 * a given cell on the board
	 * 
	 * @param x - x-coordinate of cell to search
	 * @param y - y-coordinate of cell to search 
	 * @return
	 */
	public CellContents getCellContents(int x, int y) {
		//return the content of the array at the given x and y coordinates
		return cells[y * BOARD_DIMENSIONS + x];
	}
	
	/**
	 * override paint function
	 * iterates through the board and paints
	 * necessary objects
	 * 
	 * @param graphics object
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//iterate through the game board
		for(int x = 0; x < BOARD_DIMENSIONS; x++) {
			for(int y = 0; y < BOARD_DIMENSIONS; y++) {
				//find out what is in the cell
				CellContents cellContent = getCellContents(x, y);
				//if there is something in the cell
				if(cellContent != null) {
					//call a new paint function and pass the coordinates and cell content
					fillCell(x * CELL_SIZE, y * CELL_SIZE, cellContent, g);
				}
			}
		}
		
	}
	
	/**
	 * fillCell - called by the paint function
	 * paints object to the screen 
	 * 
	 * @param x - x coordinate of object to paint
	 * @param y - y coordinate of object to paint
	 * @param item - type of object to paint
	 * @param g - graphics tool
	 */
	//paint cell based on coordinates and the content in the cell
	private void fillCell(int x, int y, CellContents item, Graphics g) {
	
		if (item == CellContents.Fruit) {
			g.setColor(Color.MAGENTA);
			g.fillOval(x + 2, y + 2, CELL_SIZE - 4, CELL_SIZE - 4);
		} else if (item == CellContents.SnakeBody) {
			g.setColor(Color.WHITE);
			g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
		} else if (item == CellContents.WallBarrier) {
			g.setColor(Color.GREEN);
			g.fillRect(x, y, CELL_SIZE-2, CELL_SIZE-2);
		}
	}

}
