package parser;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

public class parser {

	public static void main(String[] args) {
		
		// LeetCodeSubmissions/{title}/Accepted/{time}/info.txt
		
		String USER = "rexlin";
		String src = "/Users/"+ USER + "/eclipse-workspace/submissions";
		
		File submissionFolder = new File(src);
		File[] folders = submissionFolder.listFiles();

		for (File folder : folders) {
			
			String title = folder.getName();
			
			System.out.print("parsing " + title);
			
			File acFolder = new File(src+"/"+title+"/Accepted");
			if(!acFolder.exists()) continue;
			
			String[] timeArr = acFolder.list();
			
			if(timeArr.length==0){
				System.out.println(" no ac record ... continue");
				continue;
			}
			
			
			//get time path
			SimpleDateFormat sdf = new SimpleDateFormat("d-M-yyyy, HH:mm:ss aaa", Locale.ENGLISH);
			Date max = new Date(0);
			String timeStr = "{time}";
			
			for(String t : timeArr) {
				
				try {
					Date cur = sdf.parse(t);
					if(cur.after(max)) {
						max = cur;
						timeStr=t;
					}
					
				} catch (ParseException e) {
//					System.out.println( "******************" + t);
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			String timepath = src+"/"+title+"/Accepted/"+timeStr;
			
			
			
			// parsing json to get id
			StringBuilder sb = new StringBuilder();
			try (BufferedReader br = new BufferedReader(new FileReader(timepath+"/info.txt"));){
	        	while (br.ready()) {
	        		sb.append(br.readLine());
	        	}
			}catch(Exception e) {
				
	        	e.printStackTrace();
	        	return;
			}
			JSONObject jo =JSON.parseObject(sb.toString());
			String id = jo.getString("question_id");
			
			
			String[] name =  new String[] {"Solution", "java"};
			
			String[] arr = new File(timepath).list();
			for(String a : arr) {
				if(a.startsWith("info")) continue;
				name = a.split("\\.");
			}
			
			String sourcePath = timepath + "/" + name[0]+"."+name[1];
			String targetPath = "/Users/" + USER + "/git/leetcode-challenge/Problems/Java/" + id + "." + title + "." + name[1];
			
			File copied = new File(targetPath);
		    
			try (
		      InputStream in = new BufferedInputStream(
		        new FileInputStream(sourcePath));
		      OutputStream out = new BufferedOutputStream(
		        new FileOutputStream(copied))) {
		 
		        byte[] buffer = new byte[1024];
		        int lengthRead;
		        while ((lengthRead = in.read(buffer)) > 0) {
		            out.write(buffer, 0, lengthRead);
		            out.flush();
		        }
		        
		        System.out.println(" success");
		    }catch(Exception e) {
        		e.printStackTrace();
		    }
		}

	}

}
