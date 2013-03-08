package lab1.pkg1;

import java.util.ArrayList;
import lab1.pkg1.ElementLChain;



public class HungarianMethod 
{
    public String getOutput()
    {
        return output;
    }

    public Matrix solveTaskMaximizationWithPrintOutput(Matrix initMatrix)
    {
        Matrix modifMatrix = transformMaximizationTaskToMinimizationTask(initMatrix);
        return solveTaskMinimizationWithPrintOutput(modifMatrix);
    }
    
    public Matrix solveTaskMaximization(Matrix initMatrix)
    {
        Matrix modifMatrix = transformMaximizationTaskToMinimizationTask(initMatrix);
        return solveTaskMinimization(modifMatrix);
    }
    
    public Matrix solveTaskMinimizationWithPrintOutput(Matrix initMatrix)
    {
        establishPrintOutput();
        return solveStandartTask(initMatrix);       
    }
    
    public Matrix solveTaskMinimization(Matrix initMatrix)
    {
        establishNotPrintOutput();
        return solveStandartTask(initMatrix);
    }

    private Matrix solveStandartTask(Matrix sourceMatrix)
    {
        Matrix initMatrix = new ExtendMatrix(sourceMatrix);
        printOutput(initMatrix);
        ExtendMatrix prepMatrix = prepStage(initMatrix);
        printOutput(prepMatrix);
        return mainStage(prepMatrix);
    }
    
    private ExtendMatrix prepStage(Matrix matrix)
    {
        Matrix prepMatrix = createZeroElementsInMatrix(matrix);
        return buildInitIndependentZerosSystem(prepMatrix);
    }
    
    private Matrix createZeroElementsInMatrix(Matrix matrix)
    {
        Matrix prepMatrix = createZeroElementsInMatrixColumns(matrix);
        return createZeroElementsInMatrixRows(prepMatrix);
    }
    
    private Matrix createZeroElementsInMatrixColumns(Matrix matrix)
    {
        int n = matrix.getDegree();
        Matrix resMatrix = new Matrix(matrix);
        for (int j = 0; j < n; j++)
        {
            double minElem = minElemColumn(resMatrix, j);
            resMatrix = subNumberFromColumnElements(resMatrix, j, minElem);
        }        
        return resMatrix;
    }
    
    private Matrix subNumberFromColumnElements(Matrix matrix, int indexColumn, double num)
    {
        int n = matrix.getDegree();
        Matrix resMatrix = new Matrix(matrix);
        for (int i = 0; i < n; i++)
        {
            double elem = resMatrix.getVal(i, indexColumn) - num;
            resMatrix.setVal(i, indexColumn, elem);
        }
        return resMatrix;
    }
    
    private Matrix createZeroElementsInMatrixRows(Matrix matrix)
    {
        int n = matrix.getDegree();
        Matrix resMatrix = new Matrix(matrix);
        for (int i = 0; i < n; i++)
        {
            double minElem = minElemRow(resMatrix, i);
            resMatrix = subNumberFromRowElements(resMatrix, i, minElem);
        }
        return resMatrix;
    }
    
    private Matrix subNumberFromRowElements(Matrix matrix, int indexRow, double num)
    {
        int n = matrix.getDegree();
        Matrix resMatrix = new Matrix(matrix);
        for (int j = 0; j < n; j++)
        {
            double elem = resMatrix.getVal(indexRow, j) - num;
            resMatrix.setVal(indexRow, j, elem);
        }
        return resMatrix;
    }
    
    private ExtendMatrix buildInitIndependentZerosSystem(Matrix matrix)
    {
        int n = matrix.getDegree();
        ExtendMatrix resMatrix = new ExtendMatrix(matrix);
        for (int j = 0; j < n; j++)
            resMatrix = findAndMarkAcceptableColumnZeroElements(resMatrix, j);
        return resMatrix;
    }
    
