
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;




public class Problem1 {
    public static void main(String[] args) throws IOException {
        Graph g = MakeTestGraph();
        //System.out.println(g);
        //String s = g.find(g.nodes.get(0),g.nodes.get(5));
        //System.out.println(s);
        File f = new File("Test");
        Scanner sc = new Scanner(f);
        int walls = sc.nextInt();
        int lines = sc.nextInt();
        char[][] grid = null;

        Graph graph = new Graph();
        for (int i =0;i<lines;i++) {
            String line = sc.next();
            if (grid == null)
                grid = new char[lines][line.length()];
            for (int j = 0; j < line.length(); j++) {
                char c = line.toCharArray()[j];
                grid[i][j] = c;
                if (c == 'o'){
                    graph.rootNode = new Node("o",new int[]{i,j});
                    graph.nodes.add(graph.rootNode);
                }
                /**
                switch (c) {
                    case 'o':
                        graph.rootNode = new Node("Home");
                        graph.rootNode.pos = new int[]{i,j};
                        graph.nodes.add(graph.rootNode);
                        break;
                    case '-':
                        Node valley = new Node(String.format("(%s, %s)", i, j));
                        valley.pos = new int[]{i,j};
                        graph.nodes.add(valley);
                        break;
                    default:
                }
                 **/
            }
        }
        recurse(graph.rootNode.pos, grid, graph, null);
        System.out.println(graph);






    }

    public static void recurse(int[] pos, char[][] grid, Graph graph, Node prev){

        try {
            char val = grid[pos[0]][pos[1]];
            Node current = null;



            switch (val) {
                case 'x':
                    return;
                case 'o':
                    current = graph.rootNode;
                    break;
                    /**
                    graph.rootNode = new Node("o", pos);
                    graph.nodes.add(graph.rootNode);
                    current = graph.rootNode;
                    break;
                     **/
                case '-':
                    for (Node n : graph.nodes){
                        if (Arrays.equals(n.pos,pos))
                            return;
                    }
                    if (pos[0] == 0 || pos[0] == grid.length-1 || pos[1] == 0 || pos[1] == grid[0].length-1)
                        current = new Node(String.format("(E, %s, %s)", pos[0], pos[1]), pos);
                    else
                        current = new Node(String.format("(-, %s, %s)", pos[0], pos[1]), pos);
                    graph.nodes.add(current);
                    prev.connectedNodes.add(current);
                    if(!current.connectedNodes.contains(prev))
                        current.connectedNodes.add(prev);
                    break;
            }
            recurse(new int[]{pos[0] + 1, pos[1]}, grid, graph, current);
            recurse(new int[]{pos[0], pos[1] + 1}, grid, graph, current);
            recurse(new int[]{pos[0] - 1, pos[1]}, grid, graph, current);
            recurse(new int[]{pos[0], pos[1] - 1}, grid, graph, current);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }


    }


    public static Graph MakeTestGraph() {
        Node a = new Node("A");
        Node b = new Node("B");
        Node c = new Node("C");
        Node d = new Node("D");
        Node e = new Node("E");
        Node f = new Node("F");
        Node z = new Node("Z");

        a.connectedNodes.add(b);
        a.connectedNodes.add(c);

        b.connectedNodes.add(a);
        b.connectedNodes.add(c);
        b.connectedNodes.add(z);

        c.connectedNodes.add(a);
        c.connectedNodes.add(c);
        c.connectedNodes.add(d);

        d.connectedNodes.add(c);
        d.connectedNodes.add(f);

        f.connectedNodes.add(d);

        Graph g = new Graph();
        g.nodes.add(a);
        g.nodes.add(b);
        g.nodes.add(c);
        g.nodes.add(d);
        g.nodes.add(e);
        g.nodes.add(f);
        g.nodes.add(z);

        return g;
    }
}

class Graph {
    public List<Node> nodes;
    public Node rootNode;

    public Graph() {
        nodes = new ArrayList<Node>();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Node n : nodes) {
            sb.append(n.toString()).append(System.getProperty("line.separator"));
        }
        return sb.toString();
    }

    public String find (Node A, Node B){
        PriorityQueue <Node> queue = new PriorityQueue<>();
        String output = "";
        List<Node> visited = new ArrayList<>();
        queue.add(A);

        //visited.add(A);
        while (!queue.isEmpty()){
            Node current = queue.remove();
            current.path.add(current);
            //System.out.println(current.path);
            if (current.compareTo(B)==0){
                List<String> values = current.path.stream().map(x -> x.value).collect(Collectors.toList());
                return String.join(" => ", values);

            }
            if (!visited.contains(current))
                visited.add(current);
            for (Node i : current.connectedNodes){
                if(!visited.contains(i) && !queue.contains(i)){
                    queue.add(i);
                    i.path.addAll(current.path);
                    //visited.add(i);
                }
            }

        }
        return "Not Found";




    }
}

class Node implements Comparable<Node>{
    public String value;
    public ArrayList<Node> connectedNodes;
    public ArrayList<Node> path;
    public int[] pos = new int[2];

    public Node(String value) {
        this.value = value;
        connectedNodes = new ArrayList<Node>();
        path = new ArrayList<Node>();

    }

    public Node(String value, int [] pos) {
        this.value = value;
        this.pos = pos;
        connectedNodes = new ArrayList<Node>();
        path = new ArrayList<Node>();

    }

    public String toString() {
        List<String> connectedValues = connectedNodes
                .stream()
                .map(x -> x.value)
                .collect(Collectors.toList());
        return value + " => " + String.join(", ", connectedValues);
    }

    @Override
    public int compareTo(Node b) {
        if (this == b) return 0;
        return this.value.compareTo(b.value);
    }




}