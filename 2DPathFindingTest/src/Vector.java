public class Vector {
    private double x, y;

    Vector() {
        this(0, 0);
    }


    Vector(double angle) { // angle is in radians
        this(Math.cos(angle), Math.sin(angle));
    }

    Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }


    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void addToX(int a) {
        this.x += a;
    }

    public void addToY(int b) {
        this.y += b;
    }

    public void add(Vector v) {
        this.x += v.getX();
        this.y += v.getY();
    }

    public void subtract(Vector v) {
        this.x -= v.getX();
        this.y -= v.getY();
    }

    public void multiply(double a) {
        this.x *= a;
        this.y *= a;
    }

    public double getDistance(Vector vector) {
        return Math.sqrt(
                Math.pow(this.getX() - vector.getX(), 2) +
                        Math.pow(this.getY() - vector.getY(), 2));
    }

    @Override
    public String toString() {
        return String.format("<%s,%s>", this.x, this.y);
    }

    public Vector clone() {
        return new Vector(this.x, this.y);
    }
}
