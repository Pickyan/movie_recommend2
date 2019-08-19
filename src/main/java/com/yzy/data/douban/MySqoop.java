package com.yzy.douban;

import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;

import com.cloudera.sqoop.Sqoop;
import com.cloudera.sqoop.tool.ExportTool;
import com.cloudera.sqoop.tool.ImportTool;


public class MySqoop {
	
	public static void main(String[] args) {
		import_data();
//		export_data();
	}

	public static void import_data(){
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://master:9000");
//		conf.set("mapreduce.framework.name", "yarn");  
//        conf.set("yarn.resourcemanager.address", "master:8040"); 
//        conf.set("mapreduce.app-submission.cross-platform", "true");
        conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        conf.set("fs.file.impl",org.apache.hadoop.fs.LocalFileSystem.class.getName());
		ArrayList<String> list = new ArrayList<String>(); 
		
		list.add("--connect");
		list.add("jdbc:mysql://master:3306/douban");
		list.add("--table");
		list.add("rating");
		list.add("--username");
		list.add("root"); 
		list.add("--password");
		list.add("mysql"); 
		list.add("--target-dir");
		list.add("/douban/data");
		list.add("-m");
		list.add("1");
		ImportTool importer= new ImportTool();
		Sqoop sqoop = new Sqoop(importer);
		sqoop.setConf(conf);
		String[] arg = new String[1];
		arg = list.toArray(new String[0]);
		int result = Sqoop.runSqoop(sqoop, arg);
		
	}
	public static void export_data(){
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://master:9000");
//		conf.set("mapreduce.framework.name", "yarn");  
//        conf.set("yarn.resourcemanager.address", "master:8040");  
        conf.set("mapreduce.app-submission.cross-platform", "true");
        conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        conf.set("fs.file.impl",org.apache.hadoop.fs.LocalFileSystem.class.getName());
		ArrayList<String> list = new ArrayList<String>(); 
		
		list.add("--connect");
		list.add("jdbc:mysql://master:3306/test");
		list.add("--table");
		list.add("test3");
		list.add("--username");
		list.add("root"); 
		list.add("--password");
		list.add("mysql"); ;
		list.add("--fields-terminated-by");
		list.add(",");
		list.add("--export-dir");
		list.add("/sqoop/data4");
		
		ExportTool exporter = new ExportTool();
		Sqoop sqoop = new Sqoop(exporter);
		sqoop.setConf(conf);
		String[] arg = new String[1];
		arg = list.toArray(new String[0]);
		int result = Sqoop.runSqoop(sqoop, arg);
		
	}
}
