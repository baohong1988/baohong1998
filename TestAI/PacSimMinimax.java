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
import pacsim.WallCell;
import pacsim.HouseCell;
import pacsim.GhostCell;
import pacsim.PacMode;


/**
 * University Of Central Florida
 * CAP4630 - Spring 2019
 * Authors: Bao Hong and Davis Rollman;
 */
public class PacSimMinimax implements PacAction {
    private List<Point> dir;
    private List<Point> path;
    private int maxDepth;
    private int totalFood;
    private int simTime;
    private int noFearTime;
    public PacSimMinimax(int depth, String fname, int te, int gran, int max) {

        maxDepth = depth;
        totalFood = 0;
        noFearTime = 0;

        PacSim sim = new PacSim( fname, te, gran, max );
        sim.init(this);
    }

    public static void main( String[] args ) {
        String fname = args[0];
        int depth = Integer.parseInt(args[1]);

        int te = 0;
        int gr = 0;
        int ml = 0;

        if( args.length == 5)
        {
            te = Integer.parseInt(args[2]);
            gr = Integer.parseInt(args[3]);
            ml = Integer.parseInt(args[4]);
        }
        new PacSimMinimax( depth, fname, te, gr, ml);

        System.out.println("\nAdversarial Search using Minimax by Bao Hong and Davis Rollman:");
        System.out.println("\n    Game board : " + fname);

        System.out.println("    Search depth : " + depth +"\n");

        if(te>0)
        {
            System.out.println("    Preliminary runs : " + te
            + "\n    Granularity     : " + gr
            + "\n    Max move limit  : " + ml
            + "\n\nPreliminary run results :\n");
        }
    }

    @Override
    public void init() {
        path = new ArrayList<>();
        dir = new ArrayList<>();
        simTime = 0;
        dir.add(new Point(1,0)); //R
        dir.add(new Point(0,1)); //U
        dir.add(new Point(-1,0)); //L
        dir.add(new Point(0,-1)); //D


    }

    @Override
    public PacFace action( Object state ) {

        PacCell[][] grid = (PacCell[][]) state;
        PacmanCell pc = PacUtils.findPacman( grid );
        PacFace newFace = null;
        if(pc == null)
            return null;
        if(simTime == 0)
            totalFood = PacUtils.findFood(grid).size();
        if( path.isEmpty() ) {
            //Point tgt = PacUtils.nearestFood( pc.getLoc(), grid);
            BoardPosition root = new BoardPosition(grid, 0);
            //System.out.println("Curr" + pc.getLoc());
            PacCell[][] tgt = minimax(0, maxDepth, root).getState();
            Point pac = PacUtils.findPacman(tgt).getLoc();

            //System.out.println("Next: " + pac);
            path = BFSPath.getPath(grid, pc.getLoc(),pac);

        }
        Point next = path.remove( 0 );
        newFace = PacUtils.direction( pc.getLoc(), next );
        simTime++;
        return newFace;
    }
    public BoardPosition minimax(int depth, int maxDepth, BoardPosition root)
    {
        if(depth >= maxDepth)
            return evaluate(root);
        Point pac = PacUtils.findPacman(root.state).getLoc();
        List<Point> ghosts = PacUtils.findGhosts(root.state);
        List<Point> objects = new ArrayList<>();

        objects.addAll(ghosts);
        objects.add(pac);
        List<PacCell[][]> nextStates = new ArrayList<>();
        nextStates.addAll(getAllmoves(root.state, objects, 0, objects.size()-1));
        //System.out.printf("Num moves at depth %d: %d\n", depth, nextStates.size());
        for(PacCell[][] p: nextStates)
        {
            BoardPosition nextState = new BoardPosition(p, 0);
            root.next.add(nextState);
        }
        if(depth%2 == 0)
        {
            BoardPosition max = root.next.get(0);
            for(BoardPosition b : root.next)
            {
                b = minimax(depth+1, maxDepth, b);
                if(b.getEval() > max.getEval())
                {
                    max = b;
                }
            }
            //System.out.println("Max: "+max.getEval() );
            if(depth == 0)
                root.setState(max.getState());
            root.setEval(max.getEval());
        }
        if(depth%2 == 1)
        {
            BoardPosition min = root.next.get(0);
            for(BoardPosition b : root.next)
            {
                b = minimax(depth+1, maxDepth, b);
                if(b.getEval() < min.getEval())
                {
                    min = b;
                }
            }
            //System.out.println("Min: "+min.getEval() );
            //root.setState(min.getState());
            root.setEval(min.getEval());
        }
        return root;


    }

