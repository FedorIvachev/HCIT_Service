package cn.edu.tsinghua.proxytalk;

import android.util.ArrayMap;
import android.util.Log;

import java.util.List;
import java.util.Map;

import pcg.hcit_service.AccessibilityNodeInfoRecord;
import pcg.hcit_service.MyExampleClass;

//Transer money confirm page
public class Alipay_134 extends ActionDrivenLayout {
    public static final String TAG  = "VOICE_Assistant";
    private static String GREETING = "请选择转账账户";


    public Alipay_134(MyExampleClass context, String lowLevelPageName) {
        super(context, lowLevelPageName);
    }

    @Override
    public void onLoad() {
        setThreshold(0.8f);
        registerAction(new ITaskCallback<Result>() {
            @Override
            public void run(Result result) {
                Map<String, String> paraValues = new ArrayMap<>();
                switchPages("com.eg.android.AlipayGphone-0", paraValues);
            }
        }, "返回");

        /*
        registerAction(new ITaskCallback<ActionDrivenLayout.Result>() {
            @Override
            public void run(ActionDrivenLayout.Result result) { //Called when the action is matched
                proxySpeak("多少钱？");
                Map<String, String> paraValues = new ArrayMap<>();
                paraValues.put("列表朋友", "韩红萍");
                switchPages("com.eg.android.AlipayGphone-70", paraValues);
            }
        }, "给钱");
        */

        registerAction(new ITaskCallback<ActionDrivenLayout.Result>() {
            @Override
            public void run(ActionDrivenLayout.Result result) { //Called when the action is matched
                proxySpeak("谁？", new ITaskCallback<String>() {
                    @Override
                    public void run(String result) {
                        proxyListen(new ITaskCallback<String>() {
                            @Override
                            public void run(String result) {
                                Map<String, String> paraValues = new ArrayMap<>();
                                paraValues.put("列表朋友", result);
                                switchPages("com.eg.android.AlipayGphone-70", paraValues);
                            }
                        }, new ITaskCallback<String>() {
                            @Override
                            public void run(String result) {
                                //
                            }
                        });
                    }
                });
            }
        }, "给钱");


        proxySpeak(GREETING, new ITaskCallback<String>() {
            @Override
            public void run(String result) {
                listen();
                Log.i(TAG, "Greeting success_134");
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
