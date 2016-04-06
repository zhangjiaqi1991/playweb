package models

/**
  * Created by hy on 16-3-16.
  */

import controllers.Application._
import controllers.routes
import play.api._
import play.api.data._
import play.api.data.Forms._
import scala.sys.process._


case class Message(xrow: String, yrow: String,filename:String,percent:String,ram:String,cores:String)


object Message {


  var list: List[Message] = Nil


  def post(xrow: String, yrow: String,filename:String,percent:String,ram:String,cores:String) {
    var x,y,r,c:String=null

    if(xrow==""){x="2"}else{x=xrow}
    if(yrow==""){y="3"}else{y=yrow}
    if(ram==""){r="2G"}else{r=ram}
    if(cores==""){c="20"}else{c=cores}

    ("sh /root/playserver/runargs.sh"+" "+x+" "+y+" "+filename+" "+percent+" "+r+" "+c)!;
    //("echo  wtf "+" "+x+" "+y+" "+filename+" "+percent+" "+r+" "+c)!;

    list =  List(Message("已完成", "请点击下载结果",filename,"","",""))

  }


  //定义表单及其校验要求，nonEmptyText表示该项内容不得为空
  val form = Form(tuple(
    "yrow" -> text,
    "xrow" -> text,
    "filename"->nonEmptyText,
    "percent"->nonEmptyText,
    "ram"->text,
    "cores"->text
  ))

}
