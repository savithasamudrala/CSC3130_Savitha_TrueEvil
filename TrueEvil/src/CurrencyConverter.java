import java.util.*;

public class CurrencyConverter {
    private static class Edge {
        String to;
        double cost;

        public Edge(String to, double cost) {
            this.to = to;
            this.cost = cost;
        }
    }

    private static class Node implements Comparable<Node> {
        String currency;
        double cost;

        public Node(String currency, double cost) {
            this.currency = currency;
            this.cost = cost;
        }

        @Override
        public int compareTo(Node other) {
            return Double.compare(this.cost, other.cost);
        }
    }

    private Map<String, List<Edge>> graph;

    public CurrencyConverter() {
        this.graph = new HashMap<>();
    }

    public void addRate(String from, String to, double rate) {
        this.graph.putIfAbsent(from, new ArrayList<>());
        this.graph.get(from).add(new Edge(to, -Math.log(rate)));
    }

    public double findBestRate(String start, String end) {
        PriorityQueue<Node> minHeap = new PriorityQueue<>();
        Map<String, Double> minDist = new HashMap<>();
        minHeap.add(new Node(start, 0));
        minDist.put(start, 0.0);

        while (!minHeap.isEmpty()) {
            Node current = minHeap.poll();

            if (current.currency.equals(end)) {
                return Math.exp(-current.cost);
            }

            if (current.cost > minDist.getOrDefault(current.currency, Double.MAX_VALUE)) {
                continue;
            }

            for (Edge edge : graph.getOrDefault(current.currency, Collections.emptyList())) {
                double newCost = current.cost + edge.cost;
                if (newCost < minDist.getOrDefault(edge.to, Double.MAX_VALUE)) {
                    minDist.put(edge.to, newCost);
                    minHeap.add(new Node(edge.to, newCost));
                }
            }
        }

        return 0;  // or throw an exception if no path is found
    }

    public static void main(String[] args) {
        CurrencyConverter converter = new CurrencyConverter();
        converter.addRate("USD", "EUR", 0.9);
        converter.addRate("EUR", "JPY", 120);
        converter.addRate("USD", "JPY", 108);
        converter.addRate("JPY", "INR", 0.67);

        double bestRate = converter.findBestRate("USD", "INR");
        System.out.println("Best rate from USD to INR: " + bestRate);

    }
}











