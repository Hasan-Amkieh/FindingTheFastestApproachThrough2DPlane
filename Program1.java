import java.util.*;

// Solving problem "Parkour Competition" at PJAM 5.0 by Hasan Amkieh

/*
Some test cases:
3 3
257
615
891

5 10
2454530236
3451223454
7347354256
9332343218
2632670323
* */

public class Program1 {

    static TreeNode treeRoot;
    static boolean[][] pointsPassed;
    static int xSize, ySize;
    static int[][] map;
    static int currentPrice;
    static int numberOfSteps = 0;
    static Point endPoint;
    static long startedAt = System.currentTimeMillis();

    static Approach cheapestApproach;
    static Approach currentApproach;

    static TreeNode[] treeRoots;

//    static Approach[] allPassedApproaches = new Approach[1000];
//    static int lastApproachAt = 0;

    static void fillTree(TreeNode parent) {

        Point pointToAdd;

        if (endPoint.equals(parent.point)) {
            //System.out.println("!!! " + parent.point);

            // In this question, we don't need to store all the possible approaches, on the cheapest approach price is required,
//            Approach toAddApp = new Approach();
//            allPassedApproaches[lastApproachAt++] = toAddApp;
//            toAddApp.points = Arrays.copyOf(currentApproach.points, currentApproach.points.length);
//            toAddApp.totalPrice = currentApproach.totalPrice;

            if (currentPrice < cheapestApproach.totalPrice) {
                currentApproach.totalPrice = currentPrice;
                currentApproach.numberOfSteps = numberOfSteps;
                cheapestApproach = currentApproach;
                currentApproach = new Approach();
                currentApproach.points = Arrays.copyOf(cheapestApproach.points, cheapestApproach.points.length);
            }
            return;

        }

        if (parent.point.y + 1 != ySize && !pointsPassed[parent.point.x][parent.point.y + 1] && parent.parent.point.y != parent.point.y + 1) { // Right

            pointsPassed[parent.point.x][parent.point.y + 1] = true;
            currentPrice += map[parent.point.x][parent.point.y + 1];
            pointToAdd = new Point(parent.point.x, parent.point.y + 1);
            //currentApproach.add(pointToAdd);
            //System.out.println("-> " + pointToAdd.toString());
            numberOfSteps++;
            fillTree(new TreeNode(parent, pointToAdd));
            numberOfSteps--;
            //System.out.println(pointToAdd.toString() + " ->");
            //currentApproach.remove();
            currentPrice -= map[parent.point.x][parent.point.y + 1];
            pointsPassed[parent.point.x][parent.point.y + 1] = false;

        }

        if (parent.point.x - 1 != -1 && !pointsPassed[parent.point.x - 1][parent.point.y] && parent.parent.point.x != parent.point.x - 1) { // Up

            pointsPassed[parent.point.x - 1][parent.point.y] = true;
            currentPrice += map[parent.point.x - 1][parent.point.y];
            pointToAdd = new Point(parent.point.x - 1, parent.point.y);
            numberOfSteps++;
            //currentApproach.add(pointToAdd);
            fillTree(new TreeNode(parent, pointToAdd));
            //currentApproach.remove();
            numberOfSteps--;
            currentPrice -= map[parent.point.x - 1][parent.point.y];
            pointsPassed[parent.point.x - 1][parent.point.y] = false;

        }

        if (parent.point.y - 1 != -1 && !pointsPassed[parent.point.x][parent.point.y - 1] && parent.parent.point.y != parent.point.y - 1) { // Left

            pointsPassed[parent.point.x][parent.point.y - 1] = true;
            currentPrice += map[parent.point.x][parent.point.y - 1];
            pointToAdd = new Point(parent.point.x, parent.point.y - 1);
            numberOfSteps++;
            //currentApproach.add(pointToAdd);
            fillTree(new TreeNode(parent, pointToAdd));
            //currentApproach.remove();
            numberOfSteps--;
            currentPrice -= map[parent.point.x][parent.point.y - 1];
            pointsPassed[parent.point.x][parent.point.y - 1] = false;

        }

        if (parent.point.x + 1 != xSize && !pointsPassed[parent.point.x + 1][parent.point.y] && parent.parent.point.x != parent.point.x + 1) { // Down

            pointsPassed[parent.point.x + 1][parent.point.y] = true;
            currentPrice += map[parent.point.x + 1][parent.point.y];
            pointToAdd = new Point(parent.point.x + 1, parent.point.y);
            numberOfSteps++;
            //currentApproach.add(pointToAdd);
            fillTree(new TreeNode(parent, pointToAdd));
            //currentApproach.remove();
            numberOfSteps--;
            currentPrice -= map[parent.point.x + 1][parent.point.y];
            pointsPassed[parent.point.x + 1][parent.point.y] = false;

        }

    }

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        String currentLine;

