package com.east.customermanager.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.east.customermanager.log.CMLog;


public class IOUtils {
	private static final String TAG = "IOUtils";

    public static File saveInputStreamToFile(InputStream inStream, String filePath) throws Exception
	{
		File fl = new File(filePath);
        try
        {  
        	fl.createNewFile();  
        }
        catch (IOException e) 
        {  
        	CMLog.e(TAG, e);
        }  
        
	    FileOutputStream fos = new FileOutputStream(fl);
	    byte[] buffer = new byte[1024];
	    int len = -1;
	    while( (len = inStream.read(buffer)) != -1 )
	    {
	    	fos.write(buffer, 0, len);
	    }
	    fos.close();
	    CMLog.d("Downloader", "file size is " + fl.length());
	    return fl;
	}
}
