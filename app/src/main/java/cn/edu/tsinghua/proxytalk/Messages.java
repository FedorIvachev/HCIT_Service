package cn.edu.tsinghua.proxytalk;

import java.util.List;
import java.util.Map;

import pcg.hcit_service.AccessibilityNodeInfoRecord;

public class Messages extends ActionDrivenLayout {
    private static final String GREETING = "Hello my friend, here are your messages";

    public Messages() {
    }

    @Override
    public void onLoad() {
        setThreshold(0.8f);
        registerAction(new ITaskCallback<Result>() {
            @Override
            public void run(Result result) { //Called when the action is matched
                proxySpeak(result.Command);
            }
        }, "hi");
        registerAction(new ITaskCallback<Result>() {
            @Override
            public void run(Result result) { //Called when the action is matched
                proxySpeak("Switch pages...");
            }
        }, "transfer money", "transfer money to", "give money", "give money to");
        listen(); //This calls Azure asynchronously
        proxySpeak(GREETING);
    }

    @Override
    public void onChange(Map<String, List<AccessibilityNodeInfoRecord>> changeTypeToNodeList) {
    }

    @Override
    public void onListenError(String message) {
        System.err.println("An error has occurred when running voice recognition: " + message);
    }

    @Override
    public void onListenSuccess(String result) {
        //Called after testing and executing action if any
    }
}
