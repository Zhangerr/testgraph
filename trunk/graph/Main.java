package graph;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JPopupMenu;

/**
 * @author Alex Zhang
 * Add functions for perpendicular bisector, median, and altitude? <--priority (add data showing as well)
 * Clean up functions/improve structure, loop for repetitive code . (More OOP as well)
 * Add fourth point identical to first for easier looping ? (e.g getting next point in line)
 * Add solution (shows how to solve for certain element)
 * Add ability to drag scroll grid?
 * add more data display such as slope.
 * draw location data on axis?
 * rightclick
 */
public class Main extends JFrame implements KeyListener, MouseMotionListener, MouseListener {

    static Point center;
    static ArrayList<Point> points = graph.gui.points;
    static ArrayList<Integer> possible = new ArrayList<Integer>();
    static JPopupMenu jpop = new JPopupMenu();

    public Main() {
        setSize(700, 700);
        center = new Point(this.getWidth() / 2, this.getHeight() / 2);
        setTitle("Graph - Made by Alex Zhang");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setBackground(Color.black);
        addKeyListener(this);
        addMouseMotionListener(this);
        addMouseListener(this);
        for (int i = 2; i <= this.getWidth() / 2; i++) {
            if (getWidth() / 2 % i == 0) {
                possible.add(i);
            }
        }
        String[] list = new String[possible.size()];
        for (int i = 0; i < list.length; i++) {
            list[i] = possible.get(i).toString();
        }
        DefaultComboBoxModel model = new DefaultComboBoxModel(list);
        gui.jComboBox1.setModel(model);
        gui.jComboBox1.setSelectedItem(String.valueOf(scale));
        this.setVisible(true);
        JMenuItem zoomin = new JMenuItem("Zoom in");
        JMenuItem zoomout = new JMenuItem("Zoom out");
        jpop.add(zoomin);
        jpop.add(zoomout);
        zoomin.addActionListener((new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomin();
            }
        }));
        zoomout.addActionListener((new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomout();
            }
        }));
    }

    private void zoomout() {
        if (possible.indexOf(getScale()) != (possible.size() - 1)) {
            setScale(possible.get(possible.indexOf(getScale()) + 1));
            setGridRepaint(true);
            repaint();

            gui.jComboBox1.setSelectedItem(String.valueOf(scale));
        }
        jpop.setVisible(false);
    }

    private void zoomin() {
        if (possible.indexOf(getScale()) != 0) {
            setScale(possible.get(possible.indexOf(getScale()) - 1));
            setGridRepaint(true);
            repaint();

            gui.jComboBox1.setSelectedItem(String.valueOf(scale));
        }
        jpop.setVisible(false);
    }

    @Override
    public int getWidth() {
        return super.getWidth() * 3;
    }

    @Override
    public int getHeight() {
        return super.getHeight() * 3;
    }
    //increase efficiency by repainting only visible region
    //
    static boolean repaintGrid = true;
    BufferedImage lastgrid;

    static void setGridRepaint(boolean b) {
        repaintGrid = b;
    }

    @Override
    public void paint(Graphics g) {
        BufferedImage img2 = new BufferedImage(700 * 3, 700 * 3, BufferedImage.TRANSLUCENT);
        BufferedImage grid = null;
        BufferedImage combine = new BufferedImage(700, 700, BufferedImage.TRANSLUCENT);
        if (repaintGrid) {
            grid = (BufferedImage) createImage(700 * 3, 700 * 3);
            drawAdvGrid(grid.getGraphics());
            lastgrid = grid;
            repaintGrid = false;
        } else {
            grid = lastgrid;
        }
        Graphics g2 = img2.getGraphics();
        draw(g2);
        try {
            // if (grid != null) {
            //    g.drawImage(grid.getSubimage(screenx, screeny, 700, 700), 0, 0, null);
            //}
            //   g.drawImage(img2.getSubimage(screenx, screeny, 700, 700), 0, 0, null);
            combine.getGraphics().drawImage(grid.getSubimage(screenx, screeny, 700, 700), 0, 0, null);
            combine.getGraphics().drawImage(img2.getSubimage(screenx, screeny, 700, 700), 0, 0, null);
            g.drawImage(combine, 0, 0, null);
        } catch (Exception e) {
            System.out.println(screenx + " " + screeny);
            e.printStackTrace();
        }

    }
    int screenx = 700;
    int screeny = 700;

    static Point pointConvert(Point i) {
        int x = i.x;
        int y = -i.y;//Graphics coordinate plane y axis is reversed
        x *= scale; //Scale it up, default is too small .
        y *= scale;

        x += center.x;
        y += center.y; //center it
        return new Point(x, y);
    }

    static Point pointConvert(Point i, int custom) {
        int x = i.x;
        int y = -i.y;//Graphics coordinate plane y axis is reversed
        x *= custom; //Scale it up, default is too small .
        y *= custom;
        x += center.x;
        y += center.y; //center it
        return new Point(x, y);
    }

    boolean pointOnScreen(Point foo, int customscale) {
        Point zzz = pointConvert(original(foo), customscale);
        int x = zzz.x;
        int y = zzz.y;
        Rectangle r = new Rectangle(screenx + 20, screeny + 20, 670, 670); //+10 for padding, smaller tahn actual dimentions for padding
        if (r.contains(zzz)) {
            // System.out.println(".." + zzz.x);
            // System.out.println(".." + zzz.y);

            return true;
        }
        //    System.out.println(x);
        //   System.out.println(y);
        //   System.out.println(foo);
        // System.out.println(screenx);
        //  System.out.println(screeny);
        return false;
    }

    static Point original(Point converted) {
        return original(converted, scale);
    }

    static Point original(Point converted, int origscale) {
        int x = converted.x;
        int y = converted.y;
        x -= center.x;
        y -= center.y;
        x /= origscale;
        y /= origscale;
        y = -y;
        return new Point(x, y);
    }
    boolean drawMeasure = false;

    public void setDrawMeasure(boolean b) {
        drawMeasure = b;
    }

    public void draw(Graphics g) {
        drawGrid(g);
        g.setColor(Color.white);
        if (!(points.size() == 0 || points == null)) {
            makeTriangle(g, points);
            label(g, points);
            if (drawMeasure) {
                drawMeasure(g);
            }
        }
        g.setColor(Color.white);
        Font f = new Font("Segoe UI", Font.PLAIN, 12);
        g.setFont(f);
        if (screenx != 700 || screeny != 700) {
            g.drawString("Press C to recenter grid.", screenx + 50, screeny + 50);
            if (screeny == 0 || screenx == 0 || screenx == 1400 || screeny == 1400) {
                g.setColor(Color.white);
                g.drawString("End of screen.", screenx + 50, screeny + 70);
            }
        } else {
            g.drawString("Drag to explore around grid.", screenx + 50, screeny + 50);
        }
        g.drawString("Programmed by Alex Zhang.", screenx + 530, screeny + 50);
    }
    private static int scale = 30;

    static void setScale(int i) {
        scale = i;
    }

    static int getScale() {
        return scale;
    }

    void drawMeasure(Graphics g) {
        g.setColor(Color.orange);
        Font f = new Font("Segoe UI", Font.BOLD, 12);
        g.setFont(f);
        /*Point x = points.get(0);
        Point y = points.get(1);
        Point z = points.get(2);
        Point mxy = Util.midPoint(x, y);
        Point mxz = Util.midPoint(x, z);
        Point myz = Util.midPoint(y, z);
        g.drawString(String.valueOf(Util.truncate(Util.xy())), mxy.x, mxy.y);
        g.drawString(String.valueOf(Util.truncate(Util.xz())), mxz.x, mxz.y);
        g.drawString(String.valueOf(Util.truncate(Util.yz())), myz.x, myz.y);*/
        for (int i = 0; i < 3; i++) {
            Point asd = gui.points.get(i);
            Point nextone = null;
            nextone = gui.points.get(i + 1);
            Point middle = Util.midPoint(asd, nextone);
            g.drawString(String.valueOf(Util.truncate(Util.distance(original(asd), original(nextone), gui.round()))), middle.x, middle.y);
        }

    }
    private static int alpha = 50;

    public static int getAlpha() {
        return alpha;
    }

    public static void setAlpha(int i) {
        alpha = i;
    }

    void drawAdvGrid(Graphics g) {
        g.setColor(new Color(0, 255, 255, alpha));
        if (!gui.dontdrawGrid()) {
            for (int i = 0; i < this.getWidth() / scale; i++) {
                g.drawLine(0, i * scale, getWidth(), i * scale);
                g.setColor(new Color(192, 192, 192, 150));
                g.drawString("" + original(new Point(1050, i * scale)).y, 1055, i * scale);
                g.setColor(new Color(0, 255, 255, alpha));
            }

            for (int i = 0; i < this.getHeight() / scale; i++) { //vertical
                g.drawLine(i * scale, 0, i * scale, getHeight());
                g.setColor(new Color(192, 192, 192, 150));

                g.drawString("" + original(new Point(i * scale, 1050)).x, i * scale, 1065);
                //  System.out.println(String.valueOf(original(new Point(i * scale, 1050)).x).split("")[1]);
                //find out how to draw it sideways

                g.setColor(new Color(0, 255, 255, alpha));
            }
        }
    }
