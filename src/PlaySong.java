package project4;

public class PlaySong {
		static PlayerThread current = null;
		// Constructor crates and object of PlayerThread
		PlaySong(String filename) {
			current = new PlayerThread(filename);
		}
		// Plays the song
		public void playSong() {
			current.start();
		}
		// Stops the songs
		public static void stopSong() {
			current.pl.close();
		}
		
}
