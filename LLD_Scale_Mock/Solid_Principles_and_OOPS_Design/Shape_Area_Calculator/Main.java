// gemini ref : https://share.gemini.google/RD1TAFyquHAX

package Low_Level_Design.LLD_Scale_Mock.Solid_Principles_and_OOPS_Design.Shape_Area_Calculator;

import java.util.ArrayList;
import java.util.List;

// Abstract visitor :  visitor design pattern
interface ShapeVisitor<T> {
    T visit(Circle circle);
    T visit(Triangle triangle);
    T visit(Polygon polygon);
}

// Concret visitor
class AreaCalculator implements ShapeVisitor<Double> {
    public Double visit(Circle circle) {
        return Math.PI * circle.getRadius() * circle.getRadius();
    }
    public Double visit(Triangle triangle) {
        return 0.5 * triangle.getBase() * triangle.getHeight();
    }
    public Double visit(Polygon polygon) {
        return  polygon.getHeight() * polygon.getWidth();
    }
}


// Abstract shapes 
interface Shape {
    <T> T accept(ShapeVisitor<T> shapeVisitor);
}

// Concret - shapes : Circle
class Circle implements Shape {
    private double r;
    Circle(double r) {
        if(r < 0) throw new IllegalArgumentException("Radius cann't be negative..");
        this.r = r;
    } 
    public double getRadius() { return r; }

    public <T> T accept(ShapeVisitor<T> visitor) {
        return visitor.visit(this);
    }
}


// Concret - shapes : Triangle
class Triangle implements Shape {
    private double base, height;
    Triangle(double base, double height) {
        if(base<0 || height<0) throw new IllegalArgumentException("Base or height cann't be negative..");      
        this.base = base;
        this.height = height;
    } 
    public double getBase() { return base; }
    public double getHeight() { return height; }

    public <T> T accept(ShapeVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

// Concret - shapes : Polygon
class Polygon implements Shape {
    private double height, width;
    Polygon(double height, double width) {
        if(height<0 || width<0) throw new IllegalArgumentException("Height or width cann't be negative..");
        this.height = height;
        this.width = width;
    }
    public double getHeight() { return height; }
    public double getWidth() { return width; }

    public <T> T accept(ShapeVisitor<T> visitor) {
        return visitor.visit(this);
    }
}


// Abstract factory to define common abstract method 
interface ShapeCreator {
    public Shape createShape();
}
// Concret factory class to create Circle object
class CircleCreator implements ShapeCreator {
    double radius;
    CircleCreator(double radius) {
        this.radius = radius; 
    }
    public Shape createShape() { return new Circle(radius); }
}
// Concret factory class to create Triangle object
class TriangleCreator implements ShapeCreator {
    double base, height;
    TriangleCreator(double base, double height) {
        this.base = base;
        this.height = height;
    }
    public Shape createShape() { return new Triangle(base, height); }
}
// Concret factory class to create Polygon object
class PolygonCreator implements ShapeCreator {
    double height, width;
    PolygonCreator(double height, double width) {
        this.height = height;
        this.width = width;
    }
    public Shape createShape( ) { return new Polygon(height, width); }
}



public class Main {
    public static void main(String[] args) {
        try {
            List<ShapeCreator> factories = List.of(
                new CircleCreator(4),
                new TriangleCreator(4, 5.8),
                new PolygonCreator(6, 10.4)
            );

            List<Shape> shapes = factories.stream().
                                    map( ShapeCreator::createShape )
                                    .toList();
            
            ShapeVisitor<Double> areaCalculator = new AreaCalculator();
            double totalArea = shapes.stream().
                            mapToDouble( shape -> shape.accept(areaCalculator) )
                            .sum();

            System.out.println(totalArea);
        } catch(IllegalArgumentException e) {
            System.out.println("Validation error : " + e.getMessage());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
