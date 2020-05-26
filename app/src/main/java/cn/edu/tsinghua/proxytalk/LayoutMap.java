package cn.edu.tsinghua.proxytalk;

import java.util.HashMap;

public class LayoutMap {

    private static HashMap<String, Class<? extends Layout>> _map = new HashMap<>();

    public static Layout createLayout(String pageId) {
        if (!_map.containsKey(pageId))
            return (null);
        Class<? extends Layout> cl = _map.get(pageId);
        if (cl == null)
            return (null);
        try {
            return (cl.newInstance());
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            return (null);
        }
    }

    private static void registerLayout(String pageId, Class<? extends Layout> cl) {
        _map.put(pageId, cl);
    }

    static {
        //Call registerLayout here (ex: registerLayout(pageid, MyLayout.class))
        registerLayout("com.eg.android.AlipayGphone-178", Friends.class);
        //registerLayout("com.eg.android.AlipayGphone-62", Me.class);  // Layout not recognized properly
        registerLayout("com.eg.android.AlipayGphone-193", Dialog.class);

    }
}