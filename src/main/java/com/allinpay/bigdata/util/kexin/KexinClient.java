/**

* 类的描述:

* @author gaosw

* @Time 2016-12-29上午9:40:16
*

*/
package com.allinpay.bigdata.util.kexin;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.tomcat.util.codec.binary.Base64;


public class KexinClient {
	private String kexinAKey;
	private String kexinSKey;
	private String kexinApiUrl;
	protected String scheme;
	protected String hostName;
	protected int port;
	protected Integer connTimeout;
	protected Integer readTimeout;
	protected String charset = "UTF-8";
	protected String mimeType;
	
	/**
	 * 初始化入口
	 * @param akey	client_id
	 * @param skey	password
	 * @param url	请求URL 可以在URL中指定返回json 或 xml
	 */
	public KexinClient(String akey, String skey, String url) {
		this.kexinAKey = akey;
		this.kexinSKey = skey;
		this.kexinApiUrl = url;
		try {
			URI uri = new URI(url);
			this.scheme = uri.getScheme();
			this.hostName = uri.getHost();
			this.port = uri.getPort();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}
	
	/**
	 * 设置连接超时
	 * @param time 毫秒
	 */
	public void setConnTimeout(Integer time) {
		this.connTimeout = time;
	}
	
	/**
	 * 设置请求超时
	 * @param time 毫秒
	 */
	public void setReadTimeout(Integer time) {
		this.readTimeout = time;
	}
	
	/**
	 * 设置编码
	 * @param charset 默认UTF-8
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}
	
	/**
	 * 设置传输类型
	 * @param mimeType
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	
	/**
	 * get请求
	 * @return
	 * @throws 
	 */
	
public  String getVerification(String idCardCode,String idCardName) throws ConnectTimeoutException, SocketTimeoutException, Exception {
		HttpHost host = new HttpHost(hostName, port, scheme);
		HttpClient client = kexinApiUrl.startsWith("https") ? createSSLInsecureClient() : HttpClients.custom().build();    
		idCardName=URLEncoder.encode(idCardName, "utf-8");


		HttpGet get = new HttpGet(kexinApiUrl+"?idCardCode="+idCardCode+"&idCardName="+idCardName);
       
        String result = "";
        try {
        	//自定义客户端请求参数设置
            Builder customReqConf = RequestConfig.custom();  
            if (connTimeout != null) {  
                customReqConf.setConnectTimeout(connTimeout);  
            }  
            if (readTimeout != null) {  
                customReqConf.setSocketTimeout(readTimeout);  
            }
            //保存参数
            get.setConfig(customReqConf.build());  
            HttpResponse res;  
            //创建并初始化认证提供者
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
	        credentialsProvider.setCredentials(new AuthScope(host.getHostName(), host.getPort()), new UsernamePasswordCredentials(kexinAKey, kexinSKey));
	        HttpClientContext localContext = HttpClientContext.create();		        
	        
	        localContext.setCredentialsProvider(credentialsProvider);

//	        //发送请求
            res = client.execute(get,localContext);
	        //保存请求结果
            result = IOUtils.toString(res.getEntity().getContent(), charset);  
        } finally {  
        	//释放连接
        	get.releaseConnection();  
            if (kexinApiUrl.startsWith("https") && client != null  
                    && client instanceof CloseableHttpClient) {  
                ((CloseableHttpClient) client).close();  
            }  
        }  
        return result;  
	}

/**
 * 提交查询验证
 * @param number	身份证号
 * @param name		姓名
 * @param imageArr	图片二进制内容数组
 * @param userIP	IP
 * @return
 * @throws ConnectTimeoutException
 * @throws SocketTimeoutException
 * @throws Exception
 * @throws UnsupportedEncodingException
 */
public String identityVerification(String number, String name, ArrayList<byte []> imagesArrayList, String userIP ) throws ConnectTimeoutException, SocketTimeoutException, Exception, UnsupportedEncodingException {
	List<NameValuePair> data = new ArrayList<NameValuePair>();
	//构造请求参数数组
	data.add(new BasicNameValuePair("number", number));
	data.add(new BasicNameValuePair("name", name));
	data.add(new BasicNameValuePair("user_ip", userIP));
	for(int i = 0; i < imagesArrayList.size(); i++) {
		data.add(new BasicNameValuePair("feature_code[" + i + "]", new String(getFeatureCode(imagesArrayList.get(i)), charset)));
	}
	return postVerification(data);
}

/**
 * 图片特殊处理, 提取特征码
 * @param code
 * @return
 */
public static byte[] getFeatureCode(byte[] code) {
	return Base64.encodeBase64(code);
}

/**
 * 提交动作
 * @param dataList
 * @return
 * @throws ConnectTimeoutException
 * @throws SocketTimeoutException
 * @throws Exception
 */
private String postVerification(List<NameValuePair> dataList) throws ConnectTimeoutException, SocketTimeoutException, Exception {
	HttpHost host = new HttpHost(hostName, port, scheme);
	HttpClient client = kexinApiUrl.startsWith("https") ? createSSLInsecureClient() : HttpClients.custom().build();    
    HttpPost post = new HttpPost(kexinApiUrl);

    String result = "";
    try {
    	//自定义客户端请求参数设置
        Builder customReqConf = RequestConfig.custom();  
        if (connTimeout != null) {  
            customReqConf.setConnectTimeout(connTimeout);  
        }  
        if (readTimeout != null) {  
            customReqConf.setSocketTimeout(readTimeout);  
        }
        //保存参数
        post.setConfig(customReqConf.build());  
        
        HttpResponse res;  
        //创建并初始化认证提供者
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(new AuthScope(host.getHostName(), host.getPort()), new UsernamePasswordCredentials(kexinAKey, kexinSKey));
        HttpClientContext localContext = HttpClientContext.create();		        
        
        localContext.setCredentialsProvider(credentialsProvider);
        //设置post请求体
        post.setEntity(new UrlEncodedFormEntity(dataList, charset));
        //发送请求
        res = client.execute(post, localContext);
        //保存请求结果
        result = IOUtils.toString(res.getEntity().getContent(), charset);  
    } finally {  
    	//释放连接
        post.releaseConnection();  
        if (kexinApiUrl.startsWith("https") && client != null  
                && client instanceof CloseableHttpClient) {  
            ((CloseableHttpClient) client).close();  
        }  
    }  
    return result;  
}

/**
 * Post请求
 * @return
 * @throws 
 */

public String getVerification(Map<String,String>map) throws ConnectTimeoutException, SocketTimeoutException, Exception {
	HttpHost host = new HttpHost(hostName, port, scheme);
	//DefaultHttpClient httpclient = new DefaultHttpClient();
	HttpClient httpclient=createSSLInsecureClient();
	//HttpClient httpclient = kexinApiUrl.startsWith("https") ? createSSLInsecureClient() : HttpClients.custom().build();    
	

	HttpPost post = new HttpPost(kexinApiUrl);
    String result = "";
    try {
    	//自定义客户端请求参数设置
         Builder customReqConf = RequestConfig.custom();  
        if (connTimeout != null) {  
            customReqConf.setConnectTimeout(connTimeout);  
        }  
        if (readTimeout != null) {  
            customReqConf.setSocketTimeout(readTimeout);  
        }
        //保存参数
        post.setConfig(customReqConf.build());  
        
        HttpResponse res;  
        //创建并初始化认证提供者
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(new AuthScope(host.getHostName(), host.getPort()), new UsernamePasswordCredentials(kexinAKey, kexinSKey));
        //httpclient.getCredentialsProvider().setCredentials(new AuthScope(host.getHostName(), host.getPort()), new UsernamePasswordCredentials(kexinAKey, kexinSKey));
        
        HttpClientContext localContext = HttpClientContext.create();	
        List<NameValuePair> list = new ArrayList<NameValuePair>();  
        Iterator iterator =map.entrySet().iterator();  
        while(iterator.hasNext()){  
            Entry<String,String> elem = (Entry<String, String>) iterator.next();  
            list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));  
        }  
        if(list.size() > 0){  
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,charset);  
            post.setEntity(entity);  
        }
        localContext.setCredentialsProvider(credentialsProvider);

