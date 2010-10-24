/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.wrapper.fb

import scala.actors.Actor._
import scala.collection.mutable.Map
import scala.collection.mutable.Set
import websiteschema.wrapper.Wrapper

/*
 * Super Class of Function Block.
 */
abstract class FB(name:String, params:Map[String,String], wrapper: Wrapper) {

  def getName() = name

  def setInputData(input:String, obj:Any):Unit

  def getOutputData(output:String):Any

  def process(event:String):Unit

  def getRelativeDataLinksOf(event:String):Set[EventDataLink]
}

class FBLink
case class EventLink(src:String, outputEvent:String, dest:String, inputEvent:String) extends FBLink
case class DataLink(src:String, outputData:String, dest:String, destInput:String) extends FBLink
case class EventDataLink(outputEvent:String, relativeData:String) extends FBLink

class FBLinkManager {
  val eventLinks = Set[EventLink]()
  val dataLinks = Set[DataLink]()

  def addEventLink(elink:EventLink) = eventLinks += elink

  def addDataLink(dlink:DataLink) = dataLinks += dlink

  def getEventLinks(fb:String, event:String):Set[EventLink] = {
    for{
      e <- eventLinks
      if(fb equals e.src)
      if(event equals e.outputEvent)
    } yield e
  }
  
  def getDataLinks(fb:String, data:String):Set[DataLink] = for{d <- dataLinks; if(fb equals d.src); if(data equals d.outputData)} yield d
  
  def getEventDataLinks(fb:FB, event:String) = fb getRelativeDataLinksOf event
}

class FBEvent
case class BasicEvent(fb:String, name:String) extends FBEvent
case class EndEvent() extends FBEvent
