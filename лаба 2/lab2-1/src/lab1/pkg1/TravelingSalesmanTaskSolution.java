package lab1.pkg1;

import java.util.*;

public class TravelingSalesmanTaskSolution 
{
    public TravelingSalesmanTaskSolution(ExtendMatrix costsMatrix) throws Exception
    {
        this.costsMatrix = costsMatrix.copy();
        this.originalCostsMatrix = costsMatrix.copy();
        choicesList = new ArrayList<ExtendMatrix>();
        executeSolution();
    }
    
    public String getOutput()
    {
        return output;
    }
    
    public Matrix getAssignmentMatrix()
    {
        return this.assignmentMatrix;
    }
    
    public double getCost()
    {
        return getPathCost(this.assignmentMatrix);
    }
     
    private void executeSolution() throws Exception
    {
        printOutput("\n    Исходная матрица стоимостей:", costsMatrix);
        executeStartSettings();
        printOutput("\n    Первоначальная матрица назначений:", curX);
        printOutput("\n,   Первоначальная стоимость пути:", getPathCost(curX));
        while (!choicesList.isEmpty())
            executeSolutionStep();
        this.assignmentMatrix = curX;
        printOutput("\n    Полученная матрица назначений:", this.assignmentMatrix);
        printOutput("\n    Стоимость пути:", this.getCost());
    }
    
    private void executeStartSettings()
    {
        int degree = costsMatrix.getDegree();
        curX = getInitialEstimates(degree);
        curF =  getPathCost(curX);
        choicesList = new ArrayList<ExtendMatrix>();
        choicesList.add(costsMatrix.copy());
    }
    
    private Matrix getInitialEstimates(int degree)
    {
        Matrix res = new Matrix(degree);
        for (int  i = 0; i < degree; i++)
            res = getRowInitialEstimates(res, i);
        return res;       
    }
    
    private Matrix getRowInitialEstimates(Matrix matrix, int indexOfRow)
    {
        int degree = matrix.getDegree();
        for (int j = 0; j < degree; j++)
            if (isInitialEstimatesElement(indexOfRow, j, degree))
                matrix.setVal(indexOfRow, j, 1);
            else
                matrix.setVal(indexOfRow, j, 0);
        return matrix;
    }
    
    private boolean isInitialEstimatesElement(int indexOfRow, int indexOfColumn, int degree)
    {
        return indexOfColumn == (indexOfRow + 1) % degree;
    }
    
    private void executeSolutionStep() throws Exception
    {
        output += "\n номер итерации - " + numberOfIteration++;
        ExtendMatrix curMatrix = getCostsMatrixForCurrentTask();
        printOutput("\n Матрица стоимостей текущей подзадачи:", curMatrix);
        calculateCurrentSolution(curMatrix);
        changeGeneralSolutionOrAddSubtasksIfNecessary(curMatrix);
    }
    
    private ExtendMatrix getCostsMatrixForCurrentTask()
    { 
        ExtendMatrix curMatrix = choicesList.get(0);
        choicesList.remove(0);
        return curMatrix;
    }
    
    private void calculateCurrentSolution(ExtendMatrix curMatrix)
    {
        HungarianMethod hungarianMethod = new HungarianMethod(curMatrix); 
        x0 = hungarianMethod.getAssignmentMatrix();
        //output += hungarianMethod.getOutput();
        printOutput("\n    x0:", x0);
        f0 = getPathCost(x0);
        printOutput("\n    f0:", f0);
    }
    
    private double getPathCost(Matrix x)
    {
        int degree = originalCostsMatrix.getDegree();
        double value = 0;
        for (int  i = 0; i < degree; i++)
            for (int  j = 0; j < degree; j++)
                if (Math.abs(x.getVal(i, j) - 1.0) <= EPS)
                    value += originalCostsMatrix.getVal(i, j);
        return value;
    }
    
    private void changeGeneralSolutionOrAddSubtasksIfNecessary(ExtendMatrix curMatrix) throws Exception
    {
        if (f0 < curF)
            if (isCompleteCycle(x0))
                saveNewGeneralSolution();
            else
                addSubtasks(curMatrix);
    }
    
    private boolean isCompleteCycle(Matrix matrix) throws Exception
    {
        cyclesSet = new CyclesSet(matrix);
        int p = cyclesSet.getMinCycleLength();
        output += "\n Подциклы: " + cyclesSet.getOutput() + "\n\n";
        int degree = matrix.getDegree();
        return p == degree;
    }
    
    private void saveNewGeneralSolution()
    {
        curX = x0.copy();
        printOutput("\n   x*:", curX);
        curF = f0;
        printOutput("\n   f*:", curF);
    }
    
    private void addSubtasks(ExtendMatrix matrix) throws Exception
    {
        Cycle minCycle = cyclesSet.getMinCycle();
        int countOfElements = minCycle.getLength();
        for (int i = 0; i < countOfElements; i++)
            addSubtask(i, minCycle, matrix);
        printOutput("\nДобавлено " + countOfElements + 
                    " подзадачи, общее количество подзадач - " + 
                    choicesList.size() +"\n");
    }

    private void addSubtask(int indexOfSubtask, Cycle cycle, ExtendMatrix matrix) throws Exception
    {
        int countOfElements = cycle.getLength();
        int indexOfRow = cycle.getElement(indexOfSubtask);
        int indexOfColumn = cycle.getElement((indexOfSubtask + 1)% countOfElements);
        
        ExtendMatrix res = matrix.copy();
        res.setMarkToElem(indexOfRow, indexOfColumn, (char)8734);
        res.setVal(indexOfRow, indexOfColumn, Double.POSITIVE_INFINITY);
        choicesList.add(res);
    }
    
    private void printOutput(String prefix, Matrix matrix)
    {
        output += prefix;
        output += matrix.printToString();
    }
    
    private void printOutput(String prefix, double value)
    {
        output += prefix;
        output += "\n    " + value +"\n";
    }
    
    private void printOutput(String mes)
    {
        output += mes;
    }
    
    
    private CyclesSet cyclesSet;
    private Matrix curX;
    private double curF;
    private Matrix x0;
    private double f0;
    private List<ExtendMatrix> choicesList;
    private Matrix assignmentMatrix;
    private ExtendMatrix costsMatrix;
    private ExtendMatrix originalCostsMatrix;
    private String output = "";
    private final double EPS = 1e-6; 
    private int numberOfIteration = 0;
}
