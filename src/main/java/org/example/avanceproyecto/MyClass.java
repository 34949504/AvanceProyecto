package org.example.avanceproyecto;

public class MyClass {
    public static MyClass valueOf(String value) {
        return new MyClass(value);
    }

    private String value = null;

    public MyClass(String value) {
        this.value = value;
    }
}
