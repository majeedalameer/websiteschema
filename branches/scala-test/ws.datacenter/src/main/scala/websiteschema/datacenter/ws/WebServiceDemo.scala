/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.datacenter.ws

import net.liftweb.http._
import net.liftweb.http.rest.RestHelper
import net.liftweb.json.JsonAST.JString

object WebServiceDemo extends RestHelper {

  serve {
    case Req("api" :: "static" :: _, "xml", GetRequest) => <b>Static</b>
    case Req("api" :: "static" :: _, "json", GetRequest) => JString("Static")
  }

}
