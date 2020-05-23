package com.horsi.io.bio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringReader;

/**
 *  同步阻塞式IO
 *
 *  通过阻塞IO对txt文件进行读写
 *  可通过缓冲区（BufferedReader等）加缓冲数组（char[] 等）进行优化
 *
 * @author daijs
 * @since 2020/5/15 14:31
 */
public class BioDemo {

	/**
	 * 缓冲区+缓冲数组读取文件
	 * @param filePath 文件路径
	 */
	public static void readFromFile(String filePath) throws Exception {

		File file = new File(filePath);
		if(!file.exists()) {
			return;
		}
		//1024个字符缓冲数组，缓冲读
		StringBuilder builder = new StringBuilder();
		char[] data =new char[1024];
		//try-with-resource自动关闭流，无需手动关闭
		try(FileReader fileReader = new FileReader(file); BufferedReader bufferedReader = new BufferedReader(fileReader)) {
			//读取重读，是防止写入后面的空字符串
			int len;
			while ((len = bufferedReader.read(data)) != -1) {
				builder.append(data,0,len);
			}
		}
		System.out.println("读取到的文件内容是："+builder.toString());
	}

	/**
	 *  缓冲区+缓冲数组写入文件
	 * @param filePath 文件路径
	 */
	public static void writeToFile(String filePath) throws Exception {

		File file = new File(filePath);
		if(!file.exists()) {
			return;
		}
		String content = "你好啊，测试成功了";
		char[] data =new char[1024];
		//try-with-resource自动关闭流，无需手动关闭
		try(FileWriter fileWriter = new FileWriter(file); BufferedReader bufferedReader = new BufferedReader(new StringReader(content)); BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
			int len;
			while ((len = bufferedReader.read(data)) != -1) {
				bufferedWriter.write(data,0,len);
			}
			//刷新磁盘
			fileWriter.flush();
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
