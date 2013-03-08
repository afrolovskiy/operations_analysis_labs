package lab1.pkg1;

import java.util.Formatter;

public class ExtendMatrix extends Matrix
{
    public ExtendMatrix(int degree)
    {
        super(degree);
        markColumns = new byte[degree];
        markRows = new byte[degree];
        marks = new char[degree][degree];
    }
    
    public ExtendMatrix(double[][] sourceMatrix)
    {
        super(sourceMatrix);
        int degree = sourceMatrix.length;
        markColumns = new byte[degree];
        markRows = new byte[degree];
        marks = new char[degree][degree];
    }
    
    public ExtendMatrix(Matrix sourceMatrix)
    {
        super(sourceMatrix);
        int degree = sourceMatrix.getDegree();
        markColumns = new byte[degree];
        markRows = new byte[degree];
        marks = new char[degree][degree];
    }
    
    public void delMarkFromAllElements(char mark)
    {
        int n = this.getDegree();
        for (int i = 0; i < n; i++)
            delMarkFromAllElementsRow(i, mark);
    }
    
    private void delMarkFromAllElementsRow(int indexRow, char mark)
    {
        int n = this.getDegree();
        for (int j = 0; j < n; j++)
            if (hasElemMark(indexRow, j, mark))
                setMarkToElem(indexRow, j, (char) 0);
    }
    
    public void delAllMarksFromRowsAndColumns()
    {
        int n = super.getDegree();
        for (int i = 0; i < n; i++)
        {
            markColumns[i] = 0;
            markRows[i] = 0;
        }
    }
    
    public void delMarkFromElem(int indexRow, int indexColumn)
    {
        marks[indexRow][indexColumn] = (char) 0;
    }
    
    public void setMarkToElem(int indexRow, int indexColumn, char mark)
    {
        marks[indexRow][indexColumn] = mark;
    }
    
    public void setMarkToColumn(int indexColumn)
    {
        markColumns[indexColumn] = 1;
    }
    
    public void resetMarkFromColumn(int indexColumn)
    {
        markColumns[indexColumn] = 0;
    }
    
    public void setMarkToRow(int indexRow)
    {
        markRows[indexRow] = 1;
    }
    
    public void resetMarkFromRow(int indexRow)
    {
        markRows[indexRow] = 0;
    }
    
    public boolean hasElemMark(int indexRow, int indexColumn, char mark)
    {
        if (marks[indexRow][indexColumn] == mark)
            return true;
        return false;            
    }
    
    public boolean hasRowMark(int indexRow)
    {
        if (markRows[indexRow] == 1)
            return true;
        return false;
    }
    
    public boolean hasColumnMark(int indexColumn)
    {
        if (markColumns[indexColumn] == 1)
            return true;
        return false;
    }
    
    public void print()
    {
        System.out.printf("\n");
        printPlusOverMarkedColumns();
        printMatrix();
        System.out.printf("\n");
    }
    
    private void printPlusOverMarkedColumns()
    {
        int n = this.getDegree();
        for (int j = 0; j < n; j++)
            if (!hasColumnMark(j))
                System.out.printf("     ");
            else 
                System.out.printf("  +  ");
    }
    
    private void printMatrix()
    {
        int n = this.getDegree();
        for (int i = 0; i < n; i++)
            printRow(i);
    }
    
    private void printRow(int indexRow)
    {
        System.out.printf("\n");
        printAllElementsRowWithMarks(indexRow);
        printPlusNearMarkedRow(indexRow);
    }
    
    private void printAllElementsRowWithMarks(int indexRow)
    {
        int n = this.getDegree();
        for (int j = 0; j < n; j++)
        {
            double elem = this.getVal(indexRow, j);
            printElement(elem);
            printElementMark(indexRow, j);
        }
    }
    
    private void printElement(double val)
    {
        System.out.printf("%4.1f", val);
    }
    
    
    private void printElementMark(int indexRow, int indexColumn)
    {
        if (hasElemMark(indexRow, indexColumn, '*'))
            System.out.printf("*");
        else if (hasElemMark(indexRow, indexColumn, '\''))
            System.out.printf("\'");
        else
            System.out.printf(" ");  
    }
    
    private void printPlusNearMarkedRow(int indexRow)
    {
        if (hasRowMark(indexRow))
            System.out.printf(" + ");
    }
    
    public String printToString()
    {
        StringBuilder buffer = new StringBuilder("\n");
        buffer.append(printPlusOverMarkedColumnsToString());
        buffer.append(printMatrixToString());
        buffer.append("\n");
        return buffer.toString();
    }
    
    private String printPlusOverMarkedColumnsToString()
    {
        StringBuilder buffer = new StringBuilder();
        int n = this.getDegree();
        for (int j = 0; j < n; j++)
            if (!hasColumnMark(j))
                buffer.append("          ");
            else 
                buffer.append("     +    ");
        return buffer.toString(); 
    }
    
    private String printMatrixToString()
    {
        StringBuilder buffer = new StringBuilder();
        int n = this.getDegree();
        for (int i = 0; i < n; i++)
            buffer.append(printRowToString(i));
        return buffer.toString();
    }
    
    private String printRowToString(int indexRow)
    {
        StringBuilder buffer = new StringBuilder();
        buffer.append("\n");
        buffer.append(printAllElementsRowWithMarksToString(indexRow));
        buffer.append(printPlusNearMarkedRowToString(indexRow));
        return buffer.toString();
    }
    
    private String printAllElementsRowWithMarksToString(int indexRow)
    {
        StringBuilder buffer = new StringBuilder();
        int n = this.getDegree();
        for (int j = 0; j < n; j++)
        {
            double elem = this.getVal(indexRow, j);
            buffer.append(printElementToString(elem));
            buffer.append(printElementMarkToString(indexRow, j));
        }
        return buffer.toString();
    }
    
    private String printElementToString(double val)
    {
        Formatter formatter = new Formatter();
        formatter.format("%1$7.1f", val);
        return formatter.toString();
    }
    
    
    private String printElementMarkToString(int indexRow, int indexColumn)
    {
        if (hasElemMark(indexRow, indexColumn, '*'))
            return "*";
        else if (hasElemMark(indexRow, indexColumn, '\''))
            return "\'";
        else
            return " ";  
    }
    
    private String printPlusNearMarkedRowToString(int indexRow)
    {
        if (hasRowMark(indexRow))
            return "     +   ";
        return     "         ";
    }
  
    private byte[] markColumns;
    private byte[] markRows;
    private char[][] marks;
}
