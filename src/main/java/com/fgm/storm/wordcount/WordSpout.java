package com.fgm.storm.wordcount;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.Map;
import java.util.Random;

/**
 * 主要用于将我们的数据发送出去
 *继承了BaseRichSpout,表示该类是一个spout
 * @Auther: fgm
 * @Date:
 */
public class WordSpout extends BaseRichSpout {

    private SpoutOutputCollector collector;

    /**
     * 通过数组存储一些数据,然后随机获取数据,模拟数据发送
     */
    private String[] lines;

    private Random random;

    /**
     * 初始化的方法,进行准备工作,只在启动的时候调用一次
     * @param conf 相关配置信息
     * @param context 上下文,承上启下
     * @param collector 主要用于我们的nextTuple方法当中,通过这个对象往下游发送数据
     */
    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {

        this.collector = collector;
        lines = new String[]{"hello world", "hadoop storm", "spout bolt"};
        random = new Random();

    }

    /**
     * 该方法会不断的运行生产数据,往下游发送
     */
    @Override
    public void nextTuple() {
        //通过随机数随机获取数组中的数据
        int i = random.nextInt(lines.length);
        String line = lines[i];
        //通过collector调用emit方法,将我们的数据向下游发送
        try {
            Thread.sleep(500);
            collector.emit(new Values(line));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * 为我们往下游发送的数据声明一个变量值,下游可以通过这个方法获取我们上游发送过来的数据
     * @param declarer
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        //调用declare来声明我们所有发出去的字符串的一个变量,下游可以通过这个变量进行取值.
        declarer.declare(new Fields("lines"));
    }
}
