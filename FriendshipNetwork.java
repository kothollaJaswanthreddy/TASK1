import java.util.*;

public class FriendshipNetwork {
    
    private Map<String, Set<String>> network;

    public FriendshipNetwork() {
        network = new HashMap<>();
    }
    
    public void addPerson(String person) {
        network.putIfAbsent(person, new HashSet<>());
    }

    public void addFriendship(String person1, String person2) {
        network.putIfAbsent(person1, new HashSet<>());
        network.putIfAbsent(person2, new HashSet<>());
        network.get(person1).add(person2);
        network.get(person2).add(person1);
    }

    public Set<String> getFriends(String person) {
        return network.getOrDefault(person, Collections.emptySet());
    }

    public Set<String> getCommonFriends(String person1, String person2) {
        Set<String> friends1 = getFriends(person1);
        Set<String> friends2 = getFriends(person2);
        Set<String> commonFriends = new HashSet<>(friends1);
        commonFriends.retainAll(friends2);
        return commonFriends;
    }

    public int findConnectionLevel(String start, String end) {
        if (start.equals(end)) return 0;
        if (!network.containsKey(start) || !network.containsKey(end)) return -1;

        Queue<String> queue = new LinkedList<>();
        Map<String, Integer> distances = new HashMap<>();
        
        queue.add(start);
        distances.put(start, 0);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            int currentDistance = distances.get(current);
            for (String friend : network.get(current)) {
                if (!distances.containsKey(friend)) {
                    distances.put(friend, currentDistance + 1);
                    queue.add(friend);
                    if (friend.equals(end)) {
                        return distances.get(friend);
                    }
                }
            }
        }
        return -1; 
    }

    public static void main(String[] args) {
        FriendshipNetwork network = new FriendshipNetwork();
        
        network.addPerson("Alice");
        network.addPerson("Bob");
        network.addPerson("Janice");
        network.addPerson("Diana");
        
        network.addFriendship("Alice", "Bob");
        network.addFriendship("Bob", "Janice");
        network.addFriendship("Alice", "Diana");

        System.out.println("Friends of Alice: " + network.getFriends("Alice"));
        System.out.println("Friends of Bob: " + network.getFriends("Bob"));

        System.out.println("Common friends of Alice and Bob: " + network.getCommonFriends("Alice", "Bob"));

        System.out.println("Connection level between Alice and Janice: " + network.findConnectionLevel("Alice", "Janice"));
        System.out.println("Connection level between Alice and Bob: " + network.findConnectionLevel("Alice", "Bob"));
        System.out.println("Connection level between Alice and Diana: " + network.findConnectionLevel("Alice", "Diana"));
        System.out.println("Connection level between Alice and someone not connected: " + network.findConnectionLevel("Alice", "Nonexistent"));
    }
}