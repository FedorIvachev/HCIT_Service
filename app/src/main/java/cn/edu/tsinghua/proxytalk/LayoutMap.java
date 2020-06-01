package cn.edu.tsinghua.proxytalk;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import pcg.hcit_service.MyExampleClass;

public class LayoutMap {

    private static HashMap<String, Class<? extends Layout>> _map = new HashMap<>();

    public static Layout createLayout(MyExampleClass context, String pageId) {
        if (!_map.containsKey(pageId))
            return (null);
        Class<? extends Layout> cl = _map.get(pageId);
        if (cl == null)
            return (null);
        try {
            Constructor<? extends Layout> constructor = cl.getConstructor(MyExampleClass.class, String.class);
            return (constructor.newInstance(context, pageId));
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            return (null);
        }
    }

    private static void registerLayout(String pageId, Class<? extends Layout> cl) {
        _map.put(pageId, cl);
    }

    static {
        //Call registerLayout here (ex: registerLayout(pageid, MyLayout.class))
        // Alipay pages
        registerLayout("com.eg.android.AlipayGphone-0", Alipay_Index.class);
        registerLayout("com.eg.android.AlipayGphone-2", Alipay_2.class);
        registerLayout("com.eg.android.AlipayGphone-32", Alipay_32.class);
        registerLayout("com.eg.android.AlipayGphone-42", Alipay_42.class);
        registerLayout("com.eg.android.AlipayGphone-59", Alipay_59.class);
        registerLayout("com.eg.android.AlipayGphone-61", Alipay_61.class);
        registerLayout("com.eg.android.AlipayGphone-70", Alipay_70.class);
        registerLayout("com.eg.android.AlipayGphone-74", Alipay_74.class);
        registerLayout("com.eg.android.AlipayGphone-77", Alipay_77.class);
        registerLayout("com.eg.android.AlipayGphone-93", Alipay_93.class);
        registerLayout("com.eg.android.AlipayGphone-134", Alipay_134.class);
        registerLayout("com.eg.android.AlipayGphone-143", Alipay_143.class);
        registerLayout("com.eg.android.AlipayGphone-150", Alipay_150.class);
        registerLayout("com.eg.android.AlipayGphone-151", Alipay_151.class);
        registerLayout("com.eg.android.AlipayGphone-172", Alipay_172.class);
        registerLayout("com.eg.android.AlipayGphone-176", Alipay_176.class);
        registerLayout("com.eg.android.AlipayGphone-178", Alipay_178.class);
        registerLayout("com.eg.android.AlipayGphone-179", Alipay_179.class);
        registerLayout("com.eg.android.AlipayGphone-193", Alipay_193.class);
        // WeChat Pages
        registerLayout("com.tencent.mm-0", Wechat_0.class);
        registerLayout("com.tencent.mm-1", Wechat_1.class);
        registerLayout("com.tencent.mm-2", Wechat_2.class);
        registerLayout("com.tencent.mm-3", Wechat_3.class);
        registerLayout("com.tencent.mm-4", Wechat_4.class);
        registerLayout("com.tencent.mm-5", Wechat_5.class);
        registerLayout("com.tencent.mm-6", Wechat_6.class);
        registerLayout("com.tencent.mm-7", Wechat_7.class);
        registerLayout("com.tencent.mm-8", Wechat_8.class);
        registerLayout("com.tencent.mm-9", Wechat_9.class);
        registerLayout("com.tencent.mm-10", Wechat_10.class);
        registerLayout("com.tencent.mm-11", Wechat_11.class);
        registerLayout("com.tencent.mm-12", Wechat_12.class);
        registerLayout("com.tencent.mm-13", Wechat_13.class);
        registerLayout("com.tencent.mm-14", Wechat_14.class);
        registerLayout("com.tencent.mm-15", Wechat_15.class);
        registerLayout("com.tencent.mm-16", Wechat_16.class);
        registerLayout("com.tencent.mm-17", Wechat_17.class);
        registerLayout("com.tencent.mm-18", Wechat_18.class);
        registerLayout("com.tencent.mm-19", Wechat_19.class);
        registerLayout("com.tencent.mm-20", Wechat_20.class);
    }
}