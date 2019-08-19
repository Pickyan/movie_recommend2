package com.yzy.douban;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

//进行矩阵相乘；使用第1步的数据（评分矩阵-较小），第3步的数据（共现矩阵）
public class MRStep04 {
	private static List<String> scoreList = new ArrayList<>();

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://master:9000");
		FileSystem fs = FileSystem.get(conf);

		// 读取评分矩阵的数据，存储到List中
		Path src2 = new Path("/douban/step_01/part-r-00000");
		FSDataInputStream dis = fs.open(src2);
		String s = null;
		while ((s = dis.readLine()) != null) {
			scoreList.add(s);
		}
		System.out.println("--scoreList.size()=" + scoreList.size());

		Job job = Job.getInstance(conf);
		job.setJarByClass(MRStep04.class);
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		Path src = new Path("/douban/step_03");
		Path dst = new Path("/douban/step_04");
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

	public static class MyMapper extends Mapper<Object, Text, Text, Text> {
		private Text outKey = new Text();
		private Text outValue = new Text();

		@Override
		protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			// 10047547 10453730:2;10453731:2;10455078:1;10461676:1;10462489:1
			String[] a = value.toString().split("\\s+");
			String[] b = a[1].split(";");

			for (int j = 0; j < scoreList.size(); j++) { // 遍历评分矩阵-多个用户
				int sum = 0; //记录每个用户对1部电影的可能的评分
				// 100059827 19957083:2;25944714:1;22346861:1;25881669:2;24696982:3
				String[] c = scoreList.get(j).split("\\s+");
				Map<String, Integer> map = new HashMap<>(); // 存储当前用户评分过的所有电影id和分数
				String[] d = c[1].split(";");
				for (int k = 0; k < d.length; k++) {
					String m_key = d[k].substring(0, d[k].indexOf(":"));
					String m_value = d[k].substring(d[k].indexOf(":") + 1);
					map.put(m_key, Integer.parseInt(m_value));
				}
				Set<String> set = map.keySet(); // 所有的电影id

				for (int i = 0; i < b.length; i++) {
					String m_id = b[i].substring(0, b[i].indexOf(":")); // 取得共现矩阵中的电影id
					int count = Integer.parseInt(b[i].substring(b[i].indexOf(":") + 1));// 取得共现矩阵中的该电影的共现次数
					if (set.contains(m_id)) {// 如果当前用户对共现的电影评分过，则进行相乘并求和
						sum += count * map.get(m_id);
					}

				}

				outKey.set(c[0]); // 设置用户id为key
				outValue.set( a[0]+":"+sum ); 
				context.write(outKey, outValue); //用户id	电影id:分数
			}
		}
	}

	public static class MyReducer extends Reducer<Text, Text, Text, Text> {
		private Text outKey = new Text();
		private Text outValue = new Text();

		@Override
		protected void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			List<MyMovie> list = new ArrayList<>();
			for (Text t : values) {
				String[] a = t.toString().split(":");
				MyMovie m = new MyMovie(a[0], Integer.parseInt(a[1]));
				list.add(m); //电影id:分数
			}
			Collections.sort(list);
			
			//取前10部电影及评分输出
			int k = 10;
			if( list.size()<k ) {
				k = list.size();
			}
			StringBuffer s = new StringBuffer();
			for(int i=0; i<k; i++) {
				s.append( list.get(i).getName() ).append(":").append(list.get(i).getScore()).append(";");
			}
			String s2 = s.toString();
			s2 = s2.substring(0, s2.length()-1);
			outValue.set(s2);
			context.write(key, outValue); 
		}
	}
	
	public static class MyMovie implements Comparable<MyMovie>{
		private String name;
		private int score;
		
		public MyMovie() {}
		public MyMovie(String name, int score) {
			this.name = name;
			this.score = score;
		}
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getScore() {
			return score;
		}
		public void setScore(int score) {
			this.score = score;
		}
		@Override
		public int compareTo(MyMovie o) {
			return o.score - score;
		}
	}
}
