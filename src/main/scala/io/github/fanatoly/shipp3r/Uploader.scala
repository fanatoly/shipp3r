package io.github.fanatoly.s3mv

import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Path
import org.jets3t.service.impl.rest.httpclient.RestS3Service
import org.jets3t.service._
import org.jets3t.service.model._
import org.jets3t.service.security.AWSCredentials


class Uploader(propsFile: String){
  val props = Jets3tProperties.getInstance(Constants.JETS3T_PROPERTIES_FILENAME)
  props.loadAndReplaceProperties(new FileInputStream(propsFile), propsFile)
  val credsOption = 
    for(accessKey <- Option(props.getStringProperty("aws.accesskey", null));
      secretKey <- Option(props.getStringProperty("aws.secretkey", null))) yield {
      new AWSCredentials(accessKey, secretKey)
    }

  val s3 = new RestS3Service(
    credsOption.getOrElse{ throw new RuntimeException("unable to get credentials from property file") },
    "Shipp3r",
    null,
    props
  )

  def move(actions: Set[S3Upload]) = actions.foreach( handleUpload(_) )

  private def handleUpload(action: S3Upload){
    println("Uploading " + action.local)
    uploadFile(action.local, action.s3Path)
    println("Deleting " + action.local)
    Files.delete(action.local)
  }

  private def uploadFile(local: Path, remote: S3Addr){    
    val bucket = new S3Bucket(remote.bucketName)
    val s3obj = new S3Object(remote.key)
    val file = local.toFile
    s3obj.setDataInputFile(file)
    s3obj.setContentLength(file.length)
    s3.putObject(bucket, s3obj)
  }


}
