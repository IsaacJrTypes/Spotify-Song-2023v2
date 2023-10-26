/**
 * A class that manages songs and provides various functionalities in
 * handling songs
 */
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

class SongManager implements SongManagerInterface {
    private String[] years;
    private String[] yearCount;
    private Song[][] sortedByYearSongsWithSongIndexes;
    /**
     * Default constructor for the SongManager class
     */
    public SongManager() {
        this.years = null;
        this.yearCount = null;
        this.sortedByYearSongsWithSongIndexes = null;
    }

    /**
     * Retrieves the count of release years
     *
     * @return count of release years
     */
    @Override
    public int getYearCount() {
        return this.years.length;
    }

    /**
     * Retrieves the number of songs in the specified release year (by index)
     *
     * @param yearIndex the index of the release year
     * @return song count in that release year
     */
    @Override
    public int getSongCount(int yearIndex) {
        return Integer.parseInt(yearCount[yearIndex]);
    }

    /**
     * Retrieves the number of songs in all release years
     *
     * @return song count in all release years
     */
    @Override
    public int getSongCount() {
        int sum = 0;
        for(String count: yearCount) {
            sum+= Integer.parseInt(count);
        }
        return sum;
    }

    /**
     * Retrieves the release year at the specified index
     *
     * @param yearIndex index of the desired release year
     * @return release year
     */
    @Override
    public String getYearName(int yearIndex) {
        return years[yearIndex];
    }

    /**
     * Retrieves the number of songs in the specified release year (by name)
     *
     * @param year the release year
     * @return song count in that release year
     */
    @Override
    public int getSongCount(String year) {
        int index = 0;
        for (String count: yearCount) {
            if (years[index].contains(year)) {
                return Integer.parseInt(count);
            }
            index++;
        }
        return 0;
    }

    /**
     * Retrieves the song at the specific release year and song index
     *
     * @param yearIndex release year index
     * @param songIndex song index
     * @return song at that array position
     */
    @Override
        public Song getSong(int yearIndex, int songIndex) {
            if (yearIndex >= 0 && yearIndex < years.length && songIndex >= 0 && songIndex < sortedByYearSongsWithSongIndexes[yearIndex].length) {
                return sortedByYearSongsWithSongIndexes[yearIndex][songIndex];
            } else {
               throw new RuntimeException("Could not find Song in songmanager.java");
            }
        }


    /**
     * Retrieves a copy of the song array for the release year at the specified index
     *
     * @param yearIndex release year index
     * @return copy of song array (not a reference to the internal one)
     */
    @Override
    public Song[] getSongs(int yearIndex) {
        if (yearIndex >= 0 && yearIndex < getYearCount()) {
            return Arrays.copyOf(sortedByYearSongsWithSongIndexes[yearIndex],getSongCount(yearIndex));
        } else {
            return new Song[0];
        }
    }

    /**
     * Retrieves the first release year index associated with the specified song's track name
     *
     * @param trackName the track name to search for
     * @return the first release year index containing the specified song, or -1 if not found
     */
    @Override
    public int findSongYear(String trackName) {
        int index = -1;
        for(int year = 0; year <years.length; year++ ) {
            for(int songIndex = 0; songIndex< getSongCount(year); songIndex++) {
                String trackTitle = sortedByYearSongsWithSongIndexes[year][songIndex].getTrackName();
                if(trackTitle.equals(trackName)){
                    return year;
                }

            }
        }
        return index;
    }

