package functions;

public class ArrayTabulatedFunction implements TabulatedFunction {
    private FunctionPoint[] points;
    private int PointsCount;

    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX)
            throw new IllegalArgumentException("Левая граница больше или равна правой границе");
        if (pointsCount < 2)
            throw new IllegalArgumentException("Точек меньше двух");
        points = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; ++i, leftX += step)
            points[i] = new FunctionPoint(leftX, 0);
        PointsCount = pointsCount;
    }

    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) {
        if (leftX >= rightX)
            throw new IllegalArgumentException("Левая граница больше или равна правой границе");
        if (values.length < 2)
            throw new IllegalArgumentException("Точек меньше двух");
        int pointsCount =  values.length;
        points = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; ++i, leftX += step)
            points[i] = new FunctionPoint(leftX, values[i]);
        PointsCount = pointsCount;
    }

    public double getLeftDomainBorder() {
        return points[0].getX();
    }

    public double getRightDomainBorder() {
        return points[PointsCount - 1].getX();
    }

    public double getFunctionValue(double x) {
        if (x >= getLeftDomainBorder() && x <= getRightDomainBorder()) {
            for (int i = 0; i < PointsCount; ++i) {
                if (x == points[i].getX())
                    return points[i].getY();
                if (x > points[i].getX() && x <= points[i + 1].getX()) {
                    double x1 = points[i].getX();
                    double y1 = points[i].getY();
                    double x2 = points[i + 1].getX();
                    double y2 = points[i + 1].getY();
                    return ((x - x1) * (y2 - y1) / (x2 - x1)) + y1;
                }
            }
        }
        return Double.NaN;
    }

    public int getPointsCount() {
        return PointsCount;
    }

    public FunctionPoint getPoint(int index) {
        if (index < 0 || index >= PointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы набора точек");
        return new FunctionPoint(points[index]);
    }

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (index < 0 || index >= PointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы набора точек");
        if ((index == 0 || index == PointsCount - 1)) {
            if (points[index].getX() != point.getX())
                throw new InappropriateFunctionPointException("x выходит за границы определения функции");
            points[index] = new FunctionPoint(point);
        }
        else
            if (point.getX() > points[index - 1].getX() && point.getX() < points[index + 1].getX())
                points[index] = new FunctionPoint(point);
            else
                throw new InappropriateFunctionPointException("x выходит за границы определения соседних точек");
    }

    public double getPointX(int index) {
        if (index < 0 || index >= PointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы набора точек");
        return points[index].getX();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        if (index < 0 || index >= PointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы набора точек");
        if (index == 0 || index == PointsCount - 1 || x <= points[index - 1].getX() || x >= points[index + 1].getX()) 
            throw new InappropriateFunctionPointException("x выходит за границы определения соседних точек");
        points[index].setX(x);
    }

    public double getPointY(int index) {
        if (index < 0 || index >= PointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы набора точек");
        return points[index].getY();
    }

    public void setPointY(int index, double y) {
        if (index < 0 || index >= PointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы набора точек");
        points[index].setY(y);
    }

    public void deletePoint(int index) {
        if (index < 0 || index >= PointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы набора точек");
        if (PointsCount < 3)
            throw new IllegalStateException("Кол-во элементов меньше трех");
        System.arraycopy(points, index + 1, points, index, PointsCount - index - 1);
        --PointsCount;
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        int index = 0;
        if (point.getX() > points[PointsCount - 1].getX())
            index = PointsCount;
        else {
            if (point.getX() == points[index].getX())
                    throw new InappropriateFunctionPointException("Точка c такой координатой  x определена");
            while (point.getX() >= points[index].getX()) {
                if (point.getX() == points[index].getX())
                    throw new InappropriateFunctionPointException("Точка c такой координатой  x определена");
                ++index;
            }
        }
        if (PointsCount + 1 > points.length) {
            FunctionPoint[] newPoints = new FunctionPoint[PointsCount * 2 + 1];
            System.arraycopy(points, 0, newPoints, 0, index);
            newPoints[index] = new FunctionPoint(point);
            System.arraycopy(points, index, newPoints, index + 1, PointsCount - index);
            points = newPoints;
        }
        else {
            System.arraycopy(points, index, points, index + 1, PointsCount - index);
            points[index] = new FunctionPoint(point);
        }
        ++PointsCount;
    }

    public void outClass() {
         for (int i = 0; i < PointsCount; ++i) {
            System.out.printf("( %.2f, %.2f)\n", points[i].getX(), points[i].getY());
        }
    }
}