package com.yzy.douban;


import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

//根据第2步产生的数据，获取电影的共现矩阵步骤2
public class MRStep03 {
	
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://master:9000");
		FileSystem fs = FileSystem.get(conf);
		
		Job job = Job.getInstance(conf);
		job.setJarByClass(MRStep03.class);
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		Path src = new Path("/douban/step_02");
		Path dst = new Path("/douban/step_03");
		fs.delete(dst, true);
		FileInputFormat.setInputPaths(job, src);
		FileOutputFormat.setOutputPath(job, dst);
		
		job.setMapperClass(MyMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setReducerClass(MyReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		job.setPartitionerClass(MyPartitioner.class);
		job.setNumReduceTasks(4);
		
		job.waitForCompletion(true);
		System.out.println("--ok.");
	}
	
	public static class MyMapper extends Mapper<Object, Text, Text, Text>{
		private Text outKey = new Text();
		private Text outValue = new Text();
		
		@Override
		protected void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {
			//10047547,10047547	166
			String[] a = value.toString().split("\\s+");
			String[] b = a[0].split(",");
			outKey.set(b[0]);
			outValue.set(b[1]+":"+a[1]);
			context.write(outKey, outValue); //10047547		10047547:166
		}
	}
	
	public static class MyPartitioner extends Partitioner<Text, Text>{
		@Override
		public int getPartition(Text key, Text value, int numPartitions) {
			return (key.hashCode()&Integer.MAX_VALUE)%numPartitions;
		}
	}
	
	public static class MyReducer extends Reducer<Text, Text, Text, Text>{
		private Text outKey = new Text();
		private Text outValue = new Text();
		
		@Override
		protected void reduce(Text key, Iterable<Text> values, Context context) 
				throws IOException, InterruptedException {
			StringBuffer s = new StringBuffer();
			for(Text t : values) {
				s.append(t.toString()).append(";");
			}
			String s2 = s.toString();
			s2 = s2.substring(0, s2.length()-1);
			outValue.set(s2);
			context.write(key, outValue);  //10047547	10047547:166;10355625:3;10355645:1
		}
	}
}
