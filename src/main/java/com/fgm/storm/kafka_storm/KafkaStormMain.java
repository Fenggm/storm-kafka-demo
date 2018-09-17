package com.fgm.storm.kafka_storm;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;

import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.topology.TopologyBuilder;

/**
 *
 * @Auther: fgm
 * @Date:
 */
public class KafkaStormMain {
    public static void main(String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {

        /**
         * 需要跟两个参数:
         * 1.连接我们kafka的地址
         * 2.我们需要获取数据的topic(topic需提前在kafka集群上创建)
         */
        KafkaSpoutConfig.Builder<String, String> kafkaSpoutConfigBuiler = KafkaSpoutConfig.builder("node01:9092,node02:9092,node03:9092", "test");
        //设置我们的消费组
        kafkaSpoutConfigBuiler.setGroupId("kafkaStormGroup");
        //设置我们从哪一条数据进行开始消费,这里选择UNCOMMITTED_LATEST 从最后一个未提交的开始消费
        kafkaSpoutConfigBuiler.setFirstPollOffsetStrategy(KafkaSpoutConfig.FirstPollOffsetStrategy.UNCOMMITTED_LATEST);
        //调用build方法得到我们的kafkaSpoutConfig
        KafkaSpoutConfig<String, String> kafkaSpoutConfig = kafkaSpoutConfigBuiler.build();
        //通过构造器得到我们的kafkaSpout
        KafkaSpout<String, String> kafkaSpout = new KafkaSpout<String, String>(kafkaSpoutConfig);

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("kafkaSpout", kafkaSpout);
        builder.setBolt("kafkaBolt", new KafkaBolt()).localOrShuffleGrouping("kafkaSpout");

        Config config = new Config();
        if (null != args && args.length > 0) {
            StormSubmitter.submitTopology(args[0], config, builder.createTopology());

        } else {
            LocalCluster localCluster = new LocalCluster();
            localCluster.submitTopology("kafkaToStorm", config, builder.createTopology());
        }

    }
}
