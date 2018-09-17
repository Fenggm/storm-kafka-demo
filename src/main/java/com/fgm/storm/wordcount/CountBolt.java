package com.fgm.storm.wordcount;

import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @Auther: fgm
 * @Date:
 */
public class CountBolt extends BaseBasicBolt {
    /**
     * 初始化Map,static 静态化,可以避免线程安全问题.
     */
    private static Map<String, Integer> map = new HashMap<String, Integer>();

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
    }

    /**
     * 反复被调用的方法
     * @param input
     * @param collector
     */
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        //获取上游发送的数据
        String word = input.getStringByField("word");
        Integer nums = input.getIntegerByField("nums");
        //使用map将我们的数据存储起来
        if (map.containsKey(word)) {
            map.put(word, map.get(word) + nums);
        } else {
            map.put(word, nums);
        }
        System.out.println(map.toString());

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
