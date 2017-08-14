package com.kit.api.util;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Credits to http://www.sanfoundry.com/java-program-implement-quick-hull-algorithm-find-convex-hull/
 */
public class QuickHuller {

    public static Polygon hull(List<Point> points) {
        Polygon hullPoly = new Polygon();
        if (points.size() < 3) {
            points.forEach(p -> hullPoly.addPoint(p.x, p.y));
            return hullPoly;
        }
        List<Point> hullPoints = new ArrayList<>();
        Point leftMost = null;
        Point rightMost = null;
        int leftX = Integer.MAX_VALUE;
        int rightX = Integer.MIN_VALUE;
        for (Point point : points) {
            if (point.x < leftX) {
                leftMost = point;
                leftX = point.x;
            }
            if (point.x > rightX) {
                rightMost = point;
                rightX = point.x;
            }
        }
        if (leftMost == null || rightMost == null) {
            return null;
        }

        hullPoints.add(leftMost);
        hullPoints.add(rightMost);
        points.remove(leftMost);
        points.remove(rightMost);

        List<Point> right = new ArrayList<>();
        List<Point> left = new ArrayList<>();
        for (Point point : points) {
            int position = getPosition(leftMost, rightMost, point);
            if (position == 1) {
                right.add(point);
            } else if (position == -1) {
                left.add(point);
            }
        }

        hull(leftMost, rightMost, right, hullPoints);
        hull(rightMost, leftMost, left, hullPoints);

        hullPoints.forEach(p -> hullPoly.addPoint(p.x, p.y));

        return hullPoly;
    }

    private static void hull(Point leftMost, Point rightMost, List<Point> points, List<Point> hullPoints) {
        if (points.size() == 0) {
            return;
        }
        int rightPos = hullPoints.indexOf(rightMost);
        if (points.size() == 0) {
            Point p = points.get(0);
            points.clear();
            hullPoints.add(p);
        } else {
            Point furthestPoint = null;
            int dist = Integer.MIN_VALUE;

            for (Point point : points) {
                int pDist = distance(leftMost, rightMost, point);
                if (pDist > dist) {
                    furthestPoint = point;
                    dist = pDist;
                }
            }
            if (furthestPoint != null) {
                hullPoints.add(rightPos, furthestPoint);
                points.remove(furthestPoint);

                Point finalFurthestPoint = furthestPoint;

                List<Point> toLeft = points.stream()
                        .filter(point -> getPosition(leftMost, finalFurthestPoint, point) == 1).collect(Collectors.toList());
                List<Point> toRight = points.stream()
                        .filter(point -> getPosition(finalFurthestPoint, rightMost, point) == 1).collect(Collectors.toList());

                hull(leftMost, furthestPoint, toLeft, hullPoints);
                hull(furthestPoint, rightMost, toRight, hullPoints);
            }
        }
    }

    private static int distance(Point leftMost, Point rightMost, Point point) {
        int x = rightMost.x - leftMost.x;
        int y = rightMost.y - leftMost.y;
        int diff = x * (leftMost.y - point.y) - y * (leftMost.x - point.x);
        if (diff < 0) {
            return -diff;
        }

        return diff;
    }

    private static int getPosition(Point leftMost, Point rightMost, Point point) {
        int pos = (rightMost.x - leftMost.x) * (point.y - leftMost.y) - (rightMost.y - leftMost.y) * (point.x - leftMost.x);
        return Integer.compare(pos, 0);
    }
}
