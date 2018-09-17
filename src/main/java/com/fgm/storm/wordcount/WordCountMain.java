package com.fgm.storm.wordcount;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.topology.TopologyBuilder;

/**
 *
 * @Auther: fgm
 * @Date:
 */
public class WordCountMain {

    public static void main(String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        TopologyBuilder builder = new TopologyBuilder();
        //参数中parallelism hint 为设定的线程数.
        builder.setSpout("wordSpout", new WordSpout(), 2);

        /**
         * storm共有8中分组策略
         * globalGrouping 全局分组:tuple被发送给bolt的同一个并且最小task_id的任务处理,实现事务性的topology,不会存在线程安全问题
         * shuffleGrouping 随机分组:将tuple随机分配到bolt中,能够保证各task中处理的数据均衡
         * fieldsGrouping 按字段分组:根据设定的字段相同值得tuple被分配到同一个bolt进行处理
         */
        builder.setBolt("splitBolt", new SpiltBolt(), 3).shuffleGrouping("wordSpout");
        builder.setBolt("countBolt", new CountBolt(), 4).shuffleGrouping("splitBolt");

        Config config = new Config();

        //设置进程数
        config.setNumWorkers(3);

        if (null != args && args.length > 0) {
            //集群模式
            config.setDebug(false);
            StormTopology topology = builder.createTopology();
            StormSubmitter.submitTopology(args[0], config, topology);

        } else {
            //本地运行模式
            LocalCluster localCluster = new LocalCluster();
            localCluster.submitTopology("localStorm", config, builder.createTopology());

        }

    }
}
