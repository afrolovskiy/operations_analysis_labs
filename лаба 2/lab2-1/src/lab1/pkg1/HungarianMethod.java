package lab1.pkg1;

import java.util.ArrayList;
import lab1.pkg1.ElementLChain;



public class HungarianMethod 
{
    public HungarianMethod(ExtendMatrix costsMatrix)
    {
        this.costsMatrix = costsMatrix.copy();
        this.originalCostsMatrix = costsMatrix.copy();
        solveStandartTask();
    }  
    
    public Matrix getAssignmentMatrix()
    {
        return this.assignmentMatrix;
    }
    
    public String getOutput()
    {
        return output;
    }
    
    public double getCost()
    {
        double res = 0;
        int n = assignmentMatrix.getDegree();
        for (int  i = 0; i < n; i++)
            res += getCostFromRow(i);
        return res;
    }

    private double getCostFromRow(int indexOfRow)
    {
        int n = assignmentMatrix.getDegree();
        for (int  j = 0; j < n; j++)
            if (Math.abs(assignmentMatrix.getVal(indexOfRow, j) - 1.0) <= EPS)
                return this.originalCostsMatrix.getVal(indexOfRow, j);
        return 0;
    }
    
    private void solveStandartTask()
    {
        printOutput(costsMatrix);
        executePrepareStage();
        printOutput(costsMatrix);
        mainStage();
        writeOptimalSolution();
    }
    
    private void executePrepareStage()
    {
        createZeroElementsInCostsMatrix();
        buildInitIndependentZerosSystem();
    }
    
    private void createZeroElementsInCostsMatrix()
    {
        createZeroElementsInCostsMatrixColumns();
        createZeroElementsInCostsMatrixRows();
    }
    
    private void createZeroElementsInCostsMatrixColumns()
    {
        int n = costsMatrix.getDegree();
        for (int j = 0; j < n; j++)
        {
            double minElem = minElemColumn(costsMatrix, j);
            subNumberFromColumnElements(costsMatrix, j, minElem);
        }        
    }
         
    private double minElemColumn(ExtendMatrix matrix, int columnIndex)
    {
        double minElem = matrix.getVal(0, columnIndex);
        int n = matrix.getDegree();
        for (int i = 1; i < n; i++)
            if (minElem > matrix.getVal(i, columnIndex))
                minElem = matrix.getVal(i, columnIndex);
        return minElem;
    }
    
    private void subNumberFromColumnElements(ExtendMatrix matrix, int indexOfColumn, double subtractedNumber)
    {
        int n = matrix.getDegree();
        for (int i = 0; i < n; i++)
            subNumberFromElement(matrix, i, indexOfColumn, subtractedNumber);
    }
    
    private void subNumberFromElement(ExtendMatrix matrix, int indexOfRow, int indexOfColumn, double subtractedNumber)
    {
        // 8734 <- бесконечность))
        if (!matrix.hasElemMark(indexOfRow, indexOfColumn, (char)8734))
        {
            double elem = matrix.getVal(indexOfRow, indexOfColumn) - subtractedNumber;
            matrix.setVal(indexOfRow, indexOfColumn, elem);
        }
    }
    
    private void createZeroElementsInCostsMatrixRows()
    {
        int n = costsMatrix.getDegree();
        for (int i = 0; i < n; i++)
        {
            double minElem = minElemRow(costsMatrix, i);
            subNumberFromRowElements(costsMatrix, i, minElem);
        }
    }
    
    private double minElemRow(Matrix matrix, int indexOfRow)
    {
        double minElem = matrix.getVal(indexOfRow, 0);
        int n = matrix.getDegree();
        for (int j = 0; j < n; j++)
            if (minElem > matrix.getVal(indexOfRow, j))
                minElem = matrix.getVal(indexOfRow, j);
        return minElem;
    }
    
    private void subNumberFromRowElements(ExtendMatrix matrix, int indexOfRow, double subtractedNumber)
    {
        int n = matrix.getDegree();
        for (int j = 0; j < n; j++)
            subNumberFromElement(matrix, indexOfRow, j, subtractedNumber);
    }
    
