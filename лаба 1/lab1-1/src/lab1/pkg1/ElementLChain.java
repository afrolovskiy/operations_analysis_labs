package lab1.pkg1;

public class ElementLChain 
{
    public ElementLChain() {};
    
    public ElementLChain(int i, int j, char mark, double val)
    {
        aI = i;
        aJ = j;
        aMark = mark;
        aVal = val;
    }
    
    public void setI(int i)
    {
        aI = i;
    }
    
    public int getI()
    {
        return aI;
    }
    
    public void setJ(int j)
    {
        aJ = j;
    }
    
    public int getJ()
    {
        return aJ;
    }
    
    public void setMark(char mark)
    {
        aMark = mark;
    }
    
    public char getMark()
    {
        return aMark;
    }
    
    public void setVal(double val)
    {
        aVal = val;
    }
    
    public double getVal()
    {
        return aVal;
    }
    
    private int aI = 0;
    private int aJ = 0;
    private char aMark = 0;
    private double aVal = 0;
}
