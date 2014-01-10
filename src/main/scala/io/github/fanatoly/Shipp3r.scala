package io.github.fanatoly

import java.nio.file._
import s3mv._
import scopt._



object Shipp3r extends App{
  case class Params(
    jets3tFile: String = "jets3t.properties",
    localDirectory: Path = Paths.get(System.getProperty("user.dir")),
    remoteDirectory: String = "s3://",
    dryRun: Boolean = false
  )

  val parser = new OptionParser[Params]("shipp3r"){
    help("help")
    opt[String]('p', "properties").
      optional.
      valueName("<path to jets3t properties file>").
      text("Default value: " + Params().jets3tFile).
      action { (props, params)=>
        params.copy(jets3tFile = props)
      }

    opt[String]('l', "local-directory").
      optional.
      valueName("<local directory path>").
      text("Default value: " + Params().localDirectory).
      action { (dir, params)=>
        val directory = Paths.get(dir)
        if(Files.isDirectory(directory)){
          params.copy(localDirectory = Paths.get(dir))
        } else {
          throw new RuntimeException("Please, provide a directory path to move files from")
        }
      }

    opt[String]('r', "remote-directory").
      required.
      valueName("<S3 path to move file into>").
      action { (remote, params)=>
      params.copy(remoteDirectory = remote)
    }

    opt[Unit]('d', "dry-run").
      optional.
      valueName("<local directory path>").
      text("Default value: " + Params().localDirectory).
      action { (_, params)=> params.copy( dryRun = true ) }

  }

  parser.parse(args, Params()) map { params =>
    val localFileList = DirectoryScanner.listFiles(params.localDirectory)
    val uploads =
      S3FileMapper.mapPaths(params.localDirectory, params.remoteDirectory, localFileList)

    if(params.dryRun){
      uploads.foreach( println(_) )
    }else{
      new Uploader(params.jets3tFile).move(uploads)
    }
  }



}
