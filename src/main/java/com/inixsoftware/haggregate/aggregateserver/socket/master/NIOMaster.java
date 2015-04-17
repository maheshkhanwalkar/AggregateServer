package com.inixsoftware.haggregate.aggregateserver.socket.master;

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

import com.inixsoftware.haggregate.aggregateserver.talk.NIOClientTalk;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class NIOMaster implements Runnable
{
    //TODO NIO trial to allow for better scalability for the AggregateServer

    private int port;
    private ServerSocketChannel channel;

    private Selector selector;

    private Logger logger = Logger.getLogger(NIOMaster.class);
    private int cores = Runtime.getRuntime().availableProcessors();

    private ThreadPoolExecutor pool = new ThreadPoolExecutor(cores, cores, 10, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(100));

    public NIOMaster(int port)
    {
        this.port = port;
    }

    public void run()
    {
        try
        {
            channel = ServerSocketChannel.open().bind(new InetSocketAddress(port));
            selector = Selector.open();

            channel.register(selector, SelectionKey.OP_ACCEPT);
            handle();

        }
        catch (IOException e)
        {
            logger.fatal(null, e);
            System.exit(-1);
        }
    }

    private void handle()
    {
        while(true)
        {
            try
            {
                selector.select();
                Set<SelectionKey> keys = selector.keys();

                Iterator<SelectionKey> itr = keys.iterator();
                while(itr.hasNext())
                {
                    SelectionKey key = itr.next();
                    if(key.isAcceptable())
                    {
                        SocketChannel client = channel.accept();
                        client.configureBlocking(false);

                        client.register(selector, SelectionKey.OP_READ);
                    }

                    if(key.isReadable())
                    {
                        SocketChannel client = (SocketChannel)key.channel();
                        NIOClientTalk talk = new NIOClientTalk(client, key, selector);

                        pool.execute(talk);
                    }
                }
            }
            catch (IOException e)
            {
                logger.warn(null, e);
            }
        }
    }
}
