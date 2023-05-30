package com.turnedaround.hackusuproject;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

public class Maze extends GameObject{
    private Cell[][] map;
    final private int xDim;
    final private int yDim;
    final private float cellWidth;
    final private float cellHeight;
    final private UnionFind unionFind;
    protected int[] portalACoords = new int[2];
    protected int[] portalBCoords = new int[2];
    protected Cell portalA = null;
    protected Cell portalB = null;
    protected Cell start;
    protected boolean portalToggle;

    private line[] lines;
    private oval[] ovals;

    /**
     * An awesome Maze class. Generates a maze
     * @param xDim number of cells in a row
     * @param yDim number of cells in a column
     * @param screenHeight width of maze in pixels
     * @param screenWidth height of maze in pixels
     */
    Maze(int xDim, int yDim, float screenHeight, float screenWidth, boolean portal) {
        this.xDim = xDim;
        this.yDim = yDim;
        this.portalToggle = portal;
        cellWidth = screenWidth / xDim;
        cellHeight = screenHeight / yDim;

        unionFind = new UnionFind(xDim * yDim);

        map = new Cell[xDim][];
        for (int x = 0; x < xDim; x++) {
            map[x] = new Cell[yDim];
            for (int y = 0; y < yDim; y++) {
                map[x][y] = new Cell(
                        x * cellWidth,
                        y * cellHeight,
                        cellWidth,
                        cellHeight,
                        y * xDim + x
                );
            }
        }
        if(portalToggle){
            addPortals();}

        populate();

        ovals = new oval[5];
        lines = new line[50];

        for (int i = 0; i < 5; i++) {
            ovals[i] = new oval(screenWidth, screenHeight);
        }
        for (int i = 0; i < 50; i++) {
            lines[i] = new line(screenWidth, screenHeight);
        }
    }

    private void populate() {
        Random r = new Random();
        while (!unionFind.isConnected()) {
            Cell picked = findByCellId(r.nextInt(xDim * yDim));
            mergeCell(picked, r);
        }
        PathFind finder = new PathFind();
        Cell[] path = finder.findPath(map, portalA, portalB);
        for (Cell cell : path) {
            cell.setType(Cell.Type.PATH);
        }
        start = path[path.length - 1];
        path[0].setType(Cell.Type.END);
    }

    private void mergeCell(Cell picked, Random r) {
        int dir = r.nextInt(4);
        for (int j = 0; j < 4; j++) {
            if (picked.getWalls()[(dir)%4]) {
                Cell neighbor = null;
                int x = (picked.getId() % xDim);
                int y = (picked.getId() / xDim);
                switch (dir%4) {
                    case 0:
                        if (y > 0) neighbor = map[x][y - 1];
                        break;
                    case 1:
                        if (x < xDim - 1) neighbor = map[x + 1][y];
                        break;
                    case 2:
                        if (y < yDim - 1) neighbor = map[x][y + 1];
                        break;
                    case 3:
                        if (x > 0) neighbor = map[x - 1][y];
                }
                if (neighbor != null) {
                    if (unionFind.union(picked.getId(), neighbor.getId())) {
                        picked.removeWall(dir, neighbor);
                        return;
                    }
                }
            }
            dir++;
        }
    }

    // Shouldn't place portals too close (for sure not  adjacent; bad things happen)
    // or on start or finish (wouldn't be a problem if start and finish were dynamic)
    private void addPortals() {
        Random r = new Random();
        boolean found = false;
        Cell p1 = null, p2 = null;
        int row1 = -1, row2 = -1, col1 = -1, col2 = -1;
        while (!found) {
            row1 = r.nextInt(xDim);
            col1 = r.nextInt(yDim);
            p1 = findByRowCol(row1, col1); // Could replace with map[row1][col1]
            row2 = r.nextInt(xDim);
            col2 = r.nextInt(yDim);
            p2 = findByRowCol(row2, col2);
            if (Math.abs(row1 - row2) + Math.abs(col1 - col2) > (xDim + yDim) / 2) {
                found = true;
            }
        }
        p1.setType(Cell.Type.PORTALA);
        p2.setType(Cell.Type.PORTALB);
        this.portalA = p1;
        this.portalB = p2;
        this.portalACoords[0] = row1;
        this.portalACoords[1] = col1;

        this.portalBCoords[0] = row2;
        this.portalBCoords[1] = col2;
//        authenticatePortals(p1, p2);
        mergeCell(portalA, new Random());
        mergeCell(portalB, new Random());
        unionFind.union(p1.getId(), p2.getId());
    }

