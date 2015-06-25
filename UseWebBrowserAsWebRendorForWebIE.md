# Introduction #

近来，Web Rendor已经成为Web IE中的关键技术，怎么使用到开源的软件做自己的Web Rendor呢？今天我们集成了Mozilla。

SWT本来是作为一个simple AWT，后来变成了Standard Widget Toolkit，同时提供了Browser供用户调用。为了在不同平台之间使用，那么它必然集成了不同的浏览器技术，其中Mozilla就是SWT在Linux平台的一个选择。

# Details #

系统配置:
  * 操作系统openSUSE 11.1
  * Kernel: Linux ray-laptop 2.6.32.1-9-default #2 SMP Wed Dec 16 15:07:45 CST 2009 x86\_64 x86\_64 x86\_64 GNU/Linux
  * xulrunner-sdk 1.9.1.3
  * swt 3.6

操作系统和kernel就不说了，接着就是xulrunner的安装：
  * 下载：
http://releases.mozilla.org/pub/mozilla.org/xulrunner/releases/
  * 编译xulrunner：
> > 编辑配置文件.mozconfig:
> > ac\_add\_options --enable-application=xulrunner
> > mk\_add\_options MOZ\_CO\_PROJECT=xulrunner
  * 运行./configure
> > 如果有没有安装的包，就赶紧安装就好！
  * 运行make
  * 运行make sdk

SWT不提供linux x86\_64版本的下载，那么我们按照eclipse的faq来自己编译
  * http://www.eclipse.org/swt/faq.php#gtk64
> > Q: How do I build the 64-bit version of SWT GTK?
  * checkout它指定的3个工程在同一路径下后，在org.eclipse.swt.gtk.linux.x86\_64工程中运行ant就可以编译，然后运行jar uvf swt.jar **.so即可。**

运行WebBrowser请参考工程中的browser.SwtBrowser.java