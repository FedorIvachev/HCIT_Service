package cn.edu.tsinghua.proxytalk.provider;

import cn.edu.tsinghua.proxytalk.ITaskCallback;

public interface ITextToSpeechProvider {

    void speak(String text);
    void speak(String text, ITaskCallback<String> onFinish);

    void close();

}
