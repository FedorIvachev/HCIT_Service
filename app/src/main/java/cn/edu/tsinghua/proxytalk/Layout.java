package cn.edu.tsinghua.proxytalk;

import com.microsoft.cognitiveservices.speech.ResultReason;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognitionResult;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;
import com.microsoft.cognitiveservices.speech.SpeechSynthesisCancellationDetails;
import com.microsoft.cognitiveservices.speech.SpeechSynthesisResult;
import com.microsoft.cognitiveservices.speech.SpeechSynthesizer;


import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import pcg.hcit_service.AccessibilityNodeInfoRecord;
import pcg.hcit_service.R;

public abstract class Layout {
    private SpeechSynthesizer _synthesizer;
    private SpeechRecognizer _recognizer;
    private SpeechConfig _config;
    private static ExecutorService _service;

    public Layout() {

        _config = SpeechConfig.fromSubscription(AzureServices.API_KEY_1, AzureServices.REGION);
        _config.setSpeechSynthesisLanguage("zh-CN");
        _config.setSpeechSynthesisVoiceName("zh-CN-XiaoxiaoNeural"); // Comment this line for default voice (but I like this one more hhhh)
        _config.setSpeechRecognitionLanguage("zh-CN");
        _synthesizer = new SpeechSynthesizer(_config);
        _recognizer = new SpeechRecognizer(_config);
        if (_service == null)
            _service = Executors.newCachedThreadPool();
    }

    public abstract void onLoad();
    public abstract void onChange(Map<String, List<AccessibilityNodeInfoRecord>> changeTypeToNodeList);

    /**
     * Call this function when this Layout is about to be terminated
     */
    public void close() {
        _recognizer.close();
        _synthesizer.close();
        _config.close();
    }

    /**
     * Speak something through speakers
     * @param text the text to speak
     */
    public void proxySpeak(String text) {
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

    /**
     * Listen to audio then run recognition service, eventually run a callback on success
     * @param onSuccess callback to call if Azure succeeded, the callback receives the recognized text as a string
     */
    public void proxyListen(final ITaskCallback<String> onSuccess) {
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

    /**
     * Listen to audio then run recognition service, eventually run a callback on success
     * @param onSuccess callback to call if Azure succeeded, the callback receives the recognized text as a string
     * @param onFailure callbacj to call if Azure failed, the callback receives the error message as a string
     */
    public void proxyListen(final ITaskCallback<String> onSuccess, final ITaskCallback<String> onFailure) {
        Future<SpeechRecognitionResult> task = _recognizer.recognizeOnceAsync();
        runTask(task, new ITaskCallback<SpeechRecognitionResult>() {
            @Override
            public void run(SpeechRecognitionResult result) {
                if (result.getReason() == ResultReason.RecognizedSpeech) {
                    onSuccess.run(result.getText());
                } else {
                    onFailure.run(result.toString());
                }
                result.close();
            }
        });
    }

    /**
     * Run task asynchronously then run a callback on finish
     * @param task the task to run
     * @param onComplete callback to call on completion
     * @param <T> type of result
     */
    public <T> void runTask(final Future<T> task, final ITaskCallback<T> onComplete) {
        _service.submit(new Runnable() {
            @Override
            public void run() {
                T result = null;
                try {
                    result = task.get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                onComplete.run(result);
            }
        });
    }
}
