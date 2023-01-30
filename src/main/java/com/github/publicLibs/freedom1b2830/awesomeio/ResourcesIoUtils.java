package com.github.publicLibs.freedom1b2830.awesomeio;

import java.io.InputStream;
import java.util.NoSuchElementException;

public final class ResourcesIoUtils {
	public static InputStream readResource(final Class<?> contextClass, final String fullPathName)
			throws NoSuchElementException {
		final InputStream resource = contextClass.getClassLoader().getResourceAsStream(fullPathName);
		if (resource == null) {
			throw new NoSuchElementException(String.format("%s not found in %s", fullPathName, contextClass.getName()));
		}
		return resource;
	}

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
		final InputStream resource = contextClassLoader.getResourceAsStream(fullPathName);
		if (resource == null) {
			throw new NoSuchElementException(String.format("%s not found in %s", fullPathName, contextClassLoader));
		}
		return resource;
	}

	/**
	 * Читает ресурс из хранилища (берет из контекста потока)
	 *
	 * @param fullPathName полный путь к ресурсу
	 * @return
	 * @return ресурс как стрим
	 * @throws NoSuchElementException если не нашел ресурс
	 */
	public static InputStream readResource(final String fullPathName) throws NoSuchElementException {
		return readResource(Thread.currentThread().getContextClassLoader(), fullPathName);
	}

	private ResourcesIoUtils() {
	}
}
