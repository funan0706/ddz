import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Card extends JLabel implements MouseListener {
    Main main;
    String name;
    boolean up;
    boolean canClick=false;
    boolean clicked=false;

    public Card(Main m,String name,boolean up){
        this.main=m;
        this.name=name;
        this.up=up;
        if(this.up)
            this.turnFront();
        else
            this.turnRear();

        this.setSize(71,96);
        this.setVisible(true);
        this.addMouseListener(this);
    }

    public void turnRear() {
        this.setIcon(new ImageIcon("images/rear.gif"));
        this.up=true;
    }

    public void turnFront() {
        this.setIcon(new ImageIcon("images/" + name + ".gif"));
        this.up=false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (canClick) {
            Point from = this.getLocation();
            int step;
            if (clicked)
                step = -20;
            else {
                step = 20;
            }
            clicked = !clicked;
            Common.move(this, from, new Point(from.x, from.y - step));
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
