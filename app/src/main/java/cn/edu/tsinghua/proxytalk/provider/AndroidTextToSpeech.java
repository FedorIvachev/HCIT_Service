package cn.edu.tsinghua.proxytalk.provider;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import java.util.HashMap;
import java.util.Locale;

import cn.edu.tsinghua.proxytalk.ITaskCallback;

public class AndroidTextToSpeech implements ITextToSpeechProvider {

    private boolean _ready;
    private TextToSpeech _tts;

    //Should be uint64 or uint32 but no __aarch64__ macro and no unsigned available so use int64
    // => Hope you're not trying to get more than 2^63-1 requests or it will start bugging...
    private long _curRequestId;

    static class Request {
        String _text;
        ITaskCallback<String> _finish;
    }

    private HashMap<String, Request> _requests = new HashMap<>();

    public AndroidTextToSpeech(Context context) {
        _ready = false;
        _curRequestId = 0;
        _tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                _tts.setLanguage(Locale.CHINESE);
                _ready = (status == TextToSpeech.SUCCESS);
            }
        });
        _tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
            }

            @Override
            public void onDone(String utteranceId) {
                Request request = _requests.get(utteranceId);
                if (request != null) {
                    request._finish.run(request._text);
                    _requests.remove(utteranceId);
                }
            }

            @Override
            public void onError(String utteranceId) {
                Request request = _requests.get(utteranceId);
                if (request != null) {
                    System.err.println("An error has occurred while speaking some text with Android native TextToSpeech");
                    _requests.remove(utteranceId);
                }
            }
        });
    }

    private String getNextIdentifier() {
        ++_curRequestId;
        return ("HCIT_TSINGHUA_UT_ID_" + _curRequestId);
    }

    @Override
    public void speak(String text) {
        _tts.speak(text, TextToSpeech.QUEUE_ADD, null, getNextIdentifier());
    }

    @Override
    public void speak(String text, ITaskCallback<String> onFinish) {
        Request request = new Request();
        request._finish = onFinish;
        request._text = text;
        String identifier = getNextIdentifier();
        _requests.put(identifier, request);
        _tts.speak(text, TextToSpeech.QUEUE_ADD, null, identifier);
    }

    @Override
    public void close() {
        _requests.clear();
        _tts.stop();
    }

}
