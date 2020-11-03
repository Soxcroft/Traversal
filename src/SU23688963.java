
public class SU23688963 {
	
	// EXTRA FUNCTIONALITY 
	// This is for the auto-player, which can be toggled in GUI mode by pressing 'a'
	
	// Constants for the time between moves and the maximum number of times the auto
	// player will visit a square before changing its algorithm to determine its next
	// move.
	private static int SHOW_DELAY = 300;
	private static int MAX_VISITS = 10;
		
	public static char[][] copy(char[][] a1, int rows, int cols) {
		// Returns a copy of the 2D char array passed as an argument 
		char[][] a2 = new char[cols][rows];
		for (int r=0; r<rows; ++r) 
			for (int c=0; c<cols; ++c)
				a2[c][r] = a1[c][r];
		return a2;
	}
	
	public static int[][] copy(int[][] a1, int rows, int cols) {
		// Returns a copy of the 2D int array passed as an argument 
		int[][] a2 = new int[cols][rows];
		for (int r=0; r<rows; ++r) 
			for (int c=0; c<cols; ++c)
				a2[c][r] = a1[c][r];
		return a2;
	}
	
	public static int[] copy(int[] a1) {
		// Returns a copy of the 1D int array passed as an argument 
		int[] a2 = new int[a1.length];
		for (int i=0; i<a1.length; ++i)
			a2[i] = a1[i];
		return a2;
	}
	
	public static char getNextMove(int[][] cnt, int[] player, int[] target, int rows, int cols) {
		// This algorithm is made up of two parts to determine the next move of the
		// auto player. 
		// The first part tries to pick moves which minimizes its distance to the 
		// target OR pick moves which take it to tiles which have been visited the 
		// least. This ensures that the auto player doesn't only try one path. 
		char[] moves = {'h','j','k','l', 'a'};
		int min_visits = MAX_VISITS, min_dist = Integer.MAX_VALUE;
		int[][] dxy = {{-1,0},{0,1},{0,-1},{1,0}};
		int best_move = 4; //corresponds with 'a' in moves
		for (int i=0; i<4; ++i) {
			int nx = player[0] + dxy[i][0]; //new x
			int ny = (player[1] + dxy[i][1]+rows)%rows; //new y
			int dist = Math.abs(target[0]-nx) + Math.abs(target[1]-ny); //distance to target
			if (nx >=0 && nx < cols && (cnt[nx][ny] < min_visits || 
					cnt[nx][ny] == min_visits && dist < min_dist)) {
				min_visits = cnt[nx][ny];
				min_dist = dist;
				best_move = i;
			}
		}
		// The second part picks moves purely based on going to tiles with the least
		// amount of visits. This part is used when the auto player can't go to a tile
		// with fewer visits than MAX_VISITS
		if (best_move == 4) {
			min_visits = MAX_VISITS*2;
			for (int i=0; i<4; ++i) {
				int nx = player[0] + dxy[i][0];
				int ny = (player[1] + dxy[i][1]+rows)%rows;
				if (nx >=0 && nx < cols && cnt[nx][ny] < min_visits) {
					min_visits = cnt[nx][ny];
					best_move = i;
				}
			}
		}
		return moves[best_move];
	}
	
	// METHODS
	
	public static void addObjInfo(int object[], int x, int y, char obs) {
		// Adds the coordinates and character of an object to its respective array
		object[0] = x;
		object[1] = y;
		object[2] = obs;
	}
	
	public static void movePlayer(int player[], int[][] cnt, char move, int rows) {
		// Updates the player's coordinates given the character pressed.
		switch (move) {
		case 'h': player[0]--; break;
		case 'l': player[0]++; break;
		case 'k': player[1]--; break;
		case 'j': player[1]++; break;
		}
		//ensure the player wraps around the board properly 
		player[1] = (player[1]+rows)%rows;  
		//increment the players position by one on the counter
		cnt[player[0]][player[1]]++;
	}
	
	// OUTPUT METHODS
	// This includes any methods which print to the terminal or draw on the canvas
	
