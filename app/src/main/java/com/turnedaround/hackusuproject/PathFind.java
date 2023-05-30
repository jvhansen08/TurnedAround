package com.turnedaround.hackusuproject;

import java.util.ArrayList;
import java.util.LinkedList;

public class PathFind {

    private Queue queue;
    public Cell[] findPath(Cell[][] map, Cell portalA, Cell portalB) {
        queue = new Queue();
        queue.enque(new Path(map[map.length - 1][map[0].length - 1]));
        Path startPath = process(queue, map, portalA, portalB);
        queue.enque(new Path(startPath.current));
        Path endPath = process(queue, map, portalA, portalB);
        return endPath.visited.toArray(new Cell[0]);
    }

    private Path process(Queue q, Cell[][] map, Cell portalA, Cell portalB) {
        Path longest = q.peek();
        while (!q.isEmpty()) {
            Path p = q.deque();
            Cell cell = p.current;
            int x = cell.getId() % map.length;
            int y = cell.getId() / map.length;
            if (!cell.getUpWall() && !p.visited.contains(map[x][y-1])) q.enque(new Path(map[x][y-1], p));
            if (!cell.getRightWall() && !p.visited.contains(map[x+1][y])) q.enque(new Path(map[x+1][y], p));
            if (!cell.getDownWall() && !p.visited.contains(map[x][y+1])) q.enque(new Path(map[x][y+1], p));
            if (!cell.getLeftWall() && !p.visited.contains(map[x-1][y])) q.enque(new Path(map[x-1][y], p));
            if (cell.getType() == Cell.Type.PORTALA && !p.visited.contains(portalB)) q.enque(new Path(portalB, p));
            if (cell.getType() == Cell.Type.PORTALB && !p.visited.contains(portalA)) q.enque(new Path(portalA, p));
            if (p.visited.size() > longest.visited.size()) longest = p;
        }
        return longest;
    }

    private class Path {
        Cell current;
        ArrayList<Cell> visited;
        public Path(Cell cell) {
            current = cell;
            visited = new ArrayList<>();
            visited.add(cell);
        }

        public Path(Cell cell, Path from) {
            current = cell;
            visited = (ArrayList<Cell>) from.visited.clone();
            visited.add(cell);
        }
    }

    private class Queue {
        private LinkedList<Path> list;

        public Queue() {
            list = new LinkedList<Path>();
        }

        public boolean isEmpty() {
            return list.isEmpty();
        }

        public void enque(Path path) {
            list.addLast(path);
        }

        public Path deque() {
            if (list.isEmpty()) return null;
            return list.removeFirst();
        }

        public Path peek() {
            if (list.isEmpty()) return null;
            return list.getFirst();
        }

    }
}
