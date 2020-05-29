package cn.edu.tsinghua.proxytalk;

import android.util.ArrayMap;

import com.microsoft.cognitiveservices.speech.ResultReason;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognitionResult;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;
import com.microsoft.cognitiveservices.speech.SpeechSynthesisCancellationDetails;
import com.microsoft.cognitiveservices.speech.SpeechSynthesisResult;
import com.microsoft.cognitiveservices.speech.SpeechSynthesizer;


import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import pcg.hcit_service.AccessibilityNodeInfoRecord;
import pcg.hcit_service.MyExampleClass;
import pcg.hcit_service.NodeAccessController;
import pcg.hcit_service.Template.PageTemplateInfo;

public abstract class Layout {
    private SpeechSynthesizer _synthesizer;
    private SpeechRecognizer _recognizer;
    private SpeechConfig _config;
    private static ExecutorService _service;
    private String _lowLevelPageName;
    private MyExampleClass _context;

    public Layout(MyExampleClass context, String lowLevelPageName) {
        _lowLevelPageName = lowLevelPageName;
        _context = context;
        _config = SpeechConfig.fromSubscription(AzureServices.API_KEY_1, AzureServices.REGION);
        _config.setSpeechSynthesisLanguage("zh-CN");
        _config.setSpeechSynthesisVoiceName("zh-CN-XiaoxiaoNeural"); // Comment this line for default voice (but I like this one more hhhh)
        _config.setSpeechRecognitionLanguage("zh-CN");
        _synthesizer = new SpeechSynthesizer(_config);
        _recognizer = new SpeechRecognizer(_config);
        if (_service == null)
            _service = Executors.newCachedThreadPool();
    }

    /**
     * Switches pages
     * @param newPageName the new page name as given in the layout map
     * @param paraValues the switch parameters
     */
    public void switchPages(final String newPageName, Map<String, String> paraValues) {
        String lastIndex = _lowLevelPageName.substring(_lowLevelPageName.lastIndexOf('-') + 1);
        final String packageName = _lowLevelPageName.substring(0, _lowLevelPageName.lastIndexOf('-'));
        int lastIndexInt = Integer.parseInt(lastIndex);
        String newIndex = newPageName.substring(newPageName.lastIndexOf('-') + 1);
        int newIndexInt = Integer.parseInt(newIndex);
        if (paraValues == null)
            paraValues = new ArrayMap<>();

        List<PageTemplateInfo.TransInfo> res = NodeAccessController.calTransitionPath(packageName,
                lastIndexInt, newIndexInt, Collections.<PageTemplateInfo.TransInfo>emptySet(), Collections.<Integer>emptySet(),
                paraValues.keySet());
        NodeAccessController.jumpByTransInfoList(res, new NodeAccessController.JumpResCallBack() {
            @Override
            public void onResult(boolean successful, String crtPageName, int successStep, PageTemplateInfo.TransInfo crt, List<PageTemplateInfo.TransInfo> oriPath, NodeAccessController.JumpFailReason reason) {
                _context.onPageChange(_lowLevelPageName, newPageName);
            }
        }, paraValues);
    }

    public abstract void onLoad();
    public abstract void onChange(Map<String, List<AccessibilityNodeInfoRecord>> changeTypeToNodeList);

    /**
     * Call this function when this Layout is about to be terminated
     */
    public void close() {
        //_recognizer.close();
        //_synthesizer.close();
        //_config.close();
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
     * Speak something through speakers
     * @param text the text to speak
     * @param onFinish callback to call when the application has finished speaking out the text
     */
    public void proxySpeak(final String text, final ITaskCallback<String> onFinish) {
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
