package HandlerTest;

import com.sun.net.httpserver.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class TestHttpExchange extends HttpExchange {
    private final String requestMethod;
    private final ByteArrayInputStream requestBody;
    private final Headers responseHeaders = new Headers();
    private final ByteArrayOutputStream responseBody = new ByteArrayOutputStream();
    private int responseCode;
    private final InetSocketAddress remoteAddress;
    private final String query;


    public TestHttpExchange(String method, String requestBody, String query) {
        this.requestMethod = method;
        this.requestBody = new ByteArrayInputStream(requestBody.getBytes());
        this.remoteAddress = new InetSocketAddress("localhost", 8080);
        this.query = query;
    }

    @Override
    public OutputStream getResponseBody() {
        return responseBody;
    }

    @Override
    public void sendResponseHeaders(int rCode, long length) throws IOException {
        this.responseCode = rCode;
    }

    @Override
    public InputStream getRequestBody() {
        return requestBody;
    }

    @Override
    public String getRequestMethod() {
        return requestMethod;
    }

    @Override
    public HttpContext getHttpContext() {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    @Override
    public Headers getRequestHeaders() {
        return null;
    }

    @Override
    public Request with(String headerName, List<String> headerValues) {
        return super.with(headerName, headerValues);
    }

    @Override
    public Headers getResponseHeaders() {
        return responseHeaders;
    }

    @Override
    public URI getRequestURI() {
        try {
            return new URI("http://localhost:8080/?" + query);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public String getResponse() {
        return responseBody.toString();
    }

    public int getResponseCode() {
        return responseCode;
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return null;
    }

    @Override
    public String getProtocol() {
        return "";
    }

    @Override
    public Object getAttribute(String name) {
        return null;
    }

    @Override
    public void setAttribute(String name, Object value) {

    }

    @Override
    public void setStreams(InputStream i, OutputStream o) {

    }

    @Override
    public HttpPrincipal getPrincipal() {
        return null;
    }

    @Override
    public String toString() {
        return "TestHttpExchange{" +
                "requestMethod='" + requestMethod + '\'' +
                ", requestBody=" + requestBody +
                ", responseHeaders=" + responseHeaders +
                ", responseBody=" + responseBody +
                ", responseCode=" + responseCode +
                ", remoteAddress=" + remoteAddress +
                ", query='" + query + '\'' +
                '}';
    }
}
