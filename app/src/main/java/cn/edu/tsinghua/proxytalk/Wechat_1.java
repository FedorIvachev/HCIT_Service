package cn.edu.tsinghua.proxytalk;

import android.util.ArrayMap;
import android.util.Log;

import java.util.List;
import java.util.Map;

import pcg.hcit_service.AccessibilityNodeInfoRecord;
import pcg.hcit_service.MyExampleClass;

// Transfer money select amount page
public class Wechat_1 extends ActionDrivenLayout {
    private static String GREETING = "通讯录";
    public static final String TAG  = "VOICE_Assistant";

    public Wechat_1(MyExampleClass context, String lowLevelPageName) {
        super(context, lowLevelPageName);
    }

    @Override
    public void onLoad() {
        setThreshold(0.8f);
        registerAction(new ITaskCallback<Result>() {
            @Override
            public void run(Result result) {
                Map<String, String> paraValues = new ArrayMap<>();
                switchPages("com.tencent.mm-0", paraValues);
            }
        }, "返回");

        registerAction(new ITaskCallback<ActionDrivenLayout.Result>() {
            @Override
            public void run(ActionDrivenLayout.Result result) { //Called when the action is matched
                Map<String, String> paraValues = new ArrayMap<>();
                paraValues.put("用户名", "Fedor 费杰");
                switchPages("com.tencent.mm-12", paraValues);
            }
        }, "发外国人");

        registerAction(new ITaskCallback<ActionDrivenLayout.Result>() {
            @Override
            public void run(ActionDrivenLayout.Result result) { //Called when the action is matched
                Map<String, String> paraValues = new ArrayMap<>();
                switchPages("com.tencent.mm-32", paraValues);
            }
        }, "群聊");

        registerAction(new ITaskCallback<ActionDrivenLayout.Result>() {
            @Override
            public void run(ActionDrivenLayout.Result result) { //Called when the action is matched
                proxySpeak("谁的？", new ITaskCallback<String>() {
                    @Override
                    public void run(String result) {
                        proxyListen(new ITaskCallback<String>() {
                            @Override
                            public void run(String result) {
                                Map<String, String> paraValues = new ArrayMap<>();
                                paraValues.put("用户名", result);
                                switchPages("com.tencent.mm-12", paraValues);
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
                Log.i(TAG, "Greeting success_1");
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
