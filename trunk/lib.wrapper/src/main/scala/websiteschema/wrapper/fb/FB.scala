/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.wrapper.fb

import scala.actors.Actor
import scala.actors.Actor._
import scala.collection.mutable.Set
import scala.xml.Elem
import websiteschema.wrapper.Wrapper

/*
 * Super Class of Function Block.
 */
trait FB extends Actor {
  def getName():String

  def setInputData(input:String, obj:Any):Unit

  def getOutputData(output:String):Any

  var stop = false
  def act() = {
    while(!stop) {
      receive{
        case TerminateEvent() => stop = true;
        case event:String => process(event)
        case _ => println("receive invalid msg, do nothing")
      }
    }
  }

  def process(event:String):Unit

  def getRelativeDataLinksOf(event:String):Set[EventDataLink]
}

abstract class AbstractFB(name:String, params:Map[String,String], wrapper: Wrapper) extends FB {
  def getName() = name
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
  case class BasicEvent(destFB:String, eventName:String) extends FBEvent
  case class EndEvent(result:Elem) extends FBEvent
  case class TerminateEvent() extends FBEvent
