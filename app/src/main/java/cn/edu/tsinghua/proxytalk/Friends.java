package cn.edu.tsinghua.proxytalk;

import android.util.Log;

import com.microsoft.cognitiveservices.speech.SpeechRecognitionResult;

import java.util.List;
import java.util.Map;

import pcg.hcit_service.AccessibilityNodeInfoRecord;

public class Friends extends Layout {
    private static String GREETING = "Hello my friend, here are your messages";
    public Friends() {}

    @Override
    public void onLoad() {
        String result = proxyListenFEDYA();
        if (result != "bad") {
            GREETING = result;
        }
        proxySpeak(GREETING);

    }

    @Override
    public void onChange(Map<String, List<AccessibilityNodeInfoRecord>> changeTypeToNodeList) {
    }
}