    private ExtendMatrix findAndMarkAcceptableColumnZeroElements(ExtendMatrix matrix, int indexColumn)
    {
        int n = matrix.getDegree();
        ExtendMatrix resMatrix = matrix;
        for (int i = 0; i < n; i++)
        {
            if (isFitElement(resMatrix, i, indexColumn))
            {
                resMatrix.setMarkToElem(i, indexColumn, '*');
                break;
            }
        }
        return resMatrix;
    }
    
    private boolean isFitElement(ExtendMatrix matrix, int indexRow, int indexColumn)
    {
        double elem = matrix.getVal(indexRow, indexColumn);
        return isElementEqualsZero(elem) && !isExistMarkedStarZeroInRow(matrix, indexRow);        
    }
    
    private boolean isElementEqualsZero(double elem)
    {
        return Math.abs(elem) < EPS;
    }
    
    private boolean isExistMarkedStarZeroInRow(ExtendMatrix matrix, int indexRow)
    {
        int n = matrix.getDegree();
        for (int j = 0; j < n; j++)
            if (matrix.hasElemMark(indexRow, j, '*'))
                return true;
        return false;
    }
    
    private Matrix mainStage(ExtendMatrix matrix)
    {
        ExtendMatrix workMatrix = matrix; 
        int n = matrix.getDegree();
        int k = computeCountZeroInIZS(matrix);
        while (k < n)
        {
            workMatrix = executeMainActionsAlgorithm(workMatrix);
            k = computeCountZeroInIZS(workMatrix);
        }
        return writeOptimalSolution(workMatrix);
    }
    
    private int computeCountZeroInIZS(ExtendMatrix matrix)
    {
        int count = 0;
        int n = matrix.getDegree();
        for (int j = 0; j < n; j++)
            count += computeCountZeroInColumnIZS(matrix, j);
        return count;
    }
    
    private int computeCountZeroInColumnIZS(ExtendMatrix matrix, int indexColumn)
    {
        int  n = matrix.getDegree();
        for (int  i = 0; i < n; i++)
            if (matrix.hasElemMark(i, indexColumn, '*'))
                return 1;
        return 0;
    }
    
    private ExtendMatrix executeMainActionsAlgorithm(ExtendMatrix matrix)
    {
        ExtendMatrix workMatrix = matrix;
        workMatrix = markColumnsWithMarkedStarZero(workMatrix);
        printOutput(workMatrix);
        return changeIZS(workMatrix);
    }
    
    private ExtendMatrix markColumnsWithMarkedStarZero(ExtendMatrix matrix)
    {
        int n = matrix.getDegree();
        ExtendMatrix resMatrix = matrix;
        for (int j = 0; j < n; j++)
            resMatrix = checkAndMarkColumnWithMarkedStarElement(resMatrix, j);
        return resMatrix;        
    }
    
    private ExtendMatrix checkAndMarkColumnWithMarkedStarElement(ExtendMatrix matrix, int indexColumn)
    {
        ExtendMatrix resMatrix = matrix;
        int n = matrix.getDegree();
        for (int i = 0; i < n; i++)
            if (matrix.hasElemMark(i, indexColumn, '*'))
            {
                resMatrix.setMarkToColumn(indexColumn);
                break;
            }
        return resMatrix;
    }
    
    private ExtendMatrix changeIZS(ExtendMatrix matrix)
    {
        ExtendMatrix workMatrix = matrix;
        boolean flgMarkedStarZeroInRow = true;
        while (flgMarkedStarZeroInRow)
        {
            if (!checkExistUnmarkedZero(workMatrix))
                workMatrix = improveMatrix(workMatrix);

            int unmarkZeroJ = findColumnWithUnmarkElem(workMatrix);
            int unmarkZeroI = findRowWithUnmarkElem(workMatrix, unmarkZeroJ);

            workMatrix.setMarkToElem(unmarkZeroI, unmarkZeroJ, '\'');
            printOutput(workMatrix);

            if (checkExistMarkStarElementInRow(workMatrix, unmarkZeroI))
            {
                workMatrix.setMarkToRow(unmarkZeroI);
                int indMarkedStarZero = findIndexMarkedStartZeroInRow(workMatrix, unmarkZeroI);
                workMatrix.resetMarkFromColumn(indMarkedStarZero);
                printOutput(workMatrix);
            }
            else
            {
                flgMarkedStarZeroInRow = false;
                LChain lchain = new LChain();
                ArrayList<ElementLChain> masElem = lchain.getLChain(workMatrix, unmarkZeroI, unmarkZeroJ);
                workMatrix = replaceFromL(workMatrix, masElem);
                workMatrix.delAllMarksFromRowsAndColumns();
                workMatrix.delMarkFromAllElements('\'');
                printOutput(workMatrix);
            }
        }
        return workMatrix;
    }

