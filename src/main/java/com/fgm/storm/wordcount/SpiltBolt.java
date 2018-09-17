package com.fgm.storm.wordcount;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

/**
 *这个bolt主要用于接收上游发送的数据,然后对其切分,并继续向下游发送
 * bolt需继承baseBasicBolt
 * @Auther: fgm
 * @Date:
 */
public class SpiltBolt extends BaseBasicBolt {

    /**
     * execute方法会反复执行
     * @param input 上游发送的数据都封装在Tuple对象里面
     * @param collector
     */
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        //通过input对象,获取上游发送的数据
        String lines = input.getStringByField("lines");
        //通过" " 对接收到的数据进行切分
        String[] split = lines.split(" ");
        for (String word : split) {
            //将切分后的数据往下游进行发送,同时发送计数
            collector.emit(new Values(word, 1));
        }
    }

    /**
     * 给下游发送的数据声明一个变量,下游可以通过这个变量获取数据
     * @param declarer
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word", "nums"));
    }
}
