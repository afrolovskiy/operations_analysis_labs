package lab1.pkg1;

import java.util.StringTokenizer;
import javax.swing.JOptionPane;

public class MatrixObtainer 
{
    private ExtendMatrix matrix = null;
    private int degreeOfMatrix;
    private int curRowIndex;
    private int curColumnIndex;
    
    public ExtendMatrix getMatrix(String inputStr) throws Exception
    {
        if (hasFirstStringElements(inputStr))
            return parseStringForGetMatrix(inputStr);
        else
            throw new Exception("Ошибка: некорректно задана матрица!");
    }
    
    private boolean hasFirstStringElements(String str)
    {
        return (getCountWordsInFirstSubString(str) > 0);  
        
    }
    
    private int getCountWordsInFirstSubString(String str)
    {
        StringTokenizer tokenizer = new StringTokenizer(str, "\n");
        int count = 0;
        if (tokenizer.hasMoreTokens())
        {
            String firstSubStr = tokenizer.nextToken();
            count = getCountWordsInString(firstSubStr);
        }
        return count;
    }
    
    private int getCountWordsInString(String str)
    {
        StringTokenizer tokenizer = new StringTokenizer(str, " \t");
        int count = 0;
        while (tokenizer.hasMoreTokens())
        {
            String curSubStr = tokenizer.nextToken();
            count ++;
        }
        return count;
    }
    
    private ExtendMatrix parseStringForGetMatrix(String inputStr)
    {
        try{
            initializationMatrix(inputStr);
            StringTokenizer tokenizer = new StringTokenizer(inputStr, "\n");
            while (tokenizer.hasMoreTokens())
                parseSubStringForGetElementsInMatrix(tokenizer);
            return matrix;
        }
        catch (Exception err)
        {
            printExceptionInfo(err);
            return null;
        }
    }
    
    private void initializationMatrix(String inputStr)
    {
        degreeOfMatrix = getCountWordsInFirstSubString(inputStr);
        matrix = new ExtendMatrix(degreeOfMatrix);
        curColumnIndex = 0;
        curRowIndex = 0;
    }
    
    private void parseSubStringForGetElementsInMatrix(StringTokenizer tokenizer) throws Exception
    {
        String subStr = tokenizer.nextToken();
        if (hasStringCorrectNumberWords(subStr))
            addToMatrixElementsFromString(subStr);
        else
            throw new Exception("Ошибка: некорректно задана матрица!");
    }
    
    private boolean hasStringCorrectNumberWords(String subStr)
    {
        return getCountWordsInString(subStr) == degreeOfMatrix;
    }
    
    private void addToMatrixElementsFromString(String str) throws Exception
    {
        StringTokenizer tokenizer = new StringTokenizer(str, " \t");
        while (tokenizer.hasMoreTokens())
            addToMatrixElement(tokenizer);
        moveCurPositionToNextRow();
    }
    
    private void addToMatrixElement(StringTokenizer tokenizer) throws Exception
    {
        String str = tokenizer.nextToken();
        setValueMatrix(str);
        moveCurPositionToNextColumn();
    }
    
    private void setValueMatrix(String str) throws Exception
    {
        if (curRowIndex >= degreeOfMatrix)
            throw new Exception("Ошибка: некорректно задана матрица!");
        double value;
        
        if (str.length() == 1 && (str.charAt(0) == 8734 || str.charAt(0) == 'i'))
        {
            value = Double.POSITIVE_INFINITY;
            matrix.setMarkToElem(curRowIndex, curColumnIndex, (char)8734);
        }
        else
            value = new Double(str);
        matrix.setVal(curRowIndex, curColumnIndex, value);
    }
    
    private void moveCurPositionToNextRow() throws Exception
    {
        if (curRowIndex >= degreeOfMatrix)
            throw new Exception("Ошибка: некорректно задана матрица!");
        curRowIndex++;
        curColumnIndex = 0;
    }
    
    private void moveCurPositionToNextColumn()
    {
        curColumnIndex++;
    }
    
    private void printExceptionInfo(Exception err)
    {
        JOptionPane.showMessageDialog(null, err.getMessage());
    }
}
