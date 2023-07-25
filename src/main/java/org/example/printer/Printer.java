package org.example.printer;

public interface Printer<T> {
    void print(T t);
    String stringify(T t);
}
