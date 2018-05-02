package algo;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import junit.extensions.TestSetup;

public class Test1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String test="02-2016";
		String [] splits = test.split("-");
		System.out.println(splits[1]+splits[0]);
		
		Calendar calendar = new GregorianCalendar();
    	calendar.setTime(new Date());
    	int year = calendar.get(Calendar.YEAR);
    	int month = calendar.get(Calendar.MONTH);
    	System.out.println(year);
    	System.out.println(month+1);
    	
    	test = "http://localhost:8080/admin/kettle/";
    	splits = test.split("/");
    	
    	System.out.println(splits[0]+"//"+splits[2]);
	}

}
