package cn.edu.tsinghua.proxytalk;

import android.util.ArrayMap;
import android.util.Log;

import java.util.List;
import java.util.Map;

import pcg.hcit_service.AccessibilityNodeInfoRecord;
import pcg.hcit_service.MyExampleClass;

// Transfer money select amount page
public class Wechat_41 extends ActionDrivenLayout {
    private static String GREETING = "选择联系人";
    public static final String TAG  = "VOICE_Assistant";

    public Wechat_41(MyExampleClass context, String lowLevelPageName) {
        super(context, lowLevelPageName);
    }

    @Override
    public void onLoad() {
        setThreshold(0.8f);

        registerAction(new ITaskCallback<Result>() {
            @Override
            public void run(Result result) {
                Map<String, String> paraValues = new ArrayMap<>();
                switchPages("com.tencent.mm-40", paraValues);
            }
        }, "返回");

        registerAction(new ITaskCallback<Result>() {
            @Override
            public void run(Result result) {
                Map<String, String> paraValues = new ArrayMap<>();
                switchPages("com.tencent.mm-40", paraValues);
            }
        }, "确定");

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
                                paraValues.put("用户名", result);
                                switchPages("com.tencent.mm-40", paraValues);
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
        }, "用户名");

        proxySpeak(GREETING, new ITaskCallback<String>() {
            @Override
            public void run(String result) {
                listen();
                Log.i(TAG, "Greeting success_41");
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
