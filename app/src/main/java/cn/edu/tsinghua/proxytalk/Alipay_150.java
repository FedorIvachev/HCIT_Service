package cn.edu.tsinghua.proxytalk;

import android.util.ArrayMap;
import android.util.Log;

import java.util.List;
import java.util.Map;

import pcg.hcit_service.AccessibilityNodeInfoRecord;
import pcg.hcit_service.MyExampleClass;

//Transer money confirm page
public class Alipay_150 extends ActionDrivenLayout {
    private static final String GREETING = "beep";
    public static final String TAG  = "VOICE_Assistant";

    public Alipay_150(MyExampleClass context, String lowLevelPageName) {
        super(context, lowLevelPageName);
    }

    @Override
    public void onLoad() {
        setThreshold(0.8f);
        registerAction(new ITaskCallback<Result>() {
            @Override
            public void run(Result result) {
                Map<String, String> paraValues = new ArrayMap<>();
                switchPages("com.eg.android.AlipayGphone-74", paraValues);
            }
        }, "返回");
        //activate_shouldBeRunning();
        proxySpeak(GREETING, new ITaskCallback<String>() {
            @Override
            public void run(String result) {
                listen();
                Log.i(TAG, "Greeting success_150");
            }
        });
    }

    @Override
    public void onChange(Map<String, List<AccessibilityNodeInfoRecord>> changeTypeToNodeList) {
    }

    @Override
    public void onListenError(String message) {
        System.err.println("An error has occurred when running voice recognition: " + message);
        Log.i(TAG, "An error has occurred when running voice recognition: " + message);
    }

    @Override
    public void onListenSuccess(String result) {
        Log.i(TAG, "No error has occurred when running voice recognition: " + result);
    }
}
