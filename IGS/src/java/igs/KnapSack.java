package igs;
import java.util.*;

class BranchAndBoundSolver  
{
    LinkedList<Topic> topics;
    int capacity;

    BranchAndBoundSolver(LinkedList<Topic> topics, int capacity) 
    {
        this.topics = topics;
        this.capacity = capacity;    
    }
    
    class Node implements Comparable<Node> 
    {
        int level;
        LinkedList<Topic> taken;
        double bound;
        double value;
        double weight;

        Node() 
        {
           taken = new LinkedList<Topic>();
        }

        Node(Node parent) 
        {
           level = parent.level + 1;
           taken = new LinkedList<Topic>(parent.taken);
           bound = parent.bound;
           value = parent.value;
           weight = parent.weight;
        }

        // Sort by bound
        public int compareTo(Node other) 
        {
           return (int) (bound - other.bound);
        }

        void computeBound() 
        {
           int i = level;
           double w = weight;
           bound = value;
           Topic t;
           try
           {
                do 
                {
                     t = topics.get(i);
                     if (w + t.weight > capacity) 
                     {
                         i++;
                         continue;
                     }
                     w += t.weight;
                     bound += t.rank;
                     i++;

                 } while (i < topics.size());
                 bound += (capacity - w) * t.getRatio();
            }
            catch(Exception ex)
            {}
        }//computeBound
    }//Node   
   
    LinkedList<Topic> solve() 
    {
        Collections.sort(topics);

        Node best = new Node();
        Node root = new Node();
        root.computeBound();

        PriorityQueue<Node> q = new PriorityQueue<Node>();
        q.offer(root);

        //Best First Search
        while (!q.isEmpty()) 
        {
           Node node = q.poll();
           if (node.bound > best.value && node.level < topics.size()) 
           {
              Node with = new Node(node);
              Topic t = topics.get(node.level);
              with.weight += t.weight;

              if (with.weight <= capacity) 
              {
                 with.taken.add(t);
                 with.value += t.rank;
                 with.computeBound();

                 if (with.value > best.value) 
                 {
                    best = with;
                 }
                 
                 if (with.bound > best.value) 
                 {
                    q.offer(with);
                 }
              }

              Node without = new Node(node);
              without.computeBound();

              if (without.bound > best.value) 
              {
                 q.offer(without);
              }
           }//if
        }//while(!q.isEmpty())
        return best.taken;
    }//solve()
}//BranchAndBoundSolver

public class KnapSack 
{
    public LinkedList<Topic> resolveKnapSackProblem(LinkedList<Topic> topics, int capacity)
    {
        BranchAndBoundSolver bbs =new BranchAndBoundSolver(topics, capacity);
        return  bbs.solve();
    }
}//KnapSack