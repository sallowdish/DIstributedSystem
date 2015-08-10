/**
 * Created by Ray on 15-08-10.
 */
public class Stack<T> {
        //Customized Stack of generics

        private T[] container;
        private Integer currentLocation;
        private Integer size = -1;

        public Stack(int n){
            container = (T[])new Object[n];
            size = n;
            currentLocation = -1;
        }

        ///
        // push a valid index into container
        // return ture if push succeed, otherwise false
        ///
        public boolean push(T i){
            try{
                container[currentLocation+1] = i;
                currentLocation++;
                return true;
            }
            catch(Exception e){
                System.err.println(e.getMessage());
                return false;
            }
        }

        ///
        // pop a valid index into container
        // return integer(>0) if succeed, otherwise -1;
        ///
        public T pop(){
            try{
                if(currentLocation == -1){
                    throw new Exception("container is empty.");
                }
                else{
                    T result = container[currentLocation];
                    currentLocation--;
                    return result;
                }
            }
            catch(Exception e){
                System.err.println(e.getMessage());
                return null;
            }
        }

        public Integer size(){
            return size;
        }

        public boolean isEmpty(){
            return currentLocation == -1;
        }

        public Integer count() {
            return currentLocation + 1;
        }
}
