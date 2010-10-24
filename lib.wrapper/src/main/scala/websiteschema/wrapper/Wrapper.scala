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
        case EndEvent() => stop = true
        case event:BasicEvent => process(event)
      }
    }
  }

  def isStoped = stop

  def process(event:BasicEvent) {
    delivery(event)
    execute(event)
  }

  def getFB(fbName: String) = funcBlocks.get(fbName).get
  private def getFB(event:BasicEvent):FB = getFB(event.fb)

  private def execute(event:BasicEvent){
    val events = fbLinks.getEventLinks(event.fb, event.name)
    events foreach invokeFB

    def invokeFB(eventLink:EventLink) {
      val fb = getFB(eventLink.dest)
      val event = eventLink.inputEvent
      fb process event
    }
  }


  private def delivery(event:BasicEvent) {
    val fb = getFB(event)
    val relativeDatas = fb getRelativeDataLinksOf event.name
    relativeDatas.foreach(transportEventRelativeData(fb, _))

    def transportEventRelativeData(fb:FB, eventDataLink:EventDataLink): Unit = {
      val output = eventDataLink.relativeData
      val dataLinks = fbLinks.getDataLinks(fb.getName, output)
      dataLinks foreach transportData

      def transportData(dataLink:DataLink):Unit = {
        val src = getFB(dataLink.src)
        val dest = getFB(dataLink.dest)
        val delivery = src.getOutputData(dataLink.outputData)
        dest.setInputData(dataLink.destInput, delivery)
      }
    }
  }


}

object Wrapper {
  def apply(id: Int): Wrapper = new Wrapper()
}