    // Replaced by mergeCell method since this is basically repeat code from populate()
    // DEPRICATED METHOD - remove if no obvious side effects in next version
    private void authenticatePortals(Cell p1, Cell p2){
        int randInt = (int) (Math.random() * 4);
        if (randInt == 0){
            p1.removeWall(0, findByRowCol(portalACoords[0], portalACoords[1] - 1));
            unionFind.union(p1.getId(), p1.getId() - xDim);
            p2.removeWall(2, findByRowCol(portalBCoords[0], portalBCoords[1] + 1));
            unionFind.union(p2.getId(), p2.getId() + xDim);
        }
        else if (randInt == 1){
            p1.removeWall(1, findByRowCol(portalACoords[0] + 1, portalACoords[1]));
            unionFind.union(p1.getId(), p1.getId() + 1);
            p2.removeWall(3, findByRowCol(portalBCoords[0] - 1, portalBCoords[1]));
            unionFind.union(p2.getId(), p2.getId() - 1);
        }
        else if (randInt == 2){
            p1.removeWall(2, findByRowCol(portalACoords[0], portalACoords[1] + 1));
            unionFind.union(p1.getId(), p1.getId() + xDim);
            p2.removeWall(0, findByRowCol(portalBCoords[0], portalBCoords[1] - 1));
            unionFind.union(p2.getId(), p2.getId() - xDim);
        }
        else if (randInt == 3){
            p1.removeWall(3, findByRowCol(portalACoords[0] - 1, portalACoords[1]));
            unionFind.union(p1.getId(), p1.getId() - 1);
            p2.removeWall(1, findByRowCol(portalBCoords[0] + 1, portalBCoords[1]));
            unionFind.union(p2.getId(), p2.getId() + 1);
        }
    }

    public void reset() {
        for(Cell[] row : map) {
            for(Cell c : row) {
                c.reset();
            }
        }
        unionFind.reset();
        populate();
    }

    public Cell getCellAt(int x, int y) {
        if ((x < 0 || x >= xDim) || (y < 0 || y >= yDim)) return null;
        return map[x][y];
    }

    public Cell[][] getMap() {
        return map;
    }

    public float[] getCellSize() {
        return new float[]{cellWidth, cellHeight};
    }

    private Cell findByCellId(int id) {
        return map[id%xDim][id/xDim];
    }

    private Cell findByRowCol(int row, int col){
        return map[row][col];
    }

    @Override
    public void render(Canvas canvas, Paint paint) {
        float mazeWidth = xDim * cellWidth;
        float mazeHeight = yDim * cellHeight;
        paint.reset();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0xffba8425);
        canvas.drawRect(0, 0, mazeWidth, mazeHeight, paint);
        paint.setColor(0xff8c5716);
        for (oval o : ovals) {
            o.draw(canvas, paint);
        }
        for (line l : lines) {
            l.draw(canvas, paint);
        }
        paint.reset();
        paint.setColor(0xff303002);
        paint.setStrokeWidth(15);
        canvas.drawLine(0,0,0,cellHeight * yDim, paint);
        canvas.drawLine(0,0,cellWidth * xDim, 0, paint);
        for(Cell[] row : map) {
            for (Cell c : row) {
                c.render(canvas, paint);
            }
        }
    }

    private class oval {
        float randX;
        float randY;
        float randW;
        float randH;
        oval(float mazeWidth, float mazeHeight) {
            randX = (float) (mazeWidth * Math.random());
            randY = (float) (mazeHeight * Math.random());
            randW = (float) (50 * Math.random() + 15);
            randH = (float) (200 * Math.random() + 15);
        }

        void draw(Canvas canvas, Paint paint) {
            canvas.drawOval(randX - randW, randY - randH, randX + randW, randY + randH, paint);
        }
    }

    private class line {
        float x1, y1, x2, y2, width;
        line(float mazeWidth, float mazeHeight) {
            x1 = (float) (Math.random() * mazeWidth);
            y1=0;
            x2 = (float)(x1 + 20 * Math.random() - 10);
            y2=mazeHeight;
            width = (float) (3 * Math.random());
        }

        void draw(Canvas canvas, Paint paint) {
            paint.setStrokeWidth(width);
            canvas.drawLine(x1, y1, x2, y2, paint);
        }
    }
}
