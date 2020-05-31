package cn.edu.tsinghua.proxytalk;

import android.util.ArrayMap;
import android.util.Log;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import pcg.hcit_service.AccessibilityNodeInfoRecord;
import pcg.hcit_service.MyExampleClass;
import pcg.hcit_service.NodeAccessController;
import pcg.hcit_service.Template.PageTemplateInfo;

// Transfer money select amount page
public class Alipay_70 extends ActionDrivenLayout {
    private static String GREETING = "How Much?";
    public static final String TAG  = "Alipay_IndexClass";

    public Alipay_70(MyExampleClass context, String lowLevelPageName) {
        super(context, lowLevelPageName);
    }

    @Override
    public void onLoad() {
        setThreshold(0.8f);

        registerAction(new ITaskCallback<Result>() {
            @Override
            public void run(Result result) { //Called when the action is matched
                proxySpeak("对她来说太多了", new ITaskCallback<String>() {
                    @Override
                    public void run(String result) {
                        listen();
                    }
                });
            }
        }, "one hundred", "one 100", "100", "1 hundred", "1 100", "一百");

        registerAction(new ITaskCallback<Result>() {
            @Override
            public void run(Result result) {
                proxySpeak("好的", new ITaskCallback<String>() {
                    @Override
                    public void run(String result) {
                        listen();
                    }
                });
                Map<String, String> paraValues = new ArrayMap<>();
                paraValues.put("转账金额", "1.00");
                switchPages("com.eg.android.AlipayGphone-74", paraValues);
            }
        }, "one", "1", "一");

        registerAction(new ITaskCallback<Result>() {
            @Override
            public void run(Result result) {
                Map<String, String> paraValues = new ArrayMap<>();
                switchPages("com.eg.android.AlipayGphone-0", paraValues);
            }
        }, "返回");

        proxySpeak(GREETING, new ITaskCallback<String>() {
            @Override
            public void run(String result) {
                listen();
                Log.i(TAG, "Greeting success_70");
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
