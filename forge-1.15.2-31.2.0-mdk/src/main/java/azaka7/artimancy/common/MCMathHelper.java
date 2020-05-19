package azaka7.artimancy.common;

import com.mojang.datafixers.util.Pair;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public class MCMathHelper {
	
	public static final int getXPFromLevelAndProgress(int level, float frac) {
		return getXPFromLevel(level) + getXPProgress(level, frac);
	}
	
	public static final int getXPFromLevel(int level) {
		if(level > 31) {
			return (int) ((4.5*Math.pow(level, 2)) - (162*level) +2220);
		} else if(level > 16) {
			return (int) ((2.5*Math.pow(level, 2))-(40.5*level)+360);
		}
		return (int) (Math.pow(level, 2) + (6*level));
	}
	
	public static final int getXPRequired(int level) {
		if(level > 30) {
			return (9*level)-158;
		} else if(level > 15) {
			return (5*level)-38;
		}
		return (2*level)+7;
	}
	
	public static final int getXPProgress(int level, float frac) {
		return (int) Math.round(frac * (getXPFromLevel(level+1)-getXPFromLevel(level)));
	}
	
	//The following methods and classes are experimental. They should not be referenced anywhere. ConicBoundingBox.intersects() does NOT work as intended.
	
	// Given three colinear points p, q, r, the function checks if 
	// point q lies on line segment 'pr' 
	static boolean onSegment(Vec2d p, Vec2d q, Vec2d r) 
	{ 
	    if (q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) && 
	        q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y)) 
	    return true; 
	  
	    return false; 
	} 
	  
	// To find orientation of ordered triplet (p, q, r). 
	// The function returns following values 
	// 0 --> p, q and r are colinear 
	// 1 --> Clockwise 
	// 2 --> Counterclockwise 
	static int orientation(Vec2d p, Vec2d q, Vec2d r) 
	{ 
	    // See https://www.geeksforgeeks.org/orientation-3-ordered-points/ 
	    // for details of below formula. 
	    double val = (q.y - p.y) * (r.x - q.x) - 
	            (q.x - p.x) * (r.y - q.y); 
	  
	    if (val == 0) return 0; // colinear 
	  
	    return (val > 0)? 1: 2; // clock or counterclock wise 
	} 
	  
	// The main function that returns true if line segment 'p1q1' 
	// and 'p2q2' intersect. 
	static boolean doIntersect(Vec2d p1, Vec2d q1, Vec2d p2, Vec2d q2) 
	{ 
	    // Find the four orientations needed for general and 
	    // special cases 
	    int o1 = orientation(p1, q1, p2); 
	    int o2 = orientation(p1, q1, q2); 
	    int o3 = orientation(p2, q2, p1); 
	    int o4 = orientation(p2, q2, q1); 
	  
	    // General case 
	    if (o1 != o2 && o3 != o4) 
	        return true; 
	  
	    // Special Cases 
	    // p1, q1 and p2 are colinear and p2 lies on segment p1q1 
	    if (o1 == 0 && onSegment(p1, p2, q1)) return true; 
	  
	    // p1, q1 and q2 are colinear and q2 lies on segment p1q1 
	    if (o2 == 0 && onSegment(p1, q2, q1)) return true; 
	  
	    // p2, q2 and p1 are colinear and p1 lies on segment p2q2 
	    if (o3 == 0 && onSegment(p2, p1, q2)) return true; 
	  
	    // p2, q2 and q1 are colinear and q1 lies on segment p2q2 
	    if (o4 == 0 && onSegment(p2, q1, q2)) return true; 
	  
	    return false; // Doesn't fall in any of the above cases 
	}
	
	public static class Vec2d{
		private final double x, y;
		public Vec2d(double i, double j) {
			this.x = i; this.y = j;
		}
		
		public double getX() {return x;}
		
		public double getY() {return y;}
		
		public double getAxisAlignedAngle() {
			return Math.atan(y/x);
		}
		
		public Vec2d moveX(double x) {
			return new Vec2d(this.x + x, this.y);
		}
		
		public Vec2d moveY(double y) {
			return new Vec2d(this.x, this.y + y);
		}
		
		public static double getLength(Pair<Vec2d,Vec2d> line) {
			double dx = line.getSecond().getX() - line.getFirst().getX();
			double dy = line.getSecond().getY() - line.getFirst().getY();
			return Math.sqrt(dx*dx + dy*dy);
		}
		
		public static Vec2d getVecBetween(Pair<Vec2d,Vec2d> line) {
			return new Vec2d(line.getSecond().getX() - line.getFirst().getX(), line.getSecond().getY() - line.getFirst().getY());
		}
		
		@Override
		public boolean equals(Object o) {
			return o instanceof Vec2d ? (((Vec2d)o).getX() == this.getX() && ((Vec2d)o).getY() == this.getY()) : false;
		}
		
	}
	
	@Deprecated
	public static class ConicBoundingBox{
		private final Vec3d start, end;
		private final double radius;
		
		public ConicBoundingBox(Vec3d vec1, Vec3d vec2, double rad) {
			start = vec1;
			end = vec2;
			radius = rad;
		}

		public Vec3d getStartVec() {
			return start;
		}

		public Vec3d getEndVec() {
			return end;
		}

		public double getRadius() {
			return radius;
		}
		
		public Vec3d growthVector() {
			return end.subtract(start);
		}
		
		public double angle() {
			return Math.atan(radius/growthVector().length());
		}
		
		public double radiusAtSubLength(double sublength) {
			if(sublength >= 0 && sublength <= growthVector().length()) {
				return sublength * radius / growthVector().length();
			}
			return Double.NaN;
		}
		
		public Pair<Vec2d,Vec2d> getXSpace() {
			return Pair.of(new Vec2d(start.getX(), start.getY()), new Vec2d(end.getX(), end.getY()));
		}
		
		public Pair<Vec2d,Vec2d> getYSpace() {
			return Pair.of(new Vec2d(start.getX(), start.getZ()), new Vec2d(end.getX(), end.getZ()));
		}
		
		public Pair<Vec2d,Vec2d> getZSpace() {
			return Pair.of(new Vec2d(start.getZ(), start.getY()), new Vec2d(end.getZ(), end.getY()));
		}
		
		public boolean intersects(AxisAlignedBB aabb) {
			Vec3d gvec = growthVector(); //point "B", where Point "A" is (0,0,0) from start. B is at (|A->B|,0,0) from start
			//double length = gvec.length();
			Vec3d unit_a = gvec.normalize();
			Vec3d unit_b = unit_a.crossProduct(new Vec3d(0,1,0));
			Vec3d unit_c = unit_a.crossProduct(unit_b);
			Vec3d pointC = gvec.add(unit_c.mul(radius, radius, radius));
			Vec3d pointD = gvec.add(unit_b.mul(radius, radius, radius));
			//Vec3d vecBC = pointC.subtract(gvec);
			//Vec3d vecBD = pointD.subtract(gvec);
			Matrix3d3 abc_to_xyz = Matrix3d3.make(unit_a.x, unit_a.y, unit_a.z, unit_b.x, unit_b.y, unit_b.z, unit_c.x, unit_c.y, unit_c.z);
			Matrix3d3 xyz_to_abc = abc_to_xyz.getInverse();
			double aFromX = xyz_to_abc.get(0, 0), aFromY = xyz_to_abc.get(0, 1), aFromZ = xyz_to_abc.get(0, 2);
			double bFromX = xyz_to_abc.get(1, 0), bFromY = xyz_to_abc.get(1, 1), bFromZ = xyz_to_abc.get(1, 2);
			double cFromX = xyz_to_abc.get(2, 0), cFromY = xyz_to_abc.get(2, 1), cFromZ = xyz_to_abc.get(2, 2);
			aabb = aabb.offset(-start.x, -start.y, -start.z);
			AxisAlignedBB aabb_abc = new AxisAlignedBB(	aFromX*aabb.minX + aFromY* aabb.minY + aFromZ*aabb.minZ,
														bFromX*aabb.minX + bFromY* aabb.minY + bFromZ*aabb.minZ,
			/*Redefine the aabb in the cone's coords*/	cFromX*aabb.minX + cFromY* aabb.minY + cFromZ*aabb.minZ,
			/*MinX -> MinA, MinY -> MinB, MinZ->MinC*/	aFromX*aabb.maxX + aFromY* aabb.maxY + aFromZ*aabb.maxZ,
														bFromX*aabb.maxX + bFromY* aabb.maxY + bFromZ*aabb.maxZ,
														cFromX*aabb.maxX + cFromY* aabb.maxY + cFromZ*aabb.maxZ);
			
			Vec3d aabb_NearestCenter = new Vec3d(aabb_abc.minX, (aabb_abc.minY+aabb_abc.maxY)/2, (aabb_abc.minZ + aabb_abc.maxZ)/2);
			System.out.println("nearestCenter: "+aabb_NearestCenter);
			System.out.println("radius: "+radius);
			System.out.println("unit A: "+unit_a);
			System.out.println("unit B: "+unit_b);
			System.out.println("unit C: "+unit_c);
			System.out.println("Point A: (0,0,0)");
			System.out.println("Point B: "+gvec);
			System.out.println("Point C: "+pointC);
			System.out.println("Point D: "+pointD);
			double refRad = radiusAtSubLength(Math.abs(aabb_abc.minX)) + 0.25;
			System.out.println("distance:" + aabb_NearestCenter.distanceTo(new Vec3d(aabb_NearestCenter.x, 0, 0)));
			System.out.println("test radius:" + (refRad + aabb_abc.getAverageEdgeLength()/2.0D));
			if(Double.isNaN(refRad)) {return false;}
			return aabb_NearestCenter.distanceTo(new Vec3d(aabb_abc.minX, 0, 0)) <= refRad + aabb_abc.getAverageEdgeLength()/2.0D;
		}
	}
	
	public static class Matrix2d2 {
		/* Matrix
		 * [a,b]
		 * [c,d]
		 */
		private final double[][] matrix = new double[2][2];
		
		public Matrix2d2(double a, double b, double c, double d) {
			matrix[0][0]=a;
			matrix[0][1]=b;
			matrix[1][0]=c;
			matrix[1][1]=d;
		}
		
		public static Matrix2d2 make(double a, double b, double c, double d) {
			return new Matrix2d2(a,b,c,d);
		}
		
		public double getDeterminant() {
			return matrix[0][0]*matrix[1][1] - matrix[1][0]*matrix[0][1];
		}
	}

	public static class Matrix3d3 {
		/* Matrix
		 * [a,b,c]
		 * [d,e,f]
		 * [g,h,i]
		 */
		private final double[][] matrix = new double[3][3];
		
		public Matrix3d3(double a, double b, double c, double d, double e, double f, double g, double h, double i) {
			matrix[0][0]=a;
			matrix[0][1]=b;
			matrix[0][2]=c;
			matrix[1][0]=d;
			matrix[1][1]=e;
			matrix[1][2]=f;
			matrix[2][0]=g;
			matrix[2][1]=h;
			matrix[2][2]=i;
		}
		
		public double[][] getMatrix(){
			return new double[][] {{matrix[0][0],matrix[0][1],matrix[0][2]},{matrix[1][0],matrix[1][1],matrix[1][2]},{matrix[2][0],matrix[2][1],matrix[2][2]}};
		}
		
		public double get(int i, int j) {
			if(i <0 || i > 2 || j < 0 || j > 2) return Double.NaN;
			return matrix[i][j];
		}
		
		public static Matrix3d3 make(double a, double b, double c, double d, double e, double f, double g, double h, double i) {
			return new Matrix3d3(a,b,c,d,e,f,g,h,i);
		}
		
		public double getDeterminant() {
			return    matrix[0][0] * Matrix2d2.make(matrix[1][1], matrix[1][2], matrix[2][1], matrix[2][2]).getDeterminant()
					+ matrix[0][1] * Matrix2d2.make(matrix[1][0], matrix[1][2], matrix[2][0], matrix[2][2]).getDeterminant()
					+ matrix[0][2] * Matrix2d2.make(matrix[1][0], matrix[1][1], matrix[2][0], matrix[2][1]).getDeterminant();
		}
		
		public Matrix3d3 getInverse() {
			double det = this.getDeterminant();
			double a = (1.0/det) * Matrix2d2.make(matrix[1][1], matrix[1][2], matrix[2][1], matrix[2][2]).getDeterminant();
			double b = -(1.0/det) * Matrix2d2.make(matrix[1][0], matrix[1][2], matrix[2][0], matrix[2][2]).getDeterminant();
			double c = (1.0/det) * Matrix2d2.make(matrix[1][0], matrix[1][1], matrix[2][0], matrix[2][1]).getDeterminant();
			
			double d = -(1.0/det) * Matrix2d2.make(matrix[0][1], matrix[0][2], matrix[2][1], matrix[2][2]).getDeterminant();
			double e = (1.0/det) * Matrix2d2.make(matrix[0][0], matrix[2][2], matrix[2][0], matrix[2][2]).getDeterminant();
			double f = -(1.0/det) * Matrix2d2.make(matrix[0][0], matrix[0][1], matrix[2][0], matrix[2][1]).getDeterminant();
			
			double g = (1.0/det) * Matrix2d2.make(matrix[0][1], matrix[0][2], matrix[1][1], matrix[1][2]).getDeterminant();
			double h = -(1.0/det) * Matrix2d2.make(matrix[0][0], matrix[0][2], matrix[2][0], matrix[2][2]).getDeterminant();
			double i = (1.0/det) * Matrix2d2.make(matrix[0][0], matrix[0][1], matrix[1][0], matrix[1][1]).getDeterminant();
			return Matrix3d3.make(	a, d, g, 
									b, e, h, 
									c, f, i	);
		}
	}
	
}
