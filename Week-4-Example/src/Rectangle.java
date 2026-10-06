

public class Rectangle {
	public float length;    // define the length
	public float width;		// define the width var
	
	public Rectangle (float l, float w)
	{
		length = l;
		width = w;
	}
	
	public Rectangle() 
	{
		length = 0.0f;
		width = 0.0f;
	}
	
	public Rectangle(float width) {		// para
		length = 0.0f;
		this.width = width;				// this. instance使役
	}
		
	public float area() 
	{
		float result = length * width;
			return result;		// scope
	}
	
	public float perm() 
	{
		float result = 2 * (length + width);
			return result;
	}
	
}






