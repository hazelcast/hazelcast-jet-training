/*
 * Copyright (c) 2008-2019, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.Job;
import com.hazelcast.jet.pipeline.Pipeline;
import com.hazelcast.jet.pipeline.Sinks;

public class Lab3 {

    public static void main (String[] args) {
        Pipeline p = buildPipeline();

        JetInstance jet = Jet.newJetInstance();

        try {
            Job job = jet.newJob(p);
            job.join();
        } finally {
            jet.shutdown();
        }
    }

    private static Pipeline buildPipeline() {
        Pipeline p = Pipeline.create();

        // 1 - Read from the Trade Source (sources.TradeSource)
        p.drawFrom(sources.TradeSource.tradeSource())

        // 2 - Without timestamps - we don't need timestamped stream now
        .withoutTimestamps()

        // 3 - Compute max price in a rolling way
        // - the max will be updated and emitted with each incoming trade
        // - use com.hazelcast.jet.aggregate.AggregateOperations library with aggregations
        // - use com.hazelcast.jet.function.ComparatorEx library

        // 4 - Drain max to logger sink
        .drainTo(Sinks.logger());

        return p;
    }
}
