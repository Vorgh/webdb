package rest.util;

import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

public class ResettableStreamHttpServletRequest extends HttpServletRequestWrapper
{

    private byte[] rawData;
    private HttpServletRequest request;
    private ResettableServletInputStream servletStream;

    public ResettableStreamHttpServletRequest(HttpServletRequest request)
    {
        super(request);
        this.request = request;
        this.servletStream = new ResettableServletInputStream();
    }


    public void resetInputStream()
    {
        servletStream.stream = new ByteArrayInputStream(rawData);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException
    {
        if (rawData == null)
        {
            rawData = IOUtils.toByteArray(this.request.getReader(), "UTF-8");
            servletStream.stream = new ByteArrayInputStream(rawData);
        }
        return servletStream;
    }

    @Override
    public BufferedReader getReader() throws IOException
    {
        if (rawData == null)
        {
            rawData = IOUtils.toByteArray(this.request.getReader(), "UTF-8");
            servletStream.stream = new ByteArrayInputStream(rawData);
        }
        return new BufferedReader(new InputStreamReader(servletStream));
    }


    private class ResettableServletInputStream extends ServletInputStream
    {

        private InputStream stream;

        @Override
        public int read() throws IOException
        {
            return stream.read();
        }

        @Override
        public boolean isFinished()
        {
            try
            {
                return stream.available() == 0;
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        public boolean isReady()
        {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener)
        {
            throw new RuntimeException("Not implemented");
        }
    }
}
