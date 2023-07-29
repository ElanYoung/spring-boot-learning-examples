package com.starimmortal.minio.util;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.util.DigestUtils;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Objects;
import java.util.UUID;

/**
 * 文件操作工具类
 *
 * @author william@StarImmortal
 * @date 2021/02/08
 */
public class FileUtil {

	/**
	 * 获取系统默认的文件系统
	 * @return 系统默认的文件系统
	 */
	public static FileSystem getDefaultFileSystem() {
		return FileSystems.getDefault();
	}

	/**
	 * 检查文件或目录路径是否是绝对路径
	 * @param str 文件路径 例如：d:\\folder\\filename
	 * @return 是否是绝对路径
	 */
	public static boolean isAbsolute(String str) {
		Path path = getDefaultFileSystem().getPath(str);
		return path.isAbsolute();
	}

	/**
	 * 获得程序当前路径
	 * @return 程序当前路径
	 */
	public static String getCmd() {
		return System.getProperty("user.dir");
	}

	/**
	 * 初始化存储文件夹
	 * @param dir 本地文件保存位置
	 */
	public static void initStoreDir(String dir) {
		String absDir;
		if (isAbsolute(dir)) {
			absDir = dir;
		}
		else {
			String cmd = getCmd();
			Path path = getDefaultFileSystem().getPath(cmd, dir);
			absDir = path.toAbsolutePath().toString();
		}
		File file = new File(absDir);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	/**
	 * 获取文件绝对路径
	 * @param dir 本地文件保存位置
	 * @param filename 文件名
	 * @return 文件绝对路径
	 */
	public static String getFileAbsolutePath(String dir, String filename) {
		if (isAbsolute(dir)) {
			return getDefaultFileSystem().getPath(dir, filename).toAbsolutePath().toString();
		}
		else {
			return getDefaultFileSystem().getPath(getCmd(), dir, filename).toAbsolutePath().toString();
		}
	}

	/**
	 * 获得新文件的名称
	 * @param extension 文件后缀
	 * @return 新名称
	 */
	public static String getNewFilename(String extension) {
		String uuid = UUID.randomUUID().toString().replace("-", "");
		return uuid + extension;
	}

	/**
	 * 获取文件扩展名
	 * @param file 文件
	 * @return 文件拓展名
	 */
	public static String getFileExtension(MultipartFile file) {
		int index = Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf('.');
		return file.getOriginalFilename().substring(index);
	}

	/**
	 * 获取文件扩展名
	 * @param originalName 原始文件名
	 * @return 文件拓展名
	 */
	public static String getFileExtension(String originalName) {
		int index = Objects.requireNonNull(originalName).lastIndexOf('.');
		return originalName.substring(index);
	}

	/**
	 * 获取文件MD5值
	 * @param bytes 文件数据，比特流
	 * @return MD5值
	 */
	public static String getFileMd5(byte[] bytes) {
		return DigestUtils.md5DigestAsHex(bytes);
	}

	/**
	 * 获得文件的字节
	 * @param file 文件
	 * @return 字节
	 */
	public static byte[] getFileBytes(MultipartFile file) {
		byte[] bytes;
		try {
			bytes = file.getBytes();
		}
		catch (Exception e) {
			throw new RuntimeException();
		}
		return bytes;
	}

	/**
	 * 检查文件后缀
	 * @param extension 文件名后缀
	 * @return 是否通过
	 */
	public static boolean checkExtension(String[] include, String[] exclude, String extension) {
		int inLen = include == null ? 0 : include.length;
		int exLen = exclude == null ? 0 : exclude.length;
		if (inLen > 0 && exLen > 0) {
			return findInInclude(include, extension);
		}
		else if (inLen > 0) {
			// 有include，无exclude
			return findInInclude(include, extension);
		}
		else if (exLen > 0) {
			// 有exclude，无include
			return findInExclude(exclude, extension);
		}
		else {
			// 二者都没有
			return true;
		}
	}

	/**
	 * 查询文件后缀是否存在包含的扩展名里
	 * @param include 包含的拓展名
	 * @param extension 拓展名
	 * @return 是否包含：true || false
	 */
	public static boolean findInInclude(String[] include, String extension) {
		for (String str : include) {
			if (str.equals(extension)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 查询文件后缀是否存在不包含的扩展名里
	 * @param exclude 不包含的拓展名
	 * @param extension 拓展名
	 * @return 是否不包含：true || false
	 */
	public static boolean findInExclude(String[] exclude, String extension) {
		for (String str : exclude) {
			if (str.equals(extension)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 转换大小
	 * @param size 文件大小
	 * @return 转换后的字节
	 */
	public static Long parseSize(String size) {
		DataSize singleLimitData = DataSize.parse(size);
		return singleLimitData.toBytes();
	}

	/**
	 * 本地文件（图片、excel等）转换成Base64字符串
	 * @param imgPath 图片路径
	 */
	public static String convertFileToBase64(String imgPath) {
		byte[] data = null;
		// 读取图片字节数组
		try {
			InputStream in = Files.newInputStream(Paths.get(imgPath));
			data = new byte[in.available()];
			in.read(data);
			in.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组进行Base64编码，得到Base64编码的字符串
		return Base64.encodeBase64String(data);
	}

}
