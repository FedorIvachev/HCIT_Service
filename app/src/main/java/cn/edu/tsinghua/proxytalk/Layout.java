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

public abstract class Layout {
    private boolean _shouldBeRunning;
    private String _lowLevelPageName;
    private MyExampleClass _context;

    public Layout(MyExampleClass context, String lowLevelPageName) {
        _shouldBeRunning = true;
        _lowLevelPageName = lowLevelPageName;
        _context = context;
    }

    /**
     * Switches pages
     * @param newPageName the new page name as given in the layout map
     * @param paraValues the switch parameters
     */
    public void switchPages(final String newPageName, Map<String, String> paraValues) {
        if (!_shouldBeRunning)
            return;
        String lastIndex = _lowLevelPageName.substring(_lowLevelPageName.lastIndexOf('-') + 1);
        final String packageName = _lowLevelPageName.substring(0, _lowLevelPageName.lastIndexOf('-'));
        int lastIndexInt = Integer.parseInt(lastIndex);
        String newIndex = newPageName.substring(newPageName.lastIndexOf('-') + 1);
        int newIndexInt = Integer.parseInt(newIndex);
        if (paraValues == null)
            paraValues = new ArrayMap<>();

        List<PageTemplateInfo.TransInfo> res = NodeAccessController.calTransitionPath(packageName,
                lastIndexInt, newIndexInt, Collections.<PageTemplateInfo.TransInfo>emptySet(), Collections.<Integer>emptySet(),
                paraValues.keySet());
        NodeAccessController.jumpByTransInfoList(res, new NodeAccessController.JumpResCallBack() {
            @Override
            public void onResult(boolean successful, String crtPageName, int successStep, PageTemplateInfo.TransInfo crt, List<PageTemplateInfo.TransInfo> oriPath, NodeAccessController.JumpFailReason reason) {
                if (successful)
                    _context.onPageChange(_lowLevelPageName, newPageName);
                else {
                    proxySpeak("不支持该操作。寻求帮助", new ITaskCallback<String>() {
                        @Override
                        public void run(String result) {
                            //_shouldBeRunning = true;
                            _context.onPageChange(_lowLevelPageName, _lowLevelPageName);
                        }
                    });
                }
            }
        }, paraValues);
    }

    public abstract void onLoad();
    public abstract void onChange(Map<String, List<AccessibilityNodeInfoRecord>> changeTypeToNodeList);

    /**
     * Call this function when this Layout is about to be terminated
     */
    public void close() {
        _shouldBeRunning = false;
        Log.i("VOICE_Assistant", "Layout close");
    }

    /**
     * Speak something through speakers
     * @param text the text to speak
     */
    public void proxySpeak(String text) {
        if (!_shouldBeRunning)
            return;
        _context.getTextToSpeechProvider().speak(text);
    }

    /**
     * Speak something through speakers
     * @param text the text to speak
     * @param onFinish callback to call when the application has finished speaking out the text
     */
    public void proxySpeak(final String text, final ITaskCallback<String> onFinish) {
        if (!_shouldBeRunning)
            return;
        _context.getTextToSpeechProvider().speak(text, onFinish);
    }

    /**
     * Listen to audio then run recognition service, eventually run a callback on success
     * @param onSuccess callback to call if Azure succeeded, the callback receives the recognized text as a string
     */
    public void proxyListen(final ITaskCallback<String> onSuccess) {
        if (!_shouldBeRunning)
            return;
        _context.getSpeechToTextProvider().listen(onSuccess);
    }

    /**
     * Listen to audio then run recognition service, eventually run a callback on success
     * @param onSuccess callback to call if Azure succeeded, the callback receives the recognized text as a string
     * @param onFailure callbacj to call if Azure failed, the callback receives the error message as a string
     */
    public void proxyListen(final ITaskCallback<String> onSuccess, final ITaskCallback<String> onFailure) {
        Log.i("VOICE_Assistant", "Start listening");
        if (!_shouldBeRunning)
            return;
        _context.getSpeechToTextProvider().listen(onSuccess, onFailure);
    }

}
