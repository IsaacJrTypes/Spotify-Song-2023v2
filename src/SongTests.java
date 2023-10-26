import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class SongTests {
        @Test
        public void testConstruction() {
            Song song = new Song("Test Track", "Test Artist", "2023", "10", "25", "10000","70");
            assertNotNull(song);

            //Invalid
            assertThrows(IllegalArgumentException.class, () -> new Song(null, "test", "2023", "11", "25", "1000000",null));
            assertThrows(IllegalArgumentException.class, () -> new Song("test", "", "2023", "10", "25", "10000",""));
        }

        @Test
        public void testToString() {
            Song song = new Song("Test Track", "Test Artist", "2023", "10", "25", "10000","70");
            String expected = "trackName: Test Track, artistName: Test Artist, releasedYear: 2023, releasedMonth: 10, releasedDay: 25, totalNumStreamsSpot: 10000, danceability: 70";
            assertEquals(expected, song.toString());
        }

        @Test
        public void testEqualsAndHashCode() {
            Song song1 = new Song("test...", "Mr.Biggie", "2023", "11", "25", "1000000","70");
            Song song2 = new Song("test...", "Mr.Biggie", "2023", "11", "25", "1000000","70");;
            Song song3 = new Song("test", "artist", "2023", "11", "25", "1000000","70");

            assertEquals(song1, song1);
            assertEquals(song1, song2);
            assertEquals(song1.hashCode(), song2.hashCode()); // Equal objects must have equal hash codes

            assertNotEquals(song1, song3);
            assertNotEquals(song1, null);

        }
        @Test
        public void testGetters() {
            Song song = new Song("test...", "Mr.Biggie", "2023", "11", "25", "1000000","70");

            assertEquals("test...", song.getTrackName());
            assertEquals("Mr.Biggie", song.getArtistName());
            assertEquals("2023", song.getReleasedYear());
            assertEquals("11/25/2023", song.getReleaseDate());
            assertEquals("1000000", song.getTotalStreams());
            assertEquals("70", song.getDanceability());
        }
}