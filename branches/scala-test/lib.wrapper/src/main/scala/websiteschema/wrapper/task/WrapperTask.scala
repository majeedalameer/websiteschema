/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.wrapper.task

import websiteschema.wrapper._

/**
 * A WrapperTask contains the task information, such as:
 *  * task id,
 *  * task status,
 *  * corresponding wrapper.
 */
class WrapperTask(id: Int) {

  val startTime = System.currentTimeMillis
  val wrapper = Wrapper(0)
  var status:Int = WrapperTask.stop
}

object WrapperTask {
  val stop = 0
  val loading = 1
  val running = 2
  val error = -1
}