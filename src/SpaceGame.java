import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;

class BasicVar {
    public static final int SCREEN_WIDTH;
    public static final int SCREEN_HEIGHT;
    public static final String TITLE;
    public static JFrame WINDOW;
    public enum Modes {
        welcome, game, over
    }
    public static Modes current_mode;


    static {
        SCREEN_WIDTH = 900;
        SCREEN_HEIGHT = 715;
        TITLE = "Space Invaders";
        current_mode = Modes.game;
        WINDOW = new JFrame(BasicVar.TITLE);
        WINDOW.setBounds(200, 20, SCREEN_WIDTH, SCREEN_HEIGHT);
        WINDOW.setIgnoreRepaint(false);
        WINDOW.setVisible(true);
        WINDOW.setResizable(false);
        WINDOW.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

abstract class Plane {
    protected int plane_w;
    protected int plane_h;
    protected int plane_x;
    protected int plane_y;
    protected int bullet_w;
    protected int bullet_h;
    protected ArrayList<ArrayList<Integer>> bullets = new ArrayList<>();

    public abstract void drawPlane(Graphics g);
    public abstract void moveBullets();
    public abstract void addNewBullets();
    public abstract void deleteExtraBullets();
}

class Player extends Plane {

    public Player() {
        this.plane_w = 50;
        this.plane_h = 30;
        this.plane_x = BasicVar.SCREEN_WIDTH / 2 - this.plane_w / 2;
        this.plane_y = 600;
        this.bullet_w = 10;
        this.bullet_h = 20;
        this.bullets.add(new ArrayList<>(Arrays.asList(this.plane_x + this.plane_w / 2 - this.bullet_w / 2, this.plane_y)));
    }

    @Override
    public void drawPlane(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(this.plane_x, this.plane_y, this.plane_w,this.plane_h);
    }

    public void drawBullets(Graphics g) {
        g.setColor(Color.YELLOW);
        for(ArrayList<Integer> bullet : this.bullets)
            g.fillRect(bullet.get(0), bullet.get(1), this.bullet_w, this.bullet_h);
    }

    @Override
    public void moveBullets() {
        for(ArrayList<Integer> bullet : this.bullets)
            bullet.set(1, bullet.get(1) - 1);
    }

    @Override
    public void addNewBullets() {
        ArrayList<Integer> bullet = this.bullets.get(this.bullets.size() - 1);
        if(this.plane_y - bullet.get(1) > 40)
            this.bullets.add(new ArrayList<>(Arrays.asList(this.plane_x + this.plane_w / 2 - this.bullet_w / 2, this.plane_y)));

    }

    @Override
    public void deleteExtraBullets() {
        this.bullets.removeIf(bullet -> bullet.get(1) < -this.bullet_w);
    }
}

class Gameplay extends JPanel implements KeyListener, ActionListener {

    private Player player = new Player();
    private int change_x = 0;

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if(BasicVar.current_mode == BasicVar.Modes.game) {
            player.drawPlane(g);
            player.drawBullets(g);
            player.addNewBullets();
            player.moveBullets();
            player.deleteExtraBullets();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(BasicVar.current_mode == BasicVar.Modes.game) {
            player.plane_x += this.change_x;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int c = e.getKeyCode();

        if(BasicVar.current_mode == BasicVar.Modes.game) {
            if (c == KeyEvent.VK_RIGHT)
                this.change_x = 2;
            if (c == KeyEvent.VK_LEFT)
                this.change_x = -2;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        this.change_x = 0;
    }
}

public class SpaceGame {
    public static void main(String[] args) {
        Gameplay gameplay = new Gameplay();
        BasicVar.WINDOW.add(gameplay);
        BasicVar.WINDOW.addKeyListener(gameplay);

    }
}