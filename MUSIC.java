import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

public class MusicPlayer {
    public void playMusic(String filePath) {
        try {
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
            Thread.sleep(clip.getMicrosecondLength() / 1000);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class EmotionDetector {
    private static final String CASCADE_FILE = "haarcascade_frontalface_default.xml";
    private static final int EMOTION_DETECTION_WIDTH = 48;
    private static final int EMOTION_DETECTION_HEIGHT = 48;

    public String detectEmotion() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        String emotion = "neutral"; // Default emotion
        CascadeClassifier faceDetector = new CascadeClassifier(CASCADE_FILE);
        VideoCapture videoCapture = new VideoCapture(0);

        if (!videoCapture.isOpened()) {
            System.out.println("Error: Could not open the webcam.");
            return emotion;
        }

        Mat frame = new Mat();
        videoCapture.read(frame);
        if (frame.empty()) {
            System.out.println("Error: Webcam frame is empty.");
            return emotion;
        }

        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(frame, faceDetections);

        for (org.opencv.core.Rect rect : faceDetections.toArray()) {
            Mat cropped = new Mat(frame, rect);
            Mat resized = new Mat();
            Imgproc.resize(cropped, resized, new Size(EMOTION_DETECTION_WIDTH, EMOTION_DETECTION_HEIGHT));
            // Emotion detection logic goes here using the resized image
            // For simplicity, let's assume a neutral emotion
        }
        return emotion;
    }
}

public class MUSIC {
    public static void main(String[] args) {
        EmotionDetector emotionDetector = new EmotionDetector();
        MusicPlayer musicPlayer = new MusicPlayer();

        // Continuously detect emotion and play corresponding music
        while (true) {
            String emotion = emotionDetector.detectEmotion();
            String musicFilePath = getMusicFilePath(emotion); // Define a method to map emotions to music files
            System.out.println("Detected emotion: " + emotion);
            System.out.println("Playing music: " + musicFilePath);
            musicPlayer.playMusic(musicFilePath);
        }
    }

    private static String getMusicFilePath(String emotion) {
        // Define mapping of emotions to music files
        switch (emotion) {
            case "happy":
                return "happy_music.wav"; // Example music file for happy emotion
            case "sad":
                return "sad_music.wav"; // Example music file for sad emotion
            case "angry":
                return "angry_music.wav"; // Example music file for angry emotion
            case "neutral":
                return "normal_music.wav"; // Example music file for neutral emotion
            default:
                return "default_music.wav"; // Default music file
        }
    }
}
