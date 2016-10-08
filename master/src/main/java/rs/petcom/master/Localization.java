package rs.petcom.master;

import java.util.Locale;

public class Localization {

	public static String[] getLetters(){
		String[] letters = {};
		String locale = Locale.getDefault().toString();
		switch(locale){
		case "sr_RS":
		case "sr":
			String[] l = {"A","B","C","Č","Ć","D","Dž","Đ","E","F","G","H","I",
						  "J","K","L","Lj","M","N","Nj","O","P","R","S","Š",
						  "T","U","V","Z","Ž"};
			letters = l;
			break;
		}
		return letters;
	}
	
}
