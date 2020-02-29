import java.awt.*;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

public class QuadTree {
    //i dont want the quadtree to be dependant on any other class so I can use it again in another program

    public static class Rectangle {
        public int x;
        public int y;
        public int width;
        public int height;

        public Rectangle(int width, int height) { // it will find the center and create it around that
            this(width / 2, height / 2, width / 2, height / 2);
        }

        public Rectangle(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public boolean contains(Vector v) { // check if a vector is contained inside of the rectangle
            return (this.x - this.width <= v.getX()) &&
                    (this.x + this.width >= v.getX()) &&
                    (this.y - this.height <= v.getX()) &&
                    (this.y + this.height >= v.getY());
        }
    }

    public final Object drawTime = new Object();
    private Ball[] points;
    private QuadTree[] subdivisions; // if == null then there is not subtree
    private Rectangle boundary;
    private int capacity;
    private int numPoints;

    public QuadTree(Rectangle boundary) {
        this(boundary, 4); // made with a default capacity
    }

    public QuadTree(Rectangle boundary, int capacity) {
        this.points = new Ball[capacity];
        this.boundary = boundary;
        this.capacity = capacity;
        this.numPoints = 0;
    }

    public void insert(Ball b) { // TODO add logic that if some thing is bigger than on of the  current elements swap it out and make that one move down ?
        if (!overlap(b, this.boundary)) {
            return;
        }

        if (this.numPoints < this.capacity) { // puts your point in the points array of this quadTree

            for (int i = 0; i < this.capacity; i++) {
                    this.points[numPoints] = b;
                    this.numPoints++;
            }

        } else { // puts them into lower quadTrees
            if (this.subdivisions == null) {
                this.subDivide();
            }
            for (QuadTree subDivision : this.subdivisions) {
                subDivision.insert(b);
            }
        }
    }

    private boolean overlap(Ball b, Rectangle rect) {
        return !((rect.y - rect.height > b.getY() + b.getRadius())
                || (rect.y + rect.height < b.getY() - b.getRadius())
                || (rect.x + rect.width < b.getX() - b.getRadius())
                || (rect.x - rect.width > b.getX() + b.getRadius())
        );

    }

    private void subDivide() {
        this.subdivisions = new QuadTree[4];
        //uses 2d coordinate system quadrants
        //Northeast
        final Rectangle ne = new Rectangle(this.boundary.x + this.boundary.width / 2, this.boundary.y - this.boundary.height / 2, this.boundary.width / 2, this.boundary.height / 2);
        this.subdivisions[0] = new QuadTree(ne, this.capacity);

        //Northwest
        final Rectangle nw = new Rectangle(this.boundary.x - this.boundary.width / 2, this.boundary.y - this.boundary.height / 2, this.boundary.width / 2, this.boundary.height / 2);
        this.subdivisions[1] = new QuadTree(nw, this.capacity);

        //Southwest
        final Rectangle sw = new Rectangle(this.boundary.x - this.boundary.width / 2, this.boundary.y + this.boundary.height / 2, this.boundary.width / 2, this.boundary.height / 2);
        this.subdivisions[2] = new QuadTree(sw, this.capacity);

        //Southeast
        final Rectangle se = new Rectangle(this.boundary.x + this.boundary.width / 2, this.boundary.y + this.boundary.height / 2, this.boundary.width / 2, this.boundary.height / 2);
        this.subdivisions[3] = new QuadTree(se, this.capacity);

    }

    public void draw(Graphics graphics) {
        graphics.drawRect(this.boundary.x - this.boundary.width, this.boundary.y - this.boundary.height, this.boundary.width * 2, this.boundary.height * 2);
        for (int i = 0; i < this.numPoints; i++) {

            this.points[i].draw(graphics);
        }
        if (this.subdivisions != null) {
            for (int i = 0; i < this.subdivisions.length; i++) {
                this.subdivisions[i].draw(graphics);
            }
        }
    }

    public LinkedList<Ball> query(Ball og) {
        LinkedList<Ball> out = new LinkedList<>();
        this.query(og, out);
        return out;
    }

    public void query(Ball og, LinkedList<Ball> out) { //checks if any regions overlap with this ball and return all balls in the regions
        if (overlap(og, this.boundary)) {
            for (int i = 0; i < numPoints; i++) {
                if(!out.contains(this.points[i])) {
                    out.add(this.points[i]);
                }
            }
            if (this.subdivisions != null) {
                for (int i = 0; i < this.subdivisions.length; i++) {
                    this.subdivisions[i].query(og,out);
                }
            }
        }
    }

    public void clear() {
        this.numPoints = 0;
        this.subdivisions = null;
    }

    public void clear(Rectangle bounds) {
        this.boundary = bounds;
        this.clear();
    }

    @Override
    public String toString() {
        return "QuadTree{" +
                "points=" + Arrays.toString(points) +
                ", subdivisions=" + Arrays.toString(subdivisions) +
                '}';
    }
}
