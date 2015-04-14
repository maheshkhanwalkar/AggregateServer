package com.inixsoftware.aggregateserver.utils.xml;

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

import com.inixsoftware.aggregateserver.properties.ServerProperties;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class ConfParser
{
    private Logger logger = Logger.getLogger(ConfParser.class);

    public void parse(File f)
    {
        logger.info("Parsing " + f.getName());

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        ServerProperties prop = ServerProperties.getInstance();

        try
        {
            DocumentBuilder builder = dbFactory.newDocumentBuilder();
            Document doc = builder.parse(f);

            doc.getDocumentElement().normalize();
            if(!doc.getDocumentElement().getNodeName().equals("configuration"))
            {
                logger.fatal("Bad XML Configuration in: " + f.getName());
                logger.fatal("Root element MUST be <configuration>");

                System.exit(-1);
            }

            NodeList properties = doc.getElementsByTagName("property");
            for(int i = 0; i < properties.getLength(); i++)
            {
                Node node = properties.item(i);

                if(node.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element elem = (Element)node;
                    NodeList nameList = elem.getElementsByTagName("name");
                    NodeList valueList = elem.getElementsByTagName("value");

                    if(nameList.getLength() == 0 || valueList.getLength() == 0)
                    {
                        logger.fatal("Bad XML Configuration in: " + f.getName());
                        logger.fatal("No values specified for <name> and/or <value> tags");

                        System.exit(-1);
                    }

                    String name = nameList.item(0).getTextContent();
                    String value = nameList.item(0).getTextContent();

                    //TODO make this better e.g. define some class to do this check

                    if(!name.equals("server.port") && !name.equals("server.mem")
                            && !name.equals("hadoop.url") && !name.equals("deploy.type")
                                && !name.equals("master.host"))
                    {
                        logger.fatal("Bad XML Configuration in: " + f.getName());
                        logger.fatal("Invalid <name>");

                        System.exit(-1);
                    }

                    prop.setProperty(name, value);
                }

            }

            //logger.info("Finished parsing " + f.getName());

        }
        catch (Exception e)
        {
            logger.fatal(e.getMessage());
            System.exit(-1);
        }
    }
}
