package io.github.fanatoly

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import scopt._



object S3mv extends App{
  case class Params(
    jets3tFile: String = "jets3t.properties",
    localDirectory: Path = Paths.get(System.getProperty("user.dir")),
    remoteDirectory: Option[String] = None,
    dryRun: Boolean = false
  )

  val parser = new OptionParser[Params]("s3mv"){
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
      params.copy(remoteDirectory = Some(remote))
    }

    opt[Boolean]('d', "dry-run").
      optional.
      valueName("<local directory path>").
      text("Default value: " + Params().localDirectory).
      action { (dry, params)=> params.copy( dryRun = dry ) }

  }

  parser.parse(args, Params()) map { params =>
    println(params)
  }



}
