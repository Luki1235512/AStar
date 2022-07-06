import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Panel extends JPanel {

    // SCREEN SETTINGS
    final int maxCol = 15;
    final int maxRow = 10;
    final int nodeSize = 70;
    final int screenWidth = nodeSize * maxCol;
    final int screenHeight = nodeSize * maxRow;

    // NODE
    Node[][] node = new Node[maxCol][maxRow];
    Node startNode;
    Node goalNode;
    Node currentNode;
    ArrayList<Node> openList = new ArrayList<>();
    ArrayList<Node> checkList = new ArrayList<>();

    // OTHERS
    boolean goalReached = false;
    int step = 0;

    public Panel() {

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setLayout(new GridLayout(maxRow, maxCol));
        this.addKeyListener(new KeyHandler(this));
        this.setFocusable(true);

        // PLACE NODES
        int col = 0;
        int row = 0;

        while (col < maxCol && row < maxRow) {
            node[col][row] = new Node(col, row);
            node[col][row].setEnabled(false);
            this.add(node[col][row]);

            col++;
            if (col == maxCol) {
                col = 0;
                row++;
            }
        }

        // SET START AND GOAL NODE
        setStartNode();
        setGoalNode();

        // SET SOLID NODES
        setSolidNodes();

        // SET COST
        setCostOnNodes();
    }

    private void setStartNode() {
        Random random = new Random();
        int col = random.nextInt(3);
        int row = random.nextInt(9);

        node[col][row].setAsStart();
        startNode = node[col][row];
        currentNode = startNode;
    }

    private void setGoalNode() {
        Random random = new Random();
        int col = random.nextInt(14 - 11) + 11;
        int row = random.nextInt(9);

        node[col][row].setAsGoal();
        goalNode = node[col][row];
    }

    private void setSolidNodes() {

        Random random = new Random();
        int i = 0;
        while (i < 20) {
            int col = random.nextInt(12 - 4) + 6;
            int row = random.nextInt(9);
            if (!node[col][row].solid && !node[col][row].goal && !node[col][row].start) {
                node[col][row].setAsSolid();
                i++;
            }

        }
    }

    private void setCostOnNodes() {
        int col = 0;
        int row = 0;

        while (col < maxCol && row < maxRow) {
            getCost(node[col][row]);
            col++;
            if (col == maxCol) {
                col = 0;
                row++;
            }
        }
    }

    private void getCost(Node node) {

        // GET G COST (Distance from the start node)
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;

        // GET H COST (Distance from the goal node)
        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;

        // GET F COST (Total cost)
        node.fCost = node.gCost + node.hCost;

        // DISPLAY THE COST ON NODE
        if (node != startNode && node != goalNode) {
            node.setText("<html>F: " + node.fCost + "<br>G: " + node.gCost + "<br>H: " + node.hCost + "</html>");
        }
    }

    public void search() {
        int col = currentNode.col;
        int row = currentNode.row;

        currentNode.setAsChecked();
        checkList.add(currentNode);
        openList.remove(currentNode);

        // OPEN THE UP NODE
        if (row - 1 >= 0) {
            openNode(node[col][row - 1]);
        }

        // OPEN THE LEFT NODE
        if (col - 1 >= 0) {
            openNode(node[col - 1][row]);
        }

        // OPEN THE DOWN NODE
        if (row + 1 < maxRow) {
            openNode(node[col][row + 1]);
        }

        //OPEN THE RIGHT NODE
        if (col + 1 < maxCol) {
            openNode(node[col + 1][row]);
        }

        // FIND THE BEST NODE
        int bestNodeIndex = 0;
        int bestNodeFCost = Integer.MAX_VALUE;

        for (int i = 0; i < openList.size(); i++) {

            // Check if this node's F cost is better
            if (openList.get(i).fCost < bestNodeFCost) {
                bestNodeIndex = i;
                bestNodeFCost = openList.get(i).fCost;
            }

            // If F cost is equal, check the G cost
            else if (openList.get(i).fCost == bestNodeFCost) {
                if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
                    bestNodeIndex = i;
                }
            }
        }

        // After the loop, we get the best node which is our next step
        currentNode = openList.get(bestNodeIndex);
        if (currentNode == goalNode) {
            goalReached = true;
            trackThePath();
        }
    }

    public void manualSearch() {

        if (!goalReached && step < 1000) {
            search();
        }

        step++;
    }

    public void autoSearch() {

        while (!goalReached && step < 1000) {
            search();
        }

        step++;
    }

    private void openNode(Node node) {

        if (!node.open && !node.checked && !node.solid) {

            // If the node in not open yet, add it to the open list
            node.setAsOpen();
            node.parent = currentNode;
            openList.add(node);
        }
    }

    private void trackThePath() {

        // Backtrack and draw the best path
        Node current = goalNode;

        while (current != startNode) {
            current = current.parent;
            if (current != startNode) {
                current.setAsPath();
            }
        }
    }

}
