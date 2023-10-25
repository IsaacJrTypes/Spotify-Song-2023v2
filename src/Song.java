import java.util.Objects;

public record Song(String trackName, String artistsName, String releasedYear, String releasedMonth,
                   String releasedDay, String totalNumberOfStreamsOnSpotify) {


    public Song {
        //checks if there is null entry or has white space
        if (trackName == null || trackName.isBlank() || artistsName == null || artistsName.isBlank() || releasedYear == null || releasedYear.isBlank() || releasedMonth == null || releasedMonth.isBlank() || releasedDay == null || releasedDay.isBlank() || totalNumberOfStreamsOnSpotify == null || totalNumberOfStreamsOnSpotify.isBlank()) {
            throw new IllegalArgumentException("Fields must not be empty");
        }
    }

    @Override
    public String toString() {
        return "trackName: " + this.trackName + ", artistName: " + this.artistsName + ", releasedYear: " + this.releasedYear + ", releasedMonth: " + this.releasedMonth + ", releasedDay: " + this.releasedDay + ", totalNumStreamsSpot: " + this.totalNumberOfStreamsOnSpotify;
    }

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
        //String trackName, String artistsName, String releasedYear, String releasedMonth,
//                   String releasedDay, String totalNumberOfStreamsOnSpotify
        Song argSong = (Song) obj;
        return trackName.equals(argSong.trackName) && artistsName.equals(argSong.artistsName) && releasedYear.equals(argSong.releasedYear) && releasedMonth.equals(argSong.releasedMonth) && releasedDay.equals(argSong.releasedDay) && totalNumberOfStreamsOnSpotify.equals(argSong.totalNumberOfStreamsOnSpotify);
    }

    //Override hashcode
    @Override
    public int hashCode() {
        return Objects.hash(trackName, artistsName, releasedYear, releasedMonth, releasedDay, totalNumberOfStreamsOnSpotify);
    }

    public String getReleasedYear() {
        return this.releasedYear;
    }

    public String getTrackName() {
        return this.trackName;
    }

    public String getArtistName() {
        return this.artistsName;
    }

    public String getReleaseDate() {
        return this.releasedMonth + "/" + this.releasedDay + "/" + this.releasedYear;
    }

    public String getTotalStreams() {
        return this.totalNumberOfStreamsOnSpotify;
    }
}