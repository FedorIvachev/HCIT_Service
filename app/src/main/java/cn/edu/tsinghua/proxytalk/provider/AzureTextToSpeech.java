package cn.edu.tsinghua.proxytalk.provider;

import com.microsoft.cognitiveservices.speech.ResultReason;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechSynthesisCancellationDetails;
import com.microsoft.cognitiveservices.speech.SpeechSynthesisResult;
import com.microsoft.cognitiveservices.speech.SpeechSynthesizer;

import java.util.concurrent.Future;

import cn.edu.tsinghua.proxytalk.ITaskCallback;

public class AzureTextToSpeech extends AzureServices implements ITextToSpeechProvider {

    private SpeechSynthesizer _synthesizer;
    private SpeechConfig _config;

    public AzureTextToSpeech() {
        _config = createSpeechConfig();
        _synthesizer = new SpeechSynthesizer(_config);
    }

    @Override
    public void speak(String text) {
        Future<SpeechSynthesisResult> task = _synthesizer.SpeakTextAsync(text);
        runTask(task, new ITaskCallback<SpeechSynthesisResult>() {
            @Override
            public void run(SpeechSynthesisResult result) {
                if (result.getReason() == ResultReason.Canceled) {
                    String err = SpeechSynthesisCancellationDetails.fromResult(result).toString();
                    System.err.println("Error synthesizing speach: " + err);
                }
                result.close();
            }
        });
    }

    @Override
    public void speak(final String text, final ITaskCallback<String> onFinish) {
        Future<SpeechSynthesisResult> task = _synthesizer.SpeakTextAsync(text);
        runTask(task, new ITaskCallback<SpeechSynthesisResult>() {
            @Override
            public void run(SpeechSynthesisResult result) {
                if (result.getReason() == ResultReason.Canceled) {
                    String err = SpeechSynthesisCancellationDetails.fromResult(result).toString();
                    System.err.println("Error synthesizing speach: " + err);
                }
                onFinish.run(text);
                result.close();
            }
        });
    }

    @Override
    public void close() {
        _synthesizer.close();
        _config.close();
        super.close();
    }
}
