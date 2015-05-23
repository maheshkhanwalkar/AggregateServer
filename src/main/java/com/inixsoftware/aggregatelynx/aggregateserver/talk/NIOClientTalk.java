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

import com.inixsoftware.aggregatelynx.aggregateserver.aggregate.AggregateStore;
import com.inixsoftware.aggregatelynx.aggregateserver.aggregate.AggregateTable;
import com.inixsoftware.nioflex.nio.NIOServer;
import com.inixsoftware.nioflex.nio.utils.NIOUtils;
import org.apache.log4j.Logger;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

public class NIOClientTalk extends NIOServer
{
    private Logger logger = Logger.getLogger(NIOClientTalk.class);
    private AggregateStore store = AggregateStore.getInstance();

    @Override
    public void handleAccept(SocketChannel socketChannel, SelectionKey selectionKey)
    {
        logger.info("Client Accepted");
    }

    @Override
    public void handleRead(SocketChannel socketChannel, SelectionKey selectionKey)
    {
        /*
          TODO there needs to be some kind of "socket state", so that we can end
          this thread, and re-use threads in the thread pool
        */

        String apiVer = NIOUtils.readString(5, socketChannel, Charset.forName("UTF-8"));

        //AggregateLynx standard policy is to write response length
        //and then the actual data

        //The client API will read the int and prepare a buffer of that size
        //to read - it will *ONLY* read that many bytes and then prepare a response

        if(apiVer.equals("0.1.0") || apiVer.equals("0.1.1") || apiVer.equals("0.1.2"))
        {
            //API is supported
            NIOUtils.writeInt(3, socketChannel);
            NIOUtils.writeString("ACK", socketChannel, Charset.forName("UTF-8"));

            //this is very bad and defeats the point
            //of scalability

            while(true)
            {
                int len = NIOUtils.readInt(socketChannel);
                String command = NIOUtils.readString(len, socketChannel);

                /* THIS IS ALSO, BAD */
                /* This will be fixed eventually - this command checking ;)*/

                if(command.equals("CLOSE_SOCKET"))
                {
                    NIOUtils.writeInt(3, socketChannel);
                    NIOUtils.writeString("ACK", socketChannel);

                    break;
                }

                if(command.equals("CREATE_AGGREGATE"))
                {
                    logger.info("Creating new AggregateTable");

                    int lName = NIOUtils.readInt(socketChannel);
                    String name = NIOUtils.readString(lName, socketChannel);

                    int fName = NIOUtils.readInt(socketChannel);
                    String fData = NIOUtils.readString(fName, socketChannel);

                    ArrayList<String> fields = new ArrayList<String>(
                            Arrays.asList(fData.split(","))
                    );

                    int aName = NIOUtils.readInt(socketChannel);
                    String aData = NIOUtils.readString(aName, socketChannel);

                    ArrayList<String> aggregates = new ArrayList<String>(
                            Arrays.asList(aData.split(","))
                    );

                    int aVal = NIOUtils.readInt(socketChannel);
                    String matchBy = NIOUtils.readString(aVal, socketChannel);

                    ArrayList<String> compare = new ArrayList<String>(
                            Arrays.asList(matchBy.split(","))
                    );

                    store.addTable(name, fields, aggregates, compare);
                }

                if(command.equals("DO_AGGREGATE"))
                {
                    //logger.info("Aggregating data");

                    int nameLen = NIOUtils.readInt(socketChannel);
                    String name = NIOUtils.readString(nameLen, socketChannel);

                    AggregateTable table = store.getTable(name);
                    int dLen = NIOUtils.readInt(socketChannel);

                    String data = NIOUtils.readString(dLen, socketChannel);
                    table.aggregate(data);
                }

                if(command.equals("GET_RESULT"))
                {
                    int nameLen = NIOUtils.readInt(socketChannel);
                    String name = NIOUtils.readString(nameLen, socketChannel);

                    AggregateTable table = store.getTable(name);
                    NIOUtils.writeInt(table.getCount(), socketChannel);
                }
            }

        }
        else
        {
            //API isn't supported
            NIOUtils.writeInt(15, socketChannel);
            NIOUtils.writeString("ERR_UNSUPPORTED", socketChannel, Charset.forName("UTF-8"));
        }
    }

}
