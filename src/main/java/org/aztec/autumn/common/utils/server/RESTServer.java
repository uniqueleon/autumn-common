package org.aztec.autumn.common.utils.server;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;
import javax.ws.rs.core.UriBuilder;

import org.aztec.autumn.common.CommonConfig;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;
import org.glassfish.jersey.SslConfigurator;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class.
 *
 */
public class RESTServer {
    // Base URI the Grizzly HTTP server will listen on
    private static final Logger LOG = LoggerFactory.getLogger(RESTServer.class);

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer(String baseUri,String... wsPackages) {
    	LOG.info("Starting HTTP REST WebService Server!");
        // create a resource config that scans for JAX-RS resources and providers
        // in com.baitian.playtoy package
        
        // uncomment the following line if you want to enable
        // support for JSON on the service (you also have to uncomment
        // dependency on jersey-media-json module in pom.xml)
        // --
        // rc.addBinder(org.glassfish.jersey.media.json.JsonJaxbBinder);

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
    	HttpServer server = startSimpleServer(baseUri, wsPackages);
    	LOG.info("Start finished!");
    	return server;
    }
    
    public static HttpServer startSimpleServer(String baseUri,String... wsPackages){

        final ResourceConfig rc = new ResourceConfig().packages(wsPackages);
        HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(URI.create(baseUri), rc);
        return httpServer;
    }
    
    public static HttpServer startSecuredServer(String baseUri,String... wsPackages){
    	CommonConfig config = new CommonConfig();
    	Integer authType =   config.getHttpsAuthType();  
          
        SslConfigurator sslConfig = SslConfigurator.newInstance();  
        if(authType == 1){  
            sslConfig.keyStoreFile(config.getKeyStoreFile())  
                    .keyStorePassword(config.getStorePassword())  
                    .keyPassword(config.getKeyPassword());  
        }else if(authType == 2){  
            sslConfig.keyStoreFile(config.getKeyStoreFile())  
                    .keyStorePassword(config.getStorePassword())  
                    .keyPassword(config.getKeyPassword())  
                    .trustStoreFile(config.getTrustStoreFile())  
                    .trustStorePassword(config.getTrustStorePassword());  
        }  
        sslConfig.securityProtocol(config.getSecurityProtocol());  
        SSLContext sslContext = sslConfig.createSSLContext();  
          
        SSLEngineConfigurator sslEngineConfig = new SSLEngineConfigurator(sslContext);  
        //默认情况下是客户端模式，如果忘记修改模式  
        //会抛出异常  
        //javax.net.ssl.SSLProtocolException: Handshake message sequence violation, 1]  
        sslEngineConfig.setClientMode(false);  
        if(authType == 1)  
            sslEngineConfig.setWantClientAuth(true);  
        else if(authType == 2)  
            sslEngineConfig.setNeedClientAuth(true);
    	
       
        
        final ResourceConfig rc = new ResourceConfig().packages(wsPackages);
        //rc.re
        Pattern pattern = Pattern.compile(":\\d+");
        Matcher matcher = pattern.matcher(baseUri);
        matcher.find();
        
        int readPort = Integer.parseInt(matcher.group().replace(":", ""));
        URI uri = UriBuilder.fromUri(baseUri).build();  
        HttpHandler handler = ContainerFactory.createContainer(  
                HttpHandler.class, rc); 

        HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(uri, rc,true,sslEngineConfig);
        return httpServer;
    }

}

