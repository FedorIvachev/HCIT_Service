package cn.edu.tsinghua.proxytalk;

public interface ITaskCallback<T> {

    void run(T result);

}
