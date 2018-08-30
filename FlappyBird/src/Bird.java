import java.awt.*;

public class Bird
{
    public float x,y, vx, vy;
    public static final int RAD = 25;
    private Rectangle r;
    public Bird()
    {
        x = FlappyBird.WIDTH/2;
        y = FlappyBird.HEIGHT/2;
        r = new Rectangle(Math.round(x), Math.round(y), RAD*2, RAD*2);
    }
    public Rectangle getR()
    {
        return r;
    }
    public void jump()
    {
        vy = -8;
    }
    public void physic()
    {
        x+=vx;
        y+=vy;
        vy+=0.5f;
        r.setRect(Math.round(x),Math.round(y), RAD*2, RAD*2);

    }

    public void update(Graphics g)
    {
        g.setColor(Color.yellow);
        g.fillOval(Math.round(x), Math.round(y), RAD*2, RAD*2);
        //uncomment to see bird's collision area
//        g.setColor(Color.white);
//        g.drawRect(Math.round(x),Math.round(y),RAD*2,RAD*2);
    }
    public void reset()
    {
        x = FlappyBird.WIDTH/2;
        y = FlappyBird.HEIGHT/2;
        vx = vy = 0;
    }
}