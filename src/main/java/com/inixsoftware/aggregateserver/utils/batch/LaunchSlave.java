package com.inixsoftware.aggregateserver.utils.batch;

/*
    Copyright 2015 Mahesh Khanwalkar

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

//SSH ENTRY-POINT

import com.inixsoftware.aggregateserver.server.Slave;
import com.inixsoftware.aggregateserver.utils.xml.ConfigParser;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.io.File;

public class LaunchSlave
{
    static Logger logger = Logger.getLogger(LaunchSlave.class);

    public static void main(String[] args)
    {
        BasicConfigurator.configure();
        logger.info("Launching Slave AggregateServer");

        logger.info("Parsing XML Configuration Files");
        ConfigParser parser = new ConfigParser();

        try
        {
            File serverXML = new File("conf/server.xml");
            parser.parse(serverXML);

            File hadoopXML = new File("conf/hadoop.xml");
            parser.parse(hadoopXML);

            File deployXML = new File("conf/deploy.xml");
            parser.parse(deployXML);

            //TODO make a slave.xml

            File slaveXML = new File("conf/slave.xml");
            parser.parse(slaveXML);
        }
        catch (Exception e)
        {
            logger.fatal(null, e);
        }

        Slave slave = new Slave();
        slave.start();
    }
}