    /**
     * Reads the contents of a given song CSV file and creates an array of Song objects.
     *
     * @param songCSV The FileReader representing the song CSV file.
     * @param songCount The total count of songs in the CSV file.
     * @return An array of Song objects.
     * @throws CsvValidationException If there's an issue validating the CSV.
     * @throws IOException If there's an issue reading the file.
     */
    static Song[] createSongArr(FileReader songCSV, int songCount) throws CsvValidationException, IOException {
        CSVReader songReader = new CSVReader(songCSV);

        Song[] Songs = new Song[songCount];
        int index = 0; // skips header
        String[] line;
        if (songReader.verifyReader()) {
            while (index < songCount) {

                line = songReader.readNext();
                if (line[0].contains("track_name")) {
                    continue;
                }
                String trackName =line[0].trim();
                String artistName =line[1].trim();
                String releasedYear = line[3].trim();
                String releasedMonth =line[4].trim();
                String releasedDay = line[5].trim();
                String totalNumberOfStreamsOnSpotify = commaNumberFormatter(line[8].trim());

                Songs[index] = new Song(trackName, artistName, releasedYear,releasedMonth, releasedDay, totalNumberOfStreamsOnSpotify);

                index++;

            }
            songReader.close();
        }
        return Songs;
    }
        //removes bad encoding
//        private static String sanitize(String input) {
//            return input.replace("�", "");
//        }
    /**
     * Converts the contents of a file into arrays representing years and
     * heir counts.
     *
     * @param countByYearCSV The file containing count by year data.
     * @return An array of two arrays: the first contains years and the
     * second contains their respective counts.
     * @throws FileNotFoundException If the provided file is not found.
     */
    static Object[] convertFileContentToArrays(File countByYearCSV) throws FileNotFoundException {
        Scanner inputStream = new Scanner(countByYearCSV);
        String line;
        String[] years = {};
        String[] yearCount = {};
        int index = 0;
        while (inputStream.hasNextLine()) {
            line = inputStream.nextLine().trim();
            if (!line.contains(",")) {
                int ArrLength = Integer.parseInt(line);
                years = new String[ArrLength];
                yearCount = new String[ArrLength];
            } else {
                String[] data = line.split(",");
                if (isNumeric(data[0]) && isNumeric(data[1])) {
                    years[index] = data[0];
                    yearCount[index] = data[1];
                    index++;
                }
            }
        }
        inputStream.close();
        return new Object[]{years, yearCount};
    }
    /**
     * Checks if a given string can be converted to an integer.
     *
     * @param data The string to check.
     * @return True if the string can be converted to an integer, false otherwise.
     */
    public static boolean isNumeric(String data) {
        try {
            Integer.parseInt(data);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private static String commaNumberFormatter(String number) {
        StringBuilder numWithCommas = new StringBuilder(number);
        for (int i = number.length() - 3; i > 0; i -= 3) {
            numWithCommas.insert(i, ",");
        }
        return numWithCommas.toString();
    }
    /**
     * Sets the array of years
     *
     * @param years Array of years to set
     */
    public void setYearArr(String[] years) {
        this.years = years;
    }
    /**
     * Sets the array of year counts
     *
     * @param yearCount Array of year counts to set
     */
    public void setYearCountArr(String[] yearCount) {
        this.yearCount = yearCount;
    }
    /**
     * Sorts an array of Song objects based on a given category
     *
     * @param songsArr Array of Song objects
     * @param category The category based on which songs should be sorted
     * @return A sorted array of Song objects
     */
    public static Song[] sortSongsBy(Song[] songsArr, String category) {
        switch (category) {
            case "releasedYear":
                Arrays.sort(songsArr, Comparator.comparing(Song -> Song.getReleasedYear()));
                break;
            case "trackName":
                Arrays.sort(songsArr, Comparator.comparing(Song -> Song.getTrackName()));
                break;
            default:
                throw new IllegalArgumentException("Category doesn't exist");
        }
        return songsArr;
    }
    /**
     * Sets the sorted array of songs for each year
     *
     * @param sortedSongsByYear The sorted array of songs to set
     */
    public void setSongsSortedByYearWithSongIndex(Song[] sortedSongsByYear) {
        Song[][] songArraysForReference = new Song[years.length][];
        int songIndex = 0;
        for (int i = 0; i < years.length; i++) {
            int yearSongCount = Integer.parseInt(yearCount[i]);
            songArraysForReference[i] = new Song[yearSongCount];
           // System.out.print("yearI: "+i);
            for (int j = 0; j < yearSongCount; j++) {
                songArraysForReference[i][j] = sortedSongsByYear[songIndex];
                songIndex++;
            }
            //sort by alphabetized
            songArraysForReference[i] = sortSongsBy(songArraysForReference[i],"trackName");

        }
        this.sortedByYearSongsWithSongIndexes = songArraysForReference;
    }
    /**
     * Finds the index of a given year.
     *
     * @param findYear The year to find.
     * @return The index of the year in the array, or -1 if not found.
     */
    public int findYearIndex(String findYear) {
        int index = 0;
        for (String year : years) {
            if (!year.contains(findYear)) {
                index++;
            } else {
                return index;
            }
        }
        return -1;
    }
}