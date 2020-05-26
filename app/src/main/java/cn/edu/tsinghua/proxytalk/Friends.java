package cn.edu.tsinghua.proxytalk;

import java.util.List;
import java.util.Map;

import pcg.hcit_service.AccessibilityNodeInfoRecord;

public class Friends extends Layout {
    private static final String GREETING = "Hello my friend, here are your messages";
    public Friends() {}

    @Override
    public void onLoad() {
        proxySpeak(GREETING);
    }

    @Override
    public void onChange(Map<String, List<AccessibilityNodeInfoRecord>> changeTypeToNodeList) {
    }
}
