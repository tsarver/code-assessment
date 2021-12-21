#Rectangles Explained

Congratulations on receiving this fine library for reasoning about rectangles.

##How to include in your Java project

Simply include "sarver-rectangles.jar" in your application's classpath. Methods will vary depending on your deployment strategy. At the command line, use "java --class-path sarver-rectangles.jar -jar myMainApp.jar"

##Using Rectangles

The class "com.fixtufproblems.rectangles.*Rectangles*" is the primary entry point for the library. One would start by creating instances of "com.fixtufproblems.rectangles.*Rectangle*" by calling "*Rectangles*.create(Point vertex1, Point vertex2, Point vertex3, Point vertex4)" or "*Rectangles*.create(Point vertices[])" (referencing "com.fixtufproblems.rectangles.*Point*").

Please see the javadoc for that package for the details of using the library.

In future releases, the library will allow creating *Rectangle*'s with builder syntax and Turtle commands. For example:

 ```java
 import com.fixtufproblems.rectangles.*
 
 ...
 
 	Rectangle rectangle1 = Rectangles.addPoint(4,5).addPoint(4,10).addPoint(8,10).addPoint(8,5).build();
 	Rectangle rectangle2 = Rectangles.goto(4,5).forward(5).left(90).forward(4).left(90).forward(5).build();
 ```
 
 would create equivalent *Rectangle*'s.

##Assumptions

All sides of a rectangle are parallel to either the x- or y-axis. All coordinates are expressed using integers (whole numbers). Remember, there are an infinite number of values between 10.2 and 10.3.

If one Rectangle fully contains another Rectangle, then they do not intersect, because intersection is defined in terms of lines not area.

We created "com.fixtufproblems.rectangles.*Point*" in this package in order to avoid pulling in AWT libraries.