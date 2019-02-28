package io.mcnichol.phaser.phaserdemo;

import com.microsoft.cognitiveservices.speech.*;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;
import com.microsoft.cognitiveservices.speech.audio.PullAudioInputStreamCallback;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

public class SpeechRecognitionSamples {

    private static Semaphore stopRecognitionSemaphore;
    public static final String YOUR_SUBSCRIPTION_KEY = "7d21220ab2684749956f78165080c281";
    public static final String YOUR_SERVICE_REGION = "westus";


    public static String recognitionWithFileAsync() throws InterruptedException, ExecutionException {
        AtomicReference<String> recognizedText = new AtomicReference<>();
        stopRecognitionSemaphore = new Semaphore(0);
        SpeechConfig config = SpeechConfig.fromSubscription(YOUR_SUBSCRIPTION_KEY, YOUR_SERVICE_REGION);

        System.out.println(System.getProperty("user.dir"));
        AudioConfig audioInput = AudioConfig.fromWavFileInput("maybe-next-time.wav");
        SpeechRecognizer recognizer = new SpeechRecognizer(config, audioInput);
        {
            recognizer.recognizing.addEventListener(SpeechRecognitionSamples::printRecognition);

            recognizer.recognized.addEventListener((s, e) -> {
                if (e.getResult().getReason() == ResultReason.RecognizedSpeech) {
                    recognizedText.set(e.getResult().getText());
                } else if (e.getResult().getReason() == ResultReason.NoMatch) {
                    System.out.println("NOMATCH: Speech could not be recognized.");
                }
            });

            recognizer.canceled.addEventListener((s, e) -> {
                System.out.println("CANCELED: Reason=" + e.getReason());

                if (e.getReason() == CancellationReason.Error) {
                    System.out.println("CANCELED: ErrorCode=" + e.getErrorCode());
                    System.out.println("CANCELED: ErrorDetails=" + e.getErrorDetails());
                    System.out.println("CANCELED: Did you update the subscription info?");
                }

                stopRecognitionSemaphore.release();
            });

            recognizer.sessionStarted.addEventListener((s, e) -> System.out.println("\nSession started event."));

            recognizer.sessionStopped.addEventListener((s, e) -> stopRecognitionSemaphore.release());

            recognizer.startContinuousRecognitionAsync().get();

            stopRecognitionSemaphore.acquire();

            recognizer.stopContinuousRecognitionAsync().get();
        }

        return recognizedText.get();
    }

    public static void recognitionWithAudioStreamAsync() throws InterruptedException, ExecutionException, FileNotFoundException {
        stopRecognitionSemaphore = new Semaphore(0);
        SpeechConfig config = SpeechConfig.fromSubscription(YOUR_SUBSCRIPTION_KEY, YOUR_SERVICE_REGION);

        System.out.println(System.getProperty("user.dir"));
        PullAudioInputStreamCallback callback = new WavStream(new FileInputStream("maybe-next-time.wav"));
        AudioConfig audioInput = AudioConfig.fromStreamInput(callback);

        SpeechRecognizer recognizer = new SpeechRecognizer(config, audioInput);
        {
            recognizer.recognizing.addEventListener(SpeechRecognitionSamples::printRecognition);

            recognizer.recognized.addEventListener((s, e) -> {
                if (e.getResult().getReason() == ResultReason.RecognizedSpeech) {
                    System.out.println("RECOGNIZED: Text=" + e.getResult().getText());
                } else if (e.getResult().getReason() == ResultReason.NoMatch) {
                    System.out.println("NOMATCH: Speech could not be recognized.");
                }
            });

            recognizer.canceled.addEventListener((s, e) -> {
                System.out.println("CANCELED: Reason=" + e.getReason());

                if (e.getReason() == CancellationReason.Error) {
                    System.out.println("CANCELED: ErrorCode=" + e.getErrorCode());
                    System.out.println("CANCELED: ErrorDetails=" + e.getErrorDetails());
                    System.out.println("CANCELED: Did you update the subscription info?");
                }

                stopRecognitionSemaphore.release();
            });

            recognizer.sessionStarted.addEventListener((s, e) -> System.out.println("\nSession started event."));

            recognizer.sessionStopped.addEventListener((s, e) -> stopRecognitionSemaphore.release());

            recognizer.startContinuousRecognitionAsync().get();

            stopRecognitionSemaphore.acquire();

            recognizer.stopContinuousRecognitionAsync().get();
        }
    }

    private static void printRecognition(Object s, SpeechRecognitionEventArgs e) {
        System.out.println("RECOGNIZING: Text=" + e.getResult().getText());
    }
}
