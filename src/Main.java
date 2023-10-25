import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        initiateSongManager();
    }
    private static void initiateSongManager() {

        try {
            SongManager manager = new SongManager();
            //read files
            File countByYearCSV = new File("count-by-release-year.csv");
            FileReader songCSV = new FileReader("spotify-2023.csv", StandardCharsets.UTF_8);
            // create song data arrays and set
            Object[] data = SongManager.convertFileContentToArrays(countByYearCSV);
            manager.setYearArr((String[]) data[0]); //50 year items
            manager.setYearCountArr((String[]) data[1]); //use to initiate Song[][]


            //create unsorted song record array
            Song[] songsArr = SongManager.createSongArr(songCSV, manager.getSongCount());
            System.out.println("From Maine line 27");

           //sortSongs by year
            Song[] sortedSongArr = SongManager.sortSongsBy(songsArr, "releasedYear");
            manager.setSongsSortedByYearWithSongIndex(sortedSongArr);


        } catch (RuntimeException | CsvValidationException | IOException e1) {
            System.err.println(e1.getMessage());
            e1.fillInStackTrace();
        }
    }

}