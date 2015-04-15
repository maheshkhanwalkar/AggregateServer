package com.inixsoftware.aggregateserver.socket.master;

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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class MasterHandler implements Runnable
{
    private int port;
    private ServerSocketChannel channel;

    private Selector selector;
    private Logger logger = Logger.getLogger(MasterHandler.class);

    volatile HashMap<SocketChannel, Boolean> handleMap = new HashMap<SocketChannel, Boolean>();

    public MasterHandler(int port)
    {
        this.port = port;
    }

    private void deploy()
    {
        try
        {
            channel = ServerSocketChannel.open().bind(new InetSocketAddress(port));
            selector = Selector.open();

            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_ACCEPT);

            handle();
        }
        catch (IOException e)
        {
            logger.fatal(null, e);
            shutdown();

            System.exit(-1);
        }
    }

    private void handle()
    {
        logger.info("Starting NIO Multiplexer");
        while(true)
        {
            try
            {
                selector.select();

                Set<SelectionKey> set = selector.selectedKeys();
                Iterator<SelectionKey> itr = set.iterator();

                while(itr.hasNext())
                {
                    SelectionKey cKey = itr.next();

                    if(cKey.isAcceptable())
                    {
                        SocketChannel client = channel.accept();
                        client.configureBlocking(false);

                        client.register(selector, SelectionKey.OP_READ);
                        logger.info("Client Connected to AggregateServer");
                    }

                    if(cKey.isReadable())
                    {
                        SocketChannel client = (SocketChannel)cKey.channel();
                        if(handleMap.containsKey(client) && handleMap.get(client))
                        {
                            continue; //already being processed!
                        }

                        handleMap.put(client, true); //handling this
                        logger.info("Processing Request to AggregateServer");

                        //TODO process
                    }

                    itr.remove();
                }
            }
            catch (IOException e)
            {
                logger.warn(null, e);
            }
        }
    }

    private void shutdown()
    {
        try
        {
            if (channel != null)
            {
                channel.close();
            }
        }
        catch (IOException e)
        {
            logger.fatal(null, e);
            System.exit(-1);
        }
    }

    public void run()
    {
        deploy();
    }

}
