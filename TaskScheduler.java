import java.util.*;

class Task {
    String name;
    int duration;
    int est = 0;
    int eft = 0;
    int lst = Integer.MAX_VALUE;
    int lft = Integer.MAX_VALUE;
    List<Task> dependencies = new ArrayList<>();
    List<Task> dependents = new ArrayList<>();
    
    public Task(String name, int duration) {
        this.name = name;
        this.duration = duration;
    }
}

public class TaskScheduler {
    
    public static void calculateEarliestTimes(List<Task> tasks) {
        Queue<Task> queue = new LinkedList<>();
        
        for (Task task : tasks) {
            if (task.dependencies.isEmpty()) {
                queue.add(task);
            }
        }
        
        while (!queue.isEmpty()) {
            Task currentTask = queue.poll();
            currentTask.eft = currentTask.est + currentTask.duration;
            for (Task dependent : currentTask.dependents) {
                dependent.est = Math.max(dependent.est, currentTask.eft);
                if (dependent.dependencies.stream().allMatch(dep -> dep.eft > 0)) {
                    queue.add(dependent);
                }
            }
        }
    }

    public static void calculateLatestTimes(List<Task> tasks) {
        int latestCompletion = tasks.stream().mapToInt(task -> task.eft).max().orElse(0);
        for (Task task : tasks) {
            task.lft = latestCompletion;
        }
        
        Queue<Task> queue = new LinkedList<>();
        for (Task task : tasks) {
            if (task.dependents.isEmpty()) {
                task.lst = task.lft - task.duration;
                queue.add(task);
            }
        }
        
        while (!queue.isEmpty()) {
            Task currentTask = queue.poll();
            for (Task dependency : currentTask.dependencies) {
                dependency.lft = Math.min(dependency.lft, currentTask.lst);
                dependency.lst = dependency.lft - dependency.duration;
                if (dependency.dependents.stream().allMatch(dep -> dep.lst < Integer.MAX_VALUE)) {
                    queue.add(dependency);
                }
            }
        }
    }
    
    public static void main(String[] args) {

        Task t1 = new Task("T1", 3);
        Task t2 = new Task("T2", 2);
        Task t3 = new Task("T3", 4);
        Task t4 = new Task("T4", 1);
        Task t5 = new Task("T5", 5);
        
        t1.dependents.add(t2);
        t1.dependents.add(t3);
        t2.dependencies.add(t1);
        t3.dependencies.add(t1);
        t2.dependents.add(t4);
        t3.dependents.add(t5);
        t4.dependencies.add(t2);
        t5.dependencies.add(t3);
        
        List<Task> tasks = Arrays.asList(t1, t2, t3, t4, t5);
        
        calculateEarliestTimes(tasks);
        
        calculateLatestTimes(tasks);
        
        int earliestCompletionTime = tasks.stream().mapToInt(task -> task.eft).max().orElse(0);
        int latestCompletionTime = tasks.stream().mapToInt(task -> task.lft).max().orElse(0);
        
        System.out.println("Earliest completion time: " + earliestCompletionTime);
        System.out.println("Latest completion time: " + latestCompletionTime);
    }
}