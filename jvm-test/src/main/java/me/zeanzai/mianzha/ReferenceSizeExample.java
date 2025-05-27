package me.zeanzai.mianzha;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

public class ReferenceSizeExample {
    private static class ReferenceHolder {
        Object reference;
    }

    public static void main(String[] args) {
        System.out.println(VM.current().details());
        System.out.println(ClassLayout.parseClass(ReferenceHolder.class).toPrintable());
    }
}