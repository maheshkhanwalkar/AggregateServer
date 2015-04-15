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
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class LaunchSlave
{
    static Logger logger = Logger.getLogger(LaunchSlave.class);

    public static void main(String[] args)
    {
        BasicConfigurator.configure();
        logger.info("Launching Slave AggregateServer");

        //As of right now, no XML conf needed for Slave server, this may change
        //in the future

        Slave slave = new Slave();
        slave.start();
    }
}