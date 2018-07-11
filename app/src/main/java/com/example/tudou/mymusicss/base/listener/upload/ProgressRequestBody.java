package com.example.tudou.mymusicss.base.listener.upload;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * 自定义回调加载速度类RequestBody
 * Created by WZG on 2016/10/20.
 */

public class ProgressRequestBody extends RequestBody {
    //实际起作用的RequestBody
    private RequestBody delegate;
    //进度回调接口
    private final UploadProgressListener progressListener;
    private CountingSink countingSink;

    public ProgressRequestBody(RequestBody requestBody, UploadProgressListener progressListener) {
        this.delegate = requestBody;
        this.progressListener = progressListener;
    }

    @Override
    public MediaType contentType() {
        return delegate.contentType();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
/*
*
* 我发现日志拦截器中的 BufferedSink 是 Buffer 类型，
* 而实际进行网络请求的 BufferedSink 是 FixedLengthSink。
* 所以修改 ProgressRequestBody 里的 writeTo(BufferedSink sink) 方法，
* 如果传入的 sink 为 Buffer 对象，则直接写入，不进行统计，
* */


        if (sink instanceof Buffer) {
            // Log Interceptor
            delegate.writeTo(sink);
            return;
        }
        countingSink = new CountingSink(sink);
        //将CountingSink转化为BufferedSink供writeTo()使用
        BufferedSink bufferedSink = Okio.buffer(countingSink);
        delegate.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    protected final class CountingSink extends ForwardingSink {
        private long byteWritten;

        public CountingSink(Sink delegate) {
            super(delegate);
        }

        /**
         * 上传时调用该方法,在其中调用回调函数将上传进度暴露出去,该方法提供了缓冲区的自己大小
         *
         * @param source
         * @param byteCount
         * @throws IOException
         */
        @Override
        public void write(Buffer source, long byteCount) throws IOException {


            super.write(source, byteCount);
            byteWritten += byteCount;
            progressListener.onProgress(byteWritten, contentLength());
        }
    }

    /**
     * 返回文件总的字节大小
     * 如果文件大小获取失败则返回-1
     *
     * @return
     */
    @Override
    public long contentLength() {
        try {
            return delegate.contentLength();
        } catch (IOException e) {
            return -1;
        }
    }
}