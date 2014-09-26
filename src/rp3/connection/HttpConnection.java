package rp3.connection;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpConnection {
	
	public final static int HTTP_STATUS_UNAUTHORIZED = 401;
	public final static int HTTP_STATUS_NOT_FOUND = 404;
	public final static int HTTP_STATUS_FORBIDDEN = 403;
	public final static int HTTP_STATUS_BAD_REQUEST = 400;
	public final static int HTTP_STATUS_ERROR = 500;
	
	/**
     * Network connection timeout, in milliseconds.
     */
    static final int NET_CONNECT_TIMEOUT_MILLIS = 15000;  // 15 seconds

    /**
     * Network read timeout, in milliseconds.
     */
    static final int NET_READ_TIMEOUT_MILLIS = 10000;  // 10 seconds
	
	public static InputStream downloadUrl(final URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(NET_READ_TIMEOUT_MILLIS /* milliseconds */);
        conn.setConnectTimeout(NET_CONNECT_TIMEOUT_MILLIS /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }
}
