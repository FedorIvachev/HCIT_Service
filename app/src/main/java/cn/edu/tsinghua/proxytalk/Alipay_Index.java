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
    //private static String GREETING = "Hello I'm your slave. Who give money to?";
    private static final String GREETING = "你好，我是你的奴隶。 谁给钱？";
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
                //proxySpeak("Switching pages...");
                proxySpeak("切换页面");

                Map<String, String> paraValues = new ArrayMap<>();
                //paraValues.put("列表朋友", "段续光");
                paraValues.put("列表朋友", "韩红萍");
                // paraValues.put("联系人", "段续光");
                //paraValues.put("转账金额", "0.01");
                switchPages("com.eg.android.AlipayGphone-70", paraValues);
                /*List<PageTemplateInfo.TransInfo> res = NodeAccessController.calTransitionPath("com.eg.android.AlipayGphone",
                        0, 70, Collections.<PageTemplateInfo.TransInfo>emptySet(), Collections.<Integer>emptySet(),
                        paraValues.keySet());
                NodeAccessController.jumpByTransInfoList(res, new NodeAccessController.JumpResCallBack() {
                    @Override
                    public void onResult(boolean successful, String crtPageName, int successStep, PageTemplateInfo.TransInfo crt, List<PageTemplateInfo.TransInfo> oriPath, NodeAccessController.JumpFailReason reason) {
                        Log.i(TAG, "onResult: res " + successful);
                    }
                }, paraValues);*/
            }
        }, "transfer money", "transfer money to", "give money", "give money to", "汇钱", "回", "给钱");

        registerAction(new ITaskCallback<ActionDrivenLayout.Result>() {
            @Override
            public void run(ActionDrivenLayout.Result result) { //Called when the action is matched

                //proxySpeak("Switching pages...");
                //proxySpeak("切换页面");

                //Map<String, String> paraValues = new ArrayMap<>();
                //paraValues.put("列表朋友", "段续光");
                //paraValues.put("列表朋友", "韩红萍");
                // paraValues.put("联系人", "段续光");
                //paraValues.put("转账金额", "0.01");
                switchPages("com.eg.android.AlipayGphone-178", null); //Whene there are no arguments you can pass null
                /*List<PageTemplateInfo.TransInfo> res = NodeAccessController.calTransitionPath("com.eg.android.AlipayGphone",
                        0, 178, Collections.<PageTemplateInfo.TransInfo>emptySet(), Collections.<Integer>emptySet(),
                        paraValues.keySet());
                NodeAccessController.jumpByTransInfoList(res, new NodeAccessController.JumpResCallBack() {
                    @Override
                    public void onResult(boolean successful, String crtPageName, int successStep, PageTemplateInfo.TransInfo crt, List<PageTemplateInfo.TransInfo> oriPath, NodeAccessController.JumpFailReason reason) {
                        Log.i(TAG, "onResult: res " + successful);
                    }
                }, paraValues);*/
            }
        }, "messages", "show messages", "show friends", "看朋友");
        proxySpeak(GREETING, new ITaskCallback<String>() {
            @Override
            public void run(String result) {
                listen();
            }
        });
        //listen(); //This calls Azure asynchronously
    }

    @Override
    public void onChange(Map<String, List<AccessibilityNodeInfoRecord>> changeTypeToNodeList) {
    }

    @Override
    public void onListenError(String message) {
        System.err.println("An error has occurred when running voice recognition: " + message);
        Log.i(TAG, "An error has occurred when running voice recognition: " + message);
        listen();
    }

    @Override
    public void onListenSuccess(String result) {
        Log.i(TAG, "No error has occurred when running voice recognition: " + result);
        listen();
    }

}
