package cn.edu.tsinghua.proxytalk;

import android.util.ArrayMap;
import android.util.Log;

import java.util.List;
import java.util.Map;

import pcg.hcit_service.AccessibilityNodeInfoRecord;
import pcg.hcit_service.MyExampleClass;

// Transfer money select amount page
public class Wechat_15 extends ActionDrivenLayout {
    private static String GREETING = "消息";
    public static final String TAG  = "VOICE_Assistant";

    public Wechat_15(MyExampleClass context, String lowLevelPageName) {
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
                proxySpeak("什么消息？", new ITaskCallback<String>() {
                    @Override
                    public void run(String result) {
                        proxyListen(new ITaskCallback<String>() {
                                        @Override
                                        public void run(String result) {
                                            Map<String, String> paraValues = new ArrayMap<>();
                                            paraValues.put("文本信息内容", result);
                                            switchPages("com.tencent.mm-20", paraValues);
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
        }, "发消息");

        registerAction(new ITaskCallback<ActionDrivenLayout.Result>() {
            @Override
            public void run(ActionDrivenLayout.Result result) { //Called when the action is matched
                Map<String, String> paraValues = new ArrayMap<>();
                switchPages("com.tencent.mm-16", paraValues);
            }
        }, "更多功能");

        registerAction(new ITaskCallback<ActionDrivenLayout.Result>() {
            @Override
            public void run(ActionDrivenLayout.Result result) { //Called when the action is matched
                Map<String, String> paraValues = new ArrayMap<>();
                switchPages("com.tencent.mm-28", paraValues);
            }
        }, "信息内容");

        registerAction(new ITaskCallback<ActionDrivenLayout.Result>() {
            @Override
            public void run(ActionDrivenLayout.Result result) { //Called when the action is matched
                AccessibilityNodeInfoRecord crt = AccessibilityNodeInfoRecord.root;
                while (crt != null){
                    crt = crt.next(false);
                    if(crt != null)
                        proxySpeak(crt.toAllString());
                        //Log.i(TAG, "next: " + crt.toAllString());
                }
                crt = AccessibilityNodeInfoRecord.root.lastInSubTree();
                while (crt != null){
                    crt = crt.prev(false);
                    if(crt != null)
                        Log.i(TAG, "prev: " + crt.toAllString());
                }
                proxySpeak("结束了", new ITaskCallback<String>() {
                    @Override
                    public void run(String result) {
                        listen();
                        Log.i(TAG, "结束了");
                    }
                });
            }
        }, "阅读消息");



        proxySpeak(GREETING, new ITaskCallback<String>() {
            @Override
            public void run(String result) {
                listen();
                Log.i(TAG, "Greeting success_15");
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
