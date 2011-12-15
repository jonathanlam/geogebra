package geogebra3D.kernel3D;

import java.util.Collection;

import geogebra.common.kernel.Construction;
import geogebra.common.kernel.Matrix.Coords;
import geogebra.common.kernel.geos.GeoPolygon;
import geogebra.common.kernel.kernelND.GeoPointND;
import geogebra.common.kernel.kernelND.GeoSegmentND;

/**
 * @author ggb3D
 * 
 * Creates a new Pyramid
 *
 */
public class AlgoPolyhedronPointsPyramid extends AlgoPolyhedronPoints{

	
	/** creates a pyramid regarding vertices and faces description
	 * @param c construction 
	 * @param labels 
	 * @param points vertices
	 */
	public AlgoPolyhedronPointsPyramid(Construction c, String[] labels, GeoPointND[] points) {
		super(c,labels,points);

	}
	
	protected void createPolyhedron(){

		GeoPointND[] bottomPoints = getBottomPoints();
		GeoPointND topPoint = getTopPoint();
		
		bottomPointsLength = bottomPoints.length;
		

		///////////
		//vertices
		///////////

		points = new GeoPointND[bottomPointsLength+1];
		for(int i=0;i<bottomPointsLength;i++)
			points[i] = bottomPoints[i];
		points[bottomPointsLength] = topPoint;

		///////////
		//faces
		///////////

		//base of the pyramid
		setBottom(polyhedron);

		//sides of the pyramid
		for (int i=0; i<bottomPointsLength; i++){
			polyhedron.startNewFace();
			polyhedron.addPointToCurrentFace(bottomPoints[i]);
			polyhedron.addPointToCurrentFace(bottomPoints[(i+1)%(bottomPointsLength)]);
			polyhedron.addPointToCurrentFace(topPoint);//apex
			polyhedron.endCurrentFace();
		}


		polyhedron.setType(GeoPolyhedron.TYPE_PYRAMID);
		
	}
	
	protected void addBottomPoints(int length){
		
	}
	
	protected void removeBottomPoints(int length){
		
	}
	
	
	/////////////////////////////////////////////
	// END OF THE CONSTRUCTION
	////////////////////////////////////////////

	
	
	
	public void compute() {


		GeoPointND[] bottomPoints = getBottomPoints();

		//TODO remove this and replace with tesselation
		Coords interiorPoint = new Coords(4);
		for (int i=0;i<bottomPoints.length;i++){
			interiorPoint = interiorPoint.add(bottomPoints[i].getCoordsInD(3));
		}
		interiorPoint = interiorPoint.add(getTopPoint().getCoordsInD(3));
		
		interiorPoint = (Coords) interiorPoint.mul((double) 1/(bottomPoints.length+1));
		polyhedron.setInteriorPoint(interiorPoint);
		//Application.debug("interior\n"+interiorPoint);

	}
	
	

    public String getClassName() {

    	return "AlgoPyramid";

    }
    
    

	protected void updateOutput(){
		if (isOldFileVersion()){
			//add polyhedron's segments and polygons, without setting this algo as algoparent		
			int index = 0;
			if (!bottomAsInput){ //check bottom
				outputPolygons.addOutput(polyhedron.getFace(index), false);
				index++;
				for (int i=0; i<bottomPointsLength; i++)
					outputSegmentsBottom.addOutput((GeoSegment3D) polyhedron.getSegment(points[i], points[(i+1) % bottomPointsLength]),false);
			}
			
			//sides
			for (int i=0; i<bottomPointsLength; i++){
				outputPolygons.addOutput(polyhedron.getFace(index), false);
				index++;
				outputSegmentsSide.addOutput((GeoSegment3D) polyhedron.getSegment(points[i], points[bottomPointsLength]),false);
			}

		}else{
			Collection<GeoPolygon3D> faces = polyhedron.getFacesCollection();
			int step = 1;
			for (GeoPolygon polygon : faces){
				outputPolygons.addOutput((GeoPolygon3D) polygon, false);
				GeoSegmentND[] segments = polygon.getSegments();
				if(step==1 && !bottomAsInput){//bottom
					for (int i=0; i<segments.length; i++)
						outputSegmentsBottom.addOutput((GeoSegment3D) segments[i],false);	
					step++;
				}else{//sides
					outputSegmentsSide.addOutput((GeoSegment3D) polygon.getSegments()[2],false);		
					step++;
				}
			}
		}
		

		
		refreshOutput();
		
	}

	
}
