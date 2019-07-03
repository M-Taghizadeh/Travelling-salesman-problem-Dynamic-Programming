import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/*
 * TSP Dynamic Programming
 * Time Complexity: O(n^2 * 2^n)
 * Space Complexity: O(n * 2^n)
 *
 * @author M.Taghizadeh
 */
public class TSP_DP {
    static Scanner input = new Scanner(System.in);
    public static int n;
    public static int start_city;
    public static int end_state;
    public static double minlength = Double.POSITIVE_INFINITY;
    public static double[][] W;
    public static List<Integer> tour = new ArrayList<>();

    public TSP_DP(int startNode, double[][] W) {
    
        this.W = W;
        n = W.length;
        start_city = startNode;
        end_state = (1 << n) - 1;
    }
    
    //Returns the optimal tour for the traveling salesman problem.
    public List<Integer> getTour() {
        solve();
        return tour;
    }

    //Returns the minimal tour cost.
    public double getTourCost() {
        solve();
        return minlength;
    }

    public void solve(){
        //Run the solver    
        int state = 1 << start_city;
        Double[][] D = new Double[n][1 << n];
        Integer[][] P = new Integer[n][1 << n];
        minlength = tsp(start_city, state, D, P);

        for(int i = 0 ; i<n; i++){
            for(int j = 0; j<1<<n; j++){
                System.out.print(D[i][j]+"\t");
            }
            System.out.print("\n");
        }
        System.out.print("\n");
        for(int i = 0 ; i<n; i++){
            for(int j = 0; j<1<<n; j++){
                System.out.print(P[i][j]+"\t");
            }
        System.out.print("\n");
        }
   
        //Regenerate path
        int index = start_city;
        while(true){
            tour.add(index);
            System.out.println("Index"+index);
            System.out.println("state"+state);
            Integer nextIndex = P[index][state];
            System.out.println("next Index"+nextIndex);
            if(nextIndex==null) 
                break;
            
            //very very very important
            int nextState = state | (1 << nextIndex);
            state = nextState;
            index = nextIndex;
        }
        tour.add(start_city);
    }

    private double tsp(int i, int state, Double[][] D, Integer[][] P) {
    
        //Done this tour. Return cost of going back to start node.
        if(state == end_state){
            /*System.out.println("\n");
            System.out.println(distance[i][START_NODE]);
            */
            return W[i][start_city];
        }
        //Return cached answer if already computed.
        if(D[i][state] != null)
            return D[i][state];

        double minCost = Double.POSITIVE_INFINITY;
        int index = -1;
        for (int next = 0; next < n; next++) {
            //Skip if the next node has already been visited.
            if((state & (1 << next)) != 0) 
                continue;

            int nextState = state | (1 << next);
            System.out.println("state:"+state);
            System.out.println("(1 << next):"+(1 << next));
            System.out.println("nextState:"+nextState);
            System.out.println("W[i][next]:"+W[i][next]);
            double newCost = W[i][next] + tsp(next, nextState, D, P);
            System.out.println("newCost"+newCost);
            if(newCost < minCost){
                minCost = newCost;
                index = next;
            }
        }
        P[i][state] = index;
        return D[i][state] = minCost;
    }
    
    public static void main(String[] args) {
        
        //Create adjacency matrix
        System.out.print("Enter number of city:");
        n = input.nextInt();
        W = new double[n][n];
        
        int count = 0;
        while(count==0){
            System.out.print("\n[Choice Option]\n1:Create Random adjacency\n2:Insert adjacency\n");
            String choice = input.next();
            switch(choice){
                case "1":{
                    //create random adjacency
                    Random rnd = new Random();
                    for(int i = 0; i<n; i++){
                        for(int j = 0; j<n; j++){
                            if(i==j)
                                continue;
                            W[i][j] = rnd.nextInt(100)+1;
                        }
                    }
                    count = 1;
                    break;
                }
                case "2":{
                    //insert adjacency
                    System.out.println("insert cost between citys");
                    for(int i = 0; i<n; i++){
                        for(int j = 0; j<n; j++){
                            W[i][j] = input.nextInt();
                        }
                    }
                    count = 1;
                    break;
                }
                default:{
                    System.out.print("Wrong choice!!!\n");
                }
            }
        }         
        System.out.print("Start City:");
        int start = input.nextInt();
    /*
        W[0][1] = 28;
        W[0][2] = 15;
        W[0][3] = 4;
        W[1][0] = 40;
        W[1][2] = 5;
        W[1][3] = 20;
        W[2][0] = 12;
        W[2][1] = 80;
        W[2][3] = 9;
        W[3][0] = 25;
        W[3][1] = 45;
        W[3][2] = 60;
    */
        //show W:
        System.out.print("\n\t\t");
        for(int i = 0; i<n; i++){
            System.out.print("V["+i+"]"+"\t\t");
        }
        System.out.println("\n");
        for(int i = 0; i<n; i++){
            System.out.print("V["+i+"]"+"\t\t");
            for(int j = 0; j<n; j++){
                System.out.print(W[i][j]+"\t\t");
            }
            System.out.println("\n");
        }
        
        //show best tour and best cost:
        TSP_DP tsp = new TSP_DP(start, W);
        System.out.println("Tour:"+tsp.getTour());
        System.out.println("Tour Cost:"+tsp.getTourCost());
    }
}
