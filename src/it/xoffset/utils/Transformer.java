package it.xoffset.utils;

import jdk.internal.org.objectweb.asm.tree.ClassNode;

import java.util.Map;

public interface Transformer {
    void process(Map<String, ClassNode> map);
}
