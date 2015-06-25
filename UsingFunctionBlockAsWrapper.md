#Standard IEC61499 is quite new method for automation control system, and the concepts of event flow and data flow are very suitable for a wrapper.

# Introduction #

IEC61499是新一代自动控制系统的组件标准，其中引入的事件流和数据流等概念，非常适合用来构建wrapper，使其更灵活、更可扩展。在websiteschema中，仅使用IEC61499中的基本功能块模型。


# Details #

sample/
> sample.xml 一个配置Wrapper的例子
websiteschema/fb/
> 一些基本功能块
websiteschema/fb/task/
  * 由功能块拼装的执行任务。
  * Task.java     一个Task由多个URL和一个Wrapper组成，一个 Task 下属有多个 SubTask
  * SubTask.java  一个子任务由一个Wrapper和一个URL组成