        currentLine = input.nextLine();
        xSize = Integer.parseInt(currentLine.substring(0, currentLine.indexOf(' ')));
        ySize = Integer.parseInt(currentLine.substring(currentLine.indexOf(' ') + 1));

        currentApproach = new Approach();cheapestApproach = new Approach();
        treeRoots = new TreeNode[xSize];

        pointsPassed = new boolean[xSize][ySize];
        map = new int[xSize][ySize];

        // Fill the map:
        for (int indexX = 0 ; indexX != xSize ; indexX++) {
            currentLine = input.nextLine();
            for (int indexY = 0 ; indexY != ySize ; indexY++) {
                map[indexX][indexY] = Integer.parseInt(String.valueOf(currentLine.charAt(indexY)));
            }
        }

        // Change it as the program changes...
        endPoint = new Point(xSize - 1, ySize - 1);

        /*treeRoot = new TreeNode(null, 1, 0);
        treeRoot.parent = treeRoot; // This is an exceptional case for the treeRoot ONLY!
        currentPrice = map[1][0];
        pointsPassed[1][0] = true;
        // NOTE: We have to create a treeRoot if the start point or end point changes,
        fillTree(treeRoot);*/

        // For the current solved question, the start point can be any point that is at the first column,
        for (int beginAtX = 0 ; beginAtX != xSize ; beginAtX++) {
            treeRoot = new TreeNode(null, beginAtX, 0);
            treeRoot.parent = treeRoot; // This is an exceptional case for the treeRoot ONLY!
            currentPrice = map[beginAtX][0];
            pointsPassed[beginAtX][0] = true;
            // NOTE: We have to create a treeRoot if the start point or end point changes,
            treeRoots[beginAtX] = treeRoot;
            fillTree(treeRoot);
            pointsPassed[beginAtX][0] = false;
        }

        long currentTime = System.currentTimeMillis();
        System.out.println("The cheapest approach is of price " + cheapestApproach.totalPrice);
        System.out.println("Number of steps for the cheapest approach " + cheapestApproach.numberOfSteps);
        System.out.println("Took " + ((currentTime - startedAt) / 1000) + " to find the cheapest path...");
//        System.out.println(lastApproachAt);
//        System.out.println("All possible approaches: ");
//        for (Approach app : allPassedApproaches) {
//            if (app == null) break;
//            System.out.println(Arrays.toString(app.points));
//        }

    }

}

class Approach {

    // For this Pjam question, we are not required to print out the path we hae taken
    // TODO: Use a normal array, because there is no need for an ArrayList, which will take more memory,
    Point[] points = new Point[Program1.xSize * Program1.ySize];
    int totalPrice = Integer.MAX_VALUE;
    int numberOfSteps = 0;
    int lastAddedAt = 0, lastObject = -1;

    boolean equals(Approach appToCompare) {

        boolean isIdentical = true;
        for (int index = 0 ; index != this.points.length ; index++) {
            if (this.points[index] == null || appToCompare.points[index] == null) {
                break;
            }
            if (!this.points[index].equals(appToCompare.points[index])) {
                isIdentical = false;
            }
        }

        return isIdentical;

    }

    Approach() {

        ;

    }

    void add(Point point) {

        points[lastAddedAt++] = point;
        lastObject = lastAddedAt - 1;

    }

    void remove() {

        if (lastObject == -1) return;
        points[lastObject--] = null;
        lastAddedAt--;

    }

//    static Approach copyApproach(Approach oldApproach) {
//
//        Approach newApproach = new Approach();
//
//        return newApproach;
//
//    }

}

class Point {

    int x, y;

    boolean equals(Point pointToCompare) {

        return this.x == pointToCompare.x && this.y == pointToCompare.y;

    }

    Point (int x, int y) {

        this.x = x;
        this.y = y;

    }

    public String toString() {

        return "(" + x + "," + y + ")";

    }

}

class TreeNode {

    TreeNode parent;
    Point point;
    // NOTE: At some applications, we need the childrenNodes, but here we dont,
    //TreeNode[] childrenNodes = new TreeNode[3];
    // The size is 3 because it should be three of the following: Right, left, up, down

    TreeNode(TreeNode parent, Point p) {

        this.point = p;
        this.parent = parent;

    }

    TreeNode(TreeNode parent, int x, int y) {

        point = new Point(x, y);
        this.parent = parent;

    }

    ;


}