    private void buildInitIndependentZerosSystem()
    {
        int n = costsMatrix.getDegree();
        for (int j = 0; j < n; j++)
            findAndMarkAcceptableColumnZeroElements(j);
    }
    
    private void findAndMarkAcceptableColumnZeroElements(int indexColumn)
    {
        int n = costsMatrix.getDegree();
        for (int i = 0; i < n; i++)
            if (isFitElement(i, indexColumn))
            {
                costsMatrix.setMarkToElem(i, indexColumn, '*');
                break;
            }
    }
    
    private boolean isFitElement(int indexRow, int indexColumn)
    {
        double elem = costsMatrix.getVal(indexRow, indexColumn);
        return isElementEqualsZero(elem) && isNotExistMarkedStarZeroInRow(indexRow);        
    }
    
    private boolean isElementEqualsZero(double elem)
    {
        return Math.abs(elem) < EPS;
    }
    
    private boolean isNotExistMarkedStarZeroInRow(int indexRow)
    {
        return !isExistMarkedStarZeroInRow(indexRow);
    }
    
    private boolean isExistMarkedStarZeroInRow(int indexRow)
    {
        int n = costsMatrix.getDegree();
        for (int j = 0; j < n; j++)
            if (costsMatrix.hasElemMark(indexRow, j, '*'))
                return true;
        return false;
    }
    
    private void mainStage()
    {
        int n = costsMatrix.getDegree();
        int k = getCountZeroInIZS();
        while (k < n)
        {
            executeMainActionsAlgorithm();
            k = getCountZeroInIZS();
        }
    }
    
    private int getCountZeroInIZS()
    {
        int count = 0;
        int n = costsMatrix.getDegree();
        for (int j = 0; j < n; j++)
            count += getCountZeroInColumnIZS(j);
        return count;
    }
    
    private int getCountZeroInColumnIZS(int indexColumn)
    {
        int  n = costsMatrix.getDegree();
        for (int  i = 0; i < n; i++)
            if (costsMatrix.hasElemMark(i, indexColumn, '*'))
                return 1;
        return 0;
    }
    
    private void executeMainActionsAlgorithm()
    {
        markColumnsWithMarkedStarZero();
        printOutput(costsMatrix);
        changeIZS();
    }
    
    private void markColumnsWithMarkedStarZero()
    {
        int n = costsMatrix.getDegree();
        for (int j = 0; j < n; j++)
            checkAndMarkColumnWithMarkedStarElement(j);
    }
    
    private void checkAndMarkColumnWithMarkedStarElement(int indexColumn)
    {
        int n = costsMatrix.getDegree();
        for (int i = 0; i < n; i++)
            if (costsMatrix.hasElemMark(i, indexColumn, '*'))
            {
                costsMatrix.setMarkToColumn(indexColumn);
                break;
            }
    }
    
    private void changeIZS()
    {
        flgMarkedStarZeroInRow = true;
        do
        {
            improveCostsMatrixIfNecessary();            

            unmarkZeroJ = findColumnWithUnmarkElem();
            unmarkZeroI = findRowWithUnmarkElem(unmarkZeroJ);
            costsMatrix.setMarkToElem(unmarkZeroI, unmarkZeroJ, '\'');
            printOutput(costsMatrix);

            executeMainActionsInChangingIZS();           
        }while (flgMarkedStarZeroInRow);
    }
    
    private void improveCostsMatrixIfNecessary()
    {
        if (!checkExistUnmarkedZero())
            improveCostsMatrix();
    }
    
    private boolean checkExistUnmarkedZero()
    {
        int  n = costsMatrix.getDegree();
        for (int i = 0; i < n; i++)
            if (!costsMatrix.hasRowMark(i) && checkExistUnmarkedZeroInRow(i))
               return true;
        return false;
    }
    
