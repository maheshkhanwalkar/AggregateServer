package com.inixsoftware.aggregatelynx.aggregateserver.talk;

import com.inixsoftware.aggregatelynx.aggregateserver.lang.CommunicationValidator;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;

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
public class ClientTalk implements Runnable
{
    private Socket client;
    private BufferedReader br;

    private BufferedWriter bw;
    private String hostName;

    private Logger logger = Logger.getLogger(ClientTalk.class);

    public ClientTalk(Socket client)
    {
        this.client = client;
        hostName = "[" + client.getInetAddress().getHostName() + "]";
    }

    private void cleanUp()
    {
        try
        {
            br.close();
            bw.close();

            client.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private String hostName()
    {
        return hostName;
    }

    public void run()
    {
        logger.info("Processing Request");

        try
        {
            br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

            String api = br.readLine();
            if(!CommunicationValidator.isSupportedAPI(api))
            {
                logger.info(hostName() + " Unsupported API - Rejecting!");

                bw.write("ERR:BAD_API\n");
                bw.flush();

                cleanUp();
                logger.info(hostName() + " Connection Closed");

                return;
            }

            bw.write("ACK\n");
            bw.flush();

            while(true)
            {
                String cmd = br.readLine();

                if(!CommunicationValidator.isValidCMD(cmd))
                {
                    logger.info(hostName() + " Invalid Command - Rejecting!");

                    bw.write("ERR:BAD_CMD\n");
                    bw.flush();

                    cleanUp();
                    break;
                }

                if(cmd.equals("CMD:CLOSE_SOCKET"))
                {
                    bw.write("ACK\n");
                    bw.flush();

                    cleanUp();
                    break;
                }

                //TODO process command
            }

            logger.info(hostName() + " Connection Closed");

        }
        catch (IOException e)
        {
            logger.fatal(null, e);
        }

    }
}
