/**
 * GUIFrame class responsible for creating and managing the main GUI frame for the popular songs of 2023
 */
import com.opencsv.exceptions.CsvValidationException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class GUIFrame {
    // create the main JFrame
    private SongManager manager = null;
    private boolean loadedData = false;
    private int songTraverser = 0;

        /**
        * Constructor responsible for initializing the main GUI frame and its
         * components
         */
    public GUIFrame() {
        try {
            JFrame mainFrame = new JFrame("Popular Songs of 2023");
            mainFrame.setName("mainFrame");
            mainFrame.setSize(500, 575);
            // create a load, prev, next buttons
            JPanel controlPanel = new JPanel(new GridLayout(1, 3, 0, 0));
            controlPanel.setBounds(0, 50, 500, 40);
            controlPanel.setBackground(Color.cyan);
            mainFrame.add(controlPanel);
            String[] controlBtns = {"Load", "Prev", "Next"};

            //Create buttons
            JButton loadBtn = createButton(controlPanel, controlBtns[0], true);
            JButton prevBtn = createButton(controlPanel, controlBtns[1], false);
            JButton nextBtn = createButton(controlPanel, controlBtns[2], false);

            //create label and text field for song info
            JPanel songInfoPanel = new JPanel(new GridLayout(4, 2));
            songInfoPanel.setBounds(0, 200, 450, 270);
            songInfoPanel.setBackground(Color.pink);
            mainFrame.add(songInfoPanel);

            //add label and textfield for songs
            JTextField trackPanel = createLabelTextField(songInfoPanel, "Track Name:", "trackName");
            JTextField artistPanel = createLabelTextField(songInfoPanel, "Artist(s):", "artistName");
            JTextField datePanel = createLabelTextField(songInfoPanel, "Released Date:", "releasedDate");
            JTextField streamsPanel = createLabelTextField(songInfoPanel, "Total Streams:", "totalStreams");


            //Create combo box with info panel
            JPanel yearInfoPanel = new JPanel(new GridLayout(1, 2, 0, 0));
            yearInfoPanel.setBounds(0, 100, 500, 40);
            yearInfoPanel.setBackground(Color.gray);
            mainFrame.add(yearInfoPanel);

            JComboBox<String> comboBox = new JComboBox<>();
            yearInfoPanel.add(comboBox);
            comboBox.setEnabled(false);


            JLabel songStats = new JLabel("");
            songStats.setName("songStats");
            yearInfoPanel.add(songStats);
            songStats.setEnabled(true);

            loadBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    initiateSongManager();
                    loadedData = true;
                    loadBtn.setEnabled(false);
                    prevBtn.setEnabled(true);
                    nextBtn.setEnabled(true);
                    comboBox.setEnabled(true);
                    //load year data
                    if (manager != null && loadedData) {
                        for (int i = 0; i < manager.getYearCount(); i++) {
                            comboBox.addItem(manager.getYearName(i));
                        }
                    }
                    songStats.setEnabled(true);

                    songTraverser = 0;



                }
            });

            comboBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //System.out.println(e.toString());
                    Object selectedItem = comboBox.getSelectedItem();
                    if (selectedItem != null) {
                        String selectedYear = selectedItem.toString();
                        int indexOfYear = manager.findYearIndex(selectedYear);
                        Song[] copySongsArr = manager.getSongs(indexOfYear);
                        songTraverser = 0;

                        //get and set Label, title
                      JLabel songInfoLabel =  getLabelByName(yearInfoPanel,"songStats");
                        if (songInfoLabel != null) {
                            int songsInYear = manager.getSongCount(selectedYear);
                            int totalSongs = manager.getSongCount();
                            double percentage = roundToHundredths(((double) songsInYear / totalSongs));
                            //set %, song in year, total songs
                            songInfoLabel.setText(percentage+"% | "+songsInYear +" of "+ totalSongs+ " total songs");

                            int songNum = songTraverser+1;
                            mainFrame.setTitle("Songs | "+songNum+ " of "+songsInYear+" songs");
                        } else {
                            throw new RuntimeException("Can't find label");
                        }


                        //setPanels
                        trackPanel.setText(copySongsArr[songTraverser].getTrackName());
                        artistPanel.setText(copySongsArr[songTraverser].getArtistName());
                        datePanel.setText(copySongsArr[songTraverser].getReleaseDate());
                        streamsPanel.setText(copySongsArr[songTraverser].getTotalStreams());

                    }

                }
            });

            nextBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //System.out.println(e.getActionCommand());
                    Object selectedItem = comboBox.getSelectedItem();
                    if (selectedItem != null) {
                        String selectedYear = selectedItem.toString();
                        System.out.println(selectedYear);
                        int yearIndex = manager.findYearIndex(selectedYear);
                        //System.out.println("year: " + yearIndex + ", getSongCount" + manager.getSongCount(yearIndex));
                        int endOfSongArray = manager.getSongCount(yearIndex) - 1;
                        if (songTraverser >= 0 && songTraverser < endOfSongArray) {
                            songTraverser++;
                            System.out.println(songTraverser);
                            //setPanels
                            trackPanel.setText(manager.getSong(yearIndex,songTraverser).getTrackName());
                            artistPanel.setText(manager.getSong(yearIndex,songTraverser).getArtistName());
                            datePanel.setText(manager.getSong(yearIndex,songTraverser).getReleaseDate());
                            streamsPanel.setText(manager.getSong(yearIndex,songTraverser).getTotalStreams());
                        }
                        //get and set Label, title
                        JLabel songInfoLabel =  getLabelByName(yearInfoPanel,"songStats");
                        if (songInfoLabel != null) {
                            int songsInYear = manager.getSongCount(selectedYear);
                            int totalSongs = manager.getSongCount();
                            double percentage = roundToHundredths(((double) songsInYear / totalSongs));
                            //set %, song in year, total songs
                            songInfoLabel.setText(percentage+"% | "+songsInYear +" of "+ totalSongs+ " total songs");

                            int songNum = songTraverser+1;
                            mainFrame.setTitle("Songs | "+songNum+ " of "+songsInYear+" songs");
                        } else {
                            throw new RuntimeException("Can't find label");
                        }
                    }
                }
            });


             prevBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //System.out.println(e.getActionCommand());
                    Object selectedItem = comboBox.getSelectedItem();
                    if (selectedItem != null) {
                        String selectedYear = selectedItem.toString();
                        System.out.println(selectedYear);
                        int yearIndex = manager.findYearIndex(selectedYear);

                        if (songTraverser > 0 ) {
                            songTraverser--;
                            System.out.println(songTraverser);
                            //setPanels
                            trackPanel.setText(manager.getSong(yearIndex,songTraverser).getTrackName());
                            artistPanel.setText(manager.getSong(yearIndex,songTraverser).getArtistName());
                            datePanel.setText(manager.getSong(yearIndex,songTraverser).getReleaseDate());
                            streamsPanel.setText(manager.getSong(yearIndex,songTraverser).getTotalStreams());
                        }
                        //get and set Label, title
                        JLabel songInfoLabel =  getLabelByName(yearInfoPanel,"songStats");
                        if (songInfoLabel != null) {
                            int songsInYear = manager.getSongCount(selectedYear);
                            int totalSongs = manager.getSongCount();
                            double percentage = roundToHundredths(((double) songsInYear / totalSongs));
                            //set %, song in year, total songs
                            songInfoLabel.setText(percentage+"% | "+songsInYear +" of "+ totalSongs+ " total songs");

                            int songNum = songTraverser+1;
                            mainFrame.setTitle("Songs | "+songNum+ " of "+songsInYear+" songs");
                        } else {
                            throw new RuntimeException("Can't find label");
                        }
                    }
                }
            });




            mainFrame.setLayout(null);
            mainFrame.setVisible(true);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } catch (NullPointerException e3) {
            e3.printStackTrace();
        }


    }
    /**
     * Create label and text field panel with given label and name
     *
     * @param songInfoPanel Panel to which the components will be added
     * @param label label text
     * @param setName name for both label and text field
     * @return The created text field.
     */
    private static JTextField createLabelTextField(JPanel songInfoPanel, String label, String setName) {
        JLabel trackLabel = new JLabel(label);
        JTextField trackField = new JTextField("", 0);

        trackLabel.setName(setName);
        trackField.setName(setName);
        songInfoPanel.add(trackLabel);
        songInfoPanel.add(trackField);
        return trackField;
    }

    /**
     * Create a button with the given label and set its enabled status
     *
     * @param controlPanel The panel to which the button will be added
     * @param controlBtn The label of the button
     * @param enable The enabled status of the button
     * @return The created button
     */
    private JButton createButton(JPanel controlPanel, String controlBtn, boolean enable) {
        //create button, add to panel
        JButton btn = new JButton(controlBtn);
        btn.setName(controlBtn);
        controlPanel.add(btn);
        btn.setEnabled(enable);
        return btn;
    }
    /**
     * Retrieves a JLabel by its name from a given container
     *
     * @param container The container from which the JLabel is retrieved
     * @param name The name of the JLabel to retrieve
     * @return The JLabel if found, null otherwise
     */
    public static JLabel getLabelByName(Container container, String name) {
        for (Component component : container.getComponents()) {
            if (component instanceof JLabel && name.equals(component.getName())) {
                return (JLabel) component;
            }
        }
        return null;
    }
    /**
     * Rounds a given double value to two decimal places
     *
     * @param value The double value to be rounded
     * @return The rounded double value
     */
    private static double roundToHundredths(double value) {
        return Math.round(value * 10000) / 100.0; // multiplying by 10000 and dividing by 100 to get two decimal places.
    }
    /**
     * Initializes the SongManager and loads song data from CSV files.
     * Returns appropriate exception.
     */
    private void initiateSongManager() {

        try {
            manager = new SongManager();
            //read files
            File countByYearCSV = new File("count-by-release-year.csv");
            FileReader songCSV = new FileReader("spotify-2023.csv", StandardCharsets.UTF_8);
            // create song data arrays and set
            Object[] data = SongManager.convertFileContentToArrays(countByYearCSV);
            manager.setYearArr((String[]) data[0]); //50 year items
            manager.setYearCountArr((String[]) data[1]); //use to initiate Song[][]

            //create unsorted song record array
            Song[] songsArr = SongManager.createSongArr(songCSV, manager.getSongCount());

            //sortSongs by year,
            Song[] sortedSongsArr = SongManager.sortSongsBy(songsArr, "releasedYear");
            manager.setSongsSortedByYearWithSongIndex(sortedSongsArr);

            //Arrays.toString(manager.getSo)



        } catch (RuntimeException | CsvValidationException | IOException e1) {
            System.err.println(e1.getMessage());
        }
    }

}