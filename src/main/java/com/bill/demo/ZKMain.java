package com.b.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangbil on 9/22/2016.
 */
public class ZKMain {
    public static void main(String args[]) throws KeeperException, InterruptedException, IOException {


        String host = "<IP>:<PORT>";
        // 创建一个与服务器的连接
        ZooKeeper zk = new ZooKeeper(host, 1000,
                new Watcher() {
                    // 监控所有被触发的事件
                    public void process(WatchedEvent event) {
//                        System.out.println("已经触发了" + event.getType() + "事件！");
                    }
                });
        fixOne(zk, new ArrayList(), "/", "");

//        // 创建一个目录节点
//        zk.create("/testRootPath", "testRootData".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
//                CreateMode.PERSISTENT);
//        // 创建一个子目录节点
//        zk.create("/testRootPath/testChildPathOne", "testChildDataOne".getBytes(),
//                ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
//        System.out.println(new String(zk.getData("/testRootPath",false,null)));
//        // 取出子目录节点列表
//        System.out.println(zk.getChildren("/testRootPath",true));
//        // 修改子目录节点数据
//        zk.setData("/testRootPath/testChildPathOne","modifyChildDataOne".getBytes(),-1);
//        System.out.println("目录节点状态：["+zk.exists("/testRootPath",true)+"]");
//        // 创建另外一个子目录节点
//        zk.create("/testRootPath/testChildPathTwo", "testChildDataTwo".getBytes(),
//                ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
//        System.out.println(new String(zk.getData("/testRootPath/testChildPathTwo",true,null)));
//        // 删除子目录节点
//        zk.delete("/testRootPath/testChildPathTwo",-1);
//        zk.delete("/testRootPath/testChildPathOne",-1);
//        // 删除父目录节点
//        zk.delete("/testRootPath",-1);
        // 关闭连接
        zk.close();
    }

    public static void fixOne(ZooKeeper zk, List ls, String parent, String level) throws InterruptedException {

        System.out.println(parent);
        try {
            if (zk.getChildren(parent, true).isEmpty()) {

            } else {
                List<String> sub = zk.getChildren(parent, true);
                for (String subName : sub) {
                    if(parent.equals("/")) {
                        fixOne(zk, ls, parent + subName, level);
                    }else{
                        fixOne(zk, ls, parent + "/" + subName, level);
                    }
                }
            }
        } catch (KeeperException e) {
            return;
        }
    }
}
