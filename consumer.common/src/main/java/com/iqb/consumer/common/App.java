package com.iqb.consumer.common;

import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Hello world!
 * 
 */
public class App
{
    @SuppressWarnings("resource")
    public static void main(String[] args) throws Exception {
        // 项目中jar包所在物理路径
        String jarName = "C:/Users/Halen/Desktop/后台jar/包含启动/front-1.0.jar";
        JarFile jarFile = new JarFile(jarName);
        Enumeration<JarEntry> entrys = jarFile.entries();
        while (entrys.hasMoreElements()) {
            JarEntry jarEntry = entrys.nextElement();
            System.out.println(jarEntry.getName());
        }
    }
}
