/*
* University of Central Florida
* CAP4630 - Spring 2019
* Author: Davis Rollman and Bao Hong
*/

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import pacsim.*;
import java.util.Collections;

public class PacSimMinimaxD implements PacAction {
    int maxDepth = 0;
    ArrayList<Point> dirs;
    PacFace curFace = PacFace.E;
    PacFace lastFace = PacFace.E;
    Point nextDir;

    public PacSimMinimaxD( int depth, String fname, int te, int gran, int max ) {
        dirs = new ArrayList<Point>();
        dirs.add(new Point(1, 0));
        dirs.add(new Point(0, 1));
        dirs.add(new Point(-1, 0));
        dirs.add(new Point(0, -1));
        
        maxDepth = depth * 3;
        PacSim sim = new PacSim( fname, te, gran, max );
        sim.init(this);
    }

    public static void main(String[] args){
        String fname = args[0];
        int depth = Integer.parseInt(args[1]);

        int te = 0;
        int gr = 0;
        int ml = 0;

        if (args.length == 5){
            te = Integer.parseInt(args[2]);
            gr = Integer.parseInt(args[3]);
            ml = Integer.parseInt(args[4]);
        }
        new PacSimMinimaxD(depth, fname, te, gr, ml);

        System.out.println("\nAdversarial Search using Minimax by Davis Rollman and Bao Hong:");
        System.out.println("\n  Game board  : "+fname);
        System.out.println("  Search depth : "+depth+"\n");

        if (te > 0){
            System.out.println("  Preliminary runs : "+te
            +"\n  Granularity   : "+gr
            +"\n  Max move limit  : "+ml
            +"\n\nPreliminary run results :\n");
        }
    }

    @Override
    public void init(){}

    @Override
    public PacFace action(Object state){
        PacCell[][] grid = (PacCell[][]) state;
        PacFace newFace = PacFace.E;
        List<Point> ghosts = PacUtils.findGhosts(grid);
        Point pacman = PacUtils.findPacman(grid).getLoc();
        if (ghosts.size() != 2) {
            System.out.println("Error, ghosts != 2");
            return PacFace.N;
        }
        int bestVal = minimax(grid, ghosts.get(0), ghosts.get(1), pacman, 0);

        lastFace = curFace;
        return curFace;
    }

