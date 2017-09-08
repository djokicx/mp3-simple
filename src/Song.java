package project4;

import java.util.ArrayList;

// Song class that stores information from the song file
public class Song implements Comparable<Song>{
	String title, artist, album, path;

// Constructor
	public Song(String title, String artist, String album, String path) {
		this.title = title;
		this.artist = artist;
		this.album = album;
		this.path = path;
	}
// Getters and Setters
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "Song [title=" + title + ", artist=" + artist + ", album="
				+ album + ", path=" + path + "]";
	}
	// Compare to method since this class implements Comparable<Song>
	@Override
	public int compareTo(Song s) {
		int last = this.title.compareTo(s.title);
		return last;
	}
	
	

}
