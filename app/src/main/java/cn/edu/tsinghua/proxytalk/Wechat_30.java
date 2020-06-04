package cn.edu.tsinghua.proxytalk;

import android.util.ArrayMap;
import android.util.Log;

import java.util.List;
import java.util.Map;

import pcg.hcit_service.AccessibilityNodeInfoRecord;
import pcg.hcit_service.MyExampleClass;

// Transfer money select amount page
public class Wechat_30 extends ActionDrivenLayout {
    private static String GREETING = "在确定";
    public static final String TAG  = "VOICE_Assistant";

    public Wechat_30(MyExampleClass context, String lowLevelPageName) {
        super(context, lowLevelPageName);
    }

    @Override
    public void onLoad() {
        setThreshold(0.8f);

        registerAction(new ITaskCallback<Result>() {
            @Override
            public void run(Result result) {
                Map<String, String> paraValues = new ArrayMap<>();
                switchPages("com.tencent.mm-15", paraValues);
            }
        }, "取消");

        registerAction(new ITaskCallback<ActionDrivenLayout.Result>() {
            @Override
            public void run(ActionDrivenLayout.Result result) { //Called when the action is matched
                proxySpeak("谁的？", new ITaskCallback<String>() {
                    @Override
                    public void run(String result) {
                        proxyListen(new ITaskCallback<String>() {
                            @Override
                            public void run(String result) {
                                final String result1 = result;
                                proxySpeak("钟点？", new ITaskCallback<String>() {
                                    @Override
                                    public void run(String result) {
                                        proxyListen(new ITaskCallback<String>() {
                                            @Override
                                            public void run(String result2) {
                                                Map<String, String> paraValues = new ArrayMap<>();
                                                paraValues.put("日期", result1);
                                                paraValues.put("钟点", result2);
                                                switchPages("com.tencent.mm-15", paraValues);

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
                        }, new ITaskCallback<String>() {
                            @Override
                            public void run(String result) {
                                //
                            }
                        });
                    }
                });
            }
        }, "打开联系人页面");

        proxySpeak(GREETING, new ITaskCallback<String>() {
            @Override
            public void run(String result) {
                listen();
                Log.i(TAG, "Greeting success_30");
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
