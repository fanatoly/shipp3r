package io.github.fanatoly.s3mv

import java.nio.file._

object DirectoryScanner{
  def listFiles(localPath: Path): List[Path] = {
    List(localPath)
  }
}

case class S3Action(local: Path, s3Path: String)

object S3FileMapper{
  def mapPaths(paths: List[Path]): Set[S3Action] = {
    Set()
  }
}
