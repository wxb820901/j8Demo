package com.b.cassandra;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.factory.HFactory;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangbil on 9/23/2016.
 */
public class HectorMain {

    public static void main(String args[]) throws IOException {


        Map<String, String > cred = new HashMap();
        cred.put("username","");
        cred.put("password","");
        CassandraHostConfigurator config = new CassandraHostConfigurator("<IP>:<PORT>[,<IP>:<PORT>]");
        config.setMaxActive(2000);

        config.setClockResolution("MICROSECONDS");
        Cluster cluster = HFactory.getOrCreateCluster(
                "<NAME>",
                config,
                cred
        );
        if(cluster == null){
            System.out.println("cluster is null");
        }
        Keyspace keyspace = HFactory.createKeyspace("share_token1", cluster);

        ColumnFamilyTemplate<String, String> template =
                new ThriftColumnFamilyTemplate<String, String>(
                        keyspace,
                        "<Column Family Name>",
                        StringSerializer.get(),
                        StringSerializer.get());

        List<String> keys = new ArrayList();
        String logFileName = "\\redisToken.log";
        String logFileContent = FileUtils.readFileToString(new File(logFileName));
        String[] lines = logFileContent.split("\\n");
        int count = 0;
        for(String line : lines){
            System.out.println(line + ":{");
            ColumnFamilyResult<String, String> res = template.queryColumns(line);
            for (String attribute : res.getColumnNames()) {
                System.out.print(attribute+":"+res.getString(attribute)+";   ");
            }
            System.out.println("}");
            count++;
        }
        System.out.println("total = "+count);

    }


    //get key from line of log file
    public String getKey(String line){
        String[] sublines = line.split("tokenId");
        String[] sublines2 = line.split("location");
        String location = "";
        String tokeID = "";
        if(sublines.length==2){
            tokeID = sublines[1].substring(1, sublines[1].indexOf(","));
        }
        if(sublines2.length==2){
            location = sublines2[1].substring(1, sublines2[1].indexOf(","));
        }
        if(!"".equals(location) && !"".equals(tokeID)){
            return location+"/"+tokeID;
        }
        return null;
    }

}
