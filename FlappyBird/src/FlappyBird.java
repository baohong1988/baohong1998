import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Time;
import java.util.*;
import javax.swing.Timer;

public class FlappyBird implements ActionListener, KeyListener
{

    public final static int WIDTH = 1280, HEIGHT = 720, FPS = 60;

    private Bird bird;
    private flappyPanel panel;
    private JFrame frame;
    private ArrayList<Tubes> tubes;
    private Tubes tube;
    private int time, scroll;
    public int score = 0;
    private Timer timer;

    private boolean paused, started, gameOver;

    public FlappyBird()
    {
        //initialize objects
        frame = new JFrame();
        bird = new Bird();
        tube = new Tubes();
        tubes = new ArrayList<>();
        tubes.add(tube);
        panel = new flappyPanel(this, bird, tubes);

        //init frame
        frame.add(panel);
        frame.setTitle("Flappy Bird v0.1");
        frame.setSize(WIDTH, HEIGHT);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.addKeyListener(this);

        paused = true;
        started = false;
        gameOver = false;

        //setFPS
        timer = new Timer(1000/FPS, this);
        timer.start();

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        panel.repaint();

        if(!paused)
        {
            started = true;
            panel.drawScoreBoard(started);
            panel.displayInstruction(started);
            bird.physic();
            //tubes interactions

            for (Iterator<Tubes> iterator = tubes.iterator(); iterator.hasNext();)
            {
                Tubes t = iterator.next();
                t.moving();

                //score
                if(t.getG().intersects(bird.getR()) && !t.isVisited())
                {
                    t.setVisited(true);
                    score++;
                }
                //hit the tubes
                if(t.getT1().intersects(bird.getR()) || t.getT2().intersects(bird.getR()))
                {
                    paused = true;
                    gameOver = true;
                }
                if(t.x <= 0-t.tWidth)
                    iterator.remove();
            }

            //fall below the screen == lose
            if(bird.y > HEIGHT)
            {
                gameOver = true;
                paused = true;
            }
            //new tubes every 150 scroll units
            if(scroll % 150 == 0)
            {

                tubes.add(new Tubes());
		        //tubes.remove(0);
            }
            System.out.println(score);

        }
        scroll++;
        if(paused)
        {
            if(!started)
                panel.displayInstruction(false);
            if(gameOver) {
                JOptionPane.showMessageDialog(frame, "You Lose!!!!");
                bird.reset();
                tubes.clear();
                scroll = 0;
                score = 0;
                started = false;
                gameOver = false;
                panel.drawScoreBoard(started);

            }

        }


    }

    public static void main(String[] args)
    {
        new FlappyBird();
    }



    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE)
        {
            bird.jump();
            paused = false;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
