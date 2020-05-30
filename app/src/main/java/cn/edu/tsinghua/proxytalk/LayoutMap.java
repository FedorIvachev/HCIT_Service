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
        registerLayout("com.eg.android.AlipayGphone-0", Alipay_Index.class);
        registerLayout("com.eg.android.AlipayGphone-32", Alipay_32.class);
        registerLayout("com.eg.android.AlipayGphone-42", Alipay_42.class);
        registerLayout("com.eg.android.AlipayGphone-61", Alipay_61.class);
        registerLayout("com.eg.android.AlipayGphone-70", Alipay_70.class);
        registerLayout("com.eg.android.AlipayGphone-74", Alipay_74.class);
        registerLayout("com.eg.android.AlipayGphone-77", Alipay_77.class);
        registerLayout("com.eg.android.AlipayGphone-93", Alipay_93.class);
        registerLayout("com.eg.android.AlipayGphone-134", Alipay_134.class);
        registerLayout("com.eg.android.AlipayGphone-143", Alipay_143.class);
        registerLayout("com.eg.android.AlipayGphone-151", Alipay_151.class);
        registerLayout("com.eg.android.AlipayGphone-172", Alipay_172.class);
        registerLayout("com.eg.android.AlipayGphone-176", Alipay_176.class);
        registerLayout("com.eg.android.AlipayGphone-178", Alipay_178.class);
        registerLayout("com.eg.android.AlipayGphone-179", Alipay_179.class);
        registerLayout("com.eg.android.AlipayGphone-193", Alipay_193.class);
    }
}