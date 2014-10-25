import java.util.*;
import java.io.*;

public class Street {
	static ArrayList<Wizard> allWizards=new ArrayList<Wizard>();
	static Set<String> shopnameList = new HashSet<String>();
	static Map<String, Shop> allShops=new HashMap<String,Shop>();
	public static void main(String[] args){
		try {
//			Initial setup
			long startTime=System.nanoTime();
			Scanner sc=new Scanner(new File(args.length==1?args[0]:"input2"));
			while(sc.hasNextLine()){
				String nextLine=sc.nextLine();
				String[] info=nextLine.split(" ");
				if (info.length<3) {
					System.err.println("Invalid Input!");
					System.exit(1);
				}
				Wizard nextWizard=new Wizard(info[0],info[1],Arrays.copyOfRange(info, 2, info.length));
				nextWizard.startTime=startTime;
				allWizards.add(nextWizard);
				System.out.println(nextLine);
			}
			sc.close();
			System.out.println();
			for(String i:shopnameList){
				Shop shop=new Shop(i);
				allShops.put(i, shop);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		for(Wizard i:allWizards){
//			System.out.println(i.name+" "+i.type+" "+(Arrays.toString(i.shoppingList)));
			new Thread(i).start();
		}
	}
		
}
