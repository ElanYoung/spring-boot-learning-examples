package com.starimmortal.minio.util;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * MinIO工具类
 *
 * @author william@StarImmortal
 * @date 2022/04/19
 */
@Slf4j
@Component
public class MinioUtil {

	private static MinioClient minioClient;

	/**
	 * setter注入
	 * @param minioClient 客户端
	 */
	@Autowired
	public void setMinioClient(MinioClient minioClient) {
		MinioUtil.minioClient = minioClient;
	}

	/**
	 * 启动SpringBoot容器的时候初始化Bucket，如果没有Bucket则创建
	 */
	public static void createBucket(String bucketName) throws Exception {
		if (!bucketExists(bucketName)) {
			minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
		}
	}

	/**
	 * 判断Bucket是否存在
	 * @return true：存在，false：不存在
	 */
	public static boolean bucketExists(String bucketName) throws Exception {
		return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
	}

	/**
	 * 获得Bucket策略
	 * @param bucketName 存储桶名称
	 * @return Bucket策略
	 */
	public static String getBucketPolicy(String bucketName) throws Exception {
		return minioClient.getBucketPolicy(GetBucketPolicyArgs.builder().bucket(bucketName).build());
	}

	/**
	 * 获得所有Bucket列表
	 * @return Bucket列表
	 */
	public static List<Bucket> getAllBuckets(MinioClient minioClient) throws Exception {
		return minioClient.listBuckets();
	}

	/**
	 * 根据存储桶名称获取其相关信息
	 * @param bucketName 存储桶名称
	 * @return 相关信息
	 */
	public static Optional<Bucket> getBucket(String bucketName) throws Exception {
		return getAllBuckets(minioClient).stream().filter(b -> b.name().equals(bucketName)).findFirst();
	}

	/**
	 * 根据存储桶名称删除Bucket，true：删除成功；false：删除失败，文件或已不存在
	 * @param bucketName 存储桶名称
	 */
	public static void removeBucket(String bucketName) throws Exception {
		minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
	}

	/**
	 * 判断文件是否存在
	 * @param bucketName 存储桶名称
	 * @param objectName 文件名
	 * @return true：存在；false：不存在
	 */
	public static boolean isObjectExist(String bucketName, String objectName) {
		boolean exist = true;
		try {
			minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
		}
		catch (Exception e) {
			exist = false;
		}
		return exist;
	}

	/**
	 * 判断文件夹是否存在
	 * @param bucketName 存储桶名称
	 * @param objectName 文件夹名称
	 * @return true：存在；false：不存在
	 */
	public static boolean isFolderExist(String bucketName, String objectName) {
		boolean exist = false;
		try {
			ListObjectsArgs listObjectsArgs = ListObjectsArgs.builder()
				.bucket(bucketName)
				.prefix(objectName)
				.recursive(false)
				.build();
			Iterable<Result<Item>> results = minioClient.listObjects(listObjectsArgs);
			for (Result<Item> result : results) {
				Item item = result.get();
				if (item.isDir() && objectName.equals(item.objectName())) {
					exist = true;
				}
			}
		}
		catch (Exception e) {
			exist = false;
		}
		return exist;
	}

