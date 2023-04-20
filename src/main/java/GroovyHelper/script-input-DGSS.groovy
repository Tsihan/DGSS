package GroovyHelper
/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

def parse(line) {
    def (outVertex, inVertex, weight, timeStamp) = line.split(/\s+/, 4)

    def v1 = graph.addVertex(T.id, outVertex.toInteger(), T.label, outVertex)
    def v2 = graph.addVertex(T.id, inVertex.toInteger(), T.label, inVertex)


    v1.property("name", outVertex)
    v2.property("name", inVertex)


    def e1 = v1.addOutEdge("sendMessageTo", v2)
    e1.property("weight", weight.toInteger())
    e1.property("timeStamp", timeStamp.toLong())

    def e2 = v2.addInEdge("sendMessageTo", v2)
    e2.property("weight", weight.toInteger())
    e2.property("timeStamp", timeStamp.toLong())


    return e1
}
