package com.inixsoftware.aggregateserver.server;

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

import com.inixsoftware.aggregateserver.properties.ServerProperties;
import com.inixsoftware.aggregateserver.socket.ServerImpl;
import com.inixsoftware.aggregateserver.work.MasterRun;
import org.apache.log4j.Logger;

public class Master
{
    Logger logger = Logger.getLogger(Master.class);

    public void start()
    {
        logger.info("INIT MASTER");
        ServerProperties prop = ServerProperties.getInstance(); //singleton, by design

        try
        {
            int port = Integer.parseInt(prop.getProperty("server.port"));
            ServerImpl impl = new ServerImpl(port);

            impl.deploy();
            impl.beginWork(new MasterRun());
        }
        catch (Exception e)
        {
            logger.fatal("Bad value for property server.port! Check conf/server.xml");
            System.exit(-1);
        }
        //TODO
    }
}
