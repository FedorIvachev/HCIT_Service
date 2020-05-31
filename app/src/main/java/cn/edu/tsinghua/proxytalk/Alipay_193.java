// Dialog page
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

public class Alipay_193 extends ActionDrivenLayout {
    private static final String GREETING = "Reading last message from:";
    public static final String TAG  = "VOICE_Assistant";

    public Alipay_193(MyExampleClass context, String lowLevelPageName) {
        super(context, lowLevelPageName);
    }

    @Override
    public void onLoad() {

        setThreshold(0.8f);
        proxySpeak(GREETING);
        registerAction(new ITaskCallback<ActionDrivenLayout.Result>() {
            @Override
            public void run(ActionDrivenLayout.Result result) {
                switchPages("com.eg.android.AlipayGphone-0", null);
            }
        }, "返回");


        AccessibilityNodeInfoRecord crt = AccessibilityNodeInfoRecord.root;
        int node_num = 0;
        while (crt != null){
            crt = crt.next(false);
            node_num++;
            if(crt != null) {
                Log.i(TAG, "next: " + crt.toAllString());
                if (node_num == 2) proxySpeak(crt.toAllString());
                else if (node_num > 5 && crt.toAllString().charAt(0) != ' ' && crt.toAllString().charAt(0) != 'i') proxySpeak(crt.toAllString());
            }
        }
        crt = AccessibilityNodeInfoRecord.root.lastInSubTree();
        while (crt != null){
            crt = crt.prev(false);
            if(crt != null)
                Log.i(TAG, "prev: " + crt.toAllString());
        }
        listen(); //This calls Azure asynchronously
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