//static final double NINETY_DEGREES = Math.toRadians(90.0);

    //find out how to make it display position data info for moving grid
    void drawGrid(Graphics g) {
        g.setColor(Color.blue);
        g.drawLine(this.getWidth() / 2, 0, this.getWidth() / 2, this.getHeight());
        g.drawLine(0, this.getHeight() / 2, this.getWidth(), this.getHeight() / 2);
        //find out a way for location data to scroll with it.
        if (screeny > 1047) {
            g.drawLine(screenx, screeny + 60, screenx + 700, screeny + 60);
        }
        if (screeny < 355) {
            g.drawLine(screenx, screeny + 640, screenx + 700, screeny + 640);
        }
        if (screenx > 1049) {
            g.drawLine(screenx + 40, screeny, screenx + 40, screeny + 700);
        }
        if (screenx < 354) {
            g.drawLine(screenx + 660, screeny, screenx + 660, screeny + 700);
        }
        g.setColor(Color.red);
        Font f = new Font("Segoe UI", Font.BOLD + Font.ITALIC, 14);
        g.setFont(f);
        g.drawString("O", center.x - 10, center.y + 12); //Alleged "Origin" (0,0)
    }

    void makeTriangle(Graphics g, ArrayList<Point> p) {
        for (int i = 0; i < 3; i++) {
            Point a = p.get(i);
            Point b = p.get(i + 1);
            connectLine(g, a, b);
        }
        /*connectLine(g, p.get(0), p.get(1));
        connectLine(g, p.get(1), p.get(2));
        connectLine(g, p.get(2), p.get(0));*/
    }
    static String[] letters = {"X", "Y", "Z"};
    boolean labelAngles = false;

    void setLabelAngles(boolean b) {
        labelAngles = b;
    }

    void label(Graphics g, ArrayList<Point> p) {
        Font f = new Font("Segoe UI", Font.BOLD, 12);
        g.setFont(f);

        for (int i = 0; i < 3; i++) { // because of additional fourth copy point
            g.setColor(Color.green);
            g.drawString(letters[i] + " (" + original(p.get(i)).x + ", " + original(p.get(i)).y + ")", p.get(i).x - 20, p.get(i).y + 10);
            if (labelAngles) {
                g.setColor(Color.YELLOW);
                g.drawString(Util.truncate(Util.angle((p.get(i)), p.get(i + 1), p.get(i + 2), gui.round())) + "°", p.get(i).x - 20, p.get(i).y + 23);
            }
        }
    }

    static void refreshPointsToScale(int orig) {
        ArrayList<Point> temp = new ArrayList<Point>();
        for (Point i : points) {
            temp.add(original(i, orig));

        }
        points.clear();
        for (Point i : temp) {
            points.add(pointConvert(i));

        }

    }

    void connectLine(Graphics g, Point a, Point b) {
        g.drawLine(a.x, a.y, b.x, b.y);
    }
    static Main m;
    static gui gui;

    public static void main(String[] args) {
        gui = new gui();
        gui.setVisible(true);
        m = new Main();
    }

    // <editor-fold defaultstate="collapsed" desc="Listeners">
    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_C) {
            if (screenx != 700 || screeny != 700) {
                screenx = 700;
                screeny = 700;
                repaint();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_R) {
            repaint();
        } else if (e.getKeyCode() == KeyEvent.VK_P) {
            System.out.println("screenx: " + screenx + ", screeny: " + screeny);
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        if (initial != null) {
            if (jpop.isVisible()) {
                jpop.setVisible(false);
            }
            //  System.out.println("called");
            Point current = e.getPoint();
            int newx = screenx + ((initial.x - current.x) / (gui.getDrag() == 0 ? 2 : 1));
            int newy = screeny + ((initial.y - current.y) / (gui.getDrag() == 0 ? 2 : 1));
            initial = current;
            if (!(new Rectangle(0, 0, getWidth(), getHeight())).contains(new Point(newx, newy)) || newy + 700 > 2100 || newx + 700 > 2100) {
                return;
            }
            screenx = newx;
            screeny = newy;
            repaint();
        }
    }

    public void mouseMoved(MouseEvent e) {
        //  System.out.println("o-o");
        if (movescreen) {
            // System.out.println("zz");
        }
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            if (!jpop.isVisible()) {
                jpop.setVisible(true);
            }
            jpop.setLocation(e.getLocationOnScreen());
        } else if (e.getButton() == MouseEvent.BUTTON1) {
            if (jpop.isVisible()) {
                jpop.setVisible(false);
            }
        }
    }
    Point initial;
    boolean movescreen = false;

    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {


            initial = e.getPoint();
            //  System.out.println("zz2");
        } else {
            initial = null;
        }
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
        //   throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseExited(MouseEvent e) {
        //   throw new UnsupportedOperationException("Not supported yet.");
    }// </editor-fold>
}
