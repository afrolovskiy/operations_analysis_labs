package lab1.pkg1;

import java.util.ArrayList;

public class LChain 
{
    public LChain()
    {
        chain = new ArrayList<ElementLChain>();
    }
    
    public ArrayList<ElementLChain> getLChain(ExtendMatrix matrix, int startIndexRow, int startIndexColumn)
    {
        boolean flg = true;
        int n = matrix.getDegree();
        
        chain = new ArrayList<ElementLChain>();
        chain.add(new ElementLChain(startIndexRow, startIndexColumn, '\'',
                matrix.getVal(startIndexRow, startIndexColumn)));
        
        int findIndexRow;
        int findIndexColumn;
        int oldIndexColumn = startIndexColumn;
        
        while (flg)
        {
            findIndexRow = findIndexRowElementWithStar(matrix, oldIndexColumn); 
            
            if (findIndexRow >= 0)
            {
                findIndexColumn = findIndexColumnElementWithUnary(matrix, findIndexRow);

                if (findIndexColumn >= 0)
                {
                    chain.add(new ElementLChain(findIndexRow, oldIndexColumn, '*', 0));
                    chain.add(new ElementLChain(findIndexRow, findIndexColumn, '\'', 0));
                    oldIndexColumn = findIndexColumn;
                }
                else 
                    flg = false;
            }
            else
                flg = false;
        }
        
        return chain;
    }
    
    private int findIndexRowElementWithStar(ExtendMatrix matrix, int indexColumn)
    {
        int n = matrix.getDegree();
        for (int i = 0; i < n; i++)
            if (matrix.hasElemMark(i, indexColumn, '*'))
                return i;
        return -1;
    }
    
    private int findIndexColumnElementWithUnary(ExtendMatrix matrix, int indexRow)
    {
        int n = matrix.getDegree();
        for (int j = 0; j < n; j++)
            if (matrix.hasElemMark(indexRow, j, '\''))
                return j;
        return -1;
    }
    
    private ArrayList<ElementLChain> chain;
}
