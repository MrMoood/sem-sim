@Grapes([

 @Grab(group='org.slf4j', module='slf4j-simple', version='1.6.1'),

 @Grab(group = 'net.sourceforge.owlapi', module = 'owlapi-api', version = '4.2.5'),

 @Grab(group = 'net.sourceforge.owlapi', module = 'owlapi-apibinding', version = '4.2.5'),

 @Grab(group = 'net.sourceforge.owlapi', module = 'owlapi-impl', version = '4.2.5'),

 @Grab(group = 'net.sourceforge.owlapi', module = 'owlapi-parsers', version = '4.2.5'),

 @GrabConfig(systemClassLoader = true)
 ])

import org.semanticweb.owlapi.model.parameters.*

import org.semanticweb.owlapi.apibinding.OWLManager;

import org.semanticweb.owlapi.reasoner.*

import org.semanticweb.owlapi.reasoner.structural.StructuralReasoner

import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import org.semanticweb.owlapi.model.*;

import org.semanticweb.owlapi.io.*;

import org.semanticweb.owlapi.owllink.*;

import org.semanticweb.owlapi.util.*;

import org.semanticweb.owlapi.search.*;

import org.semanticweb.owlapi.manchestersyntax.renderer.*;

import org.semanticweb.owlapi.reasoner.structural.*

import groovy.json.JsonOutput

import java.io.File

OWLOntologyManager manager = OWLManager.createOWLOntologyManager()

OWLDataFactory fac = manager.getOWLDataFactory()

StructuralReasonerFactory f1 = new StructuralReasonerFactory()

ont =manager.loadOntologyFromOntologyDocument(new File("phenomenet-inferred.owl"))
ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor()
OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor)
class_id2url = [:]
def classToUrl(cl){
	cl.toString().replaceAll('<','').replaceAll('>','')//.replaceAll('_',':')
}
def classToID(cl){

	cl.toString().replaceAll('>','').replaceAll('<http://purl.obolibrary.org/obo/','')//.replaceAll('_',':')

}
def fout = new PrintWriter(new BufferedWriter(
  new FileWriter("HPOnly.txt")))
ont.getClassesInSignature(true).each {cl ->

 if(cl.toString().indexOf("/HP_")>-1 ){

  fout.println(classToID(cl))
 }
}
fout.flush()
fout.close()
