package abc;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Scanner;

class Song {
    private String title;
    private String artist;
    private int duration; // in seconds
    private boolean playing; // indicates whether the song is currently playing or paused

    public Song(String title, String artist, int duration) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.playing = false; // by default, song is not playing
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void togglePlaying() {
        playing = !playing; // toggles the playing state
    }
}

class TreeNode {
    private String userName;
    private List<Song> songs;
    private List<Song> history; // List to store history of played songs
    private TreeNode leftChild;
    private TreeNode rightChild;
    private int volumeLevel;

    public TreeNode(String userName, Song song) {
        this.userName = userName;
        this.songs = new ArrayList<>();
        this.history = new ArrayList<>(); // Initialize history list
        this.songs.add(song);
        this.leftChild = null;
        this.rightChild = null;
        this.volumeLevel = 50;
    }

    public String getUserName() {
        return userName;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public List<Song> getHistory() {
        return history;
    }

    public TreeNode getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(TreeNode leftChild) {
        this.leftChild = leftChild;
    }

    public TreeNode getRightChild() {
        return rightChild;
    }

    public void setRightChild(TreeNode rightChild) {
        this.rightChild = rightChild;
    }

    public void addSong(Song song) {
        this.songs.add(song);
    }

    public int getVolumeLevel() {
        return volumeLevel;
    }

    public void setVolumeLevel(int volumeLevel) {
        if (volumeLevel >= 0 && volumeLevel <= 100) {
            this.volumeLevel = volumeLevel;
        } else {
            System.out.println("Volume level must be between 0 and 100.");
        }
    }

    // Method to add played song to history
    public void addToHistory(Song song) {
        history.add(song);
    }
}

class BinaryTree {
    private TreeNode root;

    public BinaryTree() {
        root = null;
    }
    public TreeNode getRoot() {
        return root;
    }

    public void insert(String userName, Song song) {
        root = insertRecursive(root, userName, song);
    }

    private TreeNode insertRecursive(TreeNode current, String userName, Song song) {
        if (current == null) {
            return new TreeNode(userName, song);
        }

        if (userName.compareTo(current.getUserName()) < 0) {
            current.setLeftChild(insertRecursive(current.getLeftChild(), userName, song));
        } else if (userName.compareTo(current.getUserName()) > 0) {
            current.setRightChild(insertRecursive(current.getRightChild(), userName, song));
        } else {
            // User already exists, add song to the existing user
            current.addSong(song);
        }

        return current;
    }
    public void adjustVolume(String userName, int volumeLevel) {
        TreeNode userNode = findUser(root, userName);
        if (userNode != null) {
            System.out.println("Adjusting volume control for user: " + userName);
            // Update the volume level for the user's playlist
            userNode.setVolumeLevel(volumeLevel);
            System.out.println("Volume level adjusted to: " + volumeLevel);
        } else {
            System.out.println("User not found.");
        }
    }
    public double calculateCommonSongsPercentage(String userName1, String userName2) {
        TreeNode userNode1 = findUser(root, userName1);
        TreeNode userNode2 = findUser(root, userName2);

        if (userNode1 == null || userNode2 == null) {
            System.out.println("One or both users not found.");
            return 0.0; // Return 0 as an error code
        }

        List<Song> songsUser1 = userNode1.getSongs();
        List<Song> songsUser2 = userNode2.getSongs();

        int commonSongs = countCommonSongs(songsUser1, songsUser2);

        // Calculate the percentage of common songs
        int totalSongsUser1 = songsUser1.size();
        int totalSongsUser2 = songsUser2.size();
        double percentage = (double) commonSongs / Math.min(totalSongsUser1, totalSongsUser2) * 100.0;

        return percentage;
    }

    private int countCommonSongs(List<Song> songsUser1, List<Song> songsUser2) {
        int commonSongs = 0;
        for (Song song1 : songsUser1) {
            for (Song song2 : songsUser2) {
                if (song1.getTitle().equals(song2.getTitle())) {
                    commonSongs++;
                    break; // Break inner loop if a common song is found
                }
            }
        }
        return commonSongs;
    }



    public void playRandomSong(String userName) {
        TreeNode userNode = findUser(root, userName);
        if (userNode != null) {
            playRandomSongRecursive(userNode, new HashSet<>());
        } else {
            System.out.println("User not found");
        }
    }
    
    private void playRandomSongRecursive(TreeNode node, Set<String> playedSongs) {
        if (node != null) {
            List<Song> songs = node.getSongs();
            songs.removeAll(playedSongs);

            if (!songs.isEmpty()) {
                Random random = new Random();
                int randomIndex = random.nextInt(songs.size());
                Song randomSong = songs.get(randomIndex);
                playedSongs.add(randomSong.getTitle());
                System.out.println("Playing: " + randomSong.getTitle());
                
                // Add the played song to history
                node.addToHistory(randomSong);
            } else {
                System.out.println("No more songs available for user: " + node.getUserName());
            }
        }
    }

