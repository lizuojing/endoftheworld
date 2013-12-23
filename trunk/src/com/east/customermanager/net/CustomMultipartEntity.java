package com.east.customermanager.net;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;

public class CustomMultipartEntity extends MultipartEntity {

	private final ProgressListener listener;
	private static long contentLength = 0;
	public boolean cancle = false;

	public CustomMultipartEntity(final ProgressListener listener) {
		super();
		this.listener = listener;
	}

	public CustomMultipartEntity(final HttpMultipartMode mode, final ProgressListener listener) {
		super(mode);
		this.listener = listener;
	}

	public CustomMultipartEntity(HttpMultipartMode mode, final String boundary, final Charset charset,
			final ProgressListener listener) {
		super(mode, boundary, charset);
		this.listener = listener;
	}

	public void cancle() {
		this.cancle = true;
	}

	@Override
	public long getContentLength() {
		return super.getContentLength();
	}

	@Override
	public void writeTo(OutputStream outstream) throws IOException {
		contentLength = getContentLength();
		super.writeTo(new CountingOutputStream(outstream, this.listener));
	}

	public static interface ProgressListener {
		void progress(int num);
	}

	public class CountingOutputStream extends FilterOutputStream {

		private final ProgressListener listener;
		private long transferred;

		public CountingOutputStream(final OutputStream out, final ProgressListener listener) {
			super(out);
			this.listener = listener;
			transferred = 0;
		}

		public void write(byte[] b, int off, int len) throws IOException {
			if (!cancle) {
				out.write(b, off, len);
				transferred += len;
				if (listener != null) {
					listener.progress((int) (transferred * 1.0 / contentLength * 100));
				}
			} else {
				this.close();
			}
		}

		public void write(int b) throws IOException {
			if (!cancle) {
				out.write(b);
				transferred++;
				listener.progress((int) (transferred * 1.0 / contentLength * 100));
			} else {
				this.close();
			}
		}
	}

}
