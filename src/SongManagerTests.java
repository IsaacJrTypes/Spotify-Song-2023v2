import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;

public class SongManagerTests {

    private SongManager songManager;
    private Song[] mockSongs;

    @BeforeEach
    public void setUp() {
        songManager = new SongManager();
        // Mock data for songs
        mockSongs = new Song[]{
                new Song("song1", "artist1", "1990", "1", "1", "1000"),
                new Song("song2", "artist2", "2000", "2", "2", "2000"),
                new Song("song3", "artist3", "2001", "3", "3", "3000")
        };
        songManager.setYearArr(new String[]{"1990","2000","2001"});
        songManager.setYearCountArr(new String[]{"1","1","1"});
        songManager.setSongsSortedByYearWithSongIndex(mockSongs);
    }

    @Test
    public void testGetYearCount() {
        songManager.setYearArr(new String[]{"1990", "2000", "2001"});
        assertEquals(3, songManager.getYearCount());
    }

    @Test
    public void testGetSongCountByIndex() {
        songManager.setYearCountArr(new String[]{"10", "20", "30"});
        assertEquals(10, songManager.getSongCount(0));
    }

    @Test
    public void testGetSongCountByYear() {
        songManager.setYearArr(new String[]{"1990", "2000", "2001"});
        songManager.setYearCountArr(new String[]{"10", "20", "30"});
        assertEquals(20, songManager.getSongCount("2000"));
    }

    @Test
    public void testIsNumeric() {
        assertTrue(SongManager.isNumeric("123"));
        assertFalse(SongManager.isNumeric("12a"));
    }

    @Test
    public void testCommaNumberFormatter() {
        assertEquals("1,234,567", SongManager.commaNumberFormatter("1234567"));
    }

    @Test
    public void testConvertFileContentToArrays() throws Exception {
        // Provide a path to a test CSV file
        File countByYearCSV = new File("count-by-release-year.csv");
        Object[] result = SongManager.convertFileContentToArrays(countByYearCSV);
        assertTrue(result[0] instanceof String[]);
        assertTrue(result[1] instanceof String[]);
    }

    @Test
    public void testGetSong() {
        songManager.setYearArr(new String[]{"1990", "2000", "2001"});
        songManager.setSongsSortedByYearWithSongIndex(mockSongs);

        Song result = songManager.getSong(0, 0);
        assertEquals("song1", result.getTrackName());
    }

    @Test
    public void testGetSongs() {
        songManager.setYearArr(new String[]{"1990", "2000", "2001"});
        songManager.setSongsSortedByYearWithSongIndex(mockSongs);

        Song[] result = songManager.getSongs(1);
        assertEquals("song2", result[0].getTrackName());
    }

    @Test
    public void testSortSongsByReleasedYear() {
        Song[] sortedSongs = SongManager.sortSongsBy(mockSongs, "releasedYear");

        assertEquals("song1", sortedSongs[0].getTrackName());
        assertEquals("song2", sortedSongs[1].getTrackName());
        assertEquals("song3", sortedSongs[2].getTrackName());
    }

    @Test
    public void testSortSongsByTrackName() {
        Song[] sortedSongs = SongManager.sortSongsBy(mockSongs, "trackName");

        assertEquals("song1", sortedSongs[0].getTrackName());
        assertEquals("song2", sortedSongs[1].getTrackName());
        assertEquals("song3", sortedSongs[2].getTrackName());
    }

    @Test
    public void testFindYearIndex() {
        songManager.setYearArr(new String[]{"1990", "2000", "2001"});

        int yearIndex = songManager.findYearIndex("2001");
        assertEquals(2, yearIndex);
    }
}