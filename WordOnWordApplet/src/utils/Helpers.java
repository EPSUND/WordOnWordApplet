package utils;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class Helpers {
	
	public static URL getResourceURL(Object o, String resource)
	{
		return o.getClass().getClassLoader().getResource(resource);
	}
	
	public static String getResourceURIString(Object o, String resource)
	{
		try {
			return getResourceURL(o, resource).toURI().toString();
		} catch (URISyntaxException e) {
			System.err.println("Breakout: URL malformed");
			e.printStackTrace();
			return "";
		}
	}
	
	public static String getStackTraceString(Exception e)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString(); // stack trace as a string
	}
	
	public static String getCurrentTimeUTC()
	{
		SimpleDateFormat dateFormatUtc = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		dateFormatUtc.setTimeZone(TimeZone.getTimeZone("UTC"));
		return dateFormatUtc.format(new Date());
	}
	
	public static String utcToLocalTime(String utcDate)
	{
		try {
			SimpleDateFormat dateFormatUtc = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			dateFormatUtc.setTimeZone(TimeZone.getTimeZone("UTC"));
			Date date = dateFormatUtc.parse(utcDate);
			
			SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			return dateFormatLocal.format(date);
		} catch (ParseException e) {
			System.err.println("WordOnWord: Could not parse date");
			e.printStackTrace();
		}
		
		return "";
	}
}
