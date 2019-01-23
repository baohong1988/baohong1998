
import java.awt.Point;
import java.util.*;
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
    private List<Integer> visited;
    private List<PointCost> init_pq;
    private List<SolutionPath> sol;
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
            //initialize cost table
            //food is a list of Point
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
            //remove starting location from food array
            food.remove(0);
            System.out.println("\n");
            //print out food array
            System.out.println("Food Array:\n");
            int num_food = food.size();
            for(int i=0; i<num_food; i++)
            {
                System.out.printf("%2d : (%d,%d)\n", i, (int) food.get(i).getX(), (int) food.get(i).getY() );
            }
            System.out.println();
            //cost from starting pos to every food pallet
            //init_pq is a list of custom class PointCost
            init_pq = new ArrayList<PointCost>();
            System.out.println("Population at step 1 :");
            for(int i=0; i<num_food; i++)
            {
                PointCost pointcost = new PointCost(food.get(i), i+1, cost[0][i+1]);
                init_pq.add(pointcost);
            }
            Collections.sort(init_pq, new PointCostComparator());
            Iterator itr = init_pq.iterator();
            int i = 0;
            while (itr.hasNext() && i < init_pq.size())
            {
                PointCost next = (PointCost) itr.next();
                int c = next.getCost();
                Point d = next.getDestination();
                System.out.printf("%4d : cost=%d : [(%d,%d),%d]", i, c, (int) d.getX()
                        , (int) d.getY(), c);
                i++;
                System.out.println("\n");
            }
            //Applying NNA to every vertex
            sol = new ArrayList<>();
            for(int i=0; i<num_food; i++)
            {
                sol.addAll(NNA(init_pq.get(i).getPosInCost(), cost))
            }


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
    public List<SolutionPath> NNA(Point vertex, int[][] cost, )
    {

    }

}
class PointCostComparator implements Comparator<PointCost>{
    public int compare(PointCost p1, PointCost p2)
    {
        if(p1.cost > p2.cost)
            return 1;
        else if(p1.cost < p2.cost)
            return -1;
        return 0;
    }
}

class PointCost{
    public Point destination;
    public int vertex;
    public int cost;

    public PointCost(Point destination, int vertex, int cost)
    {
        this.vertex = vertex;
        this.destination = destination;
        this.cost = cost;
    }
    public Point getDestination(){ return this.destination; }
    public int getCost(){ return this.cost; }
    public int getVertex() { return this.vertex; }
}
