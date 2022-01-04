package QuizButts;


import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import FileDropPack.*;

public class ThreeClickWindow extends JFrame implements MouseListener {
    JPanel panel;
    JPanel header;
    int teamCount;
    JLabel lbl_teamCount;
    JLabel lbl;
    Color[] teamColors;
    String[] teamNames;
    Clip[] teamSounds;
    Timer cooldown;
    private final int cooldownSecs = 5;
    boolean onCooldown;
    private final int maxTeamCount = 3;
    public ThreeClickWindow(){
        cooldown = new Timer(cooldownSecs*1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cooldown.stop();
                cooldownFinished();
            }
        });
        onCooldown = false;
        teamCount = 0;
        teamSounds = new Clip[maxTeamCount];
        teamNames = new String[maxTeamCount];
        {
            int i = 0;
            teamNames[i++] = "ТОМАТЫ";
            teamNames[i++] = "ЩАВЕЛЬ";
            teamNames[i++] = "МАНДАРИНЫ";
        }
        teamColors = new Color[maxTeamCount];
        {
            int i = 0;
            teamColors[i++] = Color.RED;
            teamColors[i++] = Color.GREEN;
            teamColors[i++] = Color.ORANGE;
        }
        header = new JPanel();
        lbl_teamCount = new JLabel("", SwingConstants.LEFT);
        lbl_teamCount.setText(Integer.toString(teamCount));
        JLabel lbl_teamLabel = new JLabel("Количество команд:", SwingConstants.RIGHT);
        lbl_teamCount.setFont(new Font("Serif", Font.PLAIN, 20));
        lbl_teamLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        header.setLayout(new GridLayout(1,2));
        header.add(lbl_teamLabel);
        header.add(lbl_teamCount);
        header.setOpaque(false);
        super.addMouseListener(this);
        lbl = new JLabel("ВИКТОРИНА", SwingConstants.CENTER);
        lbl.setFont(new Font("Serif", Font.PLAIN, 170));
        lbl.setOpaque(false);
        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(100,100,100,100));
        panel.setBackground(Color.green);
        panel.setLayout(new GridLayout(2,1));
        panel.add(header);
        panel.add(lbl);
        this.setLayout(new GridLayout(1,1));
        initDrop();

        this.setForeground(Color.MAGENTA);

        this.add(panel, BorderLayout.CENTER);
        this.setTitle("Викторина");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }
    private void cooldownFinished(){
        panel.setBackground(Color.pink);
        lbl.setText(". . . . .");
        onCooldown = false;
    }
    private void buttonClicked(int num){
        if (teamCount == 0)
            return;
        if (onCooldown)
            return;
        num--;
        if (num >= teamColors.length)
            return;
        // block events
        lbl.setText(teamNames[num]);
        panel.setBackground(teamColors[num]);
        lbl_teamCount.setText(Integer.toString(teamCount));
        //start timer
        cooldown.start();
        onCooldown = true;
        // play sound
        teamSounds[num].setMicrosecondPosition(500000);
        teamSounds[num].start();
        System.out.println("TEAM " + num);
    }
    private void addTeam(String soundPath){
        if (teamCount == teamColors.length) {
            System.out.println("MAX TEAM COUNT REACHED");
            return;
        }
        if ( !setTeamSound(soundPath, teamCount) ){
            return;
        }
        ++teamCount;
        lbl_teamCount.setText(Integer.toString(teamCount));
    }
    private void initDrop() {
        new FileDrop(this, new FileDrop.Listener() {
            public void filesDropped(File[] files) {
                if (files[0].getPath().endsWith(".wav")) {
                    addTeam(files[0].getPath());

                } else {
                    System.out.println("WRONG FILE .wav expected");
                }
            }
        }   // end filesDropped
        );
    }
    private boolean setTeamSound(String path, int teamNum){
        AudioInputStream audioInputStream = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File(path));
            teamSounds[teamNum] = AudioSystem.getClip( );
            teamSounds[teamNum].open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        buttonClicked(e.getButton());

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
