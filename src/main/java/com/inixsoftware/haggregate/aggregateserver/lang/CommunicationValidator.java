package com.inixsoftware.haggregate.aggregateserver.lang;

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

public class CommunicationValidator
{
    public static boolean isSupportedAPI(String api)
    {
        String good = api.replace(" ", "");
        return good.equals("0.0.1"); //TODO support more API versions
    }

    public static boolean isValidCMD(String command)
    {
        String good = command.replace(" ", "");

        //TODO make a better check & support more commands
        return !(!good.equals("CMD:MAKE_AGGREGATE") && !good.equals("CMD:CLOSE_SOCKET") &&
                    !good.equals("CMD:ADD_TO_AGGREGATE"));

    }
}
