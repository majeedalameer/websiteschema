/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package code.comet

import net.liftweb._
import http._
import actor._
import scala.xml.NodeSeq
import js._
import JsCmds._
import js.jquery.JqJsCmds.{AppendHtml, FadeOut, Hide, FadeIn}
import java.util.Date
import scala.xml._
import util.Helpers
import util.Helpers._

sealed trait ChatCmd

final case class AddMessage(guid:String, msg:String, date:Date) extends ChatCmd
final case class RemoveMessage(guid:String) extends ChatCmd

object ChatServer extends LiftActor with ListenerManager {
  implicit def strToMsg(msg: String) : ChatCmd =
    new AddMessage(Helpers.nextFuncName, msg, new Date)

  private var messages: List[ChatCmd] = List("Welcome")

  def createUpdate = messages

  override def lowPriority = {
    case s: String => messages ::= s; updateListeners()
    case d: RemoveMessage => messages ::= d; updateListeners()
  }
}

class Chat extends CometActor with CometListener {
  private var msgs:List[ChatCmd] = Nil
  private var bindLine: NodeSeq = Nil

  def registerWith = ChatServer

  override def lowPriority = {
    case m: List[ChatCmd] =>
      {
        val delta = m diff msgs
        msgs = m
        updateDeltas(delta)
      }
  }

  def updateDeltas(what: List[ChatCmd]){
    partialUpdate(what.foldRight(Noop) {
        case(m:AddMessage, x) =>
          x & AppendHtml("ul_dude", doLine(m)) & Hide(m.guid) & FadeIn(m.guid, TimeSpan(0), TimeSpan(500))
        case(RemoveMessage(guid), x) =>
          x & FadeOut(guid,TimeSpan(0), TimeSpan(500)) & After(TimeSpan(500), Replace(guid, NodeSeq.Empty))
      })
  }

  def render = {
    bind("chat",
         "line" -> lines _,
         "input" -> SHtml.text("", s => ChatServer ! s)
    )
  }

  private def lines(xml:NodeSeq): NodeSeq = {
    //msgs.reverse.flatMap(m => bind("chat", xml, "msg" -> m))
    bindLine = xml
    val deleted = Set((for{
          RemoveMessage(guid) <- msgs
        } yield guid) :_*)
    for {
      m @ AddMessage(guid, msg, date) <- msgs.reverse if !deleted.contains(guid)
      node <- doLine(m)
    } yield node
  }

  private def doLine(m:AddMessage) : NodeSeq = {
    bind("chat", addId(bindLine, m.guid),
         "msg" -> m.msg,
         "btn" -> SHtml.ajaxButton("delete",
                                   () => {
          ChatServer !
          RemoveMessage(m.guid)
          Noop}))
  }
  
  private def addId(in: NodeSeq, id: String): NodeSeq = in map {
    case e: Elem => e % ("id" -> id)
    case x => x
  }
}


