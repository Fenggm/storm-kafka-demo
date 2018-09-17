# storm-kafka-demo

## 本案例采用的为Apache-storm-1.1.1.

可在本地运行,也可package后在集群环境中运行.
若需要在集群环境中运行,为避免依赖的jar包冲突,在package前,需将pom.xml文件中的 <scope>provided</scope>的注释放开.

该项目包含storm的入门案例wordcount,以及和kafka的整合案例.

## Storm当中的分组策略，一共有八种：

所谓的grouping策略就是在Spout与Bolt、Bolt与Bolt之间传递Tuple的方式。总共有八种方式：

 1）shuffleGrouping（随机分组）随机分组；将tuple随机分配到bolt中，能够保证各task中处理的数据均衡；
 2）fieldsGrouping（按照字段分组，在这里即是同一个单词只能发送给一个Bolt）按字段分组； 根据设定的字段相同值得tuple被分配到同一个bolt进行处理；
举例：builder.setBolt("mybolt", new MyStoreBolt(),5).fieldsGrouping("checkBolt",new Fields("uid"));
说明：该bolt由5个任务task执行，相同uid的元组tuple被分配到同一个task进行处理；该task接收的元祖字段是mybolt发射出的字段信息，不受uid分组的影响。
    该分组不仅方便统计而且还可以通过该方式保证相同uid的数据保存不重复（uid信息写入数据库中唯一）；

 3）allGrouping（广播发送，即每一个Tuple，每一个Bolt都会收到）广播发送：所有bolt都可以收到该tuple
 4）globalGrouping（全局分组，将Tuple分配到task id值最低的task里面）全局分组：tuple被发送给bolt的同一个并且最小task_id的任务处理，实现事务性的topology
 5）noneGrouping（随机分派）不分组：效果等同于shuffle Grouping.
 6）directGrouping（直接分组，指定Tuple与Bolt的对应发送关系）
直接分组：由tuple的发射单元直接决定tuple将发射给那个bolt，一般情况下是由接收tuple的bolt决定接收哪个bolt发射的Tuple。这是一种比较特别的分组方法，用这种分组意味着消息的发送者指定由消息接收者的哪个task处理这个消息。 只有被声明为Direct Stream的消息流可以声明这种分组方法。而且这种消息tuple必须使用emitDirect方法来发射。消息处理者可以通过TopologyContext来获取处理它的消息的taskid (OutputCollector.emit方法也会返回taskid)。
 7）Local or shuffle Grouping本地或者随机分组，优先将数据发送到本机的处理器executor，如果本机没有对应的处理器，那么再发送给其他机器的executor，避免了网络资源的拷贝，减轻网络传输的压力
 8）customGrouping （自定义的Grouping）

