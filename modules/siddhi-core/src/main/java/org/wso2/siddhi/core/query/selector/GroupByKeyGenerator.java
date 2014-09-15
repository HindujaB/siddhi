/*
 * Copyright (c) 2005 - 2014, WSO2 Inc. (http://www.wso2.org)
 * All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.siddhi.core.query.selector;

import org.wso2.siddhi.core.config.SiddhiContext;
import org.wso2.siddhi.core.event.state.MetaStateEvent;
import org.wso2.siddhi.core.event.stream.StreamEvent;
import org.wso2.siddhi.core.exception.ValidatorException;
import org.wso2.siddhi.core.executor.ExpressionExecutor;
import org.wso2.siddhi.core.executor.VariableExpressionExecutor;
import org.wso2.siddhi.core.util.parser.ExpressionParser;
import org.wso2.siddhi.core.util.parser.helper.QueryParserHelper;
import org.wso2.siddhi.query.api.expression.Variable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupByKeyGenerator {

    private VariableExpressionExecutor[] groupByExecutors = null;

    public GroupByKeyGenerator(List<Variable> groupByList,
                               MetaStateEvent metaStateEvent,
                               List<VariableExpressionExecutor> executors, SiddhiContext siddhiContext) {

        if (!groupByList.isEmpty()) {
            groupByExecutors = new VariableExpressionExecutor[groupByList.size()];
            for (int i = 0, expressionsSize = groupByList.size(); i < expressionsSize; i++) {
                try {
                    groupByExecutors[i] = (VariableExpressionExecutor) ExpressionParser.parseExpression(groupByList.get(i), null, siddhiContext, null, metaStateEvent.getMetaEvent(0), executors, false);
                } catch (ValidatorException e) {
                    //this will never happen as this is already validated
                }
//                QueryParserHelper.updateVariablePosition(metaStateEvent, Arrays.asList(groupByExecutors));
            }
        }
    }

    protected String constructEventKey(StreamEvent event) {
        if (groupByExecutors != null) {
            StringBuilder sb = new StringBuilder();
            for (ExpressionExecutor executor : groupByExecutors) {
                sb.append(executor.execute(event)).append("::");
            }
            return sb.toString();
        } else {
            return null;
        }
    }
}
