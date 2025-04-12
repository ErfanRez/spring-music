package com.music.utils;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;

import java.io.File;

public class Utility {

    public static String getDuration(File mp3File) {
        try {
            AudioFile audioFile = AudioFileIO.read(mp3File);
            MP3AudioHeader audioHeader = (MP3AudioHeader) audioFile.getAudioHeader();
            int durationInSeconds = audioHeader.getTrackLength();
            int minutes = durationInSeconds / 60;
            int seconds = durationInSeconds % 60;
            return String.format("%d:%02d", minutes, seconds);
        } catch (Exception e) {
            e.printStackTrace();
            return "0:00";
        }
    }

}
