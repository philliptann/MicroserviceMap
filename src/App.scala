import java.io.File

import scala.io.Source

object App extends App{

  println("Starting Search")

  val filePath="/Users/philliptann/worthing/dev/"

  val routesFiles= getFileTree(new File(filePath)).filter(_.getName.endsWith("app.routes"))

  var serviceNameSet = Set[String]()

  routesFiles.foreach{ f =>

    val count = serviceNameSet.size
    serviceNameSet += getServiceName(f)
    if (serviceNameSet.size > count) {
      var list =readFile(f)
      println(list+"   "+list.size)
    }

  }

  //files.foreach(f => println(getServiceName(f)) )


  //files.foreach(f => println(getServiceName(f.getAbsolutePath.replace("/Users/philliptann/worthing/dev/",""))))
 //  var files2: List[String] =files.foreach(f => f.getAbsolutePath))



  def readFile(file:File): List[String] = {

    var tempSet = Set[String]()

    println("----------------------- "+getServiceName(file)+"---------------------------------")

    val lines = Source.fromFile(file).getLines.filter(s => !s.contains("#")).toList
    lines.map {
      var t = ""
      e =>
        if (e.indexOf("/") > -1) t = e.substring(e.indexOf("/") + 1) else t = e

        if (t.indexOf("/") > -1) t = t.substring(0, t.indexOf("/")) else t = ""

        if (t.length >1) tempSet += t

        t
    }
    findScalaFiles(tempSet.toList)


   tempSet.toList
  }


  def findScalaFiles(list:List[String]): Unit ={
    //var file=
    val scalaFiles= getFileTree(new File(filePath)).filter(_.getName.endsWith(".scala"))
    scalaFiles.foreach{f =>
      findService(f, list)
    }

  }

  def findService(file:File, name:List[String]): Unit ={
    var source =Source.fromFile(file)

    name.foreach {//assets
      e =>
        val list=  source.getLines.filter(_ contains "/"+e+"/").toList

       if(list.length>0) println(list)
        e
    }
    source.close()
  }

  def getServiceName(fileName: File): String= {
    val temp = fileName.getAbsolutePath.replace(filePath,"")
    temp.substring(0,temp.indexOf("/"))
  }

  def getFileTree(f: File): Stream[File] =
    f #:: (if (f.isDirectory) f.listFiles().toStream.flatMap(getFileTree)
    else Stream.empty)

}
