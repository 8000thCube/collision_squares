package collision_squares;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import static java.util.Arrays.copyOf;
public class CollisionDetector{
	private static class ShapeWrapperArrayWrapper{
		private int start,stop;
		private void append(ShapeWrapper s){
			int i=stop++;
			ShapeWrapper[]a=array;
			if(a.length==i)a=array=copyOf(a,i*2);
			a[i]=s;
		}private void ensureCapacity(int c){if(array.length<c)array=copyOf(array,c*2);}
		private ShapeWrapper[]array;
		private ShapeWrapperArrayWrapper(){array=new ShapeWrapper[10];}
		private ShapeWrapperArrayWrapper(ShapeWrapper s,ShapeWrapper z){
			ShapeWrapper[]a=array=new ShapeWrapper[10];
			a[0]=s;
			a[1]=z;
			stop=2;
		}
	}private static void collide(CollisionConsumer collisionconsumer,ShapeWrapperArrayWrapper shapes,double centerx,double centery,double minscale,double stride,int side){
		double halfstride=stride*0.5,newstride=stride/side,x0=centerx-(side-1)*halfstride,y0=centery-(side-1)*halfstride;
		int guaranteedcollisionstop,shapestart=shapes.start,shapestop=shapes.stop;
		ShapeWrapper[]shapearray;
		guaranteedcollisionstop=shapestop*2-shapestart;
		shapes.ensureCapacity(guaranteedcollisionstop);
		shapearray=shapes.array;
		for(int n=0;n!=side;n++)for(int k=0;k!=side;k++){
			int guaranteedcollisionstart=guaranteedcollisionstop,newshapestop=shapestop;
			double cornerx,cornery,x=n*stride+x0,y=k*stride+y0;
			cornerx=x-halfstride;
			cornery=y-halfstride;
			for(int i=shapestart;i!=shapestop;i++){
				ShapeWrapper s=shapearray[i];
				if(newstride<s.CollisionScale/*||si.S.contains(cornerx,cornery,stride,stride)*/)shapearray[--guaranteedcollisionstart]=s;	
				else if(s.S.intersects(cornerx,cornery,stride,stride))shapearray[newshapestop++]=s;
			}for(int i=guaranteedcollisionstart;i!=guaranteedcollisionstop;i++){
				ShapeWrapper s=shapearray[i];
				for(int j=i+1;j!=guaranteedcollisionstop;j++)collisionconsumer.accept(x,y,stride,s,shapearray[j]);
				for(int j=shapestop;j!=newshapestop;j++)collisionconsumer.accept(x,y,stride,s,shapearray[j]);
			}if(newshapestop-shapestop<2)continue;
			if(minscale<=newstride){
				shapes.array=shapearray;
				shapes.start=shapestop;
				shapes.stop=newshapestop;
				collide(collisionconsumer,shapes,x,y,minscale,newstride,side);
			}else for(int i=shapestop;i!=newshapestop;i++){
				ShapeWrapper s=shapearray[i];
				for(int j=i+1;j!=newshapestop;j++){
					//if(s==shapearray[j])throw new RuntimeException();
					collisionconsumer.accept(x,y,stride,s,shapearray[j]);
				}
			}
		}
	}
	/*static void collide(CollisionConsumer collisionconsumer,ShapeWrapperArrayWrapper extra,ShapeWrapperArrayWrapper shapes,double centerx,double centery,double minscale,double stride,int side){
		double halfstride=stride*0.5,newstride=stride/side,x0=centerx-(side-1)*halfstride,y0=centery-(side-1)*halfstride;
		int shapestart=shapes.start,shapestop=shapes.stop;
		ShapeWrapper[]saq=shapes.array,saw;
		shapes.start=shapestop;
		for(int n=0;n!=side;n++)for(int k=0;k!=side;k++){
			int q;
			double cornerx,cornery,x=n*stride+x0,y=k*stride+y0;
			cornerx=x-halfstride;
			cornery=y-halfstride;
			extra.stop=0;
			for(int i=shapestart;i!=shapestop;i++){
				ShapeWrapper si=saq[i];
				if(newstride<si.CollisionScale/*||si.S.contains(cornerx,cornery,stride,stride)*//*)extra.append(si);	
				else if(si.S.intersects(cornerx,cornery,stride,stride))shapes.append(si);
			}saq=shapes.array;
			saw=extra.array;
			q=shapes.stop;
			for(int i=shapestop,l=extra.stop;i!=q;i++){
				ShapeWrapper si=saq[i];
				for(int z=0;l!=z;z++)collisionconsumer.accept(x,y,stride,si,saw[z]);
			}if(q-shapestop<2){
				shapes.stop=shapestop;
				continue;
			}if(minscale<=newstride){collide(collisionconsumer,extra,shapes,x,y,minscale,newstride,side);}
			else for(int i=shapestop;i!=q;i++){
				ShapeWrapper s=saq[i];
				for(int j=i+1;j!=q;j++){
					if(s==saq[j])throw new RuntimeException();
					collisionconsumer.accept(x,y,stride,s,saq[j]);
				}
			}shapes.stop=shapestop;
		}shapes.start=shapestart;
	}*/
	/*static void collide(CollisionConsumer collisionconsumer,Cons shapes,double centerx,double centery,double minscale,double stride,int side){
		double halfstride=stride*0.5,newstride=stride/side,x0=centerx-(side-1)*halfstride,y0=centery-(side-1)*halfstride;
		for(int n=0;n!=side;n++)for(int k=0;k!=side;k++){
			double x=n*stride+x0,y=k*stride+y0;//TODO shape min scale differences
			//Cons shapesAtPosition=shapes.filterCopy(o->((ShapeWrapper)o).S.intersects(x-halfstride,y-halfstride,stride,stride));
			Cons shapesAtPosition=null;
			for(Cons c=shapes;c!=null;c=(Cons)c.cdr){
				ShapeWrapper a=(ShapeWrapper)c.car;
				if(a.S.intersects(x-halfstride,y-halfstride,stride,stride))shapesAtPosition=cons(a,shapesAtPosition);
			}
			
			if(shapesAtPosition==null||shapesAtPosition.cdr==null)continue;
			if(minscale<=newstride)collide(collisionconsumer,shapesAtPosition,x,y,minscale,newstride,side);
			else collideAll(collisionconsumer,shapes,x,y,stride);
		}
	}static void collideAll(CollisionConsumer c,Cons shapes,double centerx,double centery,double scale){
		for(Cons s=shapes,d=(Cons)shapes.cdr;d!=null;s=d,d=(Cons)s.cdr){
			ShapeWrapper a=(ShapeWrapper)s.car;
			for(Cons s2=d;s2!=null;s2=(Cons)s2.cdr){
				/*if(a==s2.car){
					System.out.println(shapes.size());
					for(Object o:shapes)System.out.println(o);
					throw new RuntimeException("Error");
				}*//*c.accept(centerx,centery,scale,a,(ShapeWrapper)s2.car);
			}	
		}
	}*/
	
