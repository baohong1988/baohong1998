
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
 * Repetitive Nearest Neighbor Algorithm Agent
 * University of Central Florida
 * CAP 4600 - Spring 2019
 * Authors: Bao Hong, Davis Rollman
 *
 */
public class PacSimRNNA implements PacAction {

    private List<Point> path;
    private List<Point> food;
    private List<Integer> visited;
    private List<PointCost> unsorted_init_pq;
    private List<PointCost> init_pq;
    private List<PointCostPathHolder> sol;
    private List<Point> finalSolution;
    private int simTime;
    private int foodEaten;

    public PacSimRNNA( String fname ) {
        PacSim sim = new PacSim( fname );
        sim.init(this);
    }

    public static void main( String[] args ) {
        System.out.println("\nTSP using Repetitive Nearest Neighbor agent by Bao Hong and Davis Rollman:");
        System.out.println("\nMaze : " + args[ 0 ] + "\n" );
        new PacSimRNNA( args[ 0 ] );
    }

    @Override
    public void init() {
        simTime = 0;
        foodEaten = 0;
        path = new ArrayList<Point>();
    }

    @Override
    public PacFace action( Object state ) {
        PacCell[][] grid = (PacCell[][]) state;
        PacmanCell pc = PacUtils.findPacman( grid );

        // make sure Pac-Man is in this game
        if( pc == null ) return null;
        if(simTime == 0)
        {
            long startTime = System.currentTimeMillis();
            RNNAComputation(grid,pc);
            long endTime = System.currentTimeMillis();
            System.out.printf("\nTime to generate plan: %d msec\n", endTime - startTime);
            System.out.println("\nSolution moves:\n");

        }




        // if current path completed (or just starting out),
        // select a the nearest food using the city-block
        // measure and generate a path to that target



        if( path.isEmpty() ) {
            //Point tgt = PacUtils.nearestFood( pc.getLoc(), grid);
            Point tgt = finalSolution.get(foodEaten++);
            path = BFSPath.getPath(grid, pc.getLoc(), tgt);


//            System.out.println("Pac-Man currently at: [ " + pc.getLoc().x
//                    + ", " + pc.getLoc().y + " ]");
//            System.out.println("Setting new target  : [ " + tgt.x
//                    + ", " + tgt.y + " ]");
        }

        // take the next step on the current path

        Point next = path.remove( 0 );
        PacFace face = PacUtils.direction( pc.getLoc(), next );
        System.out.printf( "%5d : From [ %2d, %2d ] go %s%n",
                ++simTime, pc.getLoc().x, pc.getLoc().y, face );
        return face;
    }
    public void RNNAComputation(PacCell[][] grid, PacmanCell pc)
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
        unsorted_init_pq = new ArrayList<PointCost>();
        unsorted_init_pq.addAll(init_pq);
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
            System.out.println();
        }


        System.out.println("\nPopulation at step 10 :");


        sol = new ArrayList<>();
        //Applying NNA to every vertex
        for(int j=0; j<num_food; j++)
        {
            List<Integer> initPath = new ArrayList<>();
            List<Integer> listCost = new ArrayList<>();
            initPath.add(0);
            initPath.add(init_pq.get(j).getVertex());
            listCost.add(0);
            listCost.add(cost[0][init_pq.get(j).getVertex()]);
            PointCostPathHolder init_pch = new PointCostPathHolder(initPath,
                    init_pq.get(j).getCost(), listCost);
            sol.addAll(NNA(cost, init_pch));
        }
        Collections.sort(sol, new PointCostPathHolderComparator());

        for(int j=0; j<sol.size(); j++)
        {
            //System.out.print("Path "+ j +": " );
            printPath(sol.get(j), unsorted_init_pq, j);
        }
        finalSolution = new ArrayList<>();
        for(int j=1; j<=num_food; j++)
        {
            int vertex = sol.get(0).getCp().get(j);
            Point addingPt = food.get(vertex-1);
            finalSolution.add(addingPt);
        }

    }


    public void printPath(PointCostPathHolder holder, List<PointCost> unsorted_init_pq, int path_num)
    {
        List<Integer> pathHolder = holder.getCp();
        List<Integer> listCost = holder.getListCost();
        System.out.printf("%4d : cost=%d : ",path_num, holder.getTotal_cost());
        for(int i=1; i<pathHolder.size(); i++)
        {
            int ver = pathHolder.get(i);
            Point d = unsorted_init_pq.get(ver - 1).getDestination();
            int c = listCost.get(i);
            System.out.printf("[(%d,%d),%d]", (int) d.getX(), (int) d.getY(), c);

        }
        System.out.println();
    }

    public List<PointCostPathHolder> NNA(int[][] cost, PointCostPathHolder init_pch)
    {

        List<PointCostPathHolder> pch = new ArrayList<>();
        pch.add(init_pch);
        int num_vertices = cost.length;
        while (pch.get(0).getSize() < num_vertices)
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
                        ++j;
                        continue;
                    }
                    int x = cost[vertex][j]; //cost at current path
                    //int y = cost[vertex][minVertices.get(0)]; //cost at the head of the queue
                    //if current path has less cost than what at the head of the queue, enqueue new val and dequeue old one

                    while(!minVertices.isEmpty() && cost[vertex][minVertices.get(0)] > x){
                        minVertices.remove(0);
                    }
                    if(minVertices.isEmpty() || cost[vertex][minVertices.get(0)] == x)
                        minVertices.add(j);


                    j++;

                }
                else
                    j++;

            }
            //for(int i : minVertices)
                //System.out.print("Min "+ i + " ");
            //System.out.println();
            for(int l=0; l<minVertices.size(); l++)
            {
                List<Integer> listCost = new ArrayList<>();
                List<Integer> cp = new ArrayList<>();
                cp.addAll(evaluating_path.getCp());
                //for(int i : cp)
                //    System.out.print(i+" ");
                //System.out.println();
                int splitVer = minVertices.get(l);
                int cost_adding = cost[vertex][splitVer];
                int totalCost = evaluating_path.getTotal_cost() + cost_adding;
                listCost.addAll(evaluating_path.getListCost());
                cp.add(splitVer);
                listCost.add(cost_adding);
                PointCostPathHolder newPath = new PointCostPathHolder(cp, totalCost, listCost);
                //newPath.printCp();
                pch.add(newPath);
            }

        }
        return pch;


    }

}
class PointCostPathHolderComparator implements Comparator<PointCostPathHolder>{
    public int compare(PointCostPathHolder pch1, PointCostPathHolder pch2)
    {
        if(pch1.total_cost > pch2.total_cost)
            return 1;
        else if(pch1.total_cost < pch2.total_cost)
            return -1;
        return 0;
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
    public List<Integer> listCost;
    public int total_cost;
    public int size;
    public PointCostPathHolder(List<Integer> cp, int total_cost, List<Integer> listCost)
    {
        this.cp = cp;
        this.listCost = listCost;
        this.total_cost = total_cost;
        this.size = cp.size();
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

    public int getSize() {
        return size;
    }
    public void printCp(){
        for(int i=0; i<this.cp.size(); i++)
        {
            System.out.print(cp.get(i)+" ");
        }
        System.out.print("Cost : " + this.total_cost);
        System.out.println();
    }
    public int lastVertex()
    {
        return cp.get(cp.size()-1);
    }

    public List<Integer> getListCost() {
        return listCost;
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
