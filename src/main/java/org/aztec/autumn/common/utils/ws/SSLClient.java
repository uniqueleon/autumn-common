package org.aztec.autumn.common.utils.ws;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;  
//用于进行Https请求的HttpClient  
public class SSLClient extends HttpClient{  
    public SSLClient() throws Exception{  
        super();  
        SSLContext ctx = SSLContext.getInstance("TLS");  
        X509TrustManager tm = new X509TrustManager() {  
                @Override  
                public void checkClientTrusted(X509Certificate[] chain,  
                        String authType) throws CertificateException {  
                }  
                @Override  
                public void checkServerTrusted(X509Certificate[] chain,  
                        String authType) throws CertificateException {  
                }  
                @Override  
                public X509Certificate[] getAcceptedIssuers() {  
                    return null;  
                }  
        };  
        ctx.init(null, new TrustManager[]{tm}, null);
        SSLSocketFactory ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();  
        HttpConnectionManager ccm = this.getHttpConnectionManager(); 
        //ccm.
        //SchemeRegistry sr = ccm.getSchemeRegistry();  
        //sr.register(new Scheme("https", 443, ssf));  
    }  
}