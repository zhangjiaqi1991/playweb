package controllers

import models.Message
import play.api._
import play.api.mvc.MultipartFormData.FilePart
import play.api.mvc._
import play.twirl.api.Html
import play.api.Play.current
import java.io.File
import scala.collection.mutable.ListBuffer


object Application extends Controller {
  var fileNameListBuffer=new ListBuffer[File]()

  //显示留言列表和发言表单
  def m = Action {
    Ok(views.html.msgboard(Message.list, Message.form))
  }

  //处理发言
  def postMsg = Action {
    implicit request =>
    Message.form.bindFromRequest.fold(
      //处理错误
      errors => BadRequest(views.html.msgboard(Message.list, errors)), {
        case (xrow, yrow,filename,percent,ram,cores) =>
          //发言
          Message.post(xrow, yrow,filename,percent,ram,cores)
          //重新定向到显示留言列表和发言表单页面
          Redirect(routes.Application.m)
      }
    )
  }
  //上传文件
  def upload = Action(parse.multipartFormData) { request =>
    request.body.file("file").map { file =>
      import java.io.File
      val filename = file.filename
      val contentType = file.contentType
      file.ref.moveTo(new File("/root/data/"+filename))
      Redirect(routes.Application.m).flashing("success"->"上传成功")
    }.getOrElse {
      Redirect(routes.Application.m()).flashing(
        "error" -> "无文件！"
      )
    }
  }

  def getName(path:String): Unit  =  {

    val file = new java.io.File(path)
    if(file.isDirectory){
      val files = file.listFiles()
      if(files.nonEmpty){
        for(f <- files){
          if(f.isDirectory){
            fileNameListBuffer += f
            getName(f.getAbsolutePath)
          }else{
            fileNameListBuffer += f
          }
        }
      }
    }
  }

  def download(file_path:String) = Action {
    Ok.sendFile(new File(file_path))
  }

  def toDownload = Action{
    val path = s"/root/result1"
    fileNameListBuffer=new ListBuffer[File]
    getName(path)
    Ok(views.html.download(fileNameListBuffer.toList))
  }

}