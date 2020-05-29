package cn.edu.tsinghua.proxytalk;

import android.util.Log;

import java.util.List;
import java.util.Map;

import pcg.hcit_service.AccessibilityNodeInfoRecord;
import pcg.hcit_service.MyExampleClass;

public class Dialog extends Layout {
    private static final String TAG  = "DialogClass";
    private static final String GREETING = "Reading last message from:";

    public Dialog(MyExampleClass context, String lowLevelPageName) {
        super(context, lowLevelPageName);
    }

    @Override
    public void onLoad() {
        proxySpeak(GREETING);
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
    }

    @Override
    public void onChange(Map<String, List<AccessibilityNodeInfoRecord>> changeTypeToNodeList) {
    }
}
