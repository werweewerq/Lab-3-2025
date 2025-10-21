package functions;

public class LinkedListTabulatedFunction implements TabulatedFunction {
    private FunctionNode head;
    private FunctionNode lastNode;
    private int lastindex = 0;
    private int size;

    private static class FunctionNode {
        private FunctionPoint point;
        private FunctionNode next = null;
        private FunctionNode prev = null;

        public FunctionNode() {
            point = new FunctionPoint();
        }

        public FunctionNode(double x, double y) {
            point = new FunctionPoint(x, y);
        }

        public FunctionNode(FunctionNode node) {
            point = new FunctionPoint(node.point);
        }

        public FunctionNode(FunctionPoint point) {
            this.point = new FunctionPoint(point);
        }

        public FunctionPoint getPoint() {
            return point;
        }

        public FunctionNode getNext() {
            return next;
        }

        public FunctionNode getPrev() {
            return prev;
        }

        public void setPoint(FunctionPoint point) {
            this.point = point;
        }

        public void setNext(FunctionNode next) {
            this.next = next;
        }

        public void setPrev(FunctionNode prev) {
            this.prev = prev;
        }
    }

    

 /*   private FunctionNode getNodeByIndex(int index) {
        FunctionNode node;
        if (index <= lastindex / 2 || index > (size + lastindex) / 2) {
            node = head;
            if (index > (size + lastindex) / 2) {
                node = node.prev;
                for (int i = size - 1; i != index; --i)
                    node = node.prev;
            }
            else
                for (int i = 0; i != index; ++i)
                    node = node.next;
        }
        else {
            node = lastNode;
            if (index < lastindex)
                for (int i = lastindex; i != index; --i)
                    node = node.prev;
            else
                for (int i = lastindex; i != index; ++i)
                    node = node.next;
        }
        lastindex = index;
        lastNode = node;
        return node;
    }*/

    private FunctionNode getNodeByIndex(int index) {
        FunctionNode node;
        if (Math.abs(index - lastindex) <= Math.min(index, size - 1 - index)) {
            node = lastNode;
            if (index > lastindex)
                for (int i = lastindex; i < index; i++)
                    node = node.next;
            else
                for (int i = lastindex; i > index; i--)
                    node = node.prev;
        }
        else
            if (index <= size - 1 - index) {
            node = head;
            for (int i = 0; i < index; i++)
                node = node.next;
            }
            else {
            node = head.prev;
            for (int i = size - 1; i > index; i--)
                node = node.prev;
        }
        lastindex = index;
        lastNode = node;
        return node;
    }

    private FunctionNode addNodeToTail(FunctionNode node) {
        FunctionNode tail = head.prev;
        head.prev = new FunctionNode(node);
        tail.next = head.prev;
        tail.next.prev = tail;
        tail.next.next = head;
        ++size;
        return tail.next;
    }

    private FunctionNode addNodeByIndex(int index, FunctionNode node) {
        if (index == size)
            addNodeToTail(node);
        FunctionNode nextnode = getNodeByIndex(index);
        nextnode.prev.next = new FunctionNode(node);
        nextnode.prev.next.next = nextnode;
        nextnode.prev.next.prev = nextnode.prev;
        nextnode.prev = nextnode.prev.next;
        if (index == 0)
            head = head.prev;
        ++size;
        return nextnode.prev;
    }