    public TreeNode findUser(TreeNode current, String userName) {
        if (current == null) {
            return null;
        }
        if (userName.equals(current.getUserName())) {
            return current;
        }
        if (userName.compareTo(current.getUserName()) < 0) {
            return findUser(current.getLeftChild(), userName);
        } else {
            return findUser(current.getRightChild(), userName);
        }
    }
}


public class Main {
    public static void main(String[] args) {
        BinaryTree binaryTree = new BinaryTree();
        Scanner scanner = new Scanner(System.in);

        int choice;
        do {
            System.out.println("Menu:");
            System.out.println("1. Add a song to the Music Player");
            System.out.println("2. Play a random song");
            System.out.println("3. Play/Pause a particular song");
            System.out.println("4. Skip backward");
            System.out.println("5. Skip forward");
            System.out.println("6. Adjust volume control");
            System.out.println("7. History");
            System.out.println("8. Friendship feature");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            
            switch (choice) {
                case 1:
                    // Add a song to the Music Player
                    System.out.println("Enter user name: ");
                    scanner.nextLine(); // Consume newline character
                    String userName = scanner.nextLine();
                    System.out.println("Enter song title: ");
                    String title = scanner.nextLine();
                    System.out.println("Enter artist: ");
                    String artist = scanner.nextLine();
                    System.out.println("Enter duration (in seconds): ");
                    int duration = scanner.nextInt();
                    binaryTree.insert(userName, new Song(title, artist, duration));
                    break;
                case 2:
                    // Play a random song
                    System.out.println("Enter user name: ");
                    scanner.nextLine(); // Consume newline character
                    String user = scanner.nextLine();
                    binaryTree.playRandomSong(user);
                    break;
                case 3:
                    // Play/Pause a particular song
                    System.out.println("Enter user name: ");
                    scanner.nextLine(); // Consume newline character
                    String userPlayPause = scanner.nextLine();
                    System.out.println("Enter song title: ");
                    String songTitle = scanner.nextLine();
                    // Find the user node
                    TreeNode userNodePlayPause = binaryTree.findUser(binaryTree.getRoot(), userPlayPause);
                    if (userNodePlayPause != null) {
                        // Find the song in the user's playlist
                    	List<Song> songList = userNodePlayPause.getSongs();
                    	ArrayList<Song> songs = new ArrayList<>(songList);

                        boolean songFound = false;
                        for (Song song : songs) {
                            if (song.getTitle().equals(songTitle)) {
                                songFound = true;
                                // Toggle the play/pause state of the song
                                song.togglePlaying();
                                // Print the current state
                                System.out.println("Current state: " + (song.isPlaying() ? "Playing" : "Paused"));
                                break;
                            }
                        }
                        if (!songFound) {
                            System.out.println("Song not found in the user's playlist.");
                        }
                    } else {
                        System.out.println("User not found.");
                    }
                    break;

                case 4:
                    // Skip backward
                    System.out.println("Enter user name: ");
                    scanner.nextLine(); // Consume newline character
                    String userBackward = scanner.nextLine();
                    // Find the user node
                    TreeNode userNodeBackward = binaryTree.findUser(binaryTree.getRoot(), userBackward);
                    if (userNodeBackward != null) {
                        System.out.println("Skipping backward for user: " + userBackward);
                        // Implement skip backward logic here
                    } else {
                        System.out.println("User not found.");
                    }
                    break;
                case 5:
                    // Skip forward
                    System.out.println("Enter user name: ");
                    scanner.nextLine(); // Consume newline character
                    String userForward = scanner.nextLine();
                    // Find the user node
                    TreeNode userNodeForward = binaryTree.findUser(binaryTree.getRoot(), userForward);
                    if (userNodeForward != null) {
                        System.out.println("Skipping forward for user: " + userForward);
                        // Implement skip forward logic here
                    } else {
                        System.out.println("User not found.");
                    }
                    break;
                case 6:
                    // Adjust volume control
                    System.out.println("\n-- Adjust volume control --");
                    System.out.print("Enter user name: ");
                    scanner.nextLine(); // Consume newline character
                    String userVolume = scanner.nextLine();
                    System.out.print("Enter volume level (0-100): ");
                    int volumeLevel = scanner.nextInt();
                    // Find the user node and adjust volume
                    binaryTree.adjustVolume(userVolume, volumeLevel);
                    break;
      
                case 7:
                    // View history of played songs
                    System.out.println("\n-- View History of Played Songs --");
                    System.out.print("Enter user name: ");
                    scanner.nextLine(); // Consume newline character
                    String userHistory = scanner.nextLine();
                    TreeNode userNodeHistory = binaryTree.findUser(binaryTree.getRoot(), userHistory);
                    if (userNodeHistory != null) {
                        List<Song> history = userNodeHistory.getHistory();
                        if (!history.isEmpty()) {
                            System.out.println("History of played songs for user " + userHistory + ":");
                            for (Song song : history) {
                                System.out.println("- " + song.getTitle() + " by " + song.getArtist());
                            }
                        } else {
                            System.out.println("No songs played yet for user: " + userHistory);
                        }
                    } else {
                        System.out.println("User not found.");
                    }
                    break;
               
                case 8:
                    // Compare relationship based on common songs percentage
                    System.out.println("\n-- Compare Relationship Based on Common Songs Percentage --");
                    System.out.print("Enter first user name: ");
                    scanner.nextLine(); // Consume newline character
                    String userName1 = scanner.nextLine();
                    System.out.print("Enter second user name: ");
                    String userName2 = scanner.nextLine();
                    double percentage = binaryTree.calculateCommonSongsPercentage(userName1, userName2);
                    System.out.println("Percentage of common songs: " + percentage + "%");
                    
                    // Determine relationship based on percentage
                    if (percentage >= 80.0) {
                        System.out.println("Rating: Very Close Friends");
                    } else if (percentage >= 60.0) {
                        System.out.println("Rating: Good Friends");
                    } else if (percentage >= 40.0) {
                        System.out.println("Rating: Friends");
                    } else if (percentage >= 20.0) {
                        System.out.println("Rating: Acquaintances");
                    } else {
                        System.out.println("Rating: Strangers");
                    }
                    break;
                case 9:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 9.");
            }
        } while (choice != 7);

        scanner.close();
    }
}
