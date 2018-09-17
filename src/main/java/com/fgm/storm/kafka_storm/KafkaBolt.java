package com.fgm.storm.kafka_storm;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

/**
 *
 * @Auther: fgm
 * @Date:
 */
public class KafkaBolt extends BaseBasicBolt {
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        System.out.println(input.toString());
        //通过分析input数据,确定我们需要的数据
        String string = input.getString(4);
        System.out.println(string);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
