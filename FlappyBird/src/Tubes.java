import java.awt.*;
import java.util.ArrayList;

public class Tubes {
    public int x,y1,y2,ygap1, ygap2;
    public int tWidth = 60, tHeight1 = 500, tHeight2 = 700, gapWidth = 60, gapHeight = 150;
//    ArrayList<Rectangle> gap = new ArrayList<>();
    private Rectangle g, t1, t2;
    private boolean visited;
    ArrayList<Tubes> tubes = new ArrayList<>();
    public Tubes()
    {
        x = FlappyBird.WIDTH;
        y1 = (int) (Math.random() * (1 + 500) - 500);
        y2 = y1 + 650;
        ygap1 = y1+tHeight1;
        //recs for collisions and stuffs
        g = new Rectangle(x, ygap1, tWidth, y2-(y1+tHeight1));
        t1 = new Rectangle(x,y1,tWidth,tHeight1);
        t2 = new Rectangle(x,y1,tWidth,tHeight2);
        visited = false;

    }
    public Rectangle getG()
    {
        return g;
    }
    public Rectangle getT1()
    {
        return t1;
    }
    public Rectangle getT2()
    {
        return t2;
    }



    public boolean isVisited() {
        return visited;
    }
    public void setVisited(boolean s)
    {
        this.visited = s;
    }

    public void moving()
    {
        x -= 3;
        g.setRect(x,ygap1,tWidth,y2-(y1+tHeight1));
        t1.setRect(x,y1,tWidth,tHeight1);
        t2.setRect(x,y2,tWidth,tHeight2);
    }
    public void update(Graphics g)
    {
        g.setColor(Color.green);
        g.fillRect(x, y1, tWidth, tHeight1);
        g.fillRect(x, y2, tWidth, tHeight2);
        //uncomment to see the score area
//        g.setColor(Color.white);
//        g.fillRect(x, ygap1, tWidth, y2-(y1+tHeight1));
    }
}
