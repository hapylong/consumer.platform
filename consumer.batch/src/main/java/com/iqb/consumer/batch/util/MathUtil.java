package com.iqb.consumer.batch.util;

import java.util.ArrayList;
import java.util.List;

public class MathUtil {

    public static <T> List<List<T>> subListByFixedSize(List<T> ls, int size) {
        List<List<T>> lls = new ArrayList<>();
        int remaider = ls.size() % size; // (先计算出余数)
        int number = ls.size() / size; // 然后是商
        int offset = 0;// 偏移量
        for (int i = 0; i < size; i++) {
            List<T> newls = null;
            if (remaider > 0) {
                newls = ls.subList(i * number + offset, (i + 1) * number + offset + 1);
                remaider--;
                offset++;
            } else {
                newls = ls.subList(i * number + offset, (i + 1) * number + offset);
            }
            lls.add(newls);
        }
        return lls;
    }

    public static <T> List<List<T>> subListByFixedLength(List<T> ls, int length) {
        List<List<T>> lls = new ArrayList<>();
        int remaider = ls.size() % length;
        int size = ls.size() / length;
        int offset = 0;// 偏移量
        /**
         * ls.size()<length
         */
        if (size == 0) {
            lls.add(ls);
            return lls;
        }
        /**
         * ls.size()>=length
         */
        for (int i = 0; i < size; i++) {
            List<T> newls = ls.subList(i * length + offset, (i + 1) * length + offset);
            lls.add(newls);
        }
        /**
         * ls.size()==length
         */
        if (remaider == 0) {
            return lls;
        }
        List<T> newls = ls.subList(size * length + offset, size * length + remaider + offset);
        lls.add(newls);
        return lls;
    }
}
