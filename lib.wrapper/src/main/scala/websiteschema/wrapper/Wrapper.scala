/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.wrapper

import scala.actors.Actor
import scala.actors.Actor._
import websiteschema.wrapper.fb._

/*
 * An xml file descript the relationship of function blocks.
 * Wrapper build those function blocks and run them.
 */
class Wrapper extends Actor {

  val funcBlocks = Map[String, FB]()
  val fbLinks = new FBLinkManager
  
  var stop = false;

  def act() = {
    while(!stop) {
      receive {
        case EndEvent(result) => stop = true
        case event:BasicEvent => process(event)
      }
    }
  }

  def isStoped = stop

  def process(event:BasicEvent) = 
    if(!stop) {
      delivery(event)
      execute(event)
    }

  def getFB(fbName: String) = funcBlocks.get(fbName)
  private def getFB(event:BasicEvent):Option[FB] = getFB(event.fb)

  private def execute(event:BasicEvent){
    val destFBs = fbLinks.getEventLinks(event.fb, event.name)
    destFBs foreach invokeFB

    def invokeFB(eventLink:EventLink) {
      getFB(eventLink.dest) match {
        case Some(fb) =>
          val event = eventLink.inputEvent
          fb ! event
        case _ => 
      }
    }
  }

  private def delivery(event:BasicEvent) {
    getFB(event) match {
      case Some(fb) => {
          val relativeDatas = fb getRelativeDataLinksOf event.name
          relativeDatas.foreach(transportEventRelativeData(fb, _))

          def transportEventRelativeData(fb:FB, eventDataLink:EventDataLink): Unit = {
            val output = eventDataLink.relativeData
            val dataLinks = fbLinks.getDataLinks(fb.getName, output)
            dataLinks foreach transportData

            def transportData(dataLink:DataLink):Unit = {
              getFB(dataLink.src) match {
                case Some(src) =>
                  getFB(dataLink.dest) match {
                    case Some(dest) =>
                      val delivery = src.getOutputData(dataLink.outputData)
                      dest.setInputData(dataLink.destInput, delivery)
                    case _ =>
                  }
                case _ =>
              }
            }
          }
        }
      case _ => println("getFB return None")
    }
  }

}

object Wrapper {
  def apply(id: Int): Wrapper = new Wrapper()
}