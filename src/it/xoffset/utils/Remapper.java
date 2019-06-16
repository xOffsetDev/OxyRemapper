package it.xoffset.utils;

import jdk.internal.org.objectweb.asm.commons.RemappingClassAdapter;
import jdk.internal.org.objectweb.asm.commons.SimpleRemapper;
import jdk.internal.org.objectweb.asm.tree.ClassNode;

import java.util.*;

public class Remapper implements Transformer {

    @Override
    public void process(Map<String, ClassNode> map) {
        final Map<String, String> remap = new HashMap<>();
        map.keySet().stream().forEach(k->{
            final ClassNode cn = map.get(k);
            String name = genString(new Random().nextInt(3500));
            remap.put(cn.name, name);
            System.out.println(cn.name+ "    " + name);
        });
        SimpleRemapper remapper = new SimpleRemapper(remap);
        map.values().forEach(node -> {
            final ClassNode copy = new ClassNode();
            final RemappingClassAdapter adapter = new RemappingClassAdapter(copy, remapper);
            node.accept(adapter);
            map.put(node.name, copy);
            System.out.print(node.name);
        });
    }


    private String genString(int len) {
        StringBuilder sb = new StringBuilder();
        while(sb.toString().length() < len){
            char c = ((char)(492+(Math.random() * 257)));
            sb.append(c);
        }
        return sb.toString();
    }

}
