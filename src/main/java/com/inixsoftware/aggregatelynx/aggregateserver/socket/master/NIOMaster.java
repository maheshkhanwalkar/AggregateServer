package com.inixsoftware.aggregatelynx.aggregateserver.socket.master;

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

import com.inixsoftware.aggregatelynx.aggregateserver.talk.NIOClientTalk;
import com.inixsoftware.nioflex.nio.ServerDispatch;
import org.apache.log4j.Logger;

public class NIOMaster implements Runnable
{
    private int port;
    private Logger logger = Logger.getLogger(NIOMaster.class);

    public NIOMaster(int port)
    {
        this.port = port;
    }

    public void run()
    {
        logger.info("Dispatching Server");
        NIOClientTalk server = new NIOClientTalk();

        ServerDispatch dispatch = new ServerDispatch(port, server);
        dispatch.startUp();

        dispatch.joinThread();
    }
}