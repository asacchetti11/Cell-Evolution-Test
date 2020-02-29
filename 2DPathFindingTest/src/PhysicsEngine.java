import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * TODO add collision based on individual objects not just boxes
 */
//Maybe consider forces are applied on objects for a period of milliseeconds  - handled in physics engine has a list of vector 
//each object has a mass and a velocity is added - skip the acceleration part?
public class PhysicsEngine implements Runnable { //Let's just make it work with balls for now and implement forces each frame

    private CopyOnWriteArrayList<Ball> objects;
    public QuadTree qTree;
    private Canvas canvas;
    public Ball mouse;

    public PhysicsEngine(Canvas canvas) {
        this.objects = new CopyOnWriteArrayList<>();
        this.canvas = canvas;
        this.qTree = new QuadTree(new QuadTree.Rectangle(canvas.getWidth(), canvas.getHeight()), 8);
        mouse = new Ball(0, 0, 2, Color.MAGENTA);
        //objects.add(mouse);
    }

    @Override
    public void run() {
        while (canvas.isVisible()) {
            //draw all objects
            for(Ball b : objects) {
                this.qTree.insert(b);
            }
            for (Ball b : objects) {
                if (collisionDetection(b)) {
                    //b.setColor(new Color(new Random().nextInt(254) +1, 1, new Random().nextInt(254) +1));
                }
                b.applyForces();
            }
            canvas.repaint();


            try { //TODO make it run at a fps
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (qTree.drawTime) {
                this.qTree.clear(new QuadTree.Rectangle(canvas.getWidth(), canvas.getHeight()));
            }

        }
    }

    public CopyOnWriteArrayList<Ball> getObjects() {
        return this.objects;
    }

    public void addObject(Ball obj) {
        //need to check if the object can be added if the space is free
        this.objects.add(obj);
    }

    public void removeObject(Collection<Ball> balls) {
        this.objects.removeAll(balls);
    }

    public void removeObject(Ball obj) {
        this.objects.remove(obj);
    }

    // puts the forces needed for them to push away from eachother on
    // checks it against all other objects
    private boolean collisionDetection(Ball obj) {
        ArrayList<Ball> objs = hasCollided(obj);
        if (objs.size() != 0) {
            obj.setColor(new Color(
                    (int) (Math.abs(obj.getCenterLoc().getX()) / canvas.getWidth() * 255) % 255,
                    obj.getRadius() % 255,
                    (int) (Math.abs(obj.getCenterLoc().getX()) / canvas.getWidth() * 255) % 255));
            //obj.setColor(Color.RED);

            Vector forceToBeAdded = new Vector();

            //create a vector that is the weighted average of each of the balls locations
            //weight the vector with the distance
            //start from the current obj and go to other objects
            //flip vector when done and apply it to object
            //Ball b = objs.get(0);
            for (Ball b : objs) {
                Vector ballForce = b.getCenterLoc().clone();
                ballForce.subtract(obj.getCenterLoc());
                forceToBeAdded.add(ballForce);
            }
            forceToBeAdded.multiply(-.03 / objs.size()); // TODO FIGURE OUT THE CORRECT SPEED equation so that small balls move fast, big balls move slower
            //System.out.println(forceToBeAdded);

            obj.addForce(forceToBeAdded);

            return true;
        }
        obj.setColor(Color.BLACK);
        return false;
    }

    //returns a boolean if its in another object or overlapping
    private ArrayList<Ball> hasCollided(Ball obj) {
        ArrayList<Ball> collisions = new ArrayList<>();

        //for (Ball collisionObj : this.objects) {
        for (Ball collisionObj : this.qTree.query(obj)) {


            if (collisionObj != obj) {
                if (circleCollision(obj, collisionObj)) {
                    collisions.add(collisionObj);
                    //System.out.println(obj);
                    //System.out.printf("obj1-%s radius%s obj2-%s radius%s\n", obj.getCenterLoc(), obj.getRadius(), collisionObj.getCenterLoc(), collisionObj.getRadius());
                }

            }
        }
        return collisions;
    }

    private boolean circleCollision(Ball obj1, Ball obj2) {
        double distance = obj1.getDistance(obj2);
        return distance <= obj1.getRadius() + obj2.getRadius();
    }

    private boolean squareCollision(Ball obj1, Ball obj2) {
        return
                obj1.getX() - obj1.getRadius() <= obj2.getX() + obj2.getRadius()  //left side collision
                        && obj1.getX() + obj1.getRadius() >= obj2.getX() - obj2.getRadius()  //right side collision
                        && obj1.getY() - obj1.getRadius() <= obj2.getY() + obj2.getRadius()  //top collision
                        && obj1.getY() + obj1.getRadius() >= obj2.getY() - obj2.getRadius()  //bottom collision
                ;
    }

    public void splitCells(int numOfSplits) {
        final double angleDelta = 2.0 * Math.PI / numOfSplits;
        for (Ball b : this.objects) {
            final int newRadius = b.getRadius() * 2 / numOfSplits;
            for (int i = 1; i <= numOfSplits; i++) {
                Vector center = b.getCenterLoc().clone();
                Vector shift = new Vector(angleDelta * i);
                //shift.multiply(newRadius/4); /*TODO NEED TO FIGURE THIS OUT */
                shift.multiply(numOfSplits);
                center.add(shift);
                if (b.getRadius() * 2.0 / numOfSplits > 1) {
                    this.objects.add(new Ball(center, newRadius));

                }
            }
            this.objects.remove(b);
        }
    }


}
