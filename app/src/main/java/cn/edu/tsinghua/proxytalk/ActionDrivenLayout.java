package cn.edu.tsinghua.proxytalk;

import android.util.Log;

import java.util.ArrayList;

import pcg.hcit_service.MyExampleClass;

public abstract class ActionDrivenLayout extends Layout {

    private boolean _listening;

    public ActionDrivenLayout(MyExampleClass context, String lowLevelPageName) {
        super(context, lowLevelPageName);
        _listening = false;
        registerHelpAction();
    }

    private static class Action {
        ITaskCallback<Result> _function;
        String _term;
        boolean _root;
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
        boolean root = true;
        for (String s : aliases) {
            Action a = new Action();
            a._term = s;
            a._function = function;
            a._root = root;
            _actions.add(a);
            if (root)
                root = false;
        }
    }

    private void registerHelpAction() {
        registerAction(new ITaskCallback<Result>() {
            @Override
            public void run(Result result) {
                StringBuilder toSay = new StringBuilder("You can ");
                for (int i = 0; i != _actions.size(); ++i) {
                    Action a = _actions.get(i);
                    if (a._root) {
                        if (i + 1 == _actions.size()) {
                            toSay.append(" or ").append(a._term);
                        } else {
                            if (i == 0)
                                toSay.append(a._term);
                            else
                                toSay.append(", ").append(a._term);
                        }
                    }
                }
                proxySpeak(toSay.toString(), new ITaskCallback<String>() {
                    @Override
                    public void run(String result) {
                        listen();
                    }
                });
            }
        }, "help");
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
        _listening = true;
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
                    stopListen();
                    Log.i("Alipay_IndexClass", "NOT LISTENING");
                    Result res = new Result();
                    res.Command = result;
                    res.MatchedAlias = curAction._term;
                    curAction._function.run(res);
                }
                internalListenSuccess(result);
            }
        }, new ITaskCallback<String>() {
            @Override
            public void run(String result) {
                internalListenError(result);
            }
        });
    }

    /**
     * Call this function to stop listening
     */
    public void stopListen() {
        _listening = false;
    }

    private void internalListenError(String message) {
        onListenError(message);
        if (_listening)
            listen();
    }

    private void internalListenSuccess(String result) {
        onListenSuccess(result);
        if (_listening)
            listen();
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
