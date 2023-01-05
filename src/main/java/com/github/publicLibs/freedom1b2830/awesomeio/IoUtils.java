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

public final class IoUtils {
	public static final int KB = 1024;

	public static ByteArrayInputStream bytesToIS(final byte[] inputBytes) {
		return new ByteArrayInputStream(inputBytes);
	}

	public static void downloadFileFromURL(final URL url, final File tmpFile) throws IOException {
		final var data = downloadFromURL(url);
		try (InputStream bis = new ByteArrayInputStream(data)) {
			Files.copy(bis, tmpFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
	}

	public static File downloadFileTmpFromURL(final URL url) throws IOException {
		final var pid = Long.valueOf(ProcessHandle.current().pid());
		final var tmpFile = File.createTempFile(String.format("tmp-%s", pid), "file");
		tmpFile.deleteOnExit();
		downloadFileFromURL(url, tmpFile);
		return tmpFile;
	}

	public static byte[] downloadFromURL(final URL url) throws IOException {
		final var connection = (HttpURLConnection) url.openConnection();// загружаем
		try (var urlStream = connection.getInputStream()) {
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
		try (var target = new ByteArrayOutputStream()) {
			final var buf = new byte[KB];
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
		if (outputStream instanceof final ByteArrayOutputStream baos) {
			return baos.toByteArray();
		}
		try (var baos = new ByteArrayOutputStream()) {
			baos.writeTo(outputStream);
			baos.flush();
			return baos.toByteArray();
		}

	}

	public static PipedInputStream osToIs(final ByteArrayOutputStream outputStream) throws IOException {
		final var in = new PipedInputStream();
		try (final var out = new PipedOutputStream(in)) {
			outputStream.writeTo(out);
		}
		return in;
	}

	private IoUtils() {
	}

}