	public static void printBoard(char[][] board, int rows, int cols) {
		// Prints the board to the terminal according to the format on the project page
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				char obs = board[c][r];
				switch (obs) {
				case 'K': obs = 'k'; break; 
				case 'h': obs = 's'; break;
				case 'v': obs = 's'; break;
				case 'H': obs = 'S'; break;
				case 'V': obs = 'S'; break;
				case 'u': obs = 'm'; break;
				case 'd': obs = 'm'; break;
				case 'l': obs = 'm'; break;
				case 'r': obs = 'm'; break;
				case 'U': obs = 'm'; break;
				case 'D': obs = 'm'; break;
				case 'L': obs = 'm'; break;
				case 'R': obs = 'm'; break;
				}
				StdOut.print(obs);
			}
			StdOut.println();
		}
	}
	
	public static void initBoard(int rows, int cols) {
		// Initialize the GUI window
		StdDraw.setCanvasSize(cols*108, rows*108);
		// Set X scale to go from 0 to cols and Y scale from rows to 0 so that 
		// coordinates on the canvas correspond with coordinates in the 2D board array
		StdDraw.setXscale(0, cols);
		StdDraw.setYscale(rows, 0); 
		StdDraw.show(0);
	}
	
	public static void drawBoard (char[][] board, int[] player, int rows, int cols, int t) {
		// Draws the board to the canvas
		// Images from 'traversal_files' are used
		board[player[0]][player[1]] = (char) player[2];
		StdDraw.clear();		
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				String file_name = "traversal_files/tvl_";
				switch (board[c][r]) {
				case '.': file_name += "e"; break;
				case 't': file_name += "t"; break;
				case 'x': file_name += "x"; break;
				case 'u': file_name += "uh"; break;
				case 'd': file_name += "dh"; break;
				case 'l': file_name += "lh"; break;
				case 'r': file_name += "rh"; break;
				case 'U': file_name += "uv"; break;
				case 'D': file_name += "dv"; break;
				case 'L': file_name += "lv"; break;
				case 'R': file_name += "rv"; break;
				case 'h': file_name += "sh1"; break;
				case 'H': file_name += "sh0"; break;
				case 'v': file_name += "sv1"; break;
				case 'V': file_name += "sv0"; break;
				case 'k': file_name += "k1"; break;
				case 'K': file_name += "k0"; break;
				case 'p': file_name += "p1"; break;
				case 'P': file_name += "p0"; break;
				case 'Y': file_name += "s"; break;
				}
				file_name += ".png";
				// Draw an empty space and another picture over it so that the 
				// background is consistently grey
				StdDraw.picture(c+0.5, r+0.5, "traversal_files/tvl_e.png");
				StdDraw.picture(c+0.5, r+0.5, file_name);
			}
		}
		// The program then pauses for 't' seconds so that there is a delay when the
		// auto player is playing. 't' is 0 when the player is playing. 
		StdDraw.show(t); 
	}
	
	public static boolean checkState(char[][] board, int[] player, int[] target, boolean print) {
		// Check if the player has won or lost, print which of these is the case 
		// and return true or false depending on whether or not the game has ended. 
		if (player[0] == target[0] && player[1] == target[1]) {
			StdOut.println("You won!");
			return false;
		} else {
			char obs = board[player[0]][player[1]];
			if (!(obs == '.' || obs == 'k' || obs == 'K' || 
				obs == 'P' || obs == 'V' || obs == 'H')) {
				// Only print 'You lost!' if 'print' is true
				// This is to avoid it being printed to the terminal every time
				// the auto player checks a move that ends up being invalid.
				if (print) StdOut.println("You lost!");  
				return false;
			}
		}
		return true;
	}
	
	// UPDATE BOARD METHODS
	
	public static void clearBoard(char[][] board, int rows, int cols) {
		// Makes every tile on the board an empty space
		for (int r=0; r<rows; ++r) 
			for (int c=0; c<cols; ++c)
				board[c][r] = '.';
	}
	
	public static void addWalls(char[][] board, int[][] walls) {
		// Adds walls to the 2D board array.
		for (int i=0; i<walls.length; ++i) 
			board[walls[i][0]][walls[i][1]] = (char) walls[i][2];
	}
	
	public static void addKeysAndPorts(char[][] board, int[][] keys, int[][] ports, int[] player) {
		// Add the keys and ports to the board.
		// If the player has moved onto a key, change it to unavailable and change 
		// the states of all the ports when adding them.
		boolean key_change = false;
		for (int i = 0; i < keys.length; i++) {
			if (!key_change && keys[i][0] == player[0] && 
				keys[i][1] == player[1] && (char) keys[i][2] == 'k') {
				keys[i][2] = 'K'; // 'K' = unavailable key
				key_change = true;
			}
			board[keys[i][0]][keys[i][1]] = (char) keys[i][2];
		}
		for (int i = 0; i < ports.length; i++) {
			if (key_change) {
				switch (ports[i][2]) {
				case 'P': ports[i][2] = 'p'; break;
				case 'p': ports[i][2] = 'P'; break;
				}
			}
			board[ports[i][0]][ports[i][1]] = (char) ports[i][2];
		}
	}
	
	public static void addSwitchers(char[][] board, int[][] switchers, char move) {
		// Change the state of the switchers if necessary and add them to the board
		for (int i=0; i<switchers.length; ++i) {
			if (move == 'h' || move == 'l') {
				// Change horizontal switchers
				switch (switchers[i][2]) {
				case 'h': switchers[i][2] = 'H'; break;
				case 'H': switchers[i][2] = 'h'; break; 
				}
			} else {
				// Change vertical switchers
				switch (switchers[i][2]) {
				case 'v': switchers[i][2] = 'V'; break;
				case 'V': switchers[i][2] = 'v'; break; 
				}
			}
			board[switchers[i][0]][switchers[i][1]] = (char) switchers[i][2];
		}
	}
	
	public static void addMovers(char[][] board, int[][] movers, char move, int rows, int cols) {
		// Move the movers depending on the players move and add them to the board
		for (int i=0; i<movers.length; ++i) {
			if (move == 'h' || move == 'l') { 
				// Move horizontal movers
				switch (movers[i][2]) {
				case 'd': movers[i][1]++; break;
				case 'u': movers[i][1]--; break;
				case 'r': movers[i][0]++; break;
				case 'l': movers[i][0]--; break;
				}
			} else {
				// Move vertical movers
				switch (movers[i][2]) {
				case 'D': movers[i][1]++; break;
				case 'U': movers[i][1]--; break;
				case 'R': movers[i][0]++; break;
				case 'L': movers[i][0]--; break;
				}
			}
			// Ensure that they wrap around the board correctly before adding them to
			// the board
			movers[i][0] = (movers[i][0]+cols)%cols;
			movers[i][1] = (movers[i][1]+rows)%rows;
			board[movers[i][0]][movers[i][1]] = (char) movers[i][2];
		}
	}
	
	
	// MAIN
	
	public static void main(String[] args) {
		
		if (args.length == 1 || args.length == 2) {
			// If the number of arguments is 1 or 2, read in the board from the text 
			// file
			In in = new In(args[0]); // 'In' uses scanners to read from files
			in.readLine(); // Board name, which is never used
			int rows = in.readInt(), cols = in.readInt();
			char board[][] = new char[cols][rows];
			// Read the board from the text file into the 2D array
			// and count the number of different objects
			int num_movers = 0; 
			int num_walls = 0; 
			int num_switchers = 0; 
			int num_ports = 0; 
			int num_keys = 0; 
			for (int r=0; r<rows; ++r) {
				in.readChar(); // 'Dummy' char at the end of each line
				for (int c=0; c<cols; ++c) {
					char obs = in.readChar();
					if (obs == 'u' || obs == 'd' || obs == 'l' || obs == 'r') {
						num_movers++;
					} else if (obs == 'U' || obs == 'D' || obs == 'L' || obs == 'R') {
						num_movers++;
					} else if (obs == 'h' || obs == 'H' || obs == 'v' || obs == 'V') {
						num_switchers++;
					} else if (obs == 'k' || obs == 'K') {
						num_keys++;
						obs = 'k'; // 'k' is an available key, 'K' is unavailable
					} else if (obs == 'p' || obs == 'P') {
						num_ports++;
					} else if (obs == 'x' || obs == 'X') {
						num_walls++;
						obs = 'x';
					} else if (obs == 's' || obs == 'S') {
						obs = 'Y';
					} else if (obs == 't' || obs == 'T') {
						obs = 't';
					}
					// Add the character to the board
					board[c][r] = obs;
				}
			}
			
			// Create 2D arrays for the different objects.
			// The arrays will store the different objects coordinates and characters.
			// The format of the arrays will be array[n][3], 
			// where n < num_object (could be any object),
			// and array[n][0] will be the x coordinate, array[n][1] the y coordinate
			// and array[n][2] the character of object 'n'.
			// The arrays for the player and target will be 1D since there is only one
			// of each. 
			int[] player = new int[3];
			int[] target = new int[3];
			int[][] movers = new int[num_movers][3];
			int[][] walls = new int[num_walls][3];
			int[][] switchers = new int[num_switchers][3];
			int[][] ports = new int[num_ports][3];
			int[][] keys = new int[num_keys][3];
			// The number of objects are then set back to 0 and incremented by 1
			// each time they are encountered in the 2D array. They now point to 
			// object 'n' for the the different objects.
			num_movers = 0;
			num_walls = 0;
			num_switchers = 0;
			num_ports = 0;
			num_keys = 0;
			for (int r=0; r<rows; ++r) {
				for (int c=0; c<cols; ++c) {
					char obs = board[c][r];
					if (obs == 'u' || obs == 'd' || obs == 'l' || obs == 'r') {
						addObjInfo(movers[num_movers++], c, r, obs);
					} else if (obs == 'U' || obs == 'D' || obs == 'L' || obs == 'R') {
						addObjInfo(movers[num_movers++], c, r, obs);
					} else if (obs == 'h' || obs == 'H' || obs == 'v' || obs == 'V') {
						addObjInfo(switchers[num_switchers++], c, r, obs);
					} else if (obs == 'k') {
						addObjInfo(keys[num_keys++], c, r, obs); // 'k' is used to show availability
					} else if (obs == 'p' || obs == 'P') {
						addObjInfo(ports[num_ports++], c, r, obs);
					} else if (obs == 'x') {
						addObjInfo(walls[num_walls++], c, r, obs);
					} else if (obs == 't') {
						addObjInfo(target, c, r, obs);
					} else if (obs == 'Y') {
						addObjInfo(player, c, r, obs);
					}
				}
			}
			
			// Create a 2D array to count how many times the player/computer
			// has visited certain tiles
			int[][] cnt = new int[cols][rows];
			cnt[player[0]][player[1]] = 1;
		
			// PLAY THE GAME
			
			if (args.length == 1) {
				// GRAPHICS MODE
				initBoard(rows, cols); //Sets up the canvas
				drawBoard(board, player, rows, cols, 0); 
				boolean running = true;
				while (running) {
					while (!StdDraw.hasNextKeyTyped()) {
						// wait
					}
					char move = StdDraw.nextKeyTyped();
					if (move == 'q') {
						// QUIT
						running = false;
					} else if (move == 'h' || move == 'j' || move == 'k' || move == 'l'){
						if (move == 'h' && player[0] == 0 || move == 'l' && player[0] == cols-1) continue;
						// If the move is valid, update the board
						movePlayer(player, cnt, move, rows);
						clearBoard(board, rows, cols);
						board[target[0]][target[1]] = (char) target[2];
						addWalls(board, walls);
						addKeysAndPorts(board, keys, ports, player);
						addSwitchers(board, switchers, move);
						addMovers(board, movers, move, rows, cols);
						running = checkState(board, player, target, true);
						drawBoard(board, player, rows, cols, 0);
					} else if (move == 'a') {
						// Create a duplicate of the board so that the computer can
						// try moves and go back to the original board if it loses
						// All new variables start with 'n' to differentiate them
						char[][] nboard = copy(board, rows, cols);
						int[] nplayer = copy(player);
						int[] ntarget = copy(target);
						int[][] nmovers = copy(movers, 3, num_movers);
						int[][] nwalls = copy(walls, 3, num_walls);
						int[][] nswitchers = copy(switchers, 3, num_switchers);
						int[][] nports = copy(ports, 3, num_ports);
						int[][] nkeys = copy(keys, 3, num_keys);
												
						boolean nrunning = true;
						while (nrunning) {
							// Exit auto mover if the player presses 'q' or 'a' again
							if (StdDraw.hasNextKeyTyped()) {
								char nmove = StdDraw.nextKeyTyped();
								if (nmove == 'q') {
									running = false;
									break; 
								} else if (nmove == 'a') {
									break;
								}
							}
							// else, visit the least visited tile
							char nmove = getNextMove(cnt, nplayer, ntarget, rows, cols);
							if (nmove == 'a')
								break; // leave auto player mode if 'a' is pressed
							clearBoard(nboard, rows, cols);
							movePlayer(nplayer, cnt, nmove, rows);
							nboard[ntarget[0]][ntarget[1]] = (char) ntarget[2];
							addWalls(nboard, nwalls);
							addKeysAndPorts(nboard, nkeys, nports, nplayer);
							addSwitchers(nboard, nswitchers, nmove);
							addMovers(nboard, nmovers, nmove, rows, cols);
							nrunning = checkState(nboard, nplayer, ntarget, false);
							
							if (nrunning) { 
								// Copies the contents of the new 2D arrays to the 
								// original ones if the auto player didn't win/lose
								nboard[nplayer[0]][nplayer[1]] = (char) nplayer[2];
								drawBoard(nboard, nplayer, rows, cols, SHOW_DELAY);
								board = copy(nboard, rows, cols);
								player = copy(nplayer);
								target = copy(ntarget);
								movers = copy(nmovers, 3, num_movers);
								walls = copy(nwalls, 3, num_walls);
								switchers = copy(nswitchers, 3, num_switchers);
								ports = copy(nports, 3, num_ports);
								keys = copy(nkeys, 3, num_keys);
							} else if (nplayer[0] == ntarget[0] && nplayer[1] == ntarget[1]) {
								// Exit if the auto player won
								running = nrunning;
								drawBoard(nboard, nplayer, rows, cols, SHOW_DELAY);
							} else {
								// Undo the move if it resulted in a loss
								nboard = copy(board, rows, cols);
								nplayer = copy(player);
								ntarget = copy(target);
								nmovers = copy(movers, 3, num_movers);
								nwalls = copy(walls, 3, num_walls);
								nswitchers = copy(switchers, 3, num_switchers);
								nports = copy(ports, 3, num_ports);
								nkeys = copy(keys, 3, num_keys);
								nrunning = true;
							}
						}
					}
				}
				StdDraw.close();
			} else if (args.length == 2) {
				// TEXT MODE
				in = new In(args[1]);
				String sline = in.readAll();
				boolean running = true;
				for (int i=0; i<sline.length() && running; ++i) {
					char move = sline.charAt(i);
					if (move == 'x') {
						// QUIT
						running = false;
					} else if (move == 'h' || move == 'j' || move == 'k' || move == 'l') {
						// MOVE THE PLAYER
						// check if the move is valid
						if (move == 'h' && player[0] == 0 || move == 'l' && player[0] == cols-1) continue;
						// Move the player
						movePlayer(player, cnt, move, rows);
						// Update the board
						clearBoard(board, rows, cols);
						board[target[0]][target[1]] = (char) target[2];
						addWalls(board, walls);
						addKeysAndPorts(board, keys, ports, player);
						addSwitchers(board, switchers, move);
						addMovers(board, movers, move, rows, cols);
						// Check if the game should still be running
						running = checkState(board, player, target, true);
					} else {
						StdOut.println("Incorrect move");
						running = false;
					}
				}
				board[player[0]][player[1]] = (char) player[2];
				printBoard(board, rows, cols);
			}
		}
		else {
			StdOut.println("Usage: java SU23688963 [boardfile] [commandfile]");
		}		
	}

}
