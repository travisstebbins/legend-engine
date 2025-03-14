// Copyright 2024 Goldman Sachs
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.finos.legend.engine.plan.execution.stores.relational.connection.driver.vendors.duckdb;

import org.eclipse.collections.api.factory.Lists;
import org.finos.legend.engine.plan.execution.stores.relational.connection.driver.commands.Column;
import org.finos.legend.engine.plan.execution.stores.relational.connection.driver.commands.IngestionMethod;
import org.finos.legend.engine.plan.execution.stores.relational.connection.driver.commands.RelationalDatabaseCommands;
import org.finos.legend.engine.plan.execution.stores.relational.connection.driver.commands.RelationalDatabaseCommandsVisitor;

import java.util.List;
import java.util.stream.Collectors;

public class DuckDBCommands extends RelationalDatabaseCommands
{
    @Override
    public String dropTempTable(String tableName)
    {
        return this.dropTable(tableName);
    }

    @Override
    public List<String> createAndLoadTempTable(String tableName, List<Column> columns, String optionalCSVFileLocation)
    {
        return Lists.fixedSize.of(this.load(true, tableName, optionalCSVFileLocation, columns));
    }

    @Override
    public IngestionMethod getDefaultIngestionMethod()
    {
        return IngestionMethod.CLIENT_FILE;
    }

    @Override
    public String load(String tableName, String location)
    {
        return "CREATE TABLE " + tableName + " AS SELECT * FROM read_csv('" + location + "', header=true);";
    }

    @Override
    public String load(String tableName, String location, List<Column> columns)
    {
        return this.load(false, tableName, location, columns);
    }

    private String load(boolean temp, String tableName, String location, List<Column> columns)
    {
        String columnTypesString = columns.stream().map(c -> String.format("'%s': '%s'", c.name, c.type)).collect(Collectors.joining(", ", "{", "}"));
        return "CREATE " + (temp ? "TEMP" : "") + " TABLE " + tableName + " AS SELECT * FROM read_csv('" + location + "', header = true, columns = " + columnTypesString + ");";
    }

    @Override
    public String dropTable(String tableName)
    {
        return "DROP TABLE IF EXISTS " + tableName + ";";
    }

    @Override
    public <T> T accept(RelationalDatabaseCommandsVisitor<T> visitor)
    {
        return visitor.visit(this);
    }
}
