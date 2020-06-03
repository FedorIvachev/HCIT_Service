package cn.edu.tsinghua.proxytalk.provider;

import cn.edu.tsinghua.proxytalk.ITaskCallback;

public interface ISpeechToTextProvider {

    void listen(ITaskCallback<String> onSuccess);
    void listen(ITaskCallback<String> onSuccess, ITaskCallback<String> onFailure);

}
