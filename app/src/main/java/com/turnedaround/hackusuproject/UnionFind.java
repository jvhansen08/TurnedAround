package com.turnedaround.hackusuproject;

import java.util.Arrays;

public class UnionFind {
    private final int[] list;

    public UnionFind(int size) {
        list = new int[size];
        Arrays.fill(list, -1);
    }

    public int size() {
        return list.length;
    }

    public int find(int a) {
        if (invalid(a)) return -1;
        if (list[a] < 0) return a;
        list[a] = find(list[a]);
        return list[a];
    }

    public boolean compare(int a, int b) {
        return find(a) == find(b);
    }

    public boolean union(int a, int b) {
        if (invalid(a) || invalid(b) || a == b) return false;
        int fa = find(a);
        int fb = find(b);
        if (fa == fb) return false;
        list[fa] += list[fb];
        list[fb] = fa;
        return true;
    }

    public boolean isConnected() {
        int f0 = find(0);
        for (int i = 1; i < list.length; i++) {
            if (f0 != find(i)) return false;
        }
        return true;
    }

    public void reset() {
        Arrays.fill(list, -1);
    }

    private boolean invalid(int a) {
        return !(a >= 0 && a < list.length);
    }
}
