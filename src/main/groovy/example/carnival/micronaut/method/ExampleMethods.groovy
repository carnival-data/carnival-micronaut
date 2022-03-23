package example.carnival.micronaut.method



import groovy.transform.ToString
import groovy.util.logging.Slf4j
import javax.inject.Singleton
import javax.inject.Inject

import org.apache.tinkerpop.gremlin.structure.Graph
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource
import carnival.core.graph.GraphMethods
import carnival.core.graph.GraphMethod

import example.carnival.micronaut.GraphModel
//import example.carnival.micronaut.config.AppConfig
import example.carnival.micronaut.vine.ExampleDbVine
//import example.carnival.micronaut.graph.CarnivalGraph



@ToString(includeNames=true)
@Slf4j 
@Singleton
class ExampleMethods implements GraphMethods { 

    ///////////////////////////////////////////////////////////////////////////
    // FIELDS
    ///////////////////////////////////////////////////////////////////////////

//    @Inject AppConfig config
    @Inject ExampleDbVine exampleDbVine
//    @Inject CarnivalGraph carnivalGraph




    ///////////////////////////////////////////////////////////////////////////
    // SERVICE METHOD
    ///////////////////////////////////////////////////////////////////////////


    /** */
    class LoadEncounters extends GraphMethod {

        void execute(Graph graph, GraphTraversalSource g) {

            def mdt = exampleDbVine
                .method('Encounters')
                .call()
            .result

            mdt.data.values().each { rec ->
                log.trace "rec: ${rec}"
                def encV = GraphModel.VX.ENCOUNTER.instance().withProperties(
                    GraphModel.PX.ID, rec.ENCOUNTER_ID,
                    GraphModel.PX.START, rec.START,
                    GraphModel.PX.END, rec.STOP
                ).ensure(graph, g)
                def patV = GraphModel.VX.PATIENT.instance().withProperties(
                    GraphModel.PX.ID, rec.PATIENT_ID
//                    ,
//                    GraphModel.PX.START, rec.START,
//                    GraphModel.PX.END, rec.STOP
                ).ensure(graph, g)
                //TH5: what is the difference between graph and g?
//                GraphModel.EX.HAS.instance().from(patV).to(encV).create(graph, g)
                GraphModel.EX.HAS.instance().from(patV).to(encV).ensure(g)
            }

        }

    }


    class PrintEncounters extends GraphMethod {
        void execute(Graph graph, GraphTraversalSource g) {
            // g.V().isa(GraphModel.VX.ENCOUNTER).each {
            g.V().each { v ->
                v.properties().each { p ->
                    log.trace "p ${p}"
                }
//                TH5, not sure why this doesn't work
//                v.edges().each { e ->
//                    log.trace "e ${e}"
//                }

                // def foo = v.properties()
                // log.trace "foo ${foo}"
                // log.trace "bar ${v.values()}"
                // log.trace "${v.properties().length}"
                // log.trace "${foo.length}"
                log.trace "some node"
            }
            // def mylist = g.V()
            // log.trace "g.length =? ${mylist.length}"
            // List<Vertex> list = s.V().filter(bothE().limit(50).count().is(lt(50))).toList()
            // s.V(list).out().has(...)....
        }
    }



}