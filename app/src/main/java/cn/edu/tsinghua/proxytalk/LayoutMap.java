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
        registerLayout("com.eg.android.AlipayGphone-178", Messages.class);
        //registerLayout("com.eg.android.AlipayGphone-62", Me.class);  // Layout not recognized properly
        registerLayout("com.eg.android.AlipayGphone-193", Dialog.class);
        registerLayout("com.eg.android.AlipayGphone-0", Alipay_Index.class);
        registerLayout("com.eg.android.AlipayGphone-74", Alipay_74.class);
        registerLayout("com.eg.android.AlipayGphone-70", Alipay_70.class);
    }
}