package Applet;

import GameApplication.*;
import java.awt.Color;
import java.awt.Container;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import javax.swing.JApplet;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

//1 Player 1 RED
//2 Player 2 BLACK
//3 Player 3 WHITE
//Snake's Head DARK_GRAY
//Snake's Tail CYAN
//Ladder's Start ORANGE
//Ladder's End YELLOW

public class SnakeAndLadderApplet extends JApplet {
	
	public JPanel contentPane;
	private SettingsFrame settingsFrame;
	private players nameFrame;
	public static GridPanel gridPanel;
	public SidePanel sidePanel;
	public JButton btn;
	public JLabel lblMessage, lblTurn, lblGot;

	public static int snakes[][], ladders[][];

	public String name1, name2, name3;
	public int N, currentPlayer;
	public int pos1, pos2, pos3;
	private String winner;
	private int winnersteps;
	private Random generator;
	public static int dimension = 8;
	public static int Nsnakes, Nladders;
	private int steps1 = 0, steps2 = 0, steps3 = 0;

	final static Color snakehead = Color.DARK_GRAY, snaketail = Color.CYAN,
			ladderstart = Color.MAGENTA, ladderend = Color.PINK;
	static Color oddCell = Color.GREEN, evenCell = Color.BLUE;

	/**
	 * Launch the application.
	 */
	public void init() {
		setForeground(Color.BLACK);
		setBackground(Color.YELLOW);
		setSize(800, 600);

		settingsFrame = new SettingsFrame(this);
		generator = new Random();
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("Menu");
		menuBar.add(mnNewMenu);

		JMenuItem mntmNewGame = new JMenuItem("New Game");
		mntmNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reinitialise();
			}
		});
		mnNewMenu.add(mntmNewGame);

		JMenuItem mntmSettings = new JMenuItem("Settings");
		mntmSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				settingsFrame.setVisible(true);
			}
		});
		mnNewMenu.add(mntmSettings);

		JMenu mnNewMenu_1 = new JMenu("Help");
		menuBar.add(mnNewMenu_1);

		JMenuItem mntmInstructions = new JMenuItem("Instructions");
		mntmInstructions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			Instructions is = new Instructions();
			is.setVisible(true);
			}
		});
		mnNewMenu_1.add(mntmInstructions);
		contentPane.setLayout(null);
		
		gridPanel = new GridPanel(dimension);
		gridPanel.setBounds(0, 0, 508, 551);
		contentPane.add(gridPanel);

		btn = new JButton("Roll Dice");
		btn.setBounds(585, 482, 128, 33);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				diceRolled();
			}
		});
		btn.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		contentPane.add(btn);

		sidePanel = new SidePanel();
		sidePanel.setBounds(528, 0, 225, 284);
		contentPane.add(sidePanel);

		lblMessage = new JLabel("Snake at Position 45 got you!!", JLabel.CENTER);
		lblMessage.setBounds(518, 295, 285, 33);
		lblMessage.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 12));
		contentPane.add(lblMessage);

		lblTurn = new JLabel("Player's Turn", JLabel.CENTER);
		lblTurn.setBounds(579, 364, 144, 29);
		lblTurn.setFont(new Font("Times New Roman", Font.BOLD, 16));
		contentPane.add(lblTurn);

		lblGot = new JLabel("", JLabel.CENTER);
		lblGot.setBounds(585, 425, 145, 17);
		lblGot.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 16));
		contentPane.add(lblGot);
		
		setVisible(false);
		nameFrame = new players(this);
		nameFrame.setVisible(true);	
		
		setInitial();
				
	}

	public void diceRolled() {
		lblMessage.setText("");
		int total = dimension * dimension;
		int jump = generator.nextInt(6) + 1;// 1 to 6
		lblGot.setText("You got " + jump);

		if (currentPlayer == 1) {
			steps1++;
			sidePanel.setScore1(steps1);
			int newposition = pos1 + jump;
			if (newposition == total) {
				JOptionPane.showMessageDialog(null, "Congratulations " + name1
						+ "!! You have won the game in  " + steps1 + " steps");
				btn.setEnabled(false);
				winner = name1;
				winnersteps = steps1;
				scoring();
			}

			for (int i = 0; i < Nsnakes; ++i) {
				if (snakes[i][0] == newposition) {
					lblMessage.setText("Snake at board position " + newposition
							+ " got you!!");
					newposition = snakes[i][1];
					break;
				}
			}
			for (int i = 0; i < Nladders; ++i) {
				if (ladders[i][0] == newposition) {
					lblMessage.setText("You are up through ladder at position "
							+ newposition + " !");
					newposition = ladders[i][1];
					if (newposition == total) {// Ladder directly to win.
						JOptionPane.showMessageDialog(null, "Congratulations "
								+ name1 + "!! You have won the game in  "
								+ steps1 + " steps");
						btn.setEnabled(false);
						winner = name1;
						winnersteps = steps1;
						scoring();
					}
					break;
				}
			}

            if (newposition > total) {
				lblMessage.setText("You need " + (total - pos1) + " to win!!");
				newposition = pos1;
			}

			gridPanel.grid[newposition].setBackground(Color.RED);
			if (newposition != pos1 && pos2 != pos1 && pos3 != pos1)// Get the
																	// original
																	// colour of
																	// the cell
			{
				if (pos1 % 2 == 1)
					gridPanel.grid[pos1].setBackground(oddCell);
				else
					gridPanel.grid[pos1].setBackground(evenCell);
			} else if (N == 3 && pos1 == pos3)
				gridPanel.grid[pos1].setBackground(Color.WHITE);
			else if (N == 2 && pos2 == pos1)
				gridPanel.grid[pos1].setBackground(Color.BLACK);

			pos1 = newposition;
			lblTurn.setText(name2 + "\'s turn");
			currentPlayer = 2;

		}

		else if (currentPlayer == 2) {
			steps2++;
			sidePanel.setScore2(steps2);
			int newposition = pos2 + jump;
			if (newposition == total) {
				JOptionPane.showMessageDialog(null, "Congratulations " + name2
						+ "!! You have won the game in  " + steps2 + " steps");
				btn.setEnabled(false);
				winner = name2;
				winnersteps = steps2;
				scoring();
			}

			for (int i = 0; i < Nsnakes; ++i) {
				if (snakes[i][0] == newposition) {
					lblMessage.setText("Snake at board position " + newposition
							+ " got you!!");
					newposition = snakes[i][1];
					break;
				}
			}
			for (int i = 0; i < Nladders; ++i) {
				if (ladders[i][0] == newposition) {
					lblMessage.setText("You are up through ladder at position "
							+ newposition + " !");
					newposition = ladders[i][1];
					if (newposition == total) {// Ladder directly to win.
						JOptionPane.showMessageDialog(null, "Congratulations "
								+ name2 + "!! You have won the game in  "
								+ steps2 + " steps");
						btn.setEnabled(false);
						winner = name2;
						winnersteps = steps2;
						scoring();
					}
					break;
				}
			}


            if (newposition > total) {
				lblMessage.setText("You need " + (total - pos2) + " to win!!");
				newposition = pos2;
			}

			gridPanel.grid[newposition].setBackground(Color.BLACK);
			if ((newposition != pos2 && pos1 != pos2)
					|| (N == 3 && pos3 == pos2))// Get the original colour of
												// the cell
			{
				if (pos2 % 2 == 1)
					gridPanel.grid[pos2].setBackground(oddCell);
				else
					gridPanel.grid[pos2].setBackground(evenCell);
			} else if (N == 3 && pos3 == pos2)
				gridPanel.grid[pos2].setBackground(Color.WHITE);
			else if (N == 2 && pos1 == pos2)
				gridPanel.grid[pos2].setBackground(Color.RED);

			pos2 = newposition;

			if (N == 3) {
				lblTurn.setText(name3 + "\'s turn");
				currentPlayer = 3;
			} else {
				lblTurn.setText(name1 + "\'s turn");
				currentPlayer = 1;
			}

		}

		else {
			steps3++;
			sidePanel.setScore3(steps3);
			int newposition = pos3 + jump;
			if (newposition == total) {
				JOptionPane.showMessageDialog(null, "Congratulations " + name3
						+ "!! You have won the game in  " + steps3 + " steps");
				btn.setEnabled(false);
				winner = name3;
				winnersteps = steps3;
				scoring();
			}

			for (int i = 0; i < Nsnakes; ++i) {
				if (snakes[i][0] == newposition) {
					lblMessage.setText("Snake at board position " + newposition
							+ " got you!!");
					newposition = snakes[i][1];
					break;
				}
			}
			for (int i = 0; i < Nladders; ++i) {
				if (ladders[i][0] == newposition) {
					lblMessage.setText("You are up through ladder at position "
							+ newposition + " !");
					newposition = ladders[i][1];
					if (newposition == total) {// Ladder directly to win.
						JOptionPane.showMessageDialog(null, "Congratulations "
								+ name3 + "!! You have won the game in  "
								+ steps3 + " steps");
						btn.setEnabled(false);
						winner = name3;
						winnersteps = steps3;
						scoring();
					}
					break;
				}
			}

            if (newposition > total) {
				lblMessage.setText("You need " + (total - pos3) + " to win!!");
				newposition = pos3;
			}

			gridPanel.grid[newposition].setBackground(Color.WHITE);
			if (newposition != pos3 && pos2 != pos3 && pos1 != pos3)// Get the
																	// original
																	// colour of
																	// the cell
			{
				if (pos3 % 2 == 1)
					gridPanel.grid[pos3].setBackground(oddCell);
				else
					gridPanel.grid[pos3].setBackground(evenCell);
			} else if (pos2 == pos3)
				gridPanel.grid[pos3].setBackground(Color.BLACK);
			else if (pos1 == pos3)
				gridPanel.grid[pos3].setBackground(Color.RED);

			pos3 = newposition;
			lblTurn.setText(name1 + "\'s turn");
			currentPlayer = 1;

		}

	}

	public void reinitialise() {
		// TODO Auto-generated method stub

		setVisible(false);
		nameFrame = new players(this);
		nameFrame.setVisible(true);
		
		pos1=pos2=pos3=0;
		steps1=steps2=steps3=0;

		gridPanel.setVisible(false);
		gridPanel = new GridPanel(dimension);
		gridPanel.setBounds(0, 0, 508, 551);
		contentPane.add(gridPanel);

		setPositions();

		sidePanel.setVisible(false);
		sidePanel = new SidePanel();
		sidePanel.setBounds(528, 0, 225, 284);
		contentPane.add(sidePanel);

		lblMessage.setText("");
		lblTurn.setText("Player 1's turn");
		btn.setEnabled(true);

	}

	public static void setInitial() {
		// TODO Auto-generated method stub
		snakes = new int[144][2];// Maximum no. of snakes
		ladders = new int[144][2];// Maximum no. of ladders

		Nsnakes = 2;
		Nladders = 2;
		// Setting default position of snakes and ladders
		snakes[0][0] = 62;
		snakes[0][1] = 7;
		snakes[1][0] = 37;
		snakes[1][1] = 11;

		ladders[0][0] = 9;
		ladders[0][1] = 59;
		ladders[1][0] = 27;
		ladders[1][1] = 42;

		setPositions();

	}

	public static void setPositions() {

		for (int i = 0; i < Nsnakes; ++i) {
			gridPanel.grid[snakes[i][0]].setBackground(snakehead);
			gridPanel.grid[snakes[i][1]].setBackground(snaketail);

		}
		for (int i = 0; i < Nladders; ++i) {
			gridPanel.grid[ladders[i][0]].setBackground(ladderstart);
			gridPanel.grid[ladders[i][1]].setBackground(ladderend);

		}

	}

    public void scoring() {
		FileWriter fw;
		FileReader fr;
		BufferedWriter bw;
		BufferedReader br;

		File f = new File("Scores.txt");
		File t = new File("temp.txt");

		try {
			if (!f.exists())
				f.createNewFile();

			if (!t.exists())
				t.createNewFile();

			fr = new FileReader(f);
			br = new BufferedReader(fr);

			fw = new FileWriter(t);
			bw = new BufferedWriter(fw);

			String str1 = "", str2 = "";
			while ((str1 = br.readLine()) != null) {
				str2 = br.readLine();
				bw.write(str1);
				bw.newLine();
				bw.write(str2);
				bw.newLine();
			}
			br.close();
			bw.close();

			fr = new FileReader(t);
			br = new BufferedReader(fr);

			fw = new FileWriter(f);
			bw = new BufferedWriter(fw);
			int flag = 0, max = 0;

			while ((str1 = br.readLine()) != null) {
				str2 = br.readLine();
				int score = Integer.valueOf(str2);
				if (str1.equals(winner)) {
					if (score > winnersteps) {
						max = score;
						flag = 1;// Existing player with higher score
						continue;
					} else
						flag = 2;// Existing player with not higher score
				}
				bw.write(str1);
				bw.newLine();
				bw.write(str2);
				bw.newLine();
			}

			if (flag != 2) {
				bw.write(winner);
				bw.newLine();
				bw.write(""+winnersteps);
				bw.newLine();
			}

			if (flag == 1) {
				JOptionPane.showMessageDialog(null, "Congratulations!! "
						+ winner
						+ " You have bettered your past highest score of "
						+ max);
			}
			br.close();
			bw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}