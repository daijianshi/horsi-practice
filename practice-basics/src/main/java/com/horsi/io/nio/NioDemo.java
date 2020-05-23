package com.horsi.io.nio;

import com.horsi.io.bio.BioDemo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 *  异步阻塞读取文件)（正常情况下，不需要用Nio读取小文件）
 *  nio采用channel（管道）读取数据
 *  也就是所有数据都在管道的buffer里面，通过channel读取数据到buffer
 *  buffer中有几个主要标识  position（位置） limit（限制）capacity（容量，不在读写过程中改变）
 *  读模式下，将position赋给limit之后，设置为0，代表从0读取到limit位置，即原有写入的position位置
 *  写模式下，limit是最大容量capacity，写入多少，position往后移动多少
 * @author daijs
 * @since 2020/5/15 16:13
 */
public class NioDemo {

	/**
	 * Nio读取文件
	 * 1：获取channel，定义buffer
	 * 2：读取第一批数据，flip翻转至读模式，读取完成后，clear清空缓存，开启下一步read写入模式，循环至读取完
	 * @param filePath 文件路径
	 */
	public static void readFromFile(String filePath) throws Exception {

		File file = new File(filePath);
		if(!file.exists()) {
			return;
		}

		StringBuilder builder = new StringBuilder();
		try(FileInputStream fileInputStream = new FileInputStream(file); FileChannel fileChannel = fileInputStream.getChannel()) {
			ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
			//读取重读，是防止写入后面的空字符串
			while ( fileChannel.read(byteBuffer) != -1) {
				//首先翻转到读模式
				byteBuffer.flip();
				builder.append(new String(byteBuffer.array()));
				//清空缓冲区
				byteBuffer.clear();
			}
		}
		System.out.println("读取到的文件内容是："+builder.toString());
	}

	/**
	 * Nio写入文件
	 * 1：获取channel，定义buffer
	 * 2：clear清空缓存，写入第一批数据，flip翻转至读模式，channel进行write操作，循环
	 * @param filePath 文件路径
	 */
	public static void writeToFile(String filePath) throws Exception {
		File file = new File(filePath);
		if(!file.exists()) {
			return;
		}

		String content = "你好啊，测试成功了";
		//try-with-resource自动关闭流，无需手动关闭
		try(FileOutputStream fileOutputStream = new FileOutputStream(file); FileChannel fileOutputStreamChannel = fileOutputStream.getChannel()) {
			ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
			//写两遍
			for(int i=0;i<2;i++) {
				//清空缓存区
				writeBuffer.clear();
				//写入一批新数据
				writeBuffer.put(content.getBytes());
				//切换为读模式
				writeBuffer.flip();
				//此时channel的写入其实是对buffer的读取，所以才需要在读取前进行翻转成读模式
				fileOutputStreamChannel.write(writeBuffer);
			}
		}
		System.out.println("写入["+content+"]完成");
	}

	public static void main(String[] args) throws Exception {

		String filePath = BioDemo.class.getResource("/IoTest.txt").getPath();
		readFromFile(filePath);
		writeToFile(filePath);
		readFromFile(filePath);
	}

}