	private int cacheStart,cacheStop;
	private HashMap<Point2D.Double,Object>topLevelCandidates;
	private ShapeWrapperArrayWrapper[]cache;
	
	public static class ShapeWrapper{
		public double CollisionScale;
		public Shape S;
		public ShapeWrapper(Shape s){
			S=s;
		}
	}public static interface CollisionConsumer{public void accept(double centerx,double centery,double scale,ShapeWrapper a,ShapeWrapper b);}
	/*public void addBlock(Point2D.Double center,ShapeWrapper s){
		HashMap<Point2D.Double,Object>m=topLevelCandidates;
		Object o=m.put(center,s);
		if(o!=null){
			//if((o instanceof Cons&&((Cons) o).contains(s)))throw new RuntimeException("Error");
			m.put(center,cons(s,o instanceof Cons?o:cons(o,null)));
		}
	}*/public void addBlock(Point2D.Double center,ShapeWrapper s){
		HashMap<Point2D.Double,Object>m=topLevelCandidates;
		Object o=m.putIfAbsent(center,s);
		if(null!=o){
			if(o instanceof ShapeWrapperArrayWrapper){((ShapeWrapperArrayWrapper)o).append(s);}
			else if(cacheStart==cacheStop){
				int cachestart=cacheStart;
				ShapeWrapperArrayWrapper c[]=cache,w=new ShapeWrapperArrayWrapper((ShapeWrapper)o,s);
				if(cachestart==c.length)c=cache=copyOf(c,cachestart*2);
				c[cachestart]=w;
				m.put(center,w);
				cacheStart=cacheStop=cachestart+1;
			}else{
				ShapeWrapperArrayWrapper w=cache[cacheStart++];
				m.put(center,w);
				w.array[0]=(ShapeWrapper)o;
				w.array[1]=s;
				w.start=0;
				w.stop=2;
			}
		}
	}public void addSeed(double scale,double x,double y,ShapeWrapper shapewrapper){
		double halfstride,stride=scale,srcp=1.0/stride,x0,x1,y0,y1;
		Rectangle2D boundingbox;
		Shape shape=shapewrapper.S;
		boundingbox=shape.getBounds2D();
		halfstride=stride*0.5;
		//x0=(Math.floor(boundingbox.getMinX()*srcp)+0.5)*stride;
		//y0=(Math.floor(boundingbox.getMinY()*srcp)+0.5)*stride;
		x0=Math.round(boundingbox.getMinX()*srcp-1.0)*stride;
		x1=Math.round(boundingbox.getMaxX()*srcp+1.0)*stride;
		y0=Math.round(boundingbox.getMinY()*srcp-1.0)*stride;
		y1=Math.round(boundingbox.getMaxY()*srcp+1.0)*stride;
		//for(int l=(int)Math.round(((Math.ceil(boundingbox.getMaxX()*srcp)+0.5)*stride-x0)*srcp),n=0,w=(int)Math.round(((Math.ceil(boundingbox.getMaxY()*srcp)+0.5)*stride-y0)*srcp);l!=n;n++)for(int k=0;k!=w;k++){
		for(int l=(int)Math.round((x1-x0)*srcp),n=0,w=(int)Math.round((y1-y0)*srcp);n!=l;n++)for(int k=0;k!=w;k++){
			x=n*stride+x0;
			y=k*stride+y0;
			if(shape.intersects(x-halfstride,y-halfstride,stride,stride))addBlock(new Point2D.Double(x,y),shapewrapper);
		}
	}public void clear(){
		/*topLevelCandidates.forEach((p,o)->{if(o instanceof ShapeWrapperArrayWrapper){
			if(cache.length==cacheHeight)cache=copyOf(cache,cacheHeight*2);
			cache[cacheHeight++]=(ShapeWrapperArrayWrapper)o;
			((ShapeWrapperArrayWrapper)o).offset=0;
			((ShapeWrapperArrayWrapper)o).size=2;
		}});*/
		cacheStart=0;
		topLevelCandidates.clear();
	}public void collide(double maxscale,double minscale,int side,CollisionConsumer c){topLevelCandidates.forEach((p,o)->{if(o instanceof ShapeWrapperArrayWrapper)collide(c,(ShapeWrapperArrayWrapper)o,p.x,p.y,minscale,maxscale/side,side);});}
	public CollisionDetector(int initialBlockCapacity){
		cache=new ShapeWrapperArrayWrapper[initialBlockCapacity];
		topLevelCandidates=new HashMap<>(initialBlockCapacity);
	}
}
