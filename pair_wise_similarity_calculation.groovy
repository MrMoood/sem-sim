@Grab(group='com.github.sharispe', module='slib-sml', version='0.9.1')
@Grab(group='org.codehaus.gpars', module='gpars', version='1.1.0')

import java.net.*
import org.openrdf.model.vocabulary.*
import slib.sglib.io.loader.*
import slib.sml.sm.core.metrics.ic.utils.*
import slib.sml.sm.core.utils.*
import slib.sglib.io.loader.bio.obo.*
import org.openrdf.model.URI
import slib.graph.algo.extraction.rvf.instances.*
import slib.sglib.algo.graph.utils.*
import slib.utils.impl.Timer
import slib.graph.algo.extraction.utils.*
import slib.graph.model.graph.*
import slib.graph.model.repo.*
import slib.graph.model.impl.graph.memory.*
import slib.sml.sm.core.engine.*
import slib.graph.io.conf.*
import slib.graph.model.impl.graph.elements.*
import slib.graph.algo.extraction.rvf.instances.impl.*
import slib.graph.model.impl.repo.*
import slib.graph.io.util.*
import slib.graph.io.loader.*
import groovyx.gpars.GParsPool

System.setProperty("jdk.xml.entityExpansionLimit", "0");
System.setProperty("jdk.xml.totalEntitySizeLimit", "0");

def factory = URIFactoryMemory.getSingleton()

class Pheno {

  int id
  Set annotations

  public Pheno(id, annotations) {
    setId(id)
    setAnnotations(annotations)
  }

  void addAnnotation(annotation) {
    annotations.add(annotation);
  }

  def getAnnotations() {
    annotations
  }

}

def getPhentyoeOntolgy = {
  URI graph_uri = factory.getURI("http://purl.obolibrary.org/obo/")
  G graph = new GraphMemory(graph_uri)
  GDataConf goConf = new GDataConf(GFormat.RDF_XML, "phenomenet-inferred.owl")
  GraphLoaderGeneric.populate(goConf, graph)

  // Add virtual root for 3 subontologies__________________________________
  URI virtualRoot = factory.getURI("http://purl.obolibrary.org/obo/virtualRoot")
  graph.addV(virtualRoot)
  new File("HPOnly.txt").splitEachLine('\t') { items ->
    String phenoId = items[0];
    URI idURI = factory.getURI("http://purl.obolibrary.org/obo/" + phenoId);
    for (int i = 0; i < items.size(); i++) {
    String pheno = items[i].replaceAll(":", "_");
      URI phenoURI = factory.getURI("http://purl.obolibrary.org/obo/" + pheno);
        Edge e = new Edge(idURI, RDF.TYPE, phenoURI);
        graph.addE(e);
    }
  }
  GAction rooting = new GAction(GActionType.REROOTING)
  rooting.addParameter("root_uri", virtualRoot.stringValue())
  GraphActionExecutor.applyAction(factory, rooting, graph)
  return graph
}

def getURIfromGO = { go ->
 return factory.getURI("http://purl.obolibrary.org/obo/" + go)
}

def getPhenos = {
  def phenos = [].withDefault {new Pheno(0, new LinkedHashSet())}
  def i = 0

//pathogens.4sim.txt


new File("HPOnly.txt").splitEachLine('\t') { items ->

    for (int j = 0; j < items.size(); j++) {
    phenos[i].addAnnotation(getURIfromGO(items[j]))

    }
   i++
  }
  return phenos
}
phenos = getPhenos()
phenos2 = getPhenos()

graph = getPhentyoeOntolgy()
SM_Engine engine = new SM_Engine(graph)
ICconf icConf = new IC_Conf_Corpus("ResnikIC", SMConstants.FLAG_IC_ANNOT_RESNIK_1995_NORMALIZED); //Inf content - extrinsic, resnik
SMconf smConfPairwise = new SMconf("Resnik",  SMConstants.FLAG_SIM_PAIRWISE_DAG_NODE_RESNIK_1995);//resnik
smConfPairwise.setICconf(icConf);

def result = new Double[phenos.size() * phenos2.size()]
for (i = 0; i < result.size(); i++) {
  result[i] = i
}

index=0
for(i=0; i<phenos.size(); i++)
  for(j=0; j<phenos2.size(); j++)
  {
    result[index] = engine.compare(
          smConfPairwise,
          phenos[i].getAnnotations()[0],
          phenos2[j].getAnnotations()[0])
	index++
}

 def fout = new PrintWriter(new BufferedWriter(
    new FileWriter("simscores.txt")))
  for (i = 0; i < result.size(); i++) {
    fout.println(result[i])
  }
fout.flush()
fout.close()
