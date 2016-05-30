package ru.mirea.spo.lab1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
	
	List<Token> tokens = new ArrayList<Token>();
	String accum="";
	String currentLucky = null;
	int i;
	
	Pattern sm = Pattern.compile("^;$");
	Pattern varKeyWordPattern = Pattern.compile("^var$");
	Pattern varKeyWordFor = Pattern.compile("^for$");
	Pattern varKeyWordSwitch = Pattern.compile("^switch$");
	Pattern varKeyWordCase = Pattern.compile("^case$");
	Pattern varKeyWordBreak = Pattern.compile("^break$");
	Pattern varKeyWordDefault = Pattern.compile("^default$");
	
	Pattern assign_op = Pattern.compile("^=$");
	Pattern plus_op = Pattern.compile("^[+]$");
	Pattern minus_op = Pattern.compile("^[-]$");
	Pattern del_op = Pattern.compile("^[/]$");
	Pattern umn_op = Pattern.compile("^[*]$");
	Pattern less_op = Pattern.compile("^<$");
	Pattern more_op = Pattern.compile("^>$");
	Pattern lesseq_op = Pattern.compile("^<=$");
	Pattern moreeq_op = Pattern.compile("^>=$");
	Pattern inc_op = Pattern.compile("^[+]{2}$");
	Pattern dec_op = Pattern.compile("^[-]{2}$");
	
	Pattern brk_op = Pattern.compile("^[(]$");
	Pattern brc_cl = Pattern.compile("^[)]$");
	Pattern braces_op = Pattern.compile("^[{]$");
	Pattern braces_cl = Pattern.compile("^[}]$");
	Pattern cl = Pattern.compile("^[:]$");
	
	Pattern digit = Pattern.compile("^0|[1-9]{1}[0-9]*$");
	Pattern var = Pattern.compile("^[a-zA-Z]+$*");
	Pattern ws = Pattern.compile("^\\s*$");
	
	Map<String, Pattern> keyWords = new HashMap<String, Pattern>();
	Map<String, Pattern> termenals = new HashMap<String, Pattern>();
	

	
	public Lexer(){
		keyWords.put("VAR_KW", varKeyWordPattern);
		keyWords.put("FOR_KW", varKeyWordFor);
		keyWords.put("SWITCH_KW", varKeyWordSwitch);
		keyWords.put("CASE_KW", varKeyWordCase);
		keyWords.put("BREAK_KW", varKeyWordBreak);
		keyWords.put("DEFAULT_KW", varKeyWordDefault);
		
		termenals.put("SM", sm);
		termenals.put("ASSIGN_OP", assign_op);
		termenals.put("DIGIT", digit);
		termenals.put("VAR", var);
		termenals.put("WS", ws);
		
		termenals.put("PL", plus_op);
		termenals.put("MN", minus_op);
		termenals.put("DL", del_op);
		termenals.put("UMN", umn_op);
		termenals.put("LESS", less_op);
		termenals.put("MORE", more_op);
		termenals.put("LESSOREQUALLS", lesseq_op);
		termenals.put("MOREOREQUALLS", moreeq_op);
		termenals.put("INC", inc_op);
		termenals.put("DEC", dec_op);
		
		termenals.put("BR_OP", brk_op);
		termenals.put("BR_CL", brc_cl);
		termenals.put("BRECES_OP", braces_op);
		termenals.put("BRECES_CL", braces_cl);
		termenals.put("COLOB", cl);
		
	}

	public void processInput(String filename) throws IOException {
		File file = new File(filename);
		Reader reader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(reader);
		String line;
		
		while((line = bufferedReader.readLine()) != null){
			
			processLine(line);
			
			
		}
		System.out.println("TOKEN("+currentLucky+") recognized with value:"+ accum);
		tokens.add(new Token(currentLucky, accum));
		for(Token token: tokens){
			System.out.println(token);
		}
	}
	
	private void processLine(String line) {
		for(i=0; i<line.length(); i++){
			accum = accum + line.charAt(i);
			processAccum();
		}
		
	}

	private void processAccum() {
		boolean found = false;
		for(String regExpName: termenals.keySet()){
			Pattern currentPattern = termenals.get(regExpName);
			Matcher m = currentPattern.matcher(accum);
			if(m.matches()){
				currentLucky = regExpName;
				found=true;
			}else{
				
			}
		}
		if(currentLucky!=null&&!found){
			System.out.println("TOKEN("+currentLucky+") recognized with value:"+ accum.substring(0, accum.length()-1));
			tokens.add(new Token(currentLucky, accum.substring(0, accum.length()-1)));
			i--;
			accum="";
			currentLucky = null;
		}
		
		for(String regExpName: keyWords.keySet()){
			Pattern currentPattern = keyWords.get(regExpName);
			Matcher m = currentPattern.matcher(accum);
			if(m.matches()){
				currentLucky = regExpName;
			
				found=true;
			}else{
				
			}
		}
		if(currentLucky!=null&&!found){
			System.out.println("TOKEN("+currentLucky+") recognized with value:"+ accum.substring(0, accum.length()-1));
			tokens.add(new Token(currentLucky, accum.substring(0, accum.length()-1)));
			i--;
			accum="";
			currentLucky = null;
		}
		
	}
	 
	public List<Token> getTokens(){
		return tokens;
	}

}
