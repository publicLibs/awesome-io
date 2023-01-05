package com.github.publicLibs.freedom1b2830.awesomeio;

import java.io.InputStream;
import java.util.NoSuchElementException;

public final class ResourcesIoUtils {
	/**
	 * Читает ресурс из хранилища
	 *
	 * @param contextClassLoader откуда брать ресурс
	 * @param fullPathName       полный путь к ресурсу
	 * @return ресурс как стрим
	 * @throws NoSuchElementException если не нашел ресурс
	 */
	public static InputStream readResource(final ClassLoader contextClassLoader, final String fullPathName)
			throws NoSuchElementException {
		final var resource = contextClassLoader.getResourceAsStream(fullPathName);
		if (resource == null) {
			throw new NoSuchElementException(String.format("%s not found in %s", fullPathName, contextClassLoader));
		}
		return resource;
	}

	/**
	 * Читает ресурс из хранилища (берет из контекста потока)
	 *
	 * @param fullPathName полный путь к ресурсу
	 * @return ресурс как стрим
	 * @throws NoSuchElementException если не нашел ресурс
	 */
	public static Object readResource(final String fullPathName) throws NoSuchElementException {
		return readResource(Thread.currentThread().getContextClassLoader(), fullPathName);
	}

	private ResourcesIoUtils() {
	}
}
