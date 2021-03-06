package shared.locations;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents the location of an edge on a hex map
 */
public class EdgeLocation implements Serializable
{
	
	private HexLocation hexLoc;
	private EdgeDirection dir;
	
	public EdgeLocation(HexLocation hexLoc, EdgeDirection dir)
	{
		setHexLoc(hexLoc);
		setDir(dir);
	}

	public EdgeLocation(EdgeLocation edgeLocation)
	{
		setHexLoc(new HexLocation(edgeLocation.getHexLoc()));
		setDir(edgeLocation.getDir());
	}
	
	public HexLocation getHexLoc()
	{
		return hexLoc;
	}
	
	public void setHexLoc(HexLocation hexLoc)
	{
		if(hexLoc == null)
		{
			throw new IllegalArgumentException("hexLoc cannot be null");
		}
		this.hexLoc = hexLoc;
	}
	
	public EdgeDirection getDir()
	{
		return dir;
	}
	
	public void setDir(EdgeDirection dir)
	{
		this.dir = dir;
	}
	
	@Override
	public String toString()
	{
		return "EdgeLocation [hexLoc=" + hexLoc + ", dir=" + dir + "]";
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dir == null) ? 0 : dir.hashCode());
		result = prime * result + ((hexLoc == null) ? 0 : hexLoc.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		EdgeLocation other = (EdgeLocation)obj;
		if(dir != other.dir)
			return false;
		if(hexLoc == null)
		{
			if(other.hexLoc != null)
				return false;
		}
		else if(!hexLoc.equals(other.hexLoc))
			return false;
		return true;
	}
	
	/**
	 * Returns a canonical (i.e., unique) value for this edge location. Since
	 * each edge has two different locations on a map, this method converts a
	 * hex location to a single canonical form. This is useful for using hex
	 * locations as map keys.
	 * 
	 * @return Normalized hex location
	 */
	public EdgeLocation getNormalizedLocation()
	{
		
		// Return an EdgeLocation that has direction NW, N, or NE
		
		switch (dir)
		{
			case NorthWest:
			case North:
			case NorthEast:
				return this;
			case SouthWest:
			case South:
			case SouthEast:
				return new EdgeLocation(hexLoc.getNeighborLoc(dir),
										dir.getOppositeDirection());
			default:
				assert false;
				return null;
		}
	}
	
	public ArrayList<VertexLocation> getNormalizedVertices() {
		ArrayList<VertexLocation> vertices = new ArrayList<>();
		switch (dir) {
			case North:
				vertices.add(new VertexLocation(hexLoc,VertexDirection.NorthWest).getNormalizedLocation());
				vertices.add(new VertexLocation(hexLoc,VertexDirection.NorthEast).getNormalizedLocation());
				return vertices;
			case NorthEast:
				vertices.add(new VertexLocation(hexLoc,VertexDirection.NorthEast).getNormalizedLocation());
				vertices.add(new VertexLocation(hexLoc,VertexDirection.East).getNormalizedLocation());
				return vertices;
			case SouthEast:
				vertices.add(new VertexLocation(hexLoc,VertexDirection.East).getNormalizedLocation());
				vertices.add(new VertexLocation(hexLoc,VertexDirection.SouthEast).getNormalizedLocation());
				return vertices;
			case South:
				vertices.add(new VertexLocation(hexLoc,VertexDirection.SouthEast).getNormalizedLocation());
				vertices.add(new VertexLocation(hexLoc,VertexDirection.SouthWest).getNormalizedLocation());
				return vertices;
			case SouthWest:
				vertices.add(new VertexLocation(hexLoc,VertexDirection.SouthWest).getNormalizedLocation());
				vertices.add(new VertexLocation(hexLoc,VertexDirection.West).getNormalizedLocation());
				return vertices;
			case NorthWest:
				vertices.add(new VertexLocation(hexLoc,VertexDirection.West).getNormalizedLocation());
				vertices.add(new VertexLocation(hexLoc,VertexDirection.NorthWest).getNormalizedLocation());
				return vertices;
			default:
				return null;
				
		}
	}
}

