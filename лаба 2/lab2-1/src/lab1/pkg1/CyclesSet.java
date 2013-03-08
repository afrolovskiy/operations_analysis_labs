package lab1.pkg1;

import java.util.*;

public class CyclesSet 
{
    public CyclesSet(Matrix assignmentMatrix) throws Exception
    {
        this.assignmentMatrix = assignmentMatrix.copy();
        this.cycles = new ArrayList<Cycle>();
        calculateCycles();
        findMinCycle();
        printCycles();
    }
    
    public String getOutput()
    {
        return output;
    }
    
    public void calculateCycles() throws Exception
    {
        numberOfLostElements = this.assignmentMatrix.getDegree();
        while (numberOfLostElements != 0)
        {
            Cycle curCycle = getNewCycle();
            cycles.add(curCycle);
        }
    }
    
    public Cycle getNewCycle() throws Exception
    {
        int startElementIndex = getFreeStartElement();
        Cycle curCycle = new Cycle();
       // curCycle.add(startElementIndex);
       // numberOfLostElements--;;
        int curElementIndex = startElementIndex;
        int lastElementIndex = startElementIndex;
        
        do{
            curElementIndex = getIndexOfColumnWithOneElement(lastElementIndex);
            curCycle.add(curElementIndex);
            numberOfLostElements--;
            lastElementIndex = curElementIndex;
        }while (startElementIndex != curElementIndex && numberOfLostElements != 0);
        
        return curCycle;            
    }
    
    public int getIndexOfColumnWithOneElement(int indexOfRow) throws Exception
    {
        int degree = this.assignmentMatrix.getDegree();
        for (int j = 0; j < degree; j++)
            if (this.assignmentMatrix.getVal(indexOfRow, j) == 1)
                return j;
        throw new Exception("Error: not found one element");
    }
    
    public int getFreeStartElement() throws Exception
    {
        int degree = this.assignmentMatrix.getDegree();
        for (int  i = 0; i < degree; i++)
            if (isFreeElement(i))
                return i;
        throw new Exception("Error: not found free element");
    }
    
    public boolean isFreeElement(int elementIndex)
    {
        for (Cycle cycle: cycles)
            if (cycle.isExistElement(elementIndex))
                return false;
        return true;
    }
            
    public void findMinCycle()
    {
        Object minObj = Collections.min(cycles);
        minCycle = (Cycle) minObj;
    }

    public int getCountOfCycles()
    {
        return cycles.size();
    }
    
    public Cycle getMinCycle()
    {
        return this.minCycle;
    }
    
    public int getMinCycleLength()
    {
        return this.minCycle.getLength();
    }
    
    public void printCycles()
    {
        for (int  i = 0; i < cycles.size(); i++)
        {
            Cycle cycle = cycles.get(i);
            output += cycle.print();
        }
    }
    
    private int numberOfLostElements;
    private Cycle minCycle;
    private Matrix assignmentMatrix;
    private ArrayList<Cycle> cycles;
    String output = "";
}
