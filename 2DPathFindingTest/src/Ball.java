import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class Ball {
    private Vector centerLoc;
    private int radius;
    private CopyOnWriteArrayList<Vector> forces; //TODO
    private Color color;
    //dont forget mass * acceleration


    public Ball(int x, int y, int radius) {
        this( x, y, radius, Color.BLACK);
    }

    public Ball(Vector centerLoc, int radius) {
        this((int)centerLoc.getX(), (int)centerLoc.getY(), radius, Color.BLACK);
    }

    public Ball(int x, int y, int radius, Color color) {
        this.centerLoc = new Vector(x, y);
        this.radius = radius;
        this.forces = new CopyOnWriteArrayList<>();
        this.color = Color.BLACK;
    }

    public Vector getCenterLoc() {
        return this.centerLoc;
    }

    public int getRadius() {
        return this.radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setCenterLoc(Vector centerLoc) {
        this.centerLoc = centerLoc;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getX() {
        return centerLoc.getX();
    }

    public double getY() {
        return centerLoc.getY();
    }

    public void moveBy(Vector distance) {
        centerLoc.add(distance);
    }

    public void addForce(Vector force) {
        this.forces.add(force);
    }

    public double getDistance(Ball obj) {
        return this.getCenterLoc().getDistance(obj.getCenterLoc());
    }

    //TODO this is temp
    public synchronized void applyForces() { //applies the force then removes it
        for (Vector v: this.forces) {
            moveBy(v);
            this.forces.remove(v);
        }
    }

    public void draw(Graphics g) { // loss of precision here
        g.setColor(color);
        g.drawOval((int) centerLoc.getX() - radius, (int) centerLoc.getY() - radius, radius * 2, radius * 2);
    }

    @Override
    public String toString() {
        return "Ball{" +
                "centerLoc=" + centerLoc +
                ", radius=" + radius +
                ", forces=" + forces +
                ", color=" + color +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ball ball = (Ball) o;
        return radius == ball.radius &&
                centerLoc.equals(ball.centerLoc) &&
                forces.equals(ball.forces) &&
                color.equals(ball.color);
    }

}
