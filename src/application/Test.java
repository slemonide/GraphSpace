package application;

import model.space.Node;

import java.awt.*;
import java.util.*;

class Test {
    public static void main(String[] args) {
        Node node = new Node(10,5);
        System.out.println(1 / 2);

    }

    // RANGE: even naturals
    // EFFECTS: produce a random even natural
    public int randomNatural() {
        return 1; // stub
    }

    // RANGE: Point (x, y) such that p1.x <= x <= p2.x and p1.y <= y <= p2.y
    // EFFECTS: produce a point in the given range
    public Point randomPointInRange(Point p1, Point p2) {
        return null;
    }

    // RANGE: Color (r, g, b) such that img.minRed() <= r <= img.maxRed(),
    // img.minGreen() <= g <= img.maxGreen(), img.minBlue() <= g <= img.maxBlue()
    // EFFECTS: produce the algebraic average of colors in the image
    public Color averageColor(Image img) {
        return null;
    }

    // RANGE: image such that averageColor(img) == color
    // EFFECTS: produce an image with given algebraic average of colors in the image
    public Image imageFromAverage(Color color) {
        return null;
    }

    // RANGE: ApproxArea such that if |d^2f/dx^2| <= K then
    // |Area - ApproxArea| <= K(xs.last - xs.first)/24 * (xs.spacing)^2
    // EFFECTS: compute the area under the given points using the midpoint rule
    public Double midpointIntegral(Queue<Double> xs, Queue<Double> ys) {
        return 0.0;
    }

    // RANGE: ApproxArea such that if |d^2f/dx^2| <= K then
    // |Area - ApproxArea| <= K(xs.last - xs.first)/12 * (xs.spacing)^2
    // EFFECTS: compute the area under the given points using the trapezoid rule
    public Double trapezoidIntegral(Queue<Double> xs, Queue<Double> ys) {
        return 0.0;
    }

    // RANGE: ApproxArea such that if |d^4f/dx^4| <= K then
    // |Area - ApproxArea| <= K(xs.last - xs.first)/180 * (xs.spacing)^4
    // EFFECTS: compute the area under the given points using the Simpson's rule
    public Double simpsonIntegral(Queue<Double> xs, Queue<Double> ys) {
        return 0.0;
    }


}
