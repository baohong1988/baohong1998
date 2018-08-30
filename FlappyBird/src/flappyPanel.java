import javax.swing.*;
import java.awt.*;

import java.util.*;

class flappyPanel extends JPanel
{
    private Bird bird;
    private Tubes t;
    private ArrayList<Tubes> tubes;
    private JLabel scoreBoard, instruction;
    private FlappyBird game;


    public flappyPanel(FlappyBird game, Bird bird, ArrayList<Tubes> tubes)
    {
        this.game = game;
        this.bird = bird;
        this.tubes = tubes;

        setBackground(Color.cyan);
        scoreBoard = new JLabel(""+ game.score);
        scoreBoard.setBounds(game.WIDTH/2,25,100,100);
        scoreBoard.setFont(new java.awt.Font("Impact", Font.BOLD, 50));
        scoreBoard.setForeground(Color.white);
        instruction = new JLabel("Press SpaceBar to play");
        instruction.setBounds(game.WIDTH/2-200, (int)bird.y - 100, game.WIDTH,100);
        instruction.setFont(new java.awt.Font("Comic Sans MS", Font.PLAIN, 50));
        instruction.setForeground(Color.black);


    }
    public void drawScoreBoard(boolean start)
    {
        if(start)
            this.add(scoreBoard);
        else
            this.remove(scoreBoard);
    }
    public void displayInstruction(boolean start)
    {
        if(!start)
            this.add(instruction);
        else
            this.remove(instruction);

    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        bird.update(g);
        for (Tubes t : tubes)
            t.update(g);
        scoreBoard.setBounds(game.WIDTH/2,25,100,100);
        scoreBoard.setText(String.valueOf(game.score));


    }

}