    private boolean checkExistUnmarkedZeroInRow(int indexOfRow)
    {
        int n = costsMatrix.getDegree();
        for (int j = 0; j < n; j++)
            if (checkElementForEqualsZeroAndUnmarked(costsMatrix, indexOfRow, j))
                return true;
        return false;
    }
    
    private boolean checkElementForEqualsZeroAndUnmarked(ExtendMatrix matrix, int indexRow, int indexColumn)
    {
        double elem = matrix.getVal(indexRow, indexColumn);
        return !matrix.hasColumnMark(indexColumn) && isElementEqualsZero(elem);
    }

    private void improveCostsMatrix()
    {
        double h = findMinUnmarkedElem();
        subHFromUnmarkedRows(costsMatrix, h);
        addHToMarkedColumns(costsMatrix, h);
        printOutput(costsMatrix);
    }
    
    private double findMinUnmarkedElem()
    {
        flgFindUnmarkedElem = false;
        int n = costsMatrix.getDegree();
        for (int i = 0; i < n; i++)
            findMinUnmarkedElementInRow(i);
        return minElement;
    }
    
    private void findMinUnmarkedElementInRow(int indexOfRow)
    {
        int n = costsMatrix.getDegree();
        if (!costsMatrix.hasRowMark(indexOfRow))
            for (int j = 0; j < n; j++)
                isMinUnmarkedElement(indexOfRow, j);
    }
    
    private void isMinUnmarkedElement(int indexOfRow, int indexOfColumn)
    {
        if (!costsMatrix.hasColumnMark(indexOfColumn))
            if (!flgFindUnmarkedElem)
            {
                flgFindUnmarkedElem = true;
                minElement = costsMatrix.getVal(indexOfRow, indexOfColumn);
            }
            else
            {
                double elem = costsMatrix.getVal(indexOfRow, indexOfColumn);
                if (elem < minElement)
                    minElement = elem;
            }
    }
    
    private void subHFromUnmarkedRows(ExtendMatrix matrix, double h)
    {
        int n = matrix.getDegree();
        for (int i = 0; i < n; i++)
            if (!matrix.hasRowMark(i))
                subNumberFromRowElements(matrix, i, h);
    }
    
    private void addHToMarkedColumns(ExtendMatrix matrix, double h)
    {
        int n = matrix.getDegree();
        for (int j = 0; j < n; j++)
            if (matrix.hasColumnMark(j))
                addNumberToColumnElements(matrix, j, h);
    }
    
    private void addNumberToColumnElements(ExtendMatrix matrix, int indexOfColumn, double addedNumber)
    {
        int n = matrix.getDegree();
        for (int i = 0; i < n; i++)
            addNumberToElement(matrix, i, indexOfColumn, addedNumber);
    }
    
    private void addNumberToElement(ExtendMatrix matrix, int indexOfRow, int indexOfColumn, double addedNumber)
    {
        // 8734 <- бесконечность))
        if (!matrix.hasElemMark(indexOfRow, indexOfColumn, (char)8734))
        {
            double elem = matrix.getVal(indexOfRow, indexOfColumn) + addedNumber;
            matrix.setVal(indexOfRow, indexOfColumn, elem);
        }
    }
   
    public int findColumnWithUnmarkElem()
    {
        int n = costsMatrix.getDegree();
        for (int j = 0; j < n; j++)
            if (!costsMatrix.hasColumnMark(j) && isContentColumnUnmarkElem(costsMatrix, j))
                return j;
        return -1;
    }

    private boolean isContentColumnUnmarkElem(ExtendMatrix matrix, int indexColumn)
    {
        int n = matrix.getDegree();
        for (int i = 0; i < n; i++)
            if (!matrix.hasRowMark(i) && isElementUnmarked(matrix, i, indexColumn))
                return true;
        return false;
    }
    
    private boolean isElementUnmarked(ExtendMatrix matrix, int indexRow, int indexColumn)
    {
        double elem = matrix.getVal(indexRow, indexColumn);
        return (isElementEqualsZero(elem) && 
                !matrix.hasElemMark(indexRow, indexColumn, '*') && 
                !matrix.hasElemMark(indexRow, indexColumn, '\''));
    }
    
