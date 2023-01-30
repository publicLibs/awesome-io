package com.github.publicLibs.freedom1b2830.awesomeio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Random;

public final class IoUtils {
	public static final int BUFFERSIZE = 1024;

	public static ByteArrayInputStream bytesToIS(final byte[] inputBytes) {
		return new ByteArrayInputStream(inputBytes);
	}

	public static void downloadFileFromURL(final URL url, final File tmpFile) throws IOException {
		final byte[] data = downloadFromURL(url);
		try (InputStream bis = new ByteArrayInputStream(data)) {
			Files.copy(bis, tmpFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
	}

	public static File downloadFileTmpFromURL(final URL url) throws IOException {
		final long pid = new Random().nextLong();
		final File tmpFile = File.createTempFile(String.format("tmp-%s", pid), "file");
		tmpFile.deleteOnExit();
		downloadFileFromURL(url, tmpFile);
		return tmpFile;
	}

	public static byte[] downloadFromURL(final URL url) throws IOException {
		final HttpURLConnection connection = (HttpURLConnection) url.openConnection();// загружаем
		try (InputStream urlStream = connection.getInputStream()) {
			return IoUtils.isToBytes(urlStream);
		}
	}

	/**
	 * Чтение стрима в массив байтов
	 *
	 * @param inputStream входной массив
	 * @return массив байтов
	 * @throws IOException происходит есть нет даных
	 */
	public static byte[] isToBytes(final InputStream input) throws IOException {// ok
		return isToOs(input).toByteArray();
	}

	public static ByteArrayOutputStream isToOs(final InputStream input) throws IOException {
		try (ByteArrayOutputStream target = new ByteArrayOutputStream()) {
			final byte[] buf = new byte[BUFFERSIZE];
			int length;
			while ((length = input.read(buf)) != -1) {
				target.write(buf, 0, length);
			}
			return target;
		}
	}

	/**
	 * копирует байты из outputStream
	 *
	 * @param outputStream входной стрим
	 * @return байты из стрима
	 * @throws IOException вообще хз
	 */
	public static byte[] osToBytes(final OutputStream outputStream) throws IOException {
		if (outputStream instanceof ByteArrayOutputStream) {
			return ((ByteArrayOutputStream) outputStream).toByteArray();
		}
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			baos.writeTo(outputStream);
			baos.flush();
			return baos.toByteArray();
		}

	}

	public static PipedInputStream osToIs(final ByteArrayOutputStream outputStream) throws IOException {
		final PipedInputStream in = new PipedInputStream();
		try (final PipedOutputStream out = new PipedOutputStream(in)) {
			outputStream.writeTo(out);
		}
		return in;
	}

	public static InputStream readNBytesFromInputStream(final InputStream inputStream, final byte[] result)
			throws IOException {
		final int length = result.length;
		inputStream.read(result, 0, length);
		try (ByteArrayOutputStream resultStreamOS = new ByteArrayOutputStream()) {
			resultStreamOS.write(result);
			final byte[] buffer2 = new byte[BUFFERSIZE];
			while (inputStream.available() > 0) {
				final int len = inputStream.read(buffer2);
				resultStreamOS.write(buffer2, 0, len);
			}
			return IoUtils.osToIs(resultStreamOS);
		}
	}

	private IoUtils() {
	}

}
