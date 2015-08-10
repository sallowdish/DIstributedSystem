import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.lang.String;

public class Main {

	static HashMap<String, Integer> geneMap;
	
	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String s = in.readLine();
		
		geneMap=new HashMap<String, Integer>();
		
		if (s != null) {
			String[] input = s.split(",");

			Integer target = Integer.parseInt(input[input.length - 1]);

			ArrayList<String> nameList = new ArrayList<String>();

			for (int i = 0; i < input.length - 1; i++) {
				String[] rel = input[i].split("->");
				String parent=rel[0];
				String kid=rel[1];
				
				if(geneMap.get(parent)==null){
					geneMap.put(parent, 1);
					nameList.add(parent);
				}
				Integer parentGeneration=geneMap.get(parent);
				geneMap.put(kid, parentGeneration+1);
				nameList.add(kid);
			}
			
			ArrayList<String> result=new ArrayList<String>();
			for(String name:nameList){
				if (geneMap.get(name)==target) {
					result.add(name);
				}
			}
			
			Collections.sort(result, new Comparator<String>() {
				public int compare(String strA, String strB) {
					int case_insensitive=String.CASE_INSENSITIVE_ORDER.compare(strA, strB);
					return case_insensitive!=0?case_insensitive:strA.compareTo(strB);
				}
			});
			
//			System.out.println(String.join(",", result));
			for (String string : result) {
				if(string!=result.get(result.size()-1)){
					System.out.print(string+",");
				}else{
					System.out.print(string);
				}
			}
		}
	}

	
}