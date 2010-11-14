/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.wrapper.fb

import websiteschema.wrapper.Wrapper
import scala.collection.mutable.Set

class DemoFB(name:String, params:Map[String,String], wrapper:Wrapper)
  extends AbstractFB(name:String, params:Map[String,String], wrapper:Wrapper)
{

  var date:String = "hello";
  val relativeDataLinks = Set(EventDataLink("date","date"))

  def setInputData(input:String, obj:Any):Unit = {
    if(input.equals("date") && obj.isInstanceOf[String])
        date = obj.asInstanceOf[String]
  }
  
  def getOutputData(output:String):Any = if("date" equals output) date

  def process(event:String):Unit = event match {
    case "print" => {
        println(date)
        wrapper ! BasicEvent(name,"date")
    }
    case "end" => {
        println("task over")
        wrapper ! EndEvent(<result>NULL</result>)
    }
    case _ => println("invalid event")
  }

  def getRelativeDataLinksOf(event:String):Set[EventDataLink] = relativeDataLinks
  
}
