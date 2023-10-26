/**
 * Creates a record of a song. Includes track name, artist name, release date,
 * and the total number of streams on Spotify
 */
import java.util.Objects;
/**
 * Initializes a new instance of the Song record
 * The constructor checks if any provided string is null or blank and throws an exception
 *
 */
public record Song(String trackName, String artistsName, String releasedYear, String releasedMonth,
                   String releasedDay, String totalNumberOfStreamsOnSpotify, String danceability) {


    public Song {
        //checks if there is null entry or has white space
        if (trackName == null || trackName.isBlank() || artistsName == null || artistsName.isBlank() || releasedYear == null || releasedYear.isBlank() || releasedMonth == null || releasedMonth.isBlank() || releasedDay == null || releasedDay.isBlank() || totalNumberOfStreamsOnSpotify == null || totalNumberOfStreamsOnSpotify.isBlank() || danceability == null || danceability.isBlank()){
            throw new IllegalArgumentException("Fields must not be empty");
        }
    }
    /**
     * Represents the song as a string, showing all of its details.
     *
     * @return the string representation of the song.
     */
    @Override
    public String toString() {
        return "trackName: " + this.trackName + ", artistName: " + this.artistsName + ", releasedYear: " + this.releasedYear + ", releasedMonth: " + this.releasedMonth + ", releasedDay: " + this.releasedDay + ", totalNumStreamsSpot: " + this.totalNumberOfStreamsOnSpotify + this.danceability;
    }
    /**
     * Checks the equality of this song with another object.
     *
     * @param obj the object to be checked for equality with this song.
     * @return True or false if the other object is also a song and has the same details as this song
     */
    @Override
    public boolean equals(Object obj) {
        //checks if same reference obj
        if (this == obj) {
            return true;
        }
        //checks if empty or not same class -> false
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Song argSong = (Song) obj;
        return trackName.equals(argSong.trackName) &&
                artistsName.equals(argSong.artistsName) &&
                releasedYear.equals(argSong.releasedYear) &&
                releasedMonth.equals(argSong.releasedMonth) &&
                releasedDay.equals(argSong.releasedDay) &&
                totalNumberOfStreamsOnSpotify.equals(argSong.totalNumberOfStreamsOnSpotify) &&
                danceability.equals(argSong.danceability);
    }
    /**
     * Computes the hash code for this song based on string values
     *
     * @return the hash code for this song.
     */
    //Override hashcode
    @Override
    public int hashCode() {
        return Objects.hash(trackName, artistsName, releasedYear, releasedMonth, releasedDay, totalNumberOfStreamsOnSpotify,danceability);
    }
    /**
     * Retrieves the year the song was released
     *
     * @return the released year of the song
     */
    public String getReleasedYear() {
        return this.releasedYear;
    }

    /**
     * Retrieves the track name of the song
     *
     * @return the track name
     */
    public String getTrackName() {
        return this.trackName;
    }

    /**
     * Retrieves the artist's name
     *
     * @return the artist's name
     */
    public String getArtistName() {
        return this.artistsName;
    }

    /**
     * Retrieves the full release date of the song in the format MM/DD/YYYY
     *
     * @return the release date of the song.
     */
    public String getReleaseDate() {
        return this.releasedMonth + "/" + this.releasedDay + "/" + this.releasedYear;
    }

    /**
     * Retrieves the total number of streams of the song on spotify
     *
     * @return the total number of streams on Spotify.
     */
    public String getTotalStreams() {
        return this.totalNumberOfStreamsOnSpotify;
    }
    /**
     * Retrieves the danceability percentage of the song.
     *
     * @return the danceability of the song.
     */
     public String getDanceability() {
        return this.danceability;
    }
}