    //using feature such as distance to nearest food, nearest power, nearest ghost, ghost mode, but not quite sure
    //how to use all advantage
    public BoardPosition evaluate(BoardPosition root) {
        PacCell[][] grid = root.state;
        PacmanCell pac = PacUtils.findPacman(grid);
        int remainFood = (PacUtils.findFood(grid)).size();
        List<Point> ghosts = PacUtils.findGhosts(grid);
        if (pac == null) {
            root.setEval(-100);
            return root;
        }

        GhostCell ghost = PacUtils.nearestGhost(pac.getLoc(), grid);
        double val = 10.0;
        Point np = PacUtils.nearestPower(pac.getLoc(), grid);
        Point nf = PacUtils.nearestFood(pac.getLoc(), grid);
        Point ng = ghost.getLoc();
        List<Point> npp = BFSPath.getPath(grid, pac.getLoc(), np);
        List<Point> nfp = BFSPath.getPath(grid, pac.getLoc(), nf);
        List<Point> btg = BFSPath.getPath(grid, ghosts.get(0), ghosts.get(1));
        int npd = 1;

        if (nfp == null) {
            root.setEval(100);
            return root;
        }
        Point pc = pac.getLoc();
        int nfd = nfp.size();
        if (npp != null)
            npd = npp.size();
        int ngd = BFSPath.getPath(grid, pac.getLoc(), ng).size();
        val = (totalFood - remainFood) * val + (1.0 / nfd) * val - (1.0 / ngd) * val;
        double temp = val;
        if (btg.contains(pc)) {
            val = -10;
        }

        if (ngd <= 1) {
            val = -100;
        }


        if (ghost.getMode() == PacMode.FEAR) {
            val = temp + nfd;

        }
//        else
//            val += (1.0 / (double) npd) * val;
        if (ghost.getMode() == PacMode.CHASE) {
            val = (1.0 / (ghost.getModeTimer())) * val;
            if(npp != null)
                val += (1.0 / (double) npd) * val;
        }

        if (ghost.getMode() == PacMode.SCATTER) {
            val = 2*val;
        }
        root.setEval(val);
        return root;
    }

    public List<PacCell[][]> getAllmoves(PacCell[][] grid, List<Point> objects, int start, int end)
    {
        List<PacCell[][]> allmoves = new ArrayList<>();

        if(start == end)
        {
            for(Point p:dir)
            {
                Point ob = objects.get(start);
                Point newMove = new Point(ob.x + p.x, ob.y + p.y);
                PacCell[][] makeMove = null;
                if(grid[ob.x][ob.y] instanceof PacmanCell)
                {

                    if(isValidMove(grid, newMove, ob))
                        makeMove = PacUtils.movePacman(ob, newMove, grid);
                }
                if(makeMove != null)
                    allmoves.add(makeMove);
            }
            return allmoves;
        }
        for(Point p:dir)
        {
            Point ob = objects.get(start);
            Point newMove = new Point(ob.x + p.x, ob.y + p.y);
//            if(grid[ob.x][ob.y] instanceof PacmanCell)
//            {
//                if(isValidMove(grid, newMove, ob))
//                {
//                    PacCell[][] makeMove = PacUtils.movePacman(ob, newMove, grid);
//                    allmoves.addAll(getAllmoves(makeMove, objects, start + 1, end));
//                }
//
//            }
            if(grid[ob.x][ob.y] instanceof GhostCell)
            {
                PacCell[][] makeMove = PacUtils.moveGhost(ob, newMove, grid);
                allmoves.addAll(getAllmoves(makeMove, objects, start + 1, end));



            }

        }
        return allmoves;

    }
    public boolean isValidMove(PacCell[][] grid, Point ob, Point obCurr)
    {
        int x1 = grid.length;
        int y1 = grid[0].length;
        PacmanCell pc = PacUtils.findPacman( grid );
        if(grid[ob.x][ob.y] instanceof WallCell)
            return false;
        if(ob.x <=0 || ob.y <=0 || ob.x >= x1 || ob.y >= y1)
            return false;
//        if(grid[obCurr.x][obCurr.y] instanceof PacmanCell && grid[ob.x][ob.y] instanceof GhostCell)
//            return false;
        if(grid[ob.x][ob.y] instanceof HouseCell && grid[obCurr.x][obCurr.y] instanceof PacmanCell)
            return false;
        if(!PacUtils.unoccupied(ob.x, ob.y, grid))
            return false;

        return true;

    }
}
class BoardPosition
{
    PacCell[][] state;
    double eval;
    List<BoardPosition> next;
    public BoardPosition(PacCell[][] state, double eval)
    {
        this.state = state;
        this.eval = eval;
        next = new ArrayList<>();
    }
    public PacCell[][] getState()
    {
        return state;
    }
    public double getEval()
    {
        return eval;
    }
    public void setState(PacCell[][] grid) { this.state = grid; }
    public void setEval(double eval) { this.eval = eval; }
}