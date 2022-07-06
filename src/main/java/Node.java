import javax.swing.*;
import java.awt.*;

public class Node extends JButton {

    Node parent;
    int col;
    int row;
    int gCost;
    int hCost;
    int fCost;
    boolean start;
    boolean goal;
    boolean solid;
    boolean open;
    boolean checked;

    public Node(int col, int row) {
        this.col = col;
        this.row = row;

        setBackground(Color.WHITE);
        setBackground(Color.BLACK);
    }

    public void setAsStart() {
        setBackground(Color.BLUE);
        setText("Start");
        start = true;
    }

    public void setAsGoal() {
        setBackground(Color.YELLOW);
        setText("Goal");
        goal = true;
    }

    public void setAsSolid() {
        setBackground(Color.RED);
        solid = true;
    }

    public void setAsOpen() {
        open = true;
    }

    public void setAsChecked() {
        if (!start && !goal) {
            setBackground(Color.GRAY);
        }
        checked = true;
    }

    public void setAsPath() {
        setBackground(new Color(0, 102, 0));
        setForeground(Color.BLACK);
    }
}
