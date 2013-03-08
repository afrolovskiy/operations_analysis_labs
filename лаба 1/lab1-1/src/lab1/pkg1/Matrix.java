package lab1.pkg1;

import java.util.Formatter;




public class Matrix 
{
    public Matrix(int degree)
    {
        values = new double[degree][degree];
    }
    
    public Matrix(double[][] sourceMatrix)
    {
        int n = sourceMatrix.length;
        values = new double[n][n];
        for (int i = 0; i < n; i++)
            System.arraycopy(sourceMatrix[i], 0, values[i], 0, n);
    }
    
    public Matrix(Matrix sourceMatrix)
    {
        int n = sourceMatrix.getDegree();
        values = new double[n][n];
        for (int i = 0; i < n; i++)
            System.arraycopy(sourceMatrix.values[i], 0, values[i], 0, n);
    }
    
    public final void setVal(int i, int j, double val)
    {
        values[i][j] = val;
    }
    
    public final double getVal(int i , int j)
    {
        return values[i][j];
    }
    
    public final int getDegree()
    {
        return values.length;
    }
    
    public void print()
    {
        int n = values.length;
        for (int i = 0; i < n; i++)
            printRow(i);
        System.out.printf("\n");
    }
    
    public String printToString()
    {
        StringBuffer buffer = new StringBuffer();
        int n = values.length;
        for (int i = 0; i < n; i++)
            buffer.append(printRowToString(i));
        buffer.append("\n");
        return buffer.toString();
    }
    
    private void printRow(int indexRow)
    {
        int n = values.length;
        System.out.printf("\n");
        for (int j = 0; j < n; j++)
            System.out.printf("%6.1f ", values[indexRow][j]);
    }
    
    private String printRowToString(int indexRow)
    {
        Formatter formatter = new Formatter();
        int n = values.length;
        String outStr = "\n";
        for (int j = 0; j < n; j++)
            formatter.format("%1$8.1f", values[indexRow][j]);
        outStr += formatter.toString();
        return outStr;
    }
    
    private double[][] values;
}
