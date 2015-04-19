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

import com.inixsoftware.aggregatelynx.aggregateserver.talk.ClientTalk;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MasterHandler implements Runnable
{
    private int port;
    private ServerSocket server;

    private Logger logger = Logger.getLogger(MasterHandler.class);
    private int cores = Runtime.getRuntime().availableProcessors();

    private ThreadPoolExecutor pool = new ThreadPoolExecutor(cores, cores, 10,
            TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    public MasterHandler(int port)
    {
        this.port = port;
    }

    public void run()
    {
        try
        {
            server = new ServerSocket(port);
            deploy();
        }
        catch (IOException e)
        {
            logger.fatal(null, e);
            System.exit(-1);
        }
    }

    private void deploy()
    {
        logger.info("Ready to accept clients");

        while(true)
        {
            try
            {
                Socket client = server.accept();
                logger.info("Client Connected");

                ClientTalk talk = new ClientTalk(client);
                pool.execute(talk);
            }
            catch (IOException e)
            {
                logger.fatal(null, e);
            }
        }
    }
}