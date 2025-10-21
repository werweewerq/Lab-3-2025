import functions.*;

public class Main {
    public static void main(String[] args) {
        double[] values = {1, 6, 11, 16, 21, 26, 31, 36, 41, 46};
        try {
            System.out.println("Test array:");
            TabulatedFunction f = new ArrayTabulatedFunction(5, 50, values);
            test(f);
        }
        catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException: " + e.getMessage());
        }

        try {
            System.out.println("Test list:");
            TabulatedFunction f = new LinkedListTabulatedFunction(5, 50, values);
            test(f);
        }
        catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException: " + e.getMessage());
        }
    }

    private static void test(TabulatedFunction f) {
        System.out.println("Точки:");
        f.outClass();
        try {
            f.getPoint(6);
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("FunctionPointIndexOutOfBoundsException: " + e.getMessage());
        }
        try {
            f.setPoint(8, new FunctionPoint(46, 1000));
            System.out.println("После изменения точки:");
            f.outClass();
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("FunctionPointIndexOutOfBoundsException: " + e.getMessage());
        } catch (InappropriateFunctionPointException e) {
            System.out.println("InappropriateFunctionPointException: " + e.getMessage());
        }
        try {
            System.out.printf("X = %.2f\n", f.getPointX(5));
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("FunctionPointIndexOutOfBoundsException: " + e.getMessage());
        }
        try {
            f.setPointX(5, 34);
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("FunctionPointIndexOutOfBoundsException: " + e.getMessage());
        } catch (InappropriateFunctionPointException e) {
            System.out.println("InappropriateFunctionPointException: " + e.getMessage());
        }
        try {
            System.out.printf("Y = %.2f\n", f.getPointY(4));
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("FunctionPointIndexOutOfBoundsException: " + e.getMessage());
        }
        try {
            f.setPointY(6, 15);
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("FunctionPointIndexOutOfBoundsException: " + e.getMessage());
        }
        try {
            f.deletePoint(1);
            System.out.println("После удаления точки:");
            f.outClass();
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("FunctionPointIndexOutOfBoundsException: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("IllegalStateException: " + e.getMessage());
        }
        try {
            f.addPoint(new FunctionPoint(1,1));
            System.out.println("После добавления точки:");
            f.outClass();
        } catch (InappropriateFunctionPointException e) {
            System.out.println("IllegalStateException: " + e.getMessage());
        }
    }
}