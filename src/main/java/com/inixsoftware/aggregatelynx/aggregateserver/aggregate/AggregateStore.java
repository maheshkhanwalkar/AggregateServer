package com.inixsoftware.aggregatelynx.aggregateserver.aggregate;

import java.util.ArrayList;
import java.util.HashMap;

public class AggregateStore
{
    private static AggregateStore ourInstance = new AggregateStore();

    public static AggregateStore getInstance()
    {
        return ourInstance;
    }

    private HashMap<String, AggregateTable> tables;

    private AggregateStore()
    {
        tables = new HashMap<String, AggregateTable>();
    }

    public void addTable(String name, ArrayList<String> fields, ArrayList<String> aggregates,
             ArrayList<String> compare)
    {
        AggregateTable table = new AggregateTable(name);

        table.setFields(fields);
        table.setAggregates(aggregates);

        table.setCompare(compare);
        tables.put(name, table);
    }

    public AggregateTable getTable(String name)
    {
        return tables.get(name);
    }

}
