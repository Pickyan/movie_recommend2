package com.yzy.douban2;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

//根据电影的评分矩阵，计算相似度
public class MRStep02 {
	private static List<String> scoreList = new ArrayList<>();
	
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://master:9000");
		FileSystem fs = FileSystem.get(conf);
		
		//先获取数据，存储在List中
		Path src2 = new Path("/douban2/step_01/part-r-00000");
		FSDataInputStream dis = fs.open(src2);
		String s = null;
		while((s=dis.readLine())!=null) {
			scoreList.add(s);
		}
		
		Job job = Job.getInstance(conf);
		job.setJarByClass(MRStep02.class);
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		Path src = new Path("/douban2/step_01");
		Path dst = new Path("/douban2/step_02");
		fs.delete(dst, true);
		FileInputFormat.setInputPaths(job, src);
		FileOutputFormat.setOutputPath(job, dst);
		
		job.setMapperClass(MyMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setReducerClass(MyReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		job.waitForCompletion(true);
		System.out.println("--ok.");
	}
	
	public static class MyMapper extends Mapper<Object, Text, Text, Text>{
		private Text outKey = new Text();
		private Text outValue = new Text();
		
		@Override
		protected void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {
			//10047547	zona31:2;88393514:4;50584837:3;45551165:3
			String[] a = value.toString().split("\\s+");
			String[] b = a[1].split(";");
			Map<String, Integer> map_01 = new HashMap<>(); //存储map()方法中读取的每行内容的 用户及评分
			Set<String> set = new HashSet<>();//将两部电影的所有用户合并到一个set里
			for(int i=0; i<b.length; i++) {
				String u_id = b[i].substring(0, b[i].indexOf(":"));
				int score = Integer.parseInt(b[i].substring(b[i].indexOf(":")+1));
				map_01.put(u_id, score);
				set.add(u_id);
			}
			
			for(int i=0; i<scoreList.size(); i++) {
				Set<String> set2 = new HashSet<>(set);//将两部电影的所有用户合并到一个set里
				//10047542	zona:2;88393514:4;50584838:3;45551165:3
				String[] c = scoreList.get(i).toString().split("\\s+");
				if( a[0].equals(c[0]) ) {//如果是同一部电影，就不需要计算距离了
					continue;
				}
				String[] d = c[1].split(";");
				Map<String, Integer> map_02 = new HashMap<>(); //存储main()方法中读取的每行内容的 用户及评分
				for(int j=0; j<d.length; j++) {
					String u_id = d[j].substring(0, d[j].indexOf(":"));
					int score = Integer.parseInt(d[j].substring(d[j].indexOf(":")+1));
					map_02.put(u_id, score);
					set2.add(u_id);
				}
				double sum = 0;
				for(String s : set2) {//遍历所有的用户
					Integer score_01 = map_01.get(s);//取得评分
					int score_011 = score_01==null?0:score_01;//可能没有该用户的评分，则记为0分
					Integer score_02 = map_02.get(s);
					int score_022 = score_02==null?0:score_02;
					sum+=Math.pow((score_011-score_022), 2);//计算差的平方和
				}
				sum = 1/(Math.sqrt(sum)+1);//距离
				sum = Math.round(sum*1000000)/10000.0;//保留小数点后4位
				outKey.set(a[0]);
				outValue.set(c[0]+":"+sum);
				context.write(outKey, outValue); 
			}
		}
	}
	
	public static class MyReducer extends Reducer<Text, Text, Text, Text>{
		private Text outKey = new Text();
		private Text outValue = new Text();
		
		@Override
		protected void reduce(Text key, Iterable<Text> values, Context context) 
				throws IOException, InterruptedException {
			List<MyMovie> list = new ArrayList<>();
			for(Text t : values) {
				String[] a = t.toString().split(":");
				MyMovie m = new MyMovie(a[0], Double.parseDouble(a[1]));
				list.add(m);
			}
			Collections.sort(list);
			
			int k = 10;
			if(list.size()<k) {
				k = list.size();
			}
			StringBuffer s = new StringBuffer();
			for(int i=0; i<k; i++) {
				s.append(list.get(i).getName())
//					.append(":")
//					.append(list.get(i).getScore())  //最后结果不需要分数
					.append(";");
			}
			String s2 = s.toString();
			s2 = s2.substring(0, s2.length()-1);
			outValue.set( s2);
			context.write(key, outValue); 
		}
	}

	public static class MyMovie implements WritableComparable<MyMovie>{
		private String name;
		private Double score;
		
		public MyMovie() {}
		public MyMovie(String name, double score) {
			this.name = name;
			this.score = score;
		}
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Double getScore() {
			return score;
		}
		public void setScore(Double score) {
			this.score = score;
		}
		@Override
		public int compareTo(MyMovie o) {
			return o.score.compareTo(score);
		}
		@Override
		public void write(DataOutput out) throws IOException {
			out.writeUTF(name);
			out.writeDouble(score);
		}
		@Override
		public void readFields(DataInput in) throws IOException {
			name = in.readUTF();
			score = in.readDouble();
		}
		@Override
		public String toString() {
			return  name + ", " + score;
		}
		
	}
}
