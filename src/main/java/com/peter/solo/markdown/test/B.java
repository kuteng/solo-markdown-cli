package com.peter.solo.markdown.test;

public class B extends A {
    private StringBuilder builder = new StringBuilder();

    @Override
    public void update() {
        this.builder.append("B, ");
    }

    @Override
    public void print() {
        super.print();
        System.out.println(">>> B: " + builder.toString());
    }

    public static void main(String[] args) {
        B b = new B();
        A a = b;
        b.tag();
        b.update();
        a.update();
        b.print();
        a.print();
    }
}
