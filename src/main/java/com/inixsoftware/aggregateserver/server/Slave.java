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

import org.apache.log4j.Logger;

public class Slave
{
    Logger logger = Logger.getLogger(Master.class);

    public void start()
    {
        logger.info("INIT SLAVE");
        //ServerProperties prop = ServerProperties.getInstance(); //singleton, by design

        try
        {
            //TODO implement this once, master functionality is done
            //SlaveHandler slave = new SlaveHandler(7557); //internal port
            //slave.deploy();
        }
        catch (Exception e)
        {
            logger.fatal("Bad value for property server.port! Check conf/server.xml");
            System.exit(-1);
        }
    }
}
