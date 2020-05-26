package cn.edu.tsinghua.proxytalk;

import java.util.List;
import java.util.Map;

import pcg.hcit_service.AccessibilityNodeInfoRecord;

public class Me extends Layout{
    private static final String GREETING = "Your balance is: ";
    public Me() {}

    @Override
    public void onLoad() {
        proxySpeak(GREETING);
    }

    @Override
    public void onChange(Map<String, List<AccessibilityNodeInfoRecord>> changeTypeToNodeList) {
    }
}
