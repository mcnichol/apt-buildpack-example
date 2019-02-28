package io.mcnichol.phaser.phaserdemo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
public class AppController {
    @RequestMapping("/")
    public String doWork() throws ExecutionException, InterruptedException {
        return SpeechRecognitionSamples.recognitionWithFileAsync();
    }
}
