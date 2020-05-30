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

public class Alipay_Index extends ActionDrivenLayout {
    private static final String GREETING = "我能帮你";
    private static final String TAG  = "Alipay_IndexClass";

    public Alipay_Index(MyExampleClass context, String lowLevelPageName) {
        super(context, lowLevelPageName);
    }

    @Override
    public void onLoad() {
        setThreshold(0.8f);
        registerAction(new ITaskCallback<ActionDrivenLayout.Result>() {
            @Override
            public void run(ActionDrivenLayout.Result result) { //Called when the action is matched
                proxySpeak(result.Command);
            }
        }, "hi");

        registerAction(new ITaskCallback<ActionDrivenLayout.Result>() {
            @Override
            public void run(ActionDrivenLayout.Result result) { //Called when the action is matched
                proxySpeak("多少钱？");
                Map<String, String> paraValues = new ArrayMap<>();
                paraValues.put("列表朋友", "韩红萍");
                switchPages("com.eg.android.AlipayGphone-70", paraValues);
            }
        }, "transfer money", "transfer money to", "give money", "give money to", "汇钱", "给钱");

        registerAction(new ITaskCallback<ActionDrivenLayout.Result>() {
            @Override
            public void run(ActionDrivenLayout.Result result) {
                switchPages("com.eg.android.AlipayGphone-178", null);
            }
        }, "messages", "show messages", "show friends", "看朋友");

        registerAction(new ITaskCallback<ActionDrivenLayout.Result>() {
            @Override
            public void run(ActionDrivenLayout.Result result) {
                switchPages("com.eg.android.AlipayGphone-134", null);
            }
        }, "transfer", "转账");

        proxySpeak(GREETING, new ITaskCallback<String>() {
            @Override
            public void run(String result) {
                listen();
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
