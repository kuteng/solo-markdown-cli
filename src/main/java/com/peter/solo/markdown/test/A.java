package com.peter.solo.markdown.test;

public class A {
    private StringBuilder builder = new StringBuilder();

    public void update() {
        this.builder.append("A, ");
    }

    public void tag() {
        this.builder.insert(0, "[A]: ");
    }

    public void print() {
        System.out.println(">>> A: " + builder.toString());
    }
}
