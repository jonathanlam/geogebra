package geogebra3D.kernel3D;

import geogebra.common.kernel.Construction;
import geogebra.common.kernel.algos.AlgoElement.OutputHandler;
import geogebra.common.kernel.algos.AlgoElement.elementFactory;
import geogebra.common.kernel.geos.GeoElement;
import geogebra.common.kernel.geos.GeoPolygon;
import geogebra.common.kernel.kernelND.GeoSegmentND;

/**
 * @author ggb3D
 * 
 * Creates a new GeoPolyhedron
 *
 */
public abstract class AlgoPolyhedron extends AlgoElement3D{

	
	
	protected OutputHandler<GeoPolygon3D> outputPolygons;
	
	/** points generated as output  */
	protected OutputHandler<GeoPoint3D> outputPoints;

	protected OutputHandler<GeoPolyhedron> outputPolyhedron;
	
	protected GeoPolyhedron polyhedron;
	
	
	/////////////////////////////////////////////
	// POLYHEDRON OF DETERMINED TYPE
	////////////////////////////////////////////
	
	protected AlgoPolyhedron(Construction c){
		super(c);


		outputPolyhedron=new OutputHandler<GeoPolyhedron>(new elementFactory<GeoPolyhedron>() {
			public GeoPolyhedron newElement() {
				GeoPolyhedron p=new GeoPolyhedron(cons);
				p.setParentAlgorithm(AlgoPolyhedron.this);
				return p;
			}
		});
		
		outputPolyhedron.adjustOutputSize(1);
		polyhedron = getPolyhedron();
		
		
		
		outputPoints=new OutputHandler<GeoPoint3D>(new elementFactory<GeoPoint3D>() {
			public GeoPoint3D newElement() {
				GeoPoint3D p=new GeoPoint3D(cons);
				p.setCoords(0, 0, 0, 1);
				p.setParentAlgorithm(AlgoPolyhedron.this);
				return p;
			}
		});
		

		outputPolygons=new OutputHandler<GeoPolygon3D>(new elementFactory<GeoPolygon3D>() {
			public GeoPolygon3D newElement() {
				GeoPolygon3D p=new GeoPolygon3D(cons);
				return p;
			}
		});
		
		createOutputSegments();
			
		
		
		


		
		
	}
	
	/**
	 * create the output segments handlers
	 */
	abstract protected void createOutputSegments();
		
	/**
	 * 
	 * @return an output handler for segments
	 */
	protected OutputHandler<GeoSegment3D> createOutputSegmentsHandler(){
		return new OutputHandler<GeoSegment3D>(new elementFactory<GeoSegment3D>() {
			public GeoSegment3D newElement() {
				GeoSegment3D s=new GeoSegment3D(cons);
				return s;
			}
		});
	}
	

	protected void addAlgoToInput(){
		for (int i = 0; i < input.length; i++) {
			input[i].addAlgorithm(this);
		}
	}
	
	
	abstract protected void updateOutput();

	

	
	protected void setOutput(){
		
		updateOutput();
        ((Construction) cons).addToAlgorithmList(this);  
		
	}
	
	/**
	 * @return the polyhedron
	 */
	public GeoPolyhedron getPolyhedron(){
		return outputPolyhedron.getElement(0);
	}
	

	
	
	
    public void update() {
    	
        // compute output from input
        compute();
        
        //polyhedron
        polyhedron.update();
        

    }
    
  
    
	
	
	
	protected void getOutputXML(StringBuilder sb){
		super.getOutputXML(sb);
		
		//append XML for polygon and segments linked once more, to avoid override of specific properties		
		for (GeoPolygon polygon : polyhedron.getPolygonsLinked())
			polygon.getXML(sb);
		for (GeoSegmentND segment : polyhedron.getSegmentsLinked())
			((GeoElement) segment).getXML(sb);
		
	}
	
}
