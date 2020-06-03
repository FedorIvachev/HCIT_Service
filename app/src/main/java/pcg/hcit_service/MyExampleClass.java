package pcg.hcit_service;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.edu.tsinghua.proxytalk.Layout;
import cn.edu.tsinghua.proxytalk.LayoutMap;
import cn.edu.tsinghua.proxytalk.provider.AndroidTextToSpeech;
import cn.edu.tsinghua.proxytalk.provider.AzureSpeechToText;
import cn.edu.tsinghua.proxytalk.provider.AzureTextToSpeech;
import cn.edu.tsinghua.proxytalk.provider.ISpeechToTextProvider;
import cn.edu.tsinghua.proxytalk.provider.ITextToSpeechProvider;

import static android.content.Context.WINDOW_SERVICE;

class MyOverlay extends View {
    int lastX, lastY;
    Paint mPaint;
    public MyOverlay(final Context context) {
        super(context);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAlpha(255);
        mPaint.setTextSize(20);
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.CYAN);
        mPaint.setStyle(Paint.Style.STROKE);

        // comment the next line to get a transparent background
        setBackgroundColor(Color.argb(0x88, 0x0E, 0x62, 0x51));
    }
    public void hide(){
        setVisibility(INVISIBLE);
    }

    public void show(){
        setVisibility(VISIBLE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // you can get touch event here
        lastX = (int) event.getRawX();
        lastY = (int) event.getRawY();
        invalidate();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(String.format(Locale.US, "%d-%d", lastX, lastY), 0, 75, mPaint);
    }
}

class OverlayController {
    Context s;
    MyOverlay overlay;

    public OverlayController(Context s){
        this.s = s;

        // you must left a 1-pixel gap so that you can access the node info from accessibility service
        WindowManager.LayoutParams params = new WindowManager.LayoutParams((int)(Utility.screenWidthPixel - 1),
                (int)Utility.screenHeightPixel,0,0,
                WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.LEFT;

        overlay = new MyOverlay(s);
        ((WindowManager) s.getSystemService(WINDOW_SERVICE)).addView(overlay, params);
    }

    public void hideOverlay(){
        overlay.hide();
    }

    public void showOverlay(){
        overlay.show();
    }

    public boolean isOverlayActive(){
        return overlay.getVisibility() == View.VISIBLE;
    }

}

/**
 * an example class
 */
public class MyExampleClass extends InteractionProxy {
    public static final String TAG  = "MyExampleClass";
    private Context context;
    private boolean autoJumpTried;
    private Layout _layout;
    private String _lowLevelPageName;

    private ITextToSpeechProvider _textToSpeech;
    private ISpeechToTextProvider _speechToText;

    public static final boolean NEED_OVERLAY = false;

    OverlayController overlayController;
    @Override
    public void onCreate(Context context) {
        this.context = context;
        if(NEED_OVERLAY){
            overlayController = new OverlayController(context);
        }
        _textToSpeech = new AzureTextToSpeech();
        _speechToText = new AzureSpeechToText();
    }

    public ISpeechToTextProvider getSpeechToTextProvider() {
        return (_speechToText);
    }

    public ITextToSpeechProvider getTextToSpeechProvider() {
        return (_textToSpeech);
    }

    @Override
    public void onServiceConnected() {

    }

    @Override
    public void onDestroy() {
        _textToSpeech.close();
        _speechToText.close();
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public boolean ignoreNotificationWindow() {
        return true;
    }

    @Override
    public boolean onKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                if (mAudioManager != null) {
                    mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                }
                if(overlayController != null){
                    overlayController.hideOverlay();
                }
                autoJumpTried = false;
                break;
            case KeyEvent.KEYCODE_VOLUME_UP:
                mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                if (mAudioManager != null) {
                    mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                }
                if(overlayController != null){
                    overlayController.showOverlay();
                }
                autoJumpTried = false;
                break;
        }
        return super.onKeyEvent(event);
    }

    @Override
    public void onPageChange(String lastPageName, String newPageName) {
        if (_lowLevelPageName != null && _lowLevelPageName.equals(newPageName))
            return;
        final AccessibilityNodeInfoRecord[] title = {null};
        Utility.Visitor.visit(AccessibilityNodeInfoRecord.root, new Utility.Visitor() {
            @Override
            public void visitNode(AccessibilityNodeInfoRecord record) {
                if (record != null && "android:id/text1".equals(String.valueOf(record.getViewIdResourceName()))){
                    title[0] = record;
                }
            }
        });
        Utility.toast(context, newPageName + " " + (title[0] == null? "" : " #" + title[0].getText()), Toast.LENGTH_SHORT);

        if (_layout != null)
            _layout.close();
        _layout = LayoutMap.createLayout(this, newPageName);
        if (_layout != null) {
            _layout.onLoad();
            _lowLevelPageName = newPageName;
        }
        //Utility.toast(context, ("PAGE: " + newPageName), Toast.LENGTH_LONG);
        //Log.i("Wechat_", "PAGE: " + newPageName);

        // commented here to not autojump everytime
        /*if(Objects.equals(newPageName, "alipay_index0")){
            AccessibilityNodeInfoRecord crt = AccessibilityNodeInfoRecord.root;
            while (crt != null){
                crt = crt.next(false);
                if(crt != null)
                    Log.i(TAG, "next: " + crt.toAllString());
            }
            Log.i(TAG, "onPageChange: ========");
            crt = AccessibilityNodeInfoRecord.root.lastInSubTree();
            while (crt != null){
                crt = crt.prev(false);
                if(crt != null)
                    Log.i(TAG, "prev: " + crt.toAllString());
            }
            if(!autoJumpTried){
               Utility.toast(context, "Enter Alipay", Toast.LENGTH_LONG);
               Map<String, String> paraValues = new ArrayMap<>();
               //paraValues.put("列表朋友", "段续光");
               paraValues.put("列表朋友", "韩红萍");
               // paraValues.put("联系人", "段续光");
               paraValues.put("转账金额", "0.01");
                List<PageTemplateInfo.TransInfo> res = NodeAccessController.calTransitionPath("com.eg.android.AlipayGphone",
                        0, 74, Collections.<PageTemplateInfo.TransInfo>emptySet(), Collections.<Integer>emptySet(),
                        paraValues.keySet());
               NodeAccessController.jumpByTransInfoList(res, new NodeAccessController.JumpResCallBack() {
                   @Override
                   public void onResult(boolean successful, String crtPageName, int successStep, PageTemplateInfo.TransInfo crt, List<PageTemplateInfo.TransInfo> oriPath, NodeAccessController.JumpFailReason reason) {
                       Log.i(TAG, "onResult: res " + successful);
                       autoJumpTried = true;
                   }
               }, paraValues);
           }
        }*/
    }

    @Override
    public void onPageUpdate(String currentPageName, Map<String, List<AccessibilityNodeInfoRecord>> changeTypeToNodeList) {
        if (_layout != null)
            _layout.onChange(changeTypeToNodeList);
        Log.i(TAG, "page change");
        List<AccessibilityNodeInfoRecord> newCreated = changeTypeToNodeList.get(AccessibilityNodeInfoRecord.CHANGE_NEW_CREATED);
        if (newCreated != null) {
            for(AccessibilityNodeInfoRecord node: newCreated){
                Log.i(TAG, "new create: " + node.getAllTexts());
            }
        } else {
            Log.w(TAG, "new created is null");
        }
    }

    @Override
    public void onUnknownPageContentChange(String lastPageName, Map<String, List<AccessibilityNodeInfoRecord>> changeTypeToNodeList) {
        Log.i(TAG, "unknown page change");
    }

}
