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
    //private static String GREETING = "How Much?";
    private static final String GREETING = "多少钱？";
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

                //proxySpeak("That's too much for her");

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
            public void run(Result result) { //Called when the action is matched

                //proxySpeak("Okay");
                proxySpeak("好的");
                Map<String, String> paraValues = new ArrayMap<>();
                //paraValues.put("列表朋友", "段续光");
                //paraValues.put("列表朋友", "韩红萍");
                // paraValues.put("联系人", "段续光");
                paraValues.put("转账金额", "1.00");
                switchPages("com.eg.android.AlipayGphone-74", null); //Whene there are no arguments you can pass null

            }
        }, "one", "1", "一");
        //listen(); //This calls Azure asynchronously
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
        listen();
    }

    @Override
    public void onListenSuccess(String result) {
        Log.i(TAG, "No error has occurred when running voice recognition: " + result);
        listen();
    }

}
