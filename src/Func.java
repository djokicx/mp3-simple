package project4;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import com.thoughtworks.xstream.XStream;


public class Func {
	// Static ArrayList of Song objects that is user throughout the classes
static ArrayList<Song> songs = new ArrayList<Song>();

// Reading the library - Load Library button
public static void libraryRead() throws FileNotFoundException {
	// JFileChooser that sets a filter only for .txt files
	JFileChooser chooser = new JFileChooser();
	FileNameExtensionFilter filter = new FileNameExtensionFilter(
	    "(.txt) Text Files", "txt");
	chooser.setFileFilter(filter);
	// Opens up a dialog from which a user can choose a .txt file he/she wants to read
	int returnVal = chooser.showOpenDialog(null);
	// Displays the message when file is chosen
	if(returnVal == JFileChooser.APPROVE_OPTION) {
	   System.out.println("You chose to open this file: " +
	        chooser.getSelectedFile().getName());
	}
	// Gets the absolute path of the file and stores it in a file object
	String APath = chooser.getSelectedFile().getAbsolutePath();
	File text = new File(APath);
	Scanner file = new Scanner(text);
	// Extracts the first line since it is the number of songs
	file.nextLine();
	// Following the pattern where 4 lines - 1 empty line is a song it gets the parameters from the .txt file
	// It stores it in a ArrayList as Song object
		while (file.hasNext()) {
			String title = file.nextLine();
			String artist = file.nextLine();
			String album = file.nextLine();
			String path = file.nextLine();
			file.nextLine();
			songs.add(new Song(title, artist, album, path));
		}
}
public static void recursive(File dir) throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
	// Goes through folders in a recursive manner until it discovers a file
	if(dir.isFile()){ 
		// Stores the file name in a String
		String filename = dir.getName(); 
		// If it contains an .mp3 extension, using JTagger it extracts few attributes from the songs
		// It adds it to an ArrayList, creating a new Song object
		if(filename.length() > 6) { 
			String extension = filename.substring(filename.length()-3, filename.length()); 
			if(extension.contains("mp3")) {
				AudioFile f = AudioFileIO.read(dir);
				Tag tag = f.getTag();
				String artist = tag.getFirst(FieldKey.ALBUM_ARTIST);
				String album = tag.getFirst(FieldKey.ALBUM);
				String title = tag.getFirst(FieldKey.TITLE);
				String path = dir.getPath();
				songs.add(new Song(title, artist, album, path));
				
			}
			
		}
		
		return;
	}
	// Creates an array in order to be able to go back and traverse through the entire folder with recursion
	File [] array = dir.listFiles(); 
		for(File temp : array) { 
			recursive(temp); 
		}
	
}
// Method for Save Library Button that takes and ArrayList of Song objects as a parameter
public static void libraryType(ArrayList<Song> list) throws IOException{
	final int MAX = list.size();
	// JFileChooser that sets a filter only for .txt files
	JFileChooser chooser = new JFileChooser();
	FileNameExtensionFilter filter = new FileNameExtensionFilter(
	"(.txt) Text Files", "txt");
	chooser.setFileFilter(filter);
	// Opens up a dialog from where user can choose a dir to save his file
	int returnVal = chooser.showSaveDialog(null);
	// When user chooses the destination, any filename he types will add a .txt extension automatically
	// This way the computer will know with which application to open the file
	if(returnVal == JFileChooser.APPROVE_OPTION) {
		String suffix = ".txt";
		File file = chooser.getSelectedFile();
		file = new File(chooser.getSelectedFile() + suffix);
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		PrintWriter outFile = new PrintWriter(bw);
		// First line is the number of songs
		outFile.println(list.size());
			// Following the pattern 4 lines of parameters - 1 empty line it writes the text file 
			// Traverses through the ArrayList
			for(int i = 0; i < MAX; i++){
				outFile.println(list.get(i).title);
				outFile.println(list.get(i).artist);
				outFile.println(list.get(i).album);
				outFile.println(list.get(i).path);
				outFile.println();
		}
	// End of typing the file; displays the message
	outFile.close();
	System.out.println ("Output file has been created: " + file);
	}
	
}
// Insertion Sort - used for every list before displayed with GUI
// Each iteration, insertion sort removes one element from the input data finds the location it belongs within the sorted list, and inserts it there. 
// It repeats until no input elements remain.
public static void insertion(ArrayList<Song> list){
	      int i, j;
	      for (i = 1; i < list.size(); i++) {
	            Song song = list.get(i);
	            j = i;
	            while (j > 0 && list.get(j - 1).compareTo(song) > 0) {
	                  list.set(j, list.get(j-1));
	                  j--;
	            }
	            list.set(j, song);
	      }
	
	
}
// Search method
// User CharSequence as a parameter, which is user input
public static void search(CharSequence search){
	// Crates a temporary ArrayList where all the songs that match the user search will be stored
	ArrayList<Song> temp = new ArrayList<Song>();
	// Traverses through the ArrayList of existing songs and checks if the artist or title contain searched string as a substring
	for(int i = 0;i < Func.songs.size(); i++) {
		if(songs.get(i).getArtist().contains(search) || songs.get(i).getTitle().contains(search)){
			// Adds the song to a temporary Array List
			temp.add(songs.get(i));
		}
	}
	// Initial ArraList of songs is set to the temporary one; songs that satisfy search parameters
	songs = temp;
}
public static void xml(ArrayList<Song> list) throws IOException{
		final int MAX = list.size();
		// JFileChooser that sets a filter only for .xml files
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		"(.xml) XML Files", "xml");
		chooser.setFileFilter(filter);
		// Opens up a dialog from where user can choose a dir to save his file
		int returnVal = chooser.showSaveDialog(null);
		// When user chooses the destination, any filename he types will add a .xml extension automatically
		// This way the computer will know with which application to open the file
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			String suffix = ".xml";
			File file = chooser.getSelectedFile();
			file = new File(chooser.getSelectedFile() + suffix);
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			PrintWriter outFile = new PrintWriter(bw);
				for(int i = 0; i < MAX; i++){
					XStream xstream = new XStream();
					Song song = songs.get(i);
					String xml = xstream.toXML(song);
					// Test
					System.out.print(xml);
					outFile.println(xml);
					outFile.println();
			}
		// End of typing the file; displays the message
		outFile.close();
		System.out.println ("Output file has been created: " + file);
		}
}
}


