package com.app.http;



import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;


/**
 * A collection of string request parameters or files to send along with
 * requests made from an {@link AsyncHttpClient} instance.
 * <p>
 * For example:
 * <p>
 * <pre>
 * RequestParams params = new RequestParams();
 * params.put("username", "james");
 * params.put("password", "123456");
 * params.put("email", "my&#064;email.com");
 * params.put("profile_picture", new File("pic.jpg")); // Upload a File
 * params.put("profile_picture2", someInputStream); // Upload an InputStream
 * params.put("profile_picture3", new ByteArrayInputStream(someBytes)); // Upload some bytes
 *
 * AsyncHttpClient client = new AsyncHttpClient();
 * client.post("http://myendpoint.com", params, responseHandler);
 * </pre>
 */
public class RequestParams {
    private static String ENCODING = "UTF-8";

    protected ConcurrentHashMap<String, String> urlParams;
    protected ConcurrentHashMap<String, FileWrapper> fileParams;
    protected ConcurrentHashMap<String, ArrayList<String>> urlParamsWithArray;

    /**
     * Constructs a new empty <code>RequestParams</code> instance.
     */
    public RequestParams() {
        init();
    }

    /**
     * Constructs a new RequestParams instance containing the key/value
     * string params from the specified map.
     * @param source the source key/value string map to add.
     */
    public RequestParams(Map<String, String> source) {
        init();

        for(Map.Entry<String, String> entry : source.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Constructs a new RequestParams instance and populate it with a single
     * initial key/value string param.
     * @param key the key name for the intial param.
     * @param value the value string for the initial param.
     */
    public RequestParams(String key, String value) {
        init();

        put(key, value);
    }

    /**
     * Constructs a new RequestParams instance and populate it with multiple
     * initial key/value string param.
     * @param keysAndValues a sequence of keys and values. Objects are
     * automatically converted to Strings (including the value {@code null}).
     * @throws IllegalArgumentException if the number of arguments isn't even.
     */
    public RequestParams(Object... keysAndValues) {
      init();
      int len = keysAndValues.length;
      if (len % 2 != 0)
        throw new IllegalArgumentException("Supplied arguments must be even");
      for (int i = 0; i < len; i += 2) {
        String key = String.valueOf(keysAndValues[i]);
        String val = String.valueOf(keysAndValues[i + 1]);
        put(key, val);
      }
    }

    /**
     * Adds a key/value string pair to the request.
     * @param key the key name for the new param.
     * @param value the value string for the new param.
     */
    public void put(String key, String value){
        if(key != null && value != null) {
            urlParams.put(key, value);
        }
    }

    /**
     * Adds a file to the request.
     * @param key the key name for the new param.
     * @param file the file to add.
     */
    public void put(String key, File file) throws FileNotFoundException {
        put(key, new FileInputStream(file), file.getName());
    }


    /**
     * Adds a file to the request.
     * @param key the key name for the new param.
     * @param file the file to add.
     */
    public void put(String key, File file,boolean b) throws FileNotFoundException {
        put(key, new FileInputStream(file), file.getName(),getMIMEType(file));
    }
    
    /**
     * Adds param with more than one value.
     * @param key the key name for the new param.
     * @param values is the ArrayList with values for the param.
     */
    public void put(String key, ArrayList<String> values)  {
        if(key != null && values != null) {
            urlParamsWithArray.put(key, values);
        }
    }

    /**
     * Adds value to param which can have more than one value.
     * @param key the key name for the param, either existing or new.
     * @param value the value string for the new param.
     */
    public void add(String key, String value) {
        if(key != null && value != null) {
            ArrayList<String> paramArray = urlParamsWithArray.get(key);
            if (paramArray == null) {
                paramArray = new ArrayList<String>();
                this.put(key, paramArray);
            }
            paramArray.add(value);
        }
    }

    /**
     * Adds an input stream to the request.
     * @param key the key name for the new param.
     * @param stream the input stream to add.
     */
    public void put(String key, InputStream stream) {
        put(key, stream, null);
    }

    /**
     * Adds an input stream to the request.
     * @param key the key name for the new param.
     * @param stream the input stream to add.
     * @param fileName the name of the file.
     */
    public void put(String key, InputStream stream, String fileName) {
        put(key, stream, fileName, null);
    }

    /**
     * Adds an input stream to the request.
     * @param key the key name for the new param.
     * @param stream the input stream to add.
     * @param fileName the name of the file.
     * @param contentType the content type of the file, eg. application/json
     */
    public void put(String key, InputStream stream, String fileName, String contentType) {
        if(key != null && stream != null) {
            fileParams.put(key, new FileWrapper(stream, fileName, contentType));
        }
    }

    /**
     * Removes a parameter from the request.
     * @param key the key name for the parameter to remove.
     */
    public void remove(String key){
        urlParams.remove(key);
        fileParams.remove(key);
        urlParamsWithArray.remove(key);
    }

    public void clear()
    {
    	urlParams.clear();
    }
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for(ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
            if(result.length() > 0)
                result.append("&");

            result.append(entry.getKey());
            result.append("=");
            result.append(entry.getValue());
        }

        for(ConcurrentHashMap.Entry<String, FileWrapper> entry : fileParams.entrySet()) {
            if(result.length() > 0)
                result.append("&");

            result.append(entry.getKey());
            result.append("=");
            result.append("FILE");
        }

        for(ConcurrentHashMap.Entry<String, ArrayList<String>> entry : urlParamsWithArray.entrySet()) {
            if(result.length() > 0)
                result.append("&");

            ArrayList<String> values = entry.getValue();
            for (int i = 0; i < values.size(); i++) {
                if (i != 0)
                    result.append("&");
                result.append(entry.getKey());
                result.append("=");
                result.append(values.get(i));
            }
        }

        return result.toString();
    }

   /**
     * Returns an HttpEntity containing all request parameters
     */
    public HttpEntity getEntity() {
        HttpEntity entity = null;

        if(!fileParams.isEmpty()) {
            SimpleMultipartEntity multipartEntity = new SimpleMultipartEntity();

            // Add string params
            for(ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
                multipartEntity.addPart(entry.getKey(), entry.getValue());
            }

            // Add dupe params
            for(ConcurrentHashMap.Entry<String, ArrayList<String>> entry : urlParamsWithArray.entrySet()) {
                ArrayList<String> values = entry.getValue();
                for (String value : values) {
                    multipartEntity.addPart(entry.getKey(), value);
                }
            }

            // Add file params
            int currentIndex = 0;
            int lastIndex = fileParams.entrySet().size() - 1;
            for(ConcurrentHashMap.Entry<String, FileWrapper> entry : fileParams.entrySet()) {
                FileWrapper file = entry.getValue();
                if(file.inputStream != null) {
                    boolean isLast = currentIndex == lastIndex;
                    if(file.contentType != null) {
                    	multipartEntity.addPart(entry.getKey(), file.getFileName(), file.inputStream, file.contentType, isLast);
                    } else {
                        multipartEntity.addPart(entry.getKey(), file.getFileName(), file.inputStream, isLast);
                    }
                }
                currentIndex++;
            }

            entity = multipartEntity;
        } else {
            try {
                entity = new UrlEncodedFormEntity(getParamsList(), ENCODING);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return entity;
    }

    private void init(){
        urlParams = new ConcurrentHashMap<String, String>();
        fileParams = new ConcurrentHashMap<String, FileWrapper>();
        urlParamsWithArray = new ConcurrentHashMap<String, ArrayList<String>>();
    }

    protected List<BasicNameValuePair> getParamsList() {
        List<BasicNameValuePair> lparams = new LinkedList<BasicNameValuePair>();

        for(ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
            lparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        for(ConcurrentHashMap.Entry<String, ArrayList<String>> entry : urlParamsWithArray.entrySet()) {
            ArrayList<String> values = entry.getValue();
            for (String value : values) {
                lparams.add(new BasicNameValuePair(entry.getKey(), value));
            }
        }

        return lparams;
    }

    protected String getParamString() {
        return URLEncodedUtils.format(getParamsList(), ENCODING);
    }

    private static class FileWrapper {
        public InputStream inputStream;
        public String fileName;
        public String contentType;

        public FileWrapper(InputStream inputStream, String fileName, String contentType) {
            this.inputStream = inputStream;
            this.fileName = fileName;
            this.contentType = contentType;
        }

        public String getFileName() {
            if(fileName != null) {
                return fileName;
            } else {
                return "nofilename";
            }
        }
    }
    
    /**
     * 根据文件后缀名获得对应的MIME类型。
     * @param file
     */ 
    private String getMIMEType(File file) { 
         
        String type="*/*"; 
        String fName = file.getName(); 
        //获取后缀名前的分隔符"."在fName中的位置。 
        int dotIndex = fName.lastIndexOf("."); 
        if(dotIndex < 0){ 
            return type; 
        } 
        /* 获取文件的后缀名*/ 
        String end=fName.substring(dotIndex,fName.length()).toLowerCase(); 
        if(end=="")return type; 
        //在MIME和文件类型的匹配表中找到对应的MIME类型。 
        for(int i=0;i<MIME_MapTable.length;i++){ //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？ 
            if(end.equals(MIME_MapTable[i][0])) 
                type = MIME_MapTable[i][1]; 
        }        
        return type; 
    }
     
     
     
     
    private final String[][] MIME_MapTable={ 
                //{后缀名，MIME类型} 
 
                {".bmp",    "image/bmp"}, 
             
                {".gif",    "image/gif"}, 
             
                {".jpeg",   "image/jpeg"}, 
                {".jpg",    "image/jpeg"}, 
               
                {".png",    "image/png"}, 
               
            }; 
}