	/**
	 * 根据文件前缀查询文件
	 * @param bucketName 存储桶名称
	 * @param prefix 前缀
	 * @param recursive 是否使用递归查询
	 * @return MinioItem列表
	 */
	public static List<Item> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive)
			throws Exception {
		List<Item> list = new ArrayList<>();
		ListObjectsArgs listObjectsArgs = ListObjectsArgs.builder()
			.bucket(bucketName)
			.prefix(prefix)
			.recursive(recursive)
			.build();
		Iterable<Result<Item>> objectsIterator = minioClient.listObjects(listObjectsArgs);
		if (objectsIterator != null) {
			for (Result<Item> o : objectsIterator) {
				Item item = o.get();
				list.add(item);
			}
		}
		return list;
	}

	/**
	 * 获取文件流
	 * @param bucketName 存储桶名称
	 * @param objectName 文件名
	 * @return 二进制流
	 */
	public static InputStream getObject(String bucketName, String objectName) throws Exception {
		GetObjectArgs getObjectArgs = GetObjectArgs.builder().bucket(bucketName).object(objectName).build();
		return minioClient.getObject(getObjectArgs);
	}

	/**
	 * 断点下载
	 * @param bucketName 存储桶名称
	 * @param objectName 文件名称
	 * @param offset 起始字节的位置
	 * @param length 要读取的长度
	 * @return 二进制流
	 */
	public InputStream getObject(String bucketName, String objectName, long offset, long length) throws Exception {
		GetObjectArgs getObjectArgs = GetObjectArgs.builder()
			.bucket(bucketName)
			.object(objectName)
			.offset(offset)
			.length(length)
			.build();
		return minioClient.getObject(getObjectArgs);
	}

	/**
	 * 获取路径下文件列表
	 * @param bucketName 存储桶名称
	 * @param prefix 文件名称
	 * @param recursive 是否递归查找，false：模拟文件夹结构查找
	 * @return 二进制流
	 */
	public static Iterable<Result<Item>> listObjects(String bucketName, String prefix, boolean recursive) {
		ListObjectsArgs listObjectsArgs = ListObjectsArgs.builder()
			.bucket(bucketName)
			.prefix(prefix)
			.recursive(recursive)
			.build();
		return minioClient.listObjects(listObjectsArgs);
	}

	/**
	 * 使用MultipartFile进行文件上传
	 * @param bucketName 存储桶名称
	 * @param file 文件名
	 * @param objectName 对象名
	 * @return ObjectWriteResponse对象
	 */
	public static ObjectWriteResponse uploadFile(String bucketName, MultipartFile file, String objectName)
			throws Exception {
		InputStream inputStream = file.getInputStream();
		Optional<MediaType> optional = MediaTypeFactory.getMediaType(objectName);
		String mediaType = optional.orElseThrow(() -> new RuntimeException("文件类型暂不支持")).toString();
		PutObjectArgs putObjectArgs = PutObjectArgs.builder()
			.bucket(bucketName)
			.object(objectName)
			.contentType(mediaType)
			.stream(inputStream, inputStream.available(), -1)
			.build();
		return minioClient.putObject(putObjectArgs);
	}

	/**
	 * 上传本地文件
	 * @param bucketName 存储桶名称
	 * @param objectName 对象名称
	 * @param fileName 本地文件路径
	 */
	public static ObjectWriteResponse uploadFile(String bucketName, String objectName, String fileName)
			throws Exception {
		UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
			.bucket(bucketName)
			.object(objectName)
			.filename(fileName)
			.build();
		return minioClient.uploadObject(uploadObjectArgs);
	}

	/**
	 * 通过流上传文件
	 * @param bucketName 存储桶名称
	 * @param objectName 文件对象
	 * @param inputStream 文件流
	 */
	public static ObjectWriteResponse uploadFile(String bucketName, String objectName, InputStream inputStream)
			throws Exception {
		PutObjectArgs putObjectArgs = PutObjectArgs.builder()
			.bucket(bucketName)
			.object(objectName)
			.stream(inputStream, inputStream.available(), -1)
			.build();
		return minioClient.putObject(putObjectArgs);
	}

	/**
	 * 创建文件夹或目录
	 * @param bucketName 存储桶名称
	 * @param objectName 目录路径
	 */
	public static ObjectWriteResponse createDir(String bucketName, String objectName) throws Exception {
		PutObjectArgs putObjectArgs = PutObjectArgs.builder()
			.bucket(bucketName)
			.object(objectName)
			.stream(new ByteArrayInputStream(new byte[] {}), 0, -1)
			.build();
		return minioClient.putObject(putObjectArgs);
	}

	/**
	 * 获取文件信息, 如果抛出异常则说明文件不存在
	 * @param bucketName 存储桶名称
	 * @param objectName 文件名称
	 */
	public static String getFileStatusInfo(String bucketName, String objectName) throws Exception {
		StatObjectArgs statObjectArgs = StatObjectArgs.builder().bucket(bucketName).object(objectName).build();
		return minioClient.statObject(statObjectArgs).toString();
	}

	/**
	 * 拷贝文件
	 * @param bucketName 存储桶名称
	 * @param objectName 文件名
	 * @param srcBucketName 目标存储桶
	 * @param srcObjectName 目标文件名
	 */
	public static ObjectWriteResponse copyFile(String bucketName, String objectName, String srcBucketName,
			String srcObjectName) throws Exception {
		return minioClient.copyObject(CopyObjectArgs.builder()
			.source(CopySource.builder().bucket(bucketName).object(objectName).build())
			.bucket(srcBucketName)
			.object(srcObjectName)
			.build());
	}

	/**
	 * 删除文件
	 * @param bucketName 存储桶名称
	 * @param objectName 文件名称
	 */
	public static void removeFile(String bucketName, String objectName) throws Exception {
		RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build();
		minioClient.removeObject(removeObjectArgs);
	}

	/**
	 * 批量删除文件
	 * @param bucketName 存储桶名称
	 * @param keys 需要删除的文件列表
	 */
	public static void removeFiles(String bucketName, List<String> keys) {
		keys.forEach(key -> {
			try {
				removeFile(bucketName, key);
			}
			catch (Exception e) {
				log.error("批量删除失败！error:{0}", e);
			}
		});
	}

	/**
	 * 获取文件外链
	 * @param bucketName 存储桶名称
	 * @param objectName 文件名
	 * @param expires 过期时间 <=7 秒 （外链有效时间（单位：秒））
	 * @return 文件外链
	 */
	public static String getPreSignedObjectUrl(String bucketName, String objectName, Integer expires) throws Exception {
		GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
			.expiry(expires)
			.bucket(bucketName)
			.object(objectName)
			.build();
		return minioClient.getPresignedObjectUrl(args);
	}

	/**
	 * 获得文件外链
	 * @param bucketName 存储桶名称
	 * @param objectName 文件名
	 * @return 文件外链
	 */
	public static String getPreSignedObjectUrl(String bucketName, String objectName) throws Exception {
		GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
			.bucket(bucketName)
			.object(objectName)
			.method(Method.GET)
			.build();
		return minioClient.getPresignedObjectUrl(args);
	}

	/**
	 * 将URLDecoder编码转成UTF8
	 * @param str 字符串
	 * @return 编码
	 */
	public static String getUtf8ByDecoder(String str) throws UnsupportedEncodingException {
		String url = str.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
		return URLDecoder.decode(url, "UTF-8");
	}

}
