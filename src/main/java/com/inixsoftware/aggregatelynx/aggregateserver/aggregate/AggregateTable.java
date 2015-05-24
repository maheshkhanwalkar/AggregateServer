package com.inixsoftware.aggregatelynx.aggregateserver.aggregate;

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

import java.util.ArrayList;

public class AggregateTable
{
    private ArrayList<String> aggregates;
    private ArrayList<String> fields;

    private ArrayList<String> compare;

    private String name;
    private int count = 0;

    public AggregateTable(String name)
    {
        this.name = name;

        aggregates = new ArrayList<String>();
        fields = new ArrayList<String>();

        compare = new ArrayList<String>();
    }

    public void setAggregates(ArrayList<String> aggregates)
    {
        this.aggregates = aggregates;
    }

    public void setFields(ArrayList<String> fields)
    {
        this.fields = fields;
    }

    public void setCompare(ArrayList<String> compare)
    {
        this.compare = compare;
    }

    public void aggregate(String data)
    {
       String[] split = data.split(",");
       boolean logicAnd = true;

       for(int i = 0; i < aggregates.size(); i++)
       {
           String aggregate = aggregates.get(i);

           int pos = -1;

           for(int j = 0; j < fields.size(); j++)
           {
                if(fields.get(j).equals(aggregate))
                {
                    pos = j;
                    break;
                }
           }

           if(!split[pos].equals(compare.get(i)))
           {
               logicAnd = false;
           }
       }

       if(logicAnd)
           count++;
    }

    public int getCount()
    {
        return count;
    }
}
