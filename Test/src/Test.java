import java.util.ArrayList;
import java.util.List;

public class Test {

	public static void main(String[] args) {
		String s="xyz";
		List<String> l=new ArrayList<String>();
		for(int i=0;i<s.length();i++){
			String temp=s.substring(i, s.length()-1);
			StringBuilder bld= new StringBuilder(temp);
			l.add(temp);
			for(int j=i+1;j<s.length();j++){
				char ch=bld.charAt(j);
				bld.deleteCharAt(j);
				l.add(bld.toString());
			}
		}
		System.out.println(l);
	}
	
}