    private ExtendMatrix improveMatrix(ExtendMatrix matrix)
    {
        ExtendMatrix resMatrix = matrix;
        double h = findMinUnmarkedElem(resMatrix);
        resMatrix = subHFromUnmarkedRows(resMatrix, h);
        resMatrix = addHToMarkedColumns(resMatrix, h);
        printOutput(resMatrix);
        return resMatrix;
    }
    
    private double findMinUnmarkedElem(ExtendMatrix matrix)
    {
        double  minElem = 0;
        boolean flgFindUnmarkedElem = false;
        int n = matrix.getDegree();
        for (int i = 0; i < n; i++)
            if (!matrix.hasRowMark(i))
                for (int j = 0; j < n; j++)
                    if (!matrix.hasColumnMark(j))
                    {
                        if (!flgFindUnmarkedElem)
                        {
                            flgFindUnmarkedElem = true;
                            minElem = matrix.getVal(i, j);
                        }
                        else
                        {
                            double elem = matrix.getVal(i, j);
                            if (elem < minElem)
                                minElem = elem;
                        }
                    }
        return minElem;
    }
    
    private ExtendMatrix subHFromUnmarkedRows(ExtendMatrix matrix, double h)
    {
        int n = matrix.getDegree();
        ExtendMatrix res = matrix; 
        for (int i = 0; i < n; i++)
            if (!res.hasRowMark(i))
                for (int j = 0; j < n; j++)
                    res.setVal(i, j, res.getVal(i, j) - h);
        return res;
    }
    
    private ExtendMatrix addHToMarkedColumns(ExtendMatrix matrix, double h)
    {
        int n = matrix.getDegree();
        ExtendMatrix res = matrix; 
        for (int j = 0; j < n; j++)
            if (res.hasColumnMark(j))
                for (int i = 0; i < n; i++)
                    res.setVal(i, j, res.getVal(i, j) + h);
        return res;
    }
   
