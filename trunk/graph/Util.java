/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

import java.awt.Point;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author alex
 */
public class Util {

    public static double distance(Point a, Point b, boolean round) {
        int x1 = a.x;
        int y1 = a.y;
        int x2 = b.x;
        int y2 = b.y;
        if (!round) {
            return Math.sqrt((square(x1 - x2) + square(y1 - y2)));
        } else {
            return Math.round(Math.sqrt((square(x1 - x2) + square(y1 - y2))));
        }
    }

    public static double xy() {
        return distance(Main.original(gui.points.get(0)), Main.original(gui.points.get(1)), false);
    }

    public static double xz() {
        return distance(Main.original(gui.points.get(0)), Main.original(gui.points.get(2)), false);
    }

    public static double yz() {
        return distance(Main.original(gui.points.get(1)), Main.original(gui.points.get(2)), false);
    }

    public static double distance(Point a, Point b) {
        int x1 = a.x;
        int y1 = a.y;
        int x2 = b.x;
        int y2 = b.y;
        return Math.round(Math.sqrt((square(x1 - x2) + square(y1 - y2))));
    }

    public static double angle(Point a, Point b, Point c, boolean round) {
        double xy = distance(a, b, false);
        double yz = distance(b, c, false);
        double xz = distance(a, c, false);
        if (!round) {
            return Math.toDegrees(Math.acos(((square(xy) + square(xz) - square(yz)) / (2 * xy * xz))));
        } else {
            return Math.round(Math.toDegrees(Math.acos(((square(xy) + square(xz) - square(yz)) / (2 * xy * xz)))));
        }
    }

    public static double angle(double a, double b, double c, boolean round) {
        double xy = b;
        double yz = a;
        double xz = c;
        if (!round) {
            return Math.toDegrees(Math.acos(((square(xy) + square(xz) - square(yz)) / (2 * xy * xz))));
        } else {
            return Math.round(Math.toDegrees(Math.acos(((square(xy) + square(xz) - square(yz)) / (2 * xy * xz)))));
        }
    }

    /**
     * use looping instead
     * @param prime angle wanted
     * @return angle measure
     * @deprecated inefficient
     */
    @Deprecated
    public static double angle(Point prime) {
        int zzz = gui.points.indexOf(prime);
        Point a = null;
        Point b = null;
        Point c = null;
        switch (zzz) {
            case 0:
                a = gui.points.get(0);
                b = gui.points.get(1);
                c = gui.points.get(2);
                break;
            case 1:
                a = gui.points.get(1);
                b = gui.points.get(0);
                c = gui.points.get(2);
                break;
            case 2:
                a = gui.points.get(2);
                b = gui.points.get(0);
                c = gui.points.get(1);
                break;
        }
        return angle(Main.original(a), Main.original(b), Main.original(c), Main.gui.round());
    }

    public static double square(double a) {
        return Math.pow(a, 2);
    }

    public static Point midPoint(Point a, Point b) {
        int newX = (a.x + b.x) / 2;
        int newY = (a.y + b.y) / 2;
        return new Point(newX, newY);
    }

    public static int getTruncation() {
        return truncation;
    }

    public static void setTruncation(int truncation) {
        Util.truncation = truncation;
    }
    private static int truncation = 3;

    public static double truncate(double a) {
        // System.out.println(bd.floatValue());
        return truncate(a, truncation);
    }

    public static double truncate(double a, int scale) {
        try {
            BigDecimal bd = BigDecimal.valueOf(a);

            if (bd.scale() > scale) {
                bd = bd.setScale(scale, RoundingMode.HALF_EVEN);

            }
            // System.out.println(bd.floatValue());
            return bd.doubleValue();
        }catch (Exception e) {
            e.printStackTrace();
            return Double.NaN;
        }
    }
    /*public static void main(String[] args) {

    double d = 7.0710678118654755;
    System.out.println(BigDecimal.valueOf(d).setScale(3, RoundingMode.HALF_EVEN).floatValue());
    System.out.println(truncate(d));
    //System.out.println(((float) distance(new Point(0, 5), new Point(5, 0), false)));


    }*/
}
