package com.inixsoftware.aggregateserver.socket;

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

public class SlaveHandler extends Handler
{
    private Logger logger = Logger.getLogger(SlaveHandler.class);

    public SlaveHandler(int port)
    {
        this.port = port;
    }

    public void deploy()
    {
        try
        {
            channel = ServerSocketChannel.open().bind(new InetSocketAddress(port));
            selector = Selector.open();

            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_ACCEPT);
        }
        catch (IOException e)
        {
            logger.fatal(null, e);
            shutdown();

            System.exit(-1);
        }
    }

    public void shutdown()
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
}
