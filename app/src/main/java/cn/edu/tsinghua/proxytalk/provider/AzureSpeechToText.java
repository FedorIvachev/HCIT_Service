package cn.edu.tsinghua.proxytalk.provider;

import android.util.Log;

import com.microsoft.cognitiveservices.speech.ResultReason;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognitionResult;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;

import java.util.concurrent.Future;

import cn.edu.tsinghua.proxytalk.ITaskCallback;

public class AzureSpeechToText extends AzureServices implements ISpeechToTextProvider {

    private SpeechRecognizer _recognizer;
    private SpeechConfig _config;

    public AzureSpeechToText() {
        _config = createSpeechConfig();
        _recognizer = new SpeechRecognizer(_config);
    }

    @Override
    public void listen(final ITaskCallback<String> onSuccess) {
        Future<SpeechRecognitionResult> task = _recognizer.recognizeOnceAsync();
        runTask(task, new ITaskCallback<SpeechRecognitionResult>() {
            @Override
            public void run(SpeechRecognitionResult result) {
                if (result.getReason() == ResultReason.RecognizedSpeech) {
                    onSuccess.run(result.toString());
                } else {
                    System.err.println("Error recognizing speach");
                }
                result.close();
            }
        });
    }

    @Override
    public void listen(final ITaskCallback<String> onSuccess, final ITaskCallback<String> onFailure) {
        Future<SpeechRecognitionResult> task = _recognizer.recognizeOnceAsync();
        runTask(task, new ITaskCallback<SpeechRecognitionResult>() {
            @Override
            public void run(SpeechRecognitionResult result) {
                if (result.getReason() == ResultReason.RecognizedSpeech) {
                    onSuccess.run(result.getText());
                } else
                    onFailure.run(result.toString());
                result.close();
            }
        });
    }

    @Override
    public void close() {
        _recognizer.close();
        _config.close();
        super.close();
    }

}