        //发送请求
        res = httpclient.execute(post,localContext);
        //保存请求结果
        result = IOUtils.toString(res.getEntity().getContent(), charset);  
        //result=null;
    } finally {  
    	//释放连接
    	post.releaseConnection();  
        if (kexinApiUrl.startsWith("https") && httpclient != null  
                && httpclient instanceof CloseableHttpClient) {  
            ((CloseableHttpClient) httpclient).close();  
        }  
    }  
    return result;  
}
	
	
	/**
	 * 建立SSL连接
	 * @return
	 * @throws GeneralSecurityException
	 */
	private static CloseableHttpClient createSSLInsecureClient()  
            throws GeneralSecurityException {  
        try {  
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(  
                    null, new TrustStrategy() {  
                        public boolean isTrusted(X509Certificate[] chain,  
                                String authType) throws CertificateException {  
                            return true;  
                        }  
                    }).build();  
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(  
                    sslContext, new X509HostnameVerifier() {  
  
                        @Override  
                        public boolean verify(String arg0, SSLSession arg1) {  
                            return true;  
                        }  
  
                        @Override  
                        public void verify(String host, SSLSocket ssl)  
                                throws IOException {  
                        }  
  
                        @Override  
                        public void verify(String host, X509Certificate cert)  
                                throws SSLException {  
                        }  
  
                        @Override  
                        public void verify(String host, String[] cns,  
                                String[] subjectAlts) throws SSLException {  
                        }  
  
                    });  
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();  
        } catch (GeneralSecurityException e) {  
            throw e;  
        }  
    } 
	
	public static void main(String[] args) {
		try {
			 
//			// 失信被执行人：https://api.kexin.net:18443/dishonesty/format/json
//			 KexinClient client = new KexinClient("1201713", "8c4d1b3f1937d1b6f07155a9756797b1", "https://api.kexin.net:18443/dishonesty/format/json");
//			 Map<String,String> map=new HashMap<String,String>();
//			 map.put("card_num", "420123195702080414");
//		     map.put("name", URLEncoder.encode("胡建国", "utf-8"));
//		  //   map.put("name", URLEncoder.encode(new String ("王汉华".getBytes("utf-8"),"utf-8"), "utf-8"));
// 		//	   map.put("name", URLEncoder.encode("王汉华", "utf-8"));
//		 //    map.put("area", URLEncoder.encode("江苏", "utf-8"));
//		     map.put("page", "1");
//		     map.put("pagesize", "10");   
			 
			//不良记录查询
			 KexinClient client = new KexinClient("1201713", "8c4d1b3f1937d1b6f07155a9756797b1", "https://api.kexin.net:18443/illegal_record/format/json");
			 Map<String,String> map=new HashMap<String,String>();
			 map.put("idcard", "420123197808017332");
			 map.put("name", URLEncoder.encode("王汉华", "utf-8"));
		     map.put("user_ip", "116.236.169.62");
			
			 //学历查询
			 /*KexinClientTest client = new KexinClientTest("1201713", "8c4d1b3f1937d1b6f07155a9756797b1", "https://api.kexin.net:18443/education/format/json");
			 Map<String,String> map=new HashMap<String,String>();
			 map.put("idcard", "320681199110272813");
			 map.put("name", URLEncoder.encode("周小乾", "utf-8"));
		     map.put("user_ip", "116.236.169.62");*/
			
			 //身份照片
			// KexinClientTest client = new KexinClientTest("1201713", "8c4d1b3f1937d1b6f07155a9756797b1", "https://api.kexin.net:18443/idcard_query/format/json");
			 
			 //get
			 //String result= client.getVerification("","");
			 
			 //POST
    		     String result=client.getVerification(map);
			
		     /*JSONObject jsonObject = null;
	    	 jsonObject = (JSONObject) JSONObject.parse(result);
	    	 result=jsonObject.getString("name");
	    	 System.out.print(StringUtils.unicodeToChinese(result));
	    	 result=jsonObject.getString("area");
	    	 System.out.print(StringUtils.unicodeToChinese(result));*/
		} catch (ConnectTimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketTimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}