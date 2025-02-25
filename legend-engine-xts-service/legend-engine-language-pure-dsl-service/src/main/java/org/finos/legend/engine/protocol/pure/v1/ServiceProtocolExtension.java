// Copyright 2021 Goldman Sachs
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

package org.finos.legend.engine.protocol.pure.v1;

import org.eclipse.collections.api.block.function.Function0;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.Maps;
import org.eclipse.collections.api.list.MutableList;
import org.finos.legend.engine.protocol.pure.v1.extension.ProtocolSubTypeInfo;
import org.finos.legend.engine.protocol.pure.v1.extension.PureProtocolExtension;
import org.finos.legend.engine.protocol.pure.m3.PackageableElement;
import org.finos.legend.engine.protocol.pure.v1.model.packageableElement.service.DeploymentOwnership;
import org.finos.legend.engine.protocol.pure.v1.model.packageableElement.service.Execution;
import org.finos.legend.engine.protocol.pure.v1.model.packageableElement.service.MultiExecutionTest;
import org.finos.legend.engine.protocol.pure.v1.model.packageableElement.service.Ownership;
import org.finos.legend.engine.protocol.pure.v1.model.packageableElement.service.PureMultiExecution;
import org.finos.legend.engine.protocol.pure.v1.model.packageableElement.service.PureSingleExecution;
import org.finos.legend.engine.protocol.pure.v1.model.packageableElement.service.Service;
import org.finos.legend.engine.protocol.pure.v1.model.packageableElement.service.ServiceTest;
import org.finos.legend.engine.protocol.pure.v1.model.packageableElement.service.ServiceTestSuite;
import org.finos.legend.engine.protocol.pure.v1.model.packageableElement.service.ServiceTest_Legacy;
import org.finos.legend.engine.protocol.pure.v1.model.packageableElement.service.SingleExecutionTest;
import org.finos.legend.engine.protocol.pure.v1.model.packageableElement.service.ExecutionEnvironmentInstance;
import org.finos.legend.engine.protocol.pure.v1.model.packageableElement.service.ExecutionParameters;
import org.finos.legend.engine.protocol.pure.v1.model.packageableElement.service.SingleExecutionParameters;
import org.finos.legend.engine.protocol.pure.v1.model.packageableElement.service.MultiExecutionParameters;
import org.finos.legend.engine.protocol.pure.v1.model.packageableElement.service.UserListOwnership;
import org.finos.legend.engine.protocol.pure.v1.model.test.Test;
import org.finos.legend.engine.protocol.pure.v1.model.test.TestSuite;

import java.util.List;
import java.util.Map;

public class ServiceProtocolExtension implements PureProtocolExtension
{
    public static final String SERVICE_CLASSIFIER_PATH = "meta::legend::service::metamodel::Service";

    @Override
    public MutableList<String> group()
    {
        return org.eclipse.collections.impl.factory.Lists.mutable.with("PackageableElement", "Service");
    }

    @Override
    public List<Function0<List<ProtocolSubTypeInfo<?>>>> getExtraProtocolSubTypeInfoCollectors()
    {
        return Lists.fixedSize.with(() -> Lists.mutable.with(
                ProtocolSubTypeInfo.newBuilder(PackageableElement.class)
                        .withSubtype(Service.class, "service")
                        .withSubtype(ExecutionEnvironmentInstance.class, "executionEnvironmentInstance")
                        .build(),
                ProtocolSubTypeInfo.newBuilder(Execution.class)
                        .withDefaultSubType(PureSingleExecution.class)
                        .withSubtype(PureSingleExecution.class, "pureSingleExecution")
                        .withSubtype(PureMultiExecution.class, "pureMultiExecution")
                        .build(),
                ProtocolSubTypeInfo.newBuilder(ServiceTest_Legacy.class)
                        .withDefaultSubType(SingleExecutionTest.class)
                        .withSubtype(SingleExecutionTest.class, "singleExecutionTest")
                        .withSubtype(MultiExecutionTest.class, "multiExecutionTest")
                        .build(),
                ProtocolSubTypeInfo.newBuilder(TestSuite.class)
                        .withSubtype(ServiceTestSuite.class, "serviceTestSuite")
                        .build(),
                ProtocolSubTypeInfo.newBuilder(ExecutionParameters.class)
                        .withDefaultSubType(SingleExecutionParameters.class)
                        .withSubtype(SingleExecutionParameters.class, "singleExecutionParameters")
                        .withSubtype(MultiExecutionParameters.class, "multiExecutionParameters")
                        .build(),
                ProtocolSubTypeInfo.newBuilder(Test.class)
                        .withSubtype(ServiceTest.class, "serviceTest")
                        .build(),
                ProtocolSubTypeInfo.newBuilder(Ownership.class)
                        .withSubtype(DeploymentOwnership.class, "deploymentOwnership")
                        .withSubtype(UserListOwnership.class, "userListOwnership")
                        .build()
        ));
    }

    @Override
    public Map<Class<? extends PackageableElement>, String> getExtraProtocolToClassifierPathMap()
    {
        return Maps.mutable.with(
                Service.class, SERVICE_CLASSIFIER_PATH,
                ExecutionEnvironmentInstance.class, "meta::legend::service::metamodel::ExecutionEnvironmentInstance"
        );
    }
}
