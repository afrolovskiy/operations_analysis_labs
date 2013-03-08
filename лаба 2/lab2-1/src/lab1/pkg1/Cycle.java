package lab1.pkg1;

import java.util.*;

public class Cycle implements Comparable 
{
    public Cycle()
    {
        this.sequence = new ArrayList<Integer>();
    }
    
    public int getLength()
    {
        return this.sequence.size();
    }
    
    public int getElement(int index) throws Exception
    {
        try
        {
            return sequence.get(index);
        }
        catch (Exception err)
        {
            throw new Exception("Error: not found elemnt with this index");
        }
    }
    
    public int getStartElement() throws Exception
    {
        try
        {
            return this.sequence.get(0);
        }
        catch (Exception err)
        {
            throw new Exception("Error: not found first element");
        }
    }
    
    void add(int el)
    {
        sequence.add(el);
    }
    
    boolean isExistElement(int element)
    {
        for (Integer el: sequence)
            if (el.equals(element))
                return true;
        return false;
    }
    
    
    @Override
    public int compareTo(Object obj) 
    {
        if (!(obj instanceof Cycle))
            throw new UnsupportedOperationException("Not supported yet.");
        Cycle cycle = (Cycle) obj;
        int difference = this.getLength() - cycle.getLength();
        return difference;
    }
    
    public String print()
    {
        String output = "(";
        for (int  i = 0; i < sequence.size(); i++)
            output += " " + sequence.get(i);
        return output + ")";
    }
    
    private ArrayList<Integer> sequence;
}
