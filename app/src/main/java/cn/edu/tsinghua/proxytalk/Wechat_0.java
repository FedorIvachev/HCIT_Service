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
public class Wechat_0 extends ActionDrivenLayout {
    private static String GREETING = "微信";
    public static final String TAG  = "VOICE_Assistant";

    public Wechat_0(MyExampleClass context, String lowLevelPageName) {
        super(context, lowLevelPageName);
    }

    @Override
    public void onLoad() {
        setThreshold(0.8f);
        registerAction(new ITaskCallback<Result>() {
            @Override
            public void run(Result result) {
                Map<String, String> paraValues = new ArrayMap<>();
                switchPages("com.tencent.mm-1", paraValues);
            }
        }, "通讯录", "打开通讯录");

        registerAction(new ITaskCallback<Result>() {
            @Override
            public void run(Result result) {
                Map<String, String> paraValues = new ArrayMap<>();
                switchPages("com.tencent.mm-2", paraValues);
            }
        }, "发现", "打开发现");

        registerAction(new ITaskCallback<Result>() {
            @Override
            public void run(Result result) {
                Map<String, String> paraValues = new ArrayMap<>();
                switchPages("com.tencent.mm-3", paraValues);
            }
        }, "打开我");

        registerAction(new ITaskCallback<Result>() {
            @Override
            public void run(Result result) {
                Map<String, String> paraValues = new ArrayMap<>();
                switchPages("com.tencent.mm-4", paraValues);
            }
        }, "更多功能", "打开更多功能");

        registerAction(new ITaskCallback<Result>() {
            @Override
            public void run(Result result) {
                String friend = result.Command.substring(result.ParaVal);
                Map<String, String> paraValues = new ArrayMap<>();
                paraValues.put("用户名", friend);
                switchPages("com.tencent.mm-14", paraValues);
            }
        }, "语音通话给");

        registerAction(new ITaskCallback<Result>() {
            @Override
            public void run(Result result) {
                Map<String, String> paraValues = new ArrayMap<>();
                paraValues.put("用户名", "Fedor 费杰");
                switchPages("com.tencent.mm-14", paraValues);
            }
        }, "打电话给朋友");

        registerAction(new ITaskCallback<Result>() {
            @Override
            public void run(Result result) {
                String friend = result.Command.substring(result.ParaVal);
                Map<String, String> paraValues = new ArrayMap<>();
                paraValues.put("用户名", friend);
                switchPages("com.tencent.mm-15", paraValues);
            }
        }, "发消息给");



        proxySpeak(GREETING, new ITaskCallback<String>() {
            @Override
            public void run(String result) {
                listen();
                Log.i(TAG, "Greeting success_0");
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
