public class Freelist {
	//Customized Stack of Int
	
	private Integer[] stack;
	private Integer currentLocation;
	private Integer size = -1;
	
	public Freelist(int n){
		stack = new Integer[n];
		size = n;
		currentLocation = -1;
	}
	
	///
	// push a valid index into stack
	// return ture if push succeed, otherwise false
	///
	public boolean push(Integer i){
		try{
			stack[currentLocation+1] = i;
			currentLocation++;
			return true;
		}
		catch(Exception e){
			System.err.println(e.getMessage());
			return false;
		}
	}
	
	///
	// pop a valid index into stack
	// return integer(>0) if succeed, otherwise -1;
	///
	public Integer pop(){
		try{
			if(currentLocation == -1){
				throw new Exception("stack is empty.");
			}
			else{
				Integer result = stack[currentLocation];
				currentLocation--;
				return result;
			}
		}
		catch(Exception e){
			System.err.println(e.getMessage());
			return -1;
		}
	}
	
	public Integer size(){
		return size;
	}
	
	public boolean isEmpty(){
		return currentLocation == -1;
	}
	
    public Integer count(){
    	return currentLocation+1;
    }
}