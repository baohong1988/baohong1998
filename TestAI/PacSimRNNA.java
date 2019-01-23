
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import pacsim.BFSPath;
import pacsim.PacAction;
import pacsim.PacCell;
import pacsim.PacFace;
import pacsim.PacSim;
import pacsim.PacUtils;
import pacsim.PacmanCell;

/**
 * Simple Re-planning Search Agent
 * @author Dr. Demetrios Glinos
 */
public class PacSimRNNA implements PacAction {

    private List<Point> path;
    private List<Point> food;
    private int simTime;

    public PacSimRNNA( String fname ) {
        PacSim sim = new PacSim( fname );
        sim.init(this);
    }

    public static void main( String[] args ) {
        System.out.println("\nTSP using simple replanning agent by Dr. Demetrios Glinos:");
        System.out.println("\nMaze : " + args[ 0 ] + "\n" );
        new PacSimRNNA( args[ 0 ] );
    }

    @Override
    public void init() {
        simTime = 0;
        path = new ArrayList();
    }

    @Override
    public PacFace action( Object state ) {
        PacCell[][] grid = (PacCell[][]) state;
        PacmanCell pc = PacUtils.findPacman( grid );

        // make sure Pac-Man is in this game
        if( pc == null ) return null;
        if(simTime == 0)
        {
            System.out.println("\nCost Table:\n");
            food = new ArrayList<>();
            food.add(pc.getLoc());
            food.addAll(PacUtils.findFood(grid));
            int[][] cost = new int[food.size()][food.size()];

            for(int i=0; i<cost.length; i++)
            {
                for(int j=0; j<cost.length; j++)
                {
                    cost[i][j] = BFSPath.getPath(grid, food.get(i), food.get(j)).size();
                    System.out.printf("%4d", cost[i][j]);
                }
                System.out.println();

            }
            food.remove(0);
            System.out.println("\n");
            System.out.println("Food Array:\n");
            for(int i=0; i<food.size(); i++)
            {
                System.out.printf("%2d : (%d,%d)\n", i, (int) food.get(i).getX(), (int) food.get(i).getY() );
            }
            System.out.println();
            System.out.println("Population at step 1 :");
           


        }

        // if current path completed (or just starting out),
        // select a the nearest food using the city-block
        // measure and generate a path to that target

        if( path.isEmpty() ) {
            Point tgt = PacUtils.nearestFood( pc.getLoc(), grid);
            path = BFSPath.getPath(grid, pc.getLoc(), tgt);

            System.out.println("Pac-Man currently at: [ " + pc.getLoc().x
                    + ", " + pc.getLoc().y + " ]");
            System.out.println("Setting new target  : [ " + tgt.x
                    + ", " + tgt.y + " ]");
        }

        // take the next step on the current path

        Point next = path.remove( 0 );
        PacFace face = PacUtils.direction( pc.getLoc(), next );
        System.out.printf( "%5d : From [ %2d, %2d ] go %s%n",
                ++simTime, pc.getLoc().x, pc.getLoc().y, face );
        return face;
    }

}