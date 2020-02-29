import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.security.Key;

public class mainFile extends Canvas {
    public static void main(String[] args) {
        mainFile mf = new mainFile();
    }

    private PhysicsEngine physics;
    private int ballRadius = 200;
    final private int NUMBER_OF_SPLITS = 6;
    //private QuadTree qTest;


    public mainFile() {
        SwingUtilities.invokeLater(this::createGUI);
        physics = new PhysicsEngine(this);
        //qTest = new QuadTree(new QuadTree.Rectangle(640, 480));
        Thread t = new Thread(physics);
        t.start();

    }

    public void createGUI() {//GUI initialization
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640, 640);

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                //new Ball(mouseEvent.getX(), mouseEvent.getY(), 10).draw(mainFile.super.getGraphics(), Color.BLACK);
                physics.addObject(new Ball(mouseEvent.getX(), mouseEvent.getY(), ballRadius));
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });

        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                physics.addObject(new Ball(mouseEvent.getX(), mouseEvent.getY(), ballRadius));

            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                //physics.mouse.setCenterLoc(new Vector(mouseEvent.getX(), mouseEvent.getY()));
                //System.out.println(qTest.query(new Ball((int)getMousePosition().getX(), (int)getMousePosition().getY(), ballRadius)));

            }
        });

        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                switch (keyEvent.getKeyCode()) {
                    case KeyEvent.VK_OPEN_BRACKET:
                        ballRadius--;
                        break;
                    case KeyEvent.VK_CLOSE_BRACKET:
                        ballRadius++;
                        break;
                    case KeyEvent.VK_SPACE:
                        physics.splitCells(NUMBER_OF_SPLITS);
                        break;
                    case KeyEvent.VK_BACK_SPACE:
                        physics.removeObject(physics.getObjects());
                        //qTest.clear();
                        break;
                    case KeyEvent.VK_P:
                        try {
                            ballRadius = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter Size of Ball:", "" + ballRadius));
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid Ball Size");
                        }
                        break;
                }
                //System.out.println(keyEvent.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        });

        frame.add(this);
        repaint();
        frame.setVisible(true);
    }

    public void paint(Graphics g) {
        for (Ball b : physics.getObjects()) {
            b.draw(g);
        }
        synchronized (physics.qTree.drawTime) {
            //physics.qTree.draw(g);
        }
    }//TODO be able to grab ball and move it with mouse pointer, and it path back to original position
}
