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
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

//根据第一步产生的数据，获取电影的共现矩阵步骤1
public class MRStep02 {
	
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://master:9000");
		FileSystem fs = FileSystem.get(conf);
		
		Job job = Job.getInstance(conf);
		job.setJarByClass(MRStep02.class);
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		Path src = new Path("/douban/step_01");
		Path dst = new Path("/douban/step_02");
		fs.delete(dst, true);
		FileInputFormat.setInputPaths(job, src);
		FileOutputFormat.setOutputPath(job, dst);
		
		job.setMapperClass(MyMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		job.setReducerClass(MyReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		job.waitForCompletion(true);
		System.out.println("--ok.");
	}
	
	public static class MyMapper extends Mapper<Object, Text, Text, IntWritable>{
		private Text outKey = new Text();
		private IntWritable outValue = new IntWritable(1);
		
		@Override
		protected void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {
			//100059827	19957083:2;25944714:1;22346861:1;25881669:2;24696982:3;1296344:4;26297388:3;1300616:3
			String[] a = value.toString().split("\\s+");
			String[] b = a[1].split(";");
			for(int i=0; i<b.length; i++) {
				String m_id_i = b[i].substring(0, b[i].indexOf(":"));  // b[i]=19957083:2
				for(int j=0; j<b.length; j++) {
					String m_id_j = b[j].substring(0, b[j].indexOf(":")); 
					outKey.set(m_id_i + "," + m_id_j);
					context.write(outKey, outValue);  //25881669,24696982	1
				}
			}
		}
	}
	
	public static class MyReducer extends Reducer<Text, IntWritable, Text, IntWritable>{
		private Text outKey = new Text();
		private IntWritable outValue = new IntWritable();
		
		@Override
		protected void reduce(Text key, Iterable<IntWritable> values, Context context) 
				throws IOException, InterruptedException {
			int sum = 0;
			for(IntWritable i : values) {
				sum += 1;
			}
			outValue.set( sum );
			context.write(key, outValue);  //25881669,24696982	21
		}
	}
}
