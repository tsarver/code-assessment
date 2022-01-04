#Rectangles Explained

Congratulations on disovering this fine library for reasoning about rectangles.

##How to include in your Java project

Simply include "sarver-rectangles.jar" in your application's classpath. Methods will vary depending on your deployment strategy. At the command line, use "java --class-path sarver-rectangles.jar -jar myMainApp.jar"

##Using Rectangles

The class "com.fixtufproblems.rectangles.*Rectangles*" is the primary entry point for the library. One would start by creating instances of "com.fixtufproblems.rectangles.*Rectangle*" by calling "*Rectangles*.create(Point lowerLeft, Point upperRight)" (referencing "com.fixtufproblems.rectangles.*Point*"). Rectangles consist of 4 line segments in a two-dimensional Euclidian space defined with integers (whole numbers). All sides of a rectangle are parallel to either the x- or y-axis (i.e., strictly horizontal or vertical).

You can find the intersection of two *Rectangle*'s by calling "*Rectangle.intersection(Rectangle)*", which returns a *List* of *Point*'s where they intersect, or null if they don't intersect. Note that overlapping line segments are not considered to intersect.

You can test if one *Rectangle* fully contains another *Rectangle* by calling "*Rectangle.contains(Rectangle)*", which simply returns a boolean. Note: this is strict containment, meaning that no line segments from any of the rectangles overlap or intersect.
If one Rectangle fully contains another Rectangle, then they do not intersect, because intersection is defined in terms of line segments not area.

You test if two *Rectangle*'s are adjacent by calling "*Rectangle.adjacentTo(Rectangle)*". Two distinct (i.e., they don't share any area) rectangles are adjacent iff the closest two parallel line segments (of non-zero length, one from each rectangle) overlap or are at a distance of 1. Two intersecting rectangles cannot be *adjacent*, one rectangle that contains another cannot be *adjacent*, and two rectangles which share a vertex are not *adjacent* (because a point is a zero-length line segment).

Please see the javadoc for the "com.fixtufproblems.rectangles" package for the details of using the library.

In future releases, the library will allow creating *Rectangle*'s with builder syntax and Turtle commands. For example:

 ```java
 import com.fixtufproblems.rectangles.*
 
 ...
 
 	Rectangle rectangle1 = Rectangles.addPoint(4,5).addPoint(4,10).addPoint(8,10).addPoint(8,5).build();
 	Rectangle rectangle2 = Rectangles.goto(4,5).forward(5).left(90).forward(4).left(90).forward(5).build();
 ```
 
 would create equivalent *Rectangle*'s.

##Development Decisions

We created "com.fixtufproblems.rectangles.*Point*" in this package in order to avoid pulling in AWT libraries.