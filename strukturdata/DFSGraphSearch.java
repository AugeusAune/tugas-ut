package strukturdata;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

public class DFSGraphSearch {

    static class Node {

        String label;
        int value;

        Node(String label, int value) {
            this.label = label;
            this.value = value;
        }
    }

    // Graph: adjacency list (undirected)
    private final Node[] nodes;
    private final List<Integer>[] adj;

    @SuppressWarnings("unchecked")
    public DFSGraphSearch() {
        nodes = new Node[8];
        // assign node labels and values
        nodes[0] = new Node("a1", 10);
        nodes[1] = new Node("a2", 23);
        nodes[2] = new Node("a3", 5);
        nodes[3] = new Node("a4", 42); // target in examples
        nodes[4] = new Node("a5", 17);
        nodes[5] = new Node("a6", 8);
        nodes[6] = new Node("a7", 31);
        nodes[7] = new Node("a8", 27);

        adj = new ArrayList[8];
        for (int i = 0; i < 8; i++) {
            adj[i] = new ArrayList<>();
        }

        // define edges (undirected)
        addEdge(0, 1); // a1 - a2
        addEdge(0, 2); // a1 - a3
        addEdge(1, 3); // a2 - a4
        addEdge(1, 4); // a2 - a5
        addEdge(2, 5); // a3 - a6
        addEdge(3, 6); // a4 - a7
        addEdge(4, 7); // a5 - a8
        addEdge(5, 6); // a6 - a7
        addEdge(6, 7); // a7 - a8
    }

    private void addEdge(int u, int v) {
        adj[u].add(v);
        adj[v].add(u);
    }

    // DFS search, verbose printing
    public List<String> dfsSearch(int startIndex, int targetValue) {
        boolean[] visited = new boolean[nodes.length];
        Deque<Integer> stack = new ArrayDeque<>(); // used for printing stack content
        List<String> visitOrder = new ArrayList<>();
        List<Integer> path = new ArrayList<>();

        System.out.println("Mulai DFS dari " + nodes[startIndex].label + " untuk mencari nilai " + targetValue + "\n");
        boolean found = dfsHelper(startIndex, targetValue, visited, stack, visitOrder, path);
        if (!found) {
            System.out.println("\nHasil: nilai " + targetValue + " TIDAK DITEMUKAN pada graf.");
        } else {
            System.out.println("\nHasil: nilai " + targetValue + " DITEMUKAN pada node " + nodes[path.get(path.size() - 1)].label);
            System.out.print("Jalur (dari start ke target): ");
            for (int idx = 0; idx < path.size(); idx++) {
                System.out.print(nodes[path.get(idx)].label + (idx < path.size() - 1 ? " -> " : ""));
            }
            System.out.println();
        }
        System.out.print("Urutan kunjungan: ");
        for (int i = 0; i < visitOrder.size(); i++) {
            System.out.print(visitOrder.get(i) + (i < visitOrder.size() - 1 ? ", " : ""));
        }
        System.out.println();
        return Collections.emptyList();
    }

    private boolean dfsHelper(int u, int targetValue, boolean[] visited, Deque<Integer> stack,
            List<String> visitOrder, List<Integer> path) {
        visited[u] = true;
        stack.push(u); // push to stack for tracing
        visitOrder.add(nodes[u].label + "(" + nodes[u].value + ")");
        System.out.println("Masuk node " + nodes[u].label + " (value=" + nodes[u].value + ")");
        printStack(stack, "Stack setelah push:");

        // add to current path (we copy path on success)
        // use path as current path by pushing; if found, path will be returned via path param
        path.add(u);

        // check target
        if (nodes[u].value == targetValue) {
            System.out.println(">> Nilai cocok pada " + nodes[u].label);
            printStack(stack, "Stack saat ditemukan:");
            return true;
        }

        // explore neighbors in adjacency order
        for (int v : adj[u]) {
            if (!visited[v]) {
                System.out.println("  Menjelajah dari " + nodes[u].label + " ke " + nodes[v].label);
                boolean found = dfsHelper(v, targetValue, visited, stack, visitOrder, path);
                if (found) {
                    return true; // bubble up success without popping path entries

                }
            } else {
                System.out.println("  Lewat " + nodes[v].label + " (sudah dikunjungi)");
            }
        }

        // backtrack
        stack.pop();
        System.out.println("Kembali dari node " + nodes[u].label + " (backtrack)");
        printStack(stack, "Stack setelah pop:");
        // remove last from path because this branch didn't find target
        path.remove(path.size() - 1);
        return false;
    }

    private void printStack(Deque<Integer> stack, String title) {
        System.out.print(title + " [");
        Iterator<Integer> it = stack.iterator();
        List<String> elems = new ArrayList<>();
        while (it.hasNext()) {
            int idx = it.next();
            elems.add(nodes[idx].label);
        }
        System.out.print(String.join(", ", elems));
        System.out.println("]");
    }

    // main demo
    public void run() {
        this.dfsSearch(0, 42); // mulai dari a1, cari nilai 42 (ada di a4)
        System.out.println("\n---\n");
        // contoh 2: target tidak ada (99)
        this.dfsSearch(0, 99);
    }
}
