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
public class PacSimMinimax implements PacAction {


    public PacSimMinimax(int depth, String fname, int te, int gran, int max) {
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
            + "\n   Granularity     : " + gr
            + "\n   Max move limit  : " + ml
            + "\n\nPreliminary run results :\n");
        }
    }

    @Override
    public void init() {

    }

    @Override
    public PacFace action( Object state ) {

        PacCell[][] grid = (PacCell[][]) state;
        PacFace newFace = null;


        return newFace;
    }
    public PacCell[][] minimax(int depth, int maxDepth, BoardPosition root)
    {
        if(depth < maxDepth)

    }
    public List<PacCell[][]> getAllmoves(PacCell[][] grid)
    {
        List<PacCell[][]> allmoves = new ArrayList<>();




    }
}
class BoardPosition
{
    PacCell[][] state;
    int eval;
    List<BoardPosition> next;
    public BoardPosition(PacCell[][] state, int eval)
    {
        this.state = state;
        this.eval = eval;
        next = new ArrayList<>();
    }
    public PacCell[][] getState()
    {
        return state;
    }
    public int getEval()
    {
        return eval;
    }
}