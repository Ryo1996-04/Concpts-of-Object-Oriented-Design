
public class Person {
	public String fName;
	public int age;
	
	public String getFName() 
	{
		return this.fName;
	}
	
	public void setFName(String fName) {
		this.fName = fName;
	}
	
	public int getAge() 
	{
		return this.age;
	}
	
	public void setAge(int age) 
	{
		this.age = age;
	}
	public static void main(String[] args) 
	{
		Person p1 = new Person();
		System.out.println("The name of the person is :" + p1.getFName());
		System.out.println("The age of the person is :" + p1.getAge());
		
	}
	
}
