package project4;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import com.thoughtworks.xstream.XStream;


public class MPlayerPanel extends JPanel {
	
	private JButton playButton, stopButton, exitButton, loadMp3Button, saveButton, openButton, searchButton, XML;
	private JTable table = null;
	private JTextField entry;
	final static Color COLOR = Color.PINK;
	MPlayerPanel() {
	
	this.setLayout(new BorderLayout());
	
	// buttonPanelTop contains the top row of buttons:
	// load mp3 files, save and open
	JPanel buttonPanelTop = new JPanel();
	buttonPanelTop.setLayout(new GridLayout(1,3));
	loadMp3Button = new JButton("Load mp3");
	saveButton = new JButton("Save");
	openButton = new JButton("Load");
	XML = new JButton("XML");
	XML.setPreferredSize(new Dimension(5, 5));


	loadMp3Button.addActionListener(new MyButtonListener());
	saveButton.addActionListener(new MyButtonListener());
	openButton.addActionListener(new MyButtonListener());
	XML.addActionListener(new MyButtonListener());

	buttonPanelTop.add(loadMp3Button);
	buttonPanelTop.add(saveButton);
	buttonPanelTop.add(openButton);
	buttonPanelTop.add(XML);

	this.add(buttonPanelTop, BorderLayout.NORTH);
		
	
	// Bottom panel with panels: Play, Stop, Exit buttons
	JPanel buttonPanelBottom = new JPanel();
	buttonPanelBottom.setLayout(new GridLayout(1,3));
	playButton = new JButton("Play");
	stopButton = new JButton("Stop");
	exitButton = new JButton("Exit");
	searchButton = new JButton("Search");
		searchButton.setBackground(COLOR);
	entry = new JTextField();

	playButton.addActionListener(new MyButtonListener());
	stopButton.addActionListener(new MyButtonListener());
	exitButton.addActionListener(new MyButtonListener());
	searchButton.addActionListener(new MyButtonListener());

	buttonPanelBottom.add(playButton);
	buttonPanelBottom.add(stopButton);
	buttonPanelBottom.add(exitButton);
	buttonPanelBottom.add(entry);
	buttonPanelBottom.add(searchButton);
	this.add(buttonPanelBottom, BorderLayout.SOUTH);
	
}
	class MyButtonListener implements ActionListener {
		PlaySong songPlayer = null;
		public void actionPerformed(ActionEvent e) {
			// The function that does something whenever each button is pressed
			if (e.getSource() == loadMp3Button) {
				// Creates a file object, a folder where the songs are located
				File myFile = new File("mp3");
				try {
					// Clears the static ArrayList of Songs if it was previously used
					Func.songs.clear();
					// Gets all the songs from the dir using recursion
					Func.recursive(myFile);
					// Sorts the songs within an ArrayList with insertion sort
					Func.insertion(Func.songs);
					System.out.println();
				} catch (CannotReadException | IOException | TagException
						| ReadOnlyFileException | InvalidAudioFrameException e1) {
					e1.printStackTrace();
				} 
				// If table was already made, it changes its values to current attributes from the ArrayList of songs
				if(table != null){
					for(int i = 0; i < Func.songs.size();i++){
						table.setValueAt(Func.songs.get(i).getTitle(), i, 0);
						table.setValueAt(Func.songs.get(i).getArtist(), i, 1);
					}
				
				}
				// Otherwise it creates a multidimensional array of Strings that populates the songs
				// Creates the array of attributes "Title" and "Artist"
				else{
				String[][] tableElems = new String[Func.songs.size()][2];
				String[] columnNames = {"Title", "Artist"};
				for(int i = 0; i < Func.songs.size();i++){
					tableElems[i][0] = Func.songs.get(i).getTitle();
					tableElems[i][1] = Func.songs.get(i).getArtist();
				}
				// you do not need to change the code below
				// Makes the table
				table = new JTable(tableElems, columnNames );
				JScrollPane scrollPane = new JScrollPane( table );
				add( scrollPane, BorderLayout.CENTER );
				}
				updateUI();
			}
			
			else if (e.getSource() == saveButton) {
				try {
					// Creates a text file from ArrayList of Song objects
					Func.libraryType(Func.songs);
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
			else if (e.getSource() == openButton) {
				try {
					// Clears the static ArrayList of Songs if it was previously used
					Func.songs.clear();
					// Reads the .txt file, creates Song objects, and stores them into an ArrayList
					Func.libraryRead();
					// If table was already made, its changes it values to current attributes from the ArrayList of songs
					if(table != null){
						for(int i = 0; i < Func.songs.size();i++){
							table.setValueAt(Func.songs.get(i).getTitle(), i, 0);
							table.setValueAt(Func.songs.get(i).getArtist(), i, 1);
						}
					
					}
					// Otherwise it creates a multidimensional array of Strings that populates the songs
					// Creates the array of attributes "Title" and "Artist"
					else{
					String[][] tableElems = new String[Func.songs.size()][2];
					String[] columnNames = {"Title", "Artist"};
					for(int i = 0; i < Func.songs.size();i++){
						tableElems[i][0] = Func.songs.get(i).getTitle();
						tableElems[i][1] = Func.songs.get(i).getArtist();
					}
					// you do not need to change the code below
					// Makes the table
					table = new JTable(tableElems, columnNames);
					JScrollPane scrollPane = new JScrollPane( table );
					add( scrollPane, BorderLayout.CENTER );
					}
					
					updateUI();
					System.out.println("Songs Added");
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// FILL IN  CODE:  make the calls to other methods/classes, do NOT place all the code here
				// open the file songCatalog and load songs
				// into the arraylist of songs		
			}
			else if (e.getSource() == playButton) {
				// Plays the song from the selected row in GUI
				int selectedSong = table.getSelectedRow();
				Song song = Func.songs.get(selectedSong);
				// If another song is playing, when the new one is played, the previous one will stop
				if(songPlayer != null){
					PlaySong.stopSong();
				}
				songPlayer = new PlaySong(song.getPath());
				songPlayer.playSong();

			}
			// Stops the song
			else if (e.getSource() == stopButton) {
				PlaySong.stopSong();
			}
			else if (e.getSource() == exitButton) {
				// Exit the program
				System.exit(0);				
			}
			else if (e.getSource() == searchButton) {
				 // Gets the input from the entry JTextField and stores it as a CharSequence object
			     CharSequence search = entry.getText();
			     // Searches for the input in the ArrayList of songs
			     Func.search(search);
			     // Updates the table with new songs
			     for(int i = 0; i < Func.songs.size();i++){
						table.setValueAt(Func.songs.get(i).getTitle(), i, 0);
						table.setValueAt(Func.songs.get(i).getArtist(), i, 1);
			     }
			     // Sets all the rows that are not a result of the search to null
				 for(int j = Func.songs.size(); j < table.getRowCount();j++) {
					 	table.setValueAt(null, j, 0);
						table.setValueAt(null, j, 1);
				 }
				 updateUI();
				 
					}
			else if (e.getSource() == XML) {
				try {
				// Saves as .xml file
				Func.xml(Func.songs);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				}
			}
	}
	public static void main(String[] args) {
		JFrame frame = new JFrame ("Mp3 player");
	      frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
	      MPlayerPanel panel  = new MPlayerPanel();
	      panel.setPreferredSize(new Dimension(400,400));
	      frame.getContentPane().add (panel);
	      
	     
	      
	      frame.pack();
	      frame.setVisible(true);
	}
	
	
}
