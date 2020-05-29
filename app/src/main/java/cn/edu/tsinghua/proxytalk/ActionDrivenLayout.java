package cn.edu.tsinghua.proxytalk;

import java.util.ArrayList;

import pcg.hcit_service.MyExampleClass;

public abstract class ActionDrivenLayout extends Layout {

    public ActionDrivenLayout(MyExampleClass context, String lowLevelPageName) {
        super(context, lowLevelPageName);
    }

    private static class Action {
        ITaskCallback<Result> _function;
        String _term;
    }

    public static class Result {
        /**
         * Stores the entire command as detected by Azure services
         */
        String Command;

        /**
         * Stores the matched action alias
         */
        String MatchedAlias;
    }

    private ArrayList<Action> _actions = new ArrayList<>();
    private float _threshold = 0.8f;

    /**
     * Register a new action
     * @param function Runnable to run when the action is triggered
     * @param aliases all possible text for this action separated by ','
     */
    public void registerAction(ITaskCallback<Result> function, String... aliases) {
        for (String s : aliases) {
            Action a = new Action();
            a._term = s;
            a._function = function;
            _actions.add(a);
        }
    }

    /**
     * Sets the action threshold: if the greatest action equality ratio is lower than threshold, drop it
     * @param threshold the new threshold
     */
    public void setThreshold(float threshold) {
        _threshold = threshold;
    }

    private float calcRatio(String a, String b) {
        int identical = 0;
        int total = a.length();

        for (int i = 0; i != a.length(); ++i) {
            if (i < b.length() && a.charAt(i) == b.charAt(i)) {
                ++identical;
            }
        }
        return ((float)identical / (float)total);
    }

    /**
     * Start listening for the next action
     */
    public void listen() {
        proxyListen(new ITaskCallback<String>() {
            @Override
            public void run(String result) {
                float curRatio = 0;
                Action curAction = null;
                for (Action a : _actions) {
                    float ratio = calcRatio(a._term, result);
                    if (curAction == null || ratio > curRatio) {
                        curAction = a;
                        curRatio = ratio;
                    }
                }
                if (curAction != null && curRatio > _threshold)
                {
                    Result res = new Result();
                    res.Command = result;
                    res.MatchedAlias = curAction._term;
                    curAction._function.run(res);
                }
                onListenSuccess(result);
            }
        }, new ITaskCallback<String>() {
            @Override
            public void run(String result) {
                onListenError(result);
            }
        });
    }

    /**
     * Called when an error occurs while listening to mic
     * @param message the error message
     */
    public abstract void onListenError(String message);

    /**
     * Called when listen function succeeded (after running the corresponding action if any)
     * @param result the result string
     */
    public abstract void onListenSuccess(String result);
}