    public int findColumnWithUnmarkElem(ExtendMatrix matrix)
    {
        int n = matrix.getDegree();
        for (int j = 0; j < n; j++)
            if (!matrix.hasColumnMark(j) && isContentColumnUnmarkElem(matrix, j))
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
    
    public int findRowWithUnmarkElem(ExtendMatrix matrix, int indexColumn)
    {
        int n = matrix.getDegree();
        for (int i = 0; i < n; i++)
            if (!matrix.hasRowMark(i) && isElementUnmarked(matrix, i, indexColumn))
                    return i;
        return -1;                
    }
    
    private boolean checkExistUnmarkedZero(ExtendMatrix matrix)
    {
        int  n = matrix.getDegree();
        for (int i = 0; i < n; i++)
            if (!matrix.hasRowMark(i) && checkExistUnmarkedZeroInRow(matrix, i))
               return true;
        return false;
    }
    
    private boolean checkExistUnmarkedZeroInRow(ExtendMatrix matrix, int indexRow)
    {
        int n = matrix.getDegree();
        for (int j = 0; j < n; j++)
            if (checkElementForEqualsZeroAndUnmarked(matrix, indexRow, j))
                return true;
        return false;
    }
    
    private boolean checkElementForEqualsZeroAndUnmarked(ExtendMatrix matrix, int indexRow, int indexColumn)
    {
        double elem = matrix.getVal(indexRow, indexColumn);
        return !matrix.hasColumnMark(indexColumn) && isElementEqualsZero(elem);
    }
    
    private boolean checkExistMarkStarElementInRow(ExtendMatrix matrix, int i)
    {
        int n = matrix.getDegree();
        for (int j = 0; j < n; j++)
            if (matrix.hasElemMark(i, j, '*'))
                return true;
        return false;
    }  
    
    private int findIndexMarkedStartZeroInRow(ExtendMatrix matrix, int i)
    {
        int n = matrix.getDegree();
        for (int j = 0; j < n; j++)
            if (matrix.hasElemMark(i, j, '*'))
                return j;
        return -1;
    }
    
    private ExtendMatrix replaceFromL(ExtendMatrix matrix, ArrayList<ElementLChain> lchain)
    {
        ExtendMatrix resMatrix = matrix; 
        for (ElementLChain elem : lchain)
            if (elem.getMark() == '*')
                resMatrix.setMarkToElem(elem.getI(), elem.getJ(), (char) 0);
            else if (elem.getMark() == '\'')
                resMatrix.setMarkToElem(elem.getI(), elem.getJ(), '*');
        return resMatrix;
    }
     
    private double minElemColumn(Matrix matrix, int j)
    {
        double minElem = matrix.getVal(0, j);
        int n = matrix.getDegree();
        for (int i = 0; i < n; i++)
            if (minElem > matrix.getVal(i, j))
                minElem = matrix.getVal(i, j);
        return minElem;
    }

    private double minElemRow(Matrix matrix, int i)
    {
        double minElem = matrix.getVal(i, 0);
        int n = matrix.getDegree();
        for (int j = 0; j < n; j++)
            if (minElem > matrix.getVal(i, j))
                minElem = matrix.getVal(i, j);
        return minElem;
    }
    
    private Matrix transformMaximizationTaskToMinimizationTask(Matrix initMatrix)
    {
        int n = initMatrix.getDegree();
        Matrix modifMatrix = new Matrix(n);
        double maxElem = findMaxElem(initMatrix);
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                modifMatrix.setVal(i, j, maxElem - initMatrix.getVal(i, j));
        return modifMatrix;
    }
    
    private double findMaxElem(Matrix matrix)
    {
        double maxElem = matrix.getVal(0, 0);
        int n = matrix.getDegree();
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (maxElem < matrix.getVal(i, j))
                    maxElem = matrix.getVal(i, j);
        return maxElem;
    }
    
    private Matrix writeOptimalSolution(ExtendMatrix costMatrix)
    {
        int  n = costMatrix.getDegree();
        Matrix res = new Matrix(n);
        for (int j = 0; j < n; j++)
            res = addColumnInAssignmentMatrix(res, costMatrix, j);
        return res;
    }  
    
    private Matrix addColumnInAssignmentMatrix(Matrix assignmentMatrix, ExtendMatrix costMatrix, int indexColumn)
    {
        Matrix resMatrix = assignmentMatrix;
        int n = resMatrix.getDegree();
        for (int i = 0; i < n; i++)
            resMatrix = establishValueOfAssignmentMatrixElement(assignmentMatrix, costMatrix, i, indexColumn);
        return resMatrix;         
    }
    
    private Matrix establishValueOfAssignmentMatrixElement(Matrix assignmentMatrix, ExtendMatrix costMatrix, 
            int indexRow, int indexColumn)
    {
        if (costMatrix.hasElemMark(indexRow, indexColumn, '*'))
            assignmentMatrix.setVal(indexRow, indexColumn, 1);
        else 
            assignmentMatrix.setVal(indexRow, indexColumn, 0);
        return assignmentMatrix;
    }
  
    private void printOutput(Matrix matrix)
    {
        if (hasEstablishPrintOutput())
            output += matrix.printToString();
    }
    
    private void establishPrintOutput()
    {
        this.flgPrintOutput = true;
    }
    
    private void establishNotPrintOutput()
    {
        this.flgPrintOutput = false;
    }
    
    private boolean hasEstablishPrintOutput()
    {
        return this.flgPrintOutput;
    }

  
    private boolean flgPrintOutput;
    private final double EPS = 1e-6; 
    private String output = "";
}
