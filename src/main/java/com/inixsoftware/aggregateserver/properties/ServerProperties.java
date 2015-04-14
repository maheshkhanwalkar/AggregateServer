package com.inixsoftware.aggregateserver.properties;

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

import java.util.Properties;

public class ServerProperties
{
    private static ServerProperties ourInstance = new ServerProperties();
    private Properties properties = new Properties();

    public static ServerProperties getInstance()
    {
        return ourInstance;
    }

    private ServerProperties() { }

    public void setProperty(String key, String value)
    {
        properties.setProperty(key, value);
    }

    public String getProperty(String key)
    {
        return properties.getProperty(key);
    }
}
