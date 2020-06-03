package cn.edu.tsinghua.proxytalk.provider;

import com.microsoft.cognitiveservices.speech.SpeechConfig;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cn.edu.tsinghua.proxytalk.ITaskCallback;

class AzureServices {

    private static final String API_KEY_1 = "<KEY here>";
    private static final String REGION = "eastus";

    private ExecutorService _service;

    AzureServices() {
        _service = Executors.newCachedThreadPool();
    }

    SpeechConfig createSpeechConfig() {
        SpeechConfig config = SpeechConfig.fromSubscription(API_KEY_1, REGION);
        config.setSpeechSynthesisLanguage("zh-CN");
        config.setSpeechSynthesisVoiceName("zh-CN-XiaoxiaoNeural"); // Comment this line for default voice (but I like this one more hhhh)
        config.setSpeechRecognitionLanguage("zh-CN");
        return (config);
    }

    void close() {
        _service.shutdown();
    }

    <T> void runTask(final Future<T> task, final ITaskCallback<T> onComplete) {
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
