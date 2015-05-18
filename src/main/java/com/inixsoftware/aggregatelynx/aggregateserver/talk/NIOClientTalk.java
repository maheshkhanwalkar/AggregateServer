package com.inixsoftware.aggregatelynx.aggregateserver.talk;

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

import com.inixsoftware.nioflex.nio.NIOServer;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class NIOClientTalk extends NIOServer
{
    private Logger logger = Logger.getLogger(NIOClientTalk.class);

    @Override
    public void handleAccept(SocketChannel socketChannel, SelectionKey selectionKey)
    {
        logger.info("Client Accepted");
    }

    @Override
    public void handleRead(SocketChannel socketChannel, SelectionKey selectionKey)
    {

    }

    /*private SocketChannel client;
    private SelectionKey key;

    private Selector selector;
    private Logger logger = Logger.getLogger(NIOClientTalk.class);

    public NIOClientTalk(SocketChannel client, SelectionKey key, Selector selector)
    {
        this.client = client;
        this.key = key;

        this.selector = selector;
    }

    public void run()
    {
        ByteBuffer bLen = ByteBuffer.allocate(4);
        try
        {
            client.read(bLen);
            int len = bLen.getInt();

            //TODO read until len is achieved
            ByteBuffer buffer = ByteBuffer.allocate(len);
        }
        catch (IOException e)
        {
            logger.warn(null, e);
        }
    }*/
}