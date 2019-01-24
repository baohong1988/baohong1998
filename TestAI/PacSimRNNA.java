
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
    private List<PointCostPathHolder> sol;
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




            //cost from starting pos to every food pallet or init cost
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
            for(int i=0; i<num_food; i++)
            {
                PointCostPathHolder init_pch = new PointCostPathHolder(new List<Integer>().add(0).add(i+1),
                        init_pq.get(i).getCost());
                sol.addAll(NNA(cost, init_pch));
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
    public List<PointCostPathHolder> NNA(int[][] cost, PointCostPathHolder init_pch)
    {

        List<PointCostPathHolder> pch = new ArrayList<>();
        pch.add(init_pch);
        int num_iteration = cost.length-1;
        int i = 0;
        while (i < num_iteration)
        {
            List<Integer> minVertices = new ArrayList<>();
            PointCostPathHolder evaluating_path = pch.remove(0);
            int vertex = evaluating_path.lastVertex();
            int j=0;
            while (j<cost.length)
            {
                if(!evaluating_path.isVisted(j))
                {
                    if(minVertices.isEmpty())
                    {
                        minVertices.add(j);
                        continue;
                    }
                    int x = cost[vertex][j]; //cost at current path
                    int y = cost[vertex][minVertices.get(0)]; //cost at the head of the queue
                    //if current path has less cost than what at the head of the queue, enqueue new val and dequeue old one
                    if(x < y){
                        minVertices.add(j);
                        minVertices.remove(0);
                    }
                    else if(x == y)
                    {
                        minVertices.add(j);
                    }

                }

            }
            for(int l=0; l<minVertices.size(); l++)
            {
                PointCostPathHolder = new ()
            }
        }


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
class PointCostPathHolder{
    public List<Integer> cp;
    public int total_cost;
    public PointCostPathHolder(List<Integer> cp, int total_cost)
    {
        this.cp = cp;
        this.total_cost = total_cost;
    }

    public List<Integer> getCp() {
        return cp;
    }
    public int getTotal_cost()
    {
        return total_cost;
    }
    public boolean isVisted(int vertex)
    {
        if(this.cp.contains(vertex)) return true;
        return false;
    }
    public int lastVertex()
    {
        return cp.get(cp.size()-1);
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
