import com.opencsv.exceptions.CsvValidationException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;


public class GUIFrame {
    // create the main JFrame
    private SongManager manager = null;
    private boolean loadedData = false;

    private int yearIndex = 0;
    private int songTraverser = 0;


    public GUIFrame() {
        try {
            JFrame mainFrame = new JFrame("SongManager");
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


            JLabel songStats = new JLabel("Null");
            yearInfoPanel.add(songStats);
            songStats.setEnabled(false);

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
                       // System.out.println("Selected Year: " + selectedYear);
                       // System.out.println("Index of Year: " + indexOfYear);


                        // Debug: Print the contents of the manager object
                       // System.out.println(Arrays.toString(manager.getSongs(indexOfYear)));
                       // System.out.println("Get song count value: "+manager.getSongCount(selectedYear));

                        Song[] copySongsArr = manager.getSongs(indexOfYear);

                        //System.out.println("Number of Songs: " + copySongsArr.length);

//                        Song[] naturalOrderSongList = SongManager.sortSongsBy(copySongsArr,"trackName");
//                        System.out.println(naturalOrderSongList.length);


                        //manager.setAlphabatizedSongsInYear(songsOfYear);
                        //System.out.println(Arrays.toString(SongsArr))
                        songTraverser = 0;

                        //setPanels
                        trackPanel.setText(copySongsArr[songTraverser].getTrackName());
                        artistPanel.setText(copySongsArr[songTraverser].getArtistName());
                        datePanel.setText(copySongsArr[songTraverser].getReleaseDate());
                        streamsPanel.setText(copySongsArr[songTraverser].getTotalStreams());

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

    private static JTextField createLabelTextField(JPanel songInfoPanel, String label, String setName) {
        JLabel trackLabel = new JLabel(label);
        JTextField trackField = new JTextField("", 0);

        trackLabel.setName(setName);
        trackField.setName(setName);
        songInfoPanel.add(trackLabel);
        songInfoPanel.add(trackField);
        return trackField;
    }


    private JButton createButton(JPanel controlPanel, String controlBtn, boolean enable) {
        //create button, add to panel
        JButton btn = new JButton(controlBtn);
        btn.setName(controlBtn);
        controlPanel.add(btn);
        btn.setEnabled(enable);
        return btn;
    }


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