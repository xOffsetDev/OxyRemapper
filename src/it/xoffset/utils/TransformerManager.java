package it.xoffset.utils;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import sun.tools.jar.resources.jar;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.io.*;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

public class TransformerManager {
    private HashMap<String, ClassNode> map = new HashMap<>();
    private HashMap<String, byte[]> classesFile = new HashMap<>();

    public void Remap(){
        new Remapper().process(map);
        reloadClasses();
    }

    public void openJar(String file){
        try{
            JarFile jarFile = new JarFile(file);
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                InputStream is = jarFile.getInputStream(jarEntry);
                    byte[] bytes = read(is);
                    if (!jarEntry.getName().endsWith(".class")) {
                        classesFile.put(jarEntry.getName(), bytes);
                        continue;
                    }
                    ClassNode cn = new ClassNode();
                    new ClassReader(bytes).accept(cn, 8);
                    map.put(cn.name, cn);
            }
        }catch(Exception exc){System.out.println(file);}
    }

    private void write(JarOutputStream out) {
        map.values().forEach(classNode -> {
            try{
                JarEntry jarEntry = new JarEntry(classNode.name + ".class");
                ClassWriter classWriter = new ClassWriter(1);
                out.putNextEntry(jarEntry);
                classNode.accept(classWriter);
                out.write(classWriter.toByteArray());
                out.closeEntry();
            }catch(Exception exc){System.out.println(exc.getMessage());}
        });
        classesFile.keySet().forEach(str -> {
            try {
                JarEntry jarEntry = new JarEntry(str);
                out.putNextEntry(jarEntry);
                out.write(classesFile.get(str));
                out.closeEntry();
            }catch(Exception exc){System.out.println(exc.getMessage());}
        });
    }

    public void save(String file){
        try (JarOutputStream out = new JarOutputStream(new FileOutputStream(file))) {
            out.setComment("Remapped with OxyRemapper\nTelegram: @xOffset");
            this.write(out);
        }catch(Exception exc){}
    }

    private byte[] read(final InputStream in) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[512];
        int read;
        while ((read = in.read(buffer, 0, buffer.length)) != -1) {
            baos.write(buffer, 0, read);
        }
        final byte[] bytes = baos.toByteArray();
        baos.close();
        return bytes;
    }

    private void reloadClasses(){
        List<ClassNode> classes = new ArrayList<>(map.values());
        this.map.clear();
        classes.stream().forEach(classNode -> map.put(classNode.name,classNode));
    }
}