    public int findRowWithUnmarkElem(int indexColumn)
    {
        int n = costsMatrix.getDegree();
        for (int i = 0; i < n; i++)
            if (!costsMatrix.hasRowMark(i) && isElementUnmarked(costsMatrix, i, indexColumn))
                    return i;
        return -1;                
    }
    
    private void executeMainActionsInChangingIZS()
    {
        if (checkExistMarkStarElementInRow(unmarkZeroI))
            executeActionsWhenExistMarkStarElementInRow();
        else
            executeActionsWhenNotExistMarkStarElementInRow();
    }
    
    private boolean checkExistMarkStarElementInRow(int indexOfRow)
    {
        int n = costsMatrix.getDegree();
        for (int j = 0; j < n; j++)
            if (costsMatrix.hasElemMark(indexOfRow, j, '*'))
                return true;
        return false;
    }  
    
    private void executeActionsWhenExistMarkStarElementInRow()
    {
        costsMatrix.setMarkToRow(unmarkZeroI);
        int markedStarZeroIndex = findIndexMarkedStartZeroInRow(unmarkZeroI);
        costsMatrix.resetMarkFromColumn(markedStarZeroIndex);
        printOutput(costsMatrix);
    }
    
    private int findIndexMarkedStartZeroInRow(int indexOfRow)
    {
        int n = costsMatrix.getDegree();
        for (int j = 0; j < n; j++)
            if (costsMatrix.hasElemMark(indexOfRow, j, '*'))
                return j;
        return -1;
    }
    
    private void executeActionsWhenNotExistMarkStarElementInRow()
    {
        flgMarkedStarZeroInRow = false;
        LChain lchain = new LChain();
        ArrayList<ElementLChain> masElem = lchain.getLChain(costsMatrix, unmarkZeroI, unmarkZeroJ);
        replaceFromL(masElem);
        costsMatrix.delAllMarksFromRowsAndColumns();
        costsMatrix.delMarkFromAllElements('\'');
        printOutput(costsMatrix);
    }
    
    private void replaceFromL(ArrayList<ElementLChain> lchain)
    {
        for (ElementLChain elem : lchain)
            if (elem.getMark() == '*')
                costsMatrix.setMarkToElem(elem.getI(), elem.getJ(), (char) 0);
            else if (elem.getMark() == '\'')
                costsMatrix.setMarkToElem(elem.getI(), elem.getJ(), '*');
    }
    
    private void writeOptimalSolution()
    {
        int  n = costsMatrix.getDegree();
        this.assignmentMatrix = new Matrix(n);
        for (int j = 0; j < n; j++)
            addColumnInAssignmentMatrix(j);
    }  
    
    private void addColumnInAssignmentMatrix(int indexOfColumn)
    {
        int n = assignmentMatrix.getDegree();
        for (int i = 0; i < n; i++)
            establishValueOfAssignmentMatrixElement(i, indexOfColumn);
    }
    
    private void establishValueOfAssignmentMatrixElement(int indexOfRow, int indexOfColumn)
    {
        if (costsMatrix.hasElemMark(indexOfRow, indexOfColumn, '*'))
            assignmentMatrix.setVal(indexOfRow, indexOfColumn, 1);
        else 
            assignmentMatrix.setVal(indexOfRow, indexOfColumn, 0);
    }
  
    private void printOutput(Matrix matrix)
    {
        output += matrix.printToString();
    }
    

  
    private final double EPS = 1e-6; 
    private boolean flgMarkedStarZeroInRow;
    int unmarkZeroJ;
    int unmarkZeroI;
    private double  minElement;
    boolean flgFindUnmarkedElem;
    private Matrix assignmentMatrix;
    private ExtendMatrix costsMatrix;
    private ExtendMatrix originalCostsMatrix;
    private String output = "";
}