    public int minimax(PacCell[][] grid, Point ghost1, Point ghost2, Point pacman, int depth){
    
        if (depth >= maxDepth){
            return evaluate(grid);
        }
        
        //shuffle directions to not follow the same order (random)
        //Collections.shuffle(dirs);
        
        if (depth % 3 == 0){
            int max = Integer.MIN_VALUE;
            PacFace maxFace;
            for (Point p : dirs) {
                Point newPacman = new Point(p.x + pacman.x, p.y + pacman.y);
                //only go down this route if this is a valid move
                //if (PacUtils.unoccupied(newPacman.x, newPacman.y, grid)) {
                
                
                if (isValidMove(grid, newPacman)) {
                    
                    int value;
                    if (grid[newPacman.x][newPacman.y] instanceof GhostCell) {
                        value = -500;
                    }
                    else {
                        value = minimax(
                                    PacUtils.movePacman(pacman, newPacman, grid),
                                    ghost1,
                                    ghost2,
                                    newPacman,
                                    depth + 1);
                    }
                    if (value > max) {
                        max = value;
                        //This is pacman, so we update our current face from here.
                        //On the last recursive call (right before we exit minimax
                        //and return to action(), this is where the best decision
                        //is being set.
                        if (depth == 0) {
                        
                            curFace = PacUtils.direction(pacman, newPacman);
                            nextDir = p;
                            //System.out.println("depth 0, set face" + p);
                        }
                    }
                }
            }
            
            return max;
        }
        else if (depth % 3 == 1){
            int min = Integer.MAX_VALUE;
            PacFace minFace;
            for (Point p : dirs) {
                Point newGhost1 = new Point(p.x + ghost1.x, p.y + ghost1.y);
                //only go down this route if this is a valid move
                //if (PacUtils.unoccupied(newGhost1.x, newGhost1.y, grid)) {
                if (isValidMove(grid, newGhost1)) {
                    
                    int value;
                    if (grid[newGhost1.x][newGhost1.y] instanceof PacmanCell) {// || grid[newGhost1.x][newGhost1.y] instanceof GhostCell) {
                        value = -500;
                    }
                    else {
                        value = minimax(
                                    PacUtils.moveGhost(ghost1, newGhost1, grid),
                                    newGhost1,
                                    ghost2,
                                    pacman,
                                    depth + 1);
                    }
                    if (value < min) {
                        min = value;
                    }
                }
            }
            return min;
        }
        else {
            int min = Integer.MAX_VALUE;
            PacFace minFace;
            for (Point p : dirs) {
                Point newGhost2 = new Point(p.x + ghost2.x, p.y + ghost2.y);
                //only go down this route if this is a valid move
                //if (PacUtils.unoccupied(newGhost2.x, newGhost2.y, grid)) {
                if (isValidMove(grid, newGhost2)) {
                
                    int value;
                    if (grid[newGhost2.x][newGhost2.y] instanceof PacmanCell) {// || grid[newGhost2.x][newGhost2.y] instanceof GhostCell) {
                        value = -500;
                    }
                    else {
                        value = minimax(
                                    PacUtils.moveGhost(ghost2, newGhost2, grid),
                                    ghost1,
                                    newGhost2,
                                    pacman,
                                    depth + 1);
                    }
                    if (value < min) {
                        min = value;
                    }
                }
            }
            return min;
        }
    }
    
    //temp poor implementation of evaluate
    public int evaluate(PacCell[][] grid) {
        
        List<Point> ghosts = PacUtils.findGhosts(grid);
        Point pacman = PacUtils.findPacman(grid).getLoc();
        Point nearestFood = PacUtils.nearestFood(pacman, grid);
        int numFood = PacUtils.numFood(grid);
        if (numFood == 0) return 1000;
        int nfd = BFSPath.getPath(grid, pacman, nearestFood).size();
        
        int ghostDistance = 0;
        int closestGhost = 100;
        for (Point ghost : ghosts) {
            int d = BFSPath.getPath(grid, pacman, ghost).size();
            if (d < closestGhost)
                closestGhost = d;
        }
        if (nfd > 2) {
            nfd = (int)(30.0 * (1.0 / (double)nfd));
        }
        else {
            nfd = -1*numFood + 30;
        }
        
        if (closestGhost < 6) {
            nfd = (int)(-50.0 * (1.0 / (double)closestGhost));
        }
        
        return (numFood* -1) + 30;
    }
    
    public PacFace fromDir(Point p) {
        if (p.x == 1 && p.y == 0)
            return PacFace.E;
        if (p.x == 0 && p.y == 1)
            return PacFace.S;
        if (p.x == -1 && p.y == 0)
            return PacFace.W;
        if (p.x == 0 && p.y == -1)
            return PacFace.N;
        System.out.println("Error: unexpected dir! Returning default East");
        return PacFace.N;
    }
    
    //no longer used, keeping just in case, use PacUtils.unoccupied() now
    public boolean isValidMove(PacCell[][] grid, Point newP) {
        int xl = grid.length;
        int yl = grid[0].length;
        
        if (newP.x == 0 || newP.y == 0 || newP.x == xl || newP.y == yl) {
            return false;
        }
        
        if (grid[newP.x][newP.y] instanceof WallCell) {
            //System.out.println("Wall at " + newP);
            return false;
        }
//         if (PacUtils.unoccupied(newP.x, newP.y, grid))
//             return true;
        
        //System.out.println("Wall at " + newP);
        return true;
    }
}
