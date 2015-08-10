public class PFArray {
        private Place[] place;	
        private int PFArraySize;
	    private Freelist freelist;
	

	    public PFArray(int n) throws Exception{
		  
	    	if(n < 0){
	    		throw new Exception("n must ba a postive integer.");
	    	}
	    	else{
		        place = new Place[n];
			    freelist = new Freelist(n);
			    for(int i = 0; i < place.length; i++){
			    	freelist.push(i);
			    }
		        PFArraySize = n;
	    	}	
	    }
	    public void flush() {
	    	for (int i = 0; i < PFArraySize; i++){
	    		place[i] = null;
	    		freelist.push(i);
	    	}
	    }
	    
	    public boolean is_full(){
	    	return freelist.isEmpty();
	    }
	    
	    public int space_left(){
	    	return freelist.count();
	    }
	    
	    public void add_item(Place p) throws Exception
	    {
	    	// get an available position of PFArray
	    	Integer index = freelist.pop();
	    	// if index == -1, means no position is empty right now
	    	if(index == -1){
	    		throw new Exception("PFArray if full.");
	    	}
	    	else{
	    		place[index] = p;
	    		return;
	    	}
	    }
	    
	    public int position_in_array(Place p) throws Exception{
		    	for (int i = 0; i < place.length; i++) {
		    		if(place[i] == null){
						continue;
		    		}
		    		else{
		    			if(place[i].equals(p)){
		    				return i;
		    			}
		    		}
				}
		    	//if nothing is found to be equal
		    	throw new Exception("Nothing match is found.");
	    }
	    
	    
	    public int unsafe_position_in_array(Place p){
	    	for (int i = 0; i < place.length; i++) {
	    		if(place[i] == null){
					continue;
	    		}
	    		else{
	    			if(place[i].equals(p)){
	    				return i;
	    			}
	    		}
			}
	    	//if nothing is found to be equal
	    	return -1;
	    }
	    
	    public void remove_item(int n) throws Exception{
	    	if(n >= PFArraySize || n<0 ){
	    		throw new Exception("Index " + n + " is out of range.");
	    	}
	    	else if(place[n] == null)
	    	{
	    		throw new Exception("PFArray[" + n + "] is null.");
	    	}
	    	else{
	    		place[n] = null;
	    		freelist.push(n);
	    	}
	    }
	    
	    public void unsafe_remove_item(int n) throws Exception{
	    	if(n >= PFArraySize || n<0 ){
	    		throw new Exception("Index " + n + " is out of range.");
	    	}
	    	else if(place[n] == null)
	    	{
	    		//do nothing here
//	    		throw new Exception("PFArray[" + n + "] is null.");
	    		return;
	    	}
	    	else{
	    		place[n] = null;
	    		freelist.push(n);
	    	}
	    }
	    
	    public void print_all(){
	    	for(int i =0; i <place.length; i++){
	    		if(place[i] == null){
	    			continue;
	    		}
	    		else{
	    			System.out.println(i);
	    			place[i].print();
	    		}
	    	}
	    }
}


