package strukturdata;

import java.util.*;

public class GraphSearch {

    static class Graph {

        private Map<String, List<String>> adjList;

        public Graph() {
            adjList = new HashMap<>();
        }

        // Menambahkan  hubungan antar node
        public void addEdge(String source, String dest) {
            adjList.putIfAbsent(source, new ArrayList<>());
            adjList.get(source).add(dest);
        }

        // Mendapatkan tetangga dari suatu node
        public List<String> getNeighbors(String node) {
            return adjList.getOrDefault(node, new ArrayList<>());
        }

        // Menampilkan struktur graf
        public void printGraph() {
            System.out.println("\n=== STRUKTUR GRAF ===");
            for (Map.Entry<String, List<String>> entry : adjList.entrySet()) {
                System.out.println(entry.getKey() + " -> " + entry.getValue());
            }
            System.out.println("=====================\n");
        }
    }

    public static boolean DFS(Graph graph, String start, String target) {
        System.out.println("DEPTH-FIRST SEARCH (DFS) - PENCARIAN: " + target);
        System.out.println("\n" + "=".repeat(63) + "\n");

        Set<String> visited = new LinkedHashSet<>();
        Stack<String> stack = new Stack<>();

        stack.push(start);
        int step = 1;

        System.out.println("Mulai pencarian dari node: " + start);
        System.out.println("Target yang dicari: " + target);
        System.out.println("\n--- PROSES PENCARIAN DFS ---\n");

        while (!stack.isEmpty()) {
            String current = stack.pop();

            if (visited.contains(current)) {
                continue;
            }

            System.out.println("Langkah " + step + ":");
            System.out.println("  → Node saat ini: " + current);
            System.out.println("  → Stack: " + stack);

            visited.add(current);
            System.out.println("  → Visited: " + visited);

            // Cek apakah node target ditemukan
            if (current.equals(target)) {
                System.out.println("\n✓ TARGET DITEMUKAN! Node '" + target + "' telah ditemukan!");
                System.out.println("  Jalur kunjungan: " + visited);
                System.out.println("  Total langkah: " + step);
                return true;
            }

            // Tambahkan tetangga ke stack (dalam urutan terbalik agar urutan eksplorasi benar)
            List<String> neighbors = graph.getNeighbors(current);
            List<String> unvisited = new ArrayList<>();

            for (int i = neighbors.size() - 1; i >= 0; i--) {
                String neighbor = neighbors.get(i);
                if (!visited.contains(neighbor) && !stack.contains(neighbor)) {
                    stack.push(neighbor);
                    unvisited.add(neighbor);
                }
            }

            if (!unvisited.isEmpty()) {
                System.out.println("  → Menambahkan ke stack: " + unvisited);
            } else {
                System.out.println("  → Tidak ada tetangga yang belum dikunjungi");
            }

            System.out.println();
            step++;
        }

        System.out.println("\n✗ TARGET TIDAK DITEMUKAN!");
        System.out.println("  Semua node telah dikunjungi: " + visited);
        return false;
    }

    public static boolean BFS(Graph graph, String start, String target) {
        System.out.println("BREADTH-FIRST SEARCH (BFS) - PENCARIAN: " + target);
        System.out.println("\n" + "=".repeat(63) + "\n");

        Set<String> visited = new LinkedHashSet<>();
        Queue<String> queue = new LinkedList<>();

        queue.add(start);
        visited.add(start);
        int step = 1;

        System.out.println("Mulai pencarian dari node: " + start);
        System.out.println("Target yang dicari: " + target);
        System.out.println("\n--- PROSES PENCARIAN BFS ---\n");

        while (!queue.isEmpty()) {
            String current = queue.poll();

            System.out.println("Langkah " + step + ":");
            System.out.println("  → Node saat ini: " + current);
            System.out.println("  → Queue: " + queue);
            System.out.println("  → Visited: " + visited);

            // Cek apakah node target ditemukan
            if (current.equals(target)) {
                System.out.println("\n✓ TARGET DITEMUKAN! Node '" + target + "' telah ditemukan!");
                System.out.println("  Jalur kunjungan: " + visited);
                System.out.println("  Total langkah: " + step);
                return true;
            }

            // Tambahkan semua tetangga yang belum dikunjungi ke queue
            List<String> neighbors = graph.getNeighbors(current);
            List<String> added = new ArrayList<>();

            for (String neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    queue.add(neighbor);
                    visited.add(neighbor);
                    added.add(neighbor);
                }
            }

            if (!added.isEmpty()) {
                System.out.println("  → Menambahkan ke queue: " + added);
            } else {
                System.out.println("  → Tidak ada tetangga baru untuk ditambahkan");
            }

            System.out.println();
            step++;
        }

        System.out.println("\n✗ TARGET TIDAK DITEMUKAN!");
        System.out.println("  Semua node telah dikunjungi: " + visited);
        return false;
    }

    public static void main(String[] args) {
        // Membuat graf dengan 8 node
        Graph graph = new Graph();

        graph.addEdge("a1", "a2");
        graph.addEdge("a1", "a3");
        graph.addEdge("a2", "a4");
        graph.addEdge("a2", "a5");
        graph.addEdge("a3", "a6");
        graph.addEdge("a4", "a7");
        graph.addEdge("a5", "a7");
        graph.addEdge("a6", "a8");

        // Menampilkan struktur graf
        graph.printGraph();

        // Node yang akan dicari
        String targetNode = "a7";
        String startNode = "a1";

        // Melakukan pencarian dengan DFS
        // DFS(graph, startNode, targetNode);

        System.out.println("\n\n" + "=".repeat(63) + "\n");

        // Melakukan pencarian dengan BFS
        BFS(graph, startNode, targetNode);

        //   PENJELASAN ALGORITMA");
        // ═══════════════════════════════════════════════════════════"
        // DFS (Depth-First Search):");
        //   - Menggunakan struktur data STACK (LIFO)");
        //   - Menelusuri graf sedalam mungkin sebelum backtrack");
        //   - Urutan: a1 → a2 → a4 → a7 (target ditemukan)");
        //   - Cocok untuk: maze solving, topological sorting");
        // BFS (Breadth-First Search):");
        //   - Menggunakan struktur data QUEUE (FIFO)");
        //   - Menelusuri graf selebar mungkin (level by level)");
        //   - Urutan: a1 → a2 → a3 → a4 → a5 → a6 → a7 (target)");
        //   - Cocok untuk: shortest path, level-order traversal");
        // ═══════════════════════════════════════════════════════════
    }
}