    private FunctionNode deleteNodeByIndex(int index) {
        FunctionNode node = getNodeByIndex(index);
        node.next.prev = node.prev;
        node.prev.next = node.next;
        if (index == 0)
            head = head.next;
        --size;
        lastindex = 0;
        lastNode = head;
        return node;
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX)
            throw new IllegalArgumentException("Левая граница больше или равна правой границе");
        if (pointsCount < 2)
            throw new IllegalArgumentException("Точек меньше двух");
        double step = (rightX - leftX) / (pointsCount - 1);
        head = new FunctionNode(leftX, 0);
        head.next = head;
        head.prev = head;
        ++size;
        leftX += step;
        for(int i = 1; i != pointsCount; ++i, leftX += step)
            addNodeToTail(new FunctionNode(leftX, 0));
        lastNode = head;
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        if (leftX >= rightX)
            throw new IllegalArgumentException("Левая граница больше или равна правой границе");
        if (values.length < 2)
            throw new IllegalArgumentException("Точек меньше двух");
        int pointsCount = values.length;
        double step = (rightX - leftX) / (pointsCount - 1);
        head = new FunctionNode(leftX, values[0]);
        head.next = head;
        head.prev = head;
        ++size;
        leftX += step;
        for(int i = 1; i != pointsCount; ++i, leftX += step)
            addNodeToTail(new FunctionNode(leftX, values[i]));
        lastNode = head;
    }

    public double getLeftDomainBorder() {
        return head.point.getX();
    }

    public double getRightDomainBorder() {
        return head.prev.point.getX();
    }

    public double getFunctionValue(double x) {
        if (x >= getLeftDomainBorder() && x <= getRightDomainBorder()) {
            FunctionNode node = head;
            for (int i = 0; i < size; ++i, node = node.next) {
                if (x == node.point.getX())
                    return node.point.getY();
                if (x > node.point.getX() && x <= node.next.point.getX()) {
                    double x1 = node.point.getX();
                    double y1 = node.point.getY();
                    double x2 = node.next.point.getX();
                    double y2 = node.next.point.getY();
                    return ((x - x1) * (y2 - y1) / (x2 - x1)) + y1;
                }
            }
        }
        return Double.NaN;
    }

    public int getPointsCount() {
        return size;
    }

    public FunctionPoint getPoint(int index) {
        if (index < 0 || index >= size)
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы набора точек");
        return new FunctionPoint(getNodeByIndex(index).point);
    }

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (index < 0 || index >= size)
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы набора точек");
        if ((index == 0 || index == size - 1)) {
            if (getNodeByIndex(index).point.getX() != point.getX())
                throw new InappropriateFunctionPointException("x выходит за границы определения функции");
            getNodeByIndex(index).point = new FunctionPoint(point);
        }
        else
            if (point.getX() > getNodeByIndex(index).prev.point.getX() && point.getX() < getNodeByIndex(index).next.point.getX()) {
                getNodeByIndex(index).point = new FunctionPoint(point);
            }
            else
                throw new InappropriateFunctionPointException("x выходит за границы определения соседних точек");
    }

    public double getPointX(int index) {
        if (index < 0 || index >= size)
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы набора точек");
        return getNodeByIndex(index).point.getX();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        if (index < 0 || index >= size)
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы набора точек");
        if (index == 0 || index == size - 1 || x <= getNodeByIndex(index).prev.point.getX() || x >= getNodeByIndex(index).next.point.getX()) 
            throw new InappropriateFunctionPointException("x выходит за границы определения соседних точек");
        getNodeByIndex(index).point.setX(x);
    }

    public double getPointY(int index) {
        if (index < 0 || index >= size)
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы набора точек");
        return getNodeByIndex(index).point.getY();
    }

    public void setPointY(int index, double y) {
        if (index < 0 || index >= size)
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы набора точек");
        getNodeByIndex(index).point.setY(y);
    }

    public void deletePoint(int index) {
        if (index < 0 || index >= size)
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы набора точек");
        if (size < 3)
            throw new IllegalStateException("Кол-во элементов меньше трех");
        deleteNodeByIndex(index);
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        int index = 0;
        if (point.getX() > head.prev.point.getX())
            index = size;
        else {
            if (point.getX() == getNodeByIndex(index).point.getX())
                    throw new InappropriateFunctionPointException("Точка c такой координатой  x определена");
            while (point.getX() >= getNodeByIndex(index).point.getX()) {
                if (point.getX() == getNodeByIndex(index).point.getX())
                    throw new InappropriateFunctionPointException("Точка c такой координатой  x определена");
                ++index;
            }
        }
        addNodeByIndex(index, new FunctionNode(point));
    }

    public void outClass() {
        FunctionNode node = head;
        for (int i = 0; i < size; ++i, node = node.next) {
            System.out.printf("( %.2f, %.2f)\n", node.point.getX(), node.point.getY());
        }
        System.out.println();
    }
}