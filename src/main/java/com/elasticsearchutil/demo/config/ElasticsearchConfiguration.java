package com.elasticsearchutil.demo.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Configuration
public class ElasticsearchConfiguration {

    @Value("${elasticsearch.url}")
    private String url;

    @Value("${elasticsearch.apiKey}")
    private String apiKey;

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        RestClientBuilder builder = RestClient.builder(HttpHost.create(url));
        // ignore ssl context
        this.ignoreSSLContext(builder);
        builder.setDefaultHeaders(new Header[]{
                new BasicHeader("Authorization", "ApiKey " + apiKey)
        });
        RestClient restClient = builder.build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }

    private void ignoreSSLContext(RestClientBuilder builder) {
        builder.setHttpClientConfigCallback(localeResolver -> {
            try {
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                            }

                            @Override
                            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                            }

                            @Override
                            public X509Certificate[] getAcceptedIssuers() {
                                return new X509Certificate[0];
                            }
                        }
                }, null);
                return localeResolver.setSSLContext(sslContext);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
