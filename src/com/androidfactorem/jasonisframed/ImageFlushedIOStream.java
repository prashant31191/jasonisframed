package com.androidfactorem.jasonisframed;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageFlushedIOStream extends FilterInputStream {
	public ImageFlushedIOStream(InputStream inputStream) {
		super(inputStream);
	}

	@Override
	public long skip(long n) throws IOException {
		long totalBytesSkipped = 0L;
		while (totalBytesSkipped < n) {
			long bytesSkipped = in.skip(n - totalBytesSkipped);
			if (bytesSkipped == 0L) {
				int byte1 = read();
				if (byte1 < 0) {
					break;
				} else {
					bytesSkipped = 1;
				}
			}
			totalBytesSkipped += bytesSkipped;
		}
		return totalBytesSkipped;
	